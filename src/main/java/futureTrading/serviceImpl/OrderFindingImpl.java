package futureTrading.serviceImpl;

import futureTrading.dto.BrokerHistoryOrder;
import futureTrading.dto.TraderHistoryOrder;
import futureTrading.entities.*;
import futureTrading.repositories.BrokerRepo;
import futureTrading.repositories.FuturesOrderRepo;
import futureTrading.repositories.FuturesProductRepo;
import futureTrading.repositories.TraderRepo;
import futureTrading.service.OrderFinding;
import futureTrading.service.RedisService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderFindingImpl implements OrderFinding {

    @Autowired
    private RedisService redisService;

    @Autowired
    private  BrokerRepo brokerRepo;

    @Autowired
    private TraderRepo traderRepo;

    @Autowired
    private FuturesOrderRepo futuresOrderRepo;

    @Autowired
    private FuturesProductRepo futuresProductRepo;



    @Override
    public List<OrderInMD> getMyUnfinishedOrderInMD(String traderId, String brokerId, String productId) {
        List<OrderInMD> orderInMDS = redisService.getOrder(brokerId, productId);
        List<OrderInMD> unfinishedOrders = new ArrayList<>();
        for (OrderInMD order:orderInMDS) {
            if (order.getTraderId().contains(traderId)) {
                unfinishedOrders.add(order);
            }
        }
        return unfinishedOrders;
    }

    @Override
    public List<FuturesOrder> getMyFinishedOrders(Long traderId) {
        Trader trader = traderRepo.getById(traderId);
        //List<FuturesOrder> futuresOrders = new ArrayList<>();
        List<FuturesOrder> sellerOrders = futuresOrderRepo.findAllBySeller(trader);
        List<FuturesOrder> buyerOrders = futuresOrderRepo.findAllByBuyer(trader);
        sellerOrders.addAll(buyerOrders);//buyerOrders
        return sellerOrders;
    }

    @Override
    public List<OrderInMD> getAllOrderInMD(String brokerId, String productId) {
        return redisService.getOrder(brokerId, productId);
    }

    @Override
    public List<FuturesOrder> getAllFinishedOrdersInBroker(Long brokerId) {

        Broker broker = brokerRepo.getById(brokerId);
        return futuresOrderRepo.findAllByBroker(broker);
    }

    public List<TraderHistoryOrder> getPendingOrdersInOneMD(Long traderId, Long brokerId, Long productId) {

        List<TraderHistoryOrder> traderHistoryOrders = new ArrayList<>();
        Broker broker = brokerRepo.getById(brokerId);
        FuturesProduct futuresProduct = futuresProductRepo.getById(productId);
        List<OrderInMD> pendingOrders = getMyUnfinishedOrderInMD(traderId.toString(), brokerId.toString(), productId.toString());
        for (OrderInMD order:pendingOrders) {
            TraderHistoryOrder traderHistoryOrder = new TraderHistoryOrder();
            traderHistoryOrder.setBroker(broker.getName());
            traderHistoryOrder.setBuyOrSell(order.getType());
            switch (order.getTraderId()){
                case "M":
                    traderHistoryOrder.setOrderType("Market Order");
                    break;
                case "S":
                    traderHistoryOrder.setOrderType("Stop Order");
                    break;
                default:
                    traderHistoryOrder.setOrderType("Limit Order");
            }
            traderHistoryOrder.setProductName(futuresProduct.getName());
            traderHistoryOrder.setPrice(order.getPrice());
            traderHistoryOrder.setVolume(order.getAmount());
            traderHistoryOrder.setState("Pending");
            traderHistoryOrder.setUpdateTime(order.getCreateTime().toString());
            traderHistoryOrders.add(traderHistoryOrder);
        }
        return traderHistoryOrders;
    }

    public List<TraderHistoryOrder> getFinishedOrdersInMySQL(Long traderId    /*, Long brokerId, Long productId*/  ) {

        List<TraderHistoryOrder> traderHistoryOrders = new ArrayList<>();
        Trader trader = traderRepo.getById(traderId);
        //Broker broker = brokerRepo.getById(brokerId);
        //FuturesProduct futuresProduct = futuresProductRepo.getById(productId);
        List<FuturesOrder> finishedBuyOrders = futuresOrderRepo.findAllByBuyer(trader);
        for (FuturesOrder order:finishedBuyOrders) {
            TraderHistoryOrder traderHistoryOrder = new TraderHistoryOrder();
            traderHistoryOrder.setProductName(order.getProduct().getName());
            traderHistoryOrder.setBroker(order.getBroker().getName());
            traderHistoryOrder.setOrderType(order.getOrderType().name());
            traderHistoryOrder.setPrice(order.getPrice());
            traderHistoryOrder.setVolume(order.getAmount());
            traderHistoryOrder.setState("Finished");
            traderHistoryOrder.setUpdateTime(order.getFinishTime().toString());
            traderHistoryOrder.setBuyOrSell("Buy");
            traderHistoryOrders.add(traderHistoryOrder);
        }

        List<FuturesOrder> finishedSellOrders = futuresOrderRepo.findAllBySeller(trader);
        for (FuturesOrder order:finishedSellOrders) {
            TraderHistoryOrder traderHistoryOrder = new TraderHistoryOrder();
            traderHistoryOrder.setProductName(order.getProduct().getName());
            traderHistoryOrder.setBroker(order.getBroker().getName());
            traderHistoryOrder.setOrderType(order.getOrderType().name());
            traderHistoryOrder.setPrice(order.getPrice());
            traderHistoryOrder.setVolume(order.getAmount());
            traderHistoryOrder.setState("Finished");
            traderHistoryOrder.setUpdateTime(order.getFinishTime().toString());
            traderHistoryOrder.setBuyOrSell("Sell");
            traderHistoryOrders.add(traderHistoryOrder);
        }
        return traderHistoryOrders;
    }

    @Override
    public List<TraderHistoryOrder> getTraderHistory(Long traderId) {

        List<TraderHistoryOrder> traderHistoryOrders = getPendingOrdersInOneMD(traderId, (long) 1,(long)1);
        traderHistoryOrders.addAll(getPendingOrdersInOneMD(traderId,(long)1,(long)2));
        traderHistoryOrders.addAll(getPendingOrdersInOneMD(traderId,(long)1,(long)3));
        traderHistoryOrders.addAll(getPendingOrdersInOneMD(traderId,(long)2,(long)1));
        traderHistoryOrders.addAll(getPendingOrdersInOneMD(traderId,(long)2,(long)2));
        traderHistoryOrders.addAll(getPendingOrdersInOneMD(traderId,(long)2,(long)3));
        traderHistoryOrders.addAll(getFinishedOrdersInMySQL(traderId));
        return traderHistoryOrders;
    }

    public List<BrokerHistoryOrder> getPendingOrdersInBroker(Long brokerId, Long productId) {
        FuturesProduct product = futuresProductRepo.getById(productId);
        List<OrderInMD> pendingOrdersInMD = redisService.getOrder(brokerId.toString(), productId.toString());
        List<BrokerHistoryOrder> pendingOrders = new ArrayList<>();
        for (OrderInMD order:pendingOrdersInMD) {
            BrokerHistoryOrder brokerHistoryOrder = new BrokerHistoryOrder();
            brokerHistoryOrder.setProductName(product.getName());
            Trader trader = traderRepo.getById(Long.valueOf(order.getTraderId()));
            brokerHistoryOrder.setSeller(trader.getName());
            brokerHistoryOrder.setSellerComp(trader.getCompName());
            brokerHistoryOrder.setPrice(order.getPrice());
            brokerHistoryOrder.setVolume(order.getAmount());
            brokerHistoryOrder.setState("Pending");
            brokerHistoryOrder.setUpdateTime(order.getCreateTime().toString());
            brokerHistoryOrder.setBuyOrSell(order.getType());
            pendingOrders.add(brokerHistoryOrder);
        }
        return pendingOrders;
    }

    public List<BrokerHistoryOrder> getFinishedOrdersInBroker(Long brokerId) {
        Broker broker = brokerRepo.getById(brokerId);
        List<FuturesOrder> ordersInMySQL = futuresOrderRepo.findAllByBroker(broker);
        List<BrokerHistoryOrder> finishedOrders = new ArrayList<>();
        for (FuturesOrder order:ordersInMySQL) {
            BrokerHistoryOrder brokerHistoryOrder = new BrokerHistoryOrder();
            brokerHistoryOrder.setProductName(order.getProduct().getName());
            if (order.getOrderType() == FuturesOrder.OrderType.CANCEL) {
                brokerHistoryOrder.setOrderType("Cancel");
            }
            //brokerHistoryOrder.setSeller(order.getInitiator().name());
            if (order.getInitiator().equals(FuturesOrder.OrderInitiator.SELLER)) {
                if (order.getSeller()!=null) {
                    brokerHistoryOrder.setSeller(order.getSeller().getName());
                    brokerHistoryOrder.setSellerComp(order.getSeller().getCompName());
                    brokerHistoryOrder.setBuyOrSell("Sell");
                }
            }
            else {
                if (order.getBuyer()!=null) {
                    brokerHistoryOrder.setSeller(order.getBuyer().getName());
                    brokerHistoryOrder.setSellerComp(order.getBuyer().getCompName());
                    brokerHistoryOrder.setBuyOrSell("Buy");
                }
            }
            brokerHistoryOrder.setPrice(order.getPrice());
            brokerHistoryOrder.setVolume(order.getAmount());
            brokerHistoryOrder.setState("Finished");
            brokerHistoryOrder.setUpdateTime(order.getFinishTime().toString());
            finishedOrders.add(brokerHistoryOrder);
        }
        return finishedOrders;
    }

    @Override
    public List<BrokerHistoryOrder> getBrokerHistory(Long brokerId) {
        List<BrokerHistoryOrder> pendingOrders = getPendingOrdersInBroker(brokerId,(long)1);
        pendingOrders.addAll(getPendingOrdersInBroker(brokerId,(long)2));
        pendingOrders.addAll(getPendingOrdersInBroker(brokerId,(long)3));
        List<BrokerHistoryOrder> finishedOrders = getFinishedOrdersInBroker(brokerId);
        finishedOrders.addAll(pendingOrders);
        return finishedOrders;
    }
}

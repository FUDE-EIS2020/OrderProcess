package futureTrading.serviceImpl;

import futureTrading.dto.OrderInMDDto;
import futureTrading.entities.OrderInMD;
import futureTrading.repositories.BrokerRepo;
import futureTrading.repositories.FuturesOrderRepo;
import futureTrading.repositories.FuturesProductRepo;
import futureTrading.repositories.TraderRepo;
import futureTrading.service.MarketDepthService;
import futureTrading.service.OrderProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProcessServiceImpl implements OrderProcessService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private MarketDepthService marketDepthService;

    @Autowired
    private BrokerRepo brokerRepo;

    @Autowired
    private TraderRepo traderRepo;

    @Autowired
    private FuturesProductRepo futuresProductRepo;

    @Autowired
    private FuturesOrderRepo futuresOrderRepo;

    @Override
    public void processOrder(OrderInMDDto orderInMDDto) {

    }

    @Override
    public void notifyGateWay() {
        kafkaTemplate.send("test", "order XX processed");
    }

    private void saveOrderTransaction() {
    }

//    private void saveOrderTransaction(String productId, Double price, Integer amount, String orderType, String brokerId, String buyerId, String sellerId, String initiator) {
//        UUID productUuid = UUID.fromString(productId);
//        UUID sellerUuid = UUID.fromString(sellerId);
//        UUID buyerUuid = UUID.fromString(buyerId);
//        UUID brokerUuid = UUID.fromString(brokerId);
//
//        FuturesProduct product = futuresProductRepo.getById(productUuid);
//        Trader seller = traderRepo.getById(sellerUuid);
//        Trader buyer = traderRepo.getById(buyerUuid);
//        Broker broker = brokerRepo.getById(brokerUuid);
//
////        FuturesOrder order = new FuturesOrder();
//
//    }

    private void processMarketOrder(OrderInMDDto orderInMDDto, String brokerId, String productId) {
        switch (orderInMDDto.getType()) {
            case "buy":
                List<OrderInMD> sellOrders = marketDepthService.getSellOrdersInMD(brokerId, productId);
                Integer remainingAmount = orderInMDDto.getAmount();
                for (OrderInMD order : sellOrders) {
                    Integer amount = order.getAmount();
                    if (amount > remainingAmount) {
                        // 当订单可以满足这次的market order
                        order.setAmount(amount - remainingAmount);
                        saveOrderTransaction();
                        remainingAmount = 0;
                        // TODO: set orders in md
                        break;
                    } else if (amount.equals(remainingAmount)) {
                        saveOrderTransaction();
                        sellOrders.remove(order);
                        remainingAmount = 0;
                        // TODO: set orders in md
                        break;
                    } else {
                        // 这个订单数量少，应该接着往下面进行， 别忘了最后修改remaining amount
                        sellOrders.remove(order);
                        saveOrderTransaction();
                        remainingAmount = remainingAmount - order.getAmount();
                    }
                }

                if (remainingAmount > 0) {
                    // save market order
                }
                break;
            case "sell":
                List<OrderInMD> buyOrders = marketDepthService.getBuyOrdersInMD(brokerId, productId);
                Integer remainingAmount1 = orderInMDDto.getAmount();
                for (OrderInMD order : buyOrders) {
                    Integer amount = order.getAmount();
                    if (amount > remainingAmount1) {
                        // 当订单可以满足这次的market order
                        order.setAmount(amount - remainingAmount1);
                        saveOrderTransaction();
                        remainingAmount1 = 0;
                        // TODO: set orders in md
                        break;
                    } else if (amount.equals(remainingAmount1)) {
                        saveOrderTransaction();
                        buyOrders.remove(order);
                        remainingAmount1 = 0;
                        // TODO: set orders in md
                        break;
                    } else {
                        // 这个订单数量少，应该接着往下面进行， 别忘了最后修改remaining amount
                        buyOrders.remove(order);
                        saveOrderTransaction();
                        remainingAmount1 = remainingAmount1 - order.getAmount();
                    }
                }

                if (remainingAmount1 > 0) {
                    // save market order
                }
                break;
            default:
        }
    }

    private void processLimitOrder(OrderInMDDto orderInMDDto, String brokerId, String productId) {
        List<OrderInMD> sellOrders = marketDepthService.getSellOrdersInMD(brokerId, productId);
        List<OrderInMD> buyOrders = marketDepthService.getBuyOrdersInMD(brokerId, productId);
        switch (orderInMDDto.getType()) {
            case "buy":
                if (sellOrders.size() == 0 || sellOrders.get(0).getPrice() > orderInMDDto.getPrice()) {
                    // 当没有卖单 或者 卖单的最低价比这次买单的定价高（意思就是这个价钱买不到） 那么直接把这个order加入到list里
                    Double sellPrice = sellOrders.get(0).getPrice();

                } else {
                    // 这种情况是挂的买单要比当前的最低售价高（或相等），需要进行交易
                }
                break;
            case "sell":
                if (buyOrders.size() == 0 || buyOrders.get(0).getPrice() < orderInMDDto.getPrice()) {
                    // 当没有买单 或者当前买单的最高价不到这次的卖单（意思就是这个卖单卖不出去） 直接把order加入list
                    Double buyPrice = buyOrders.get(0).getPrice();

                } else {
                    // 这种情况是挂的卖单比当前最高的买单高（或相等），需要进行交易
                }
                break;
            default:
        }
    }

    private void processStopOrder(OrderInMDDto orderInMDDto) {

    }

    private void processCancelOrder() {

    }
}

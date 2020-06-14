package futureTrading.serviceImpl;

import futureTrading.entities.Broker;
import futureTrading.entities.FuturesOrder;
import futureTrading.entities.OrderInMD;
import futureTrading.entities.Trader;
import futureTrading.repositories.BrokerRepo;
import futureTrading.repositories.FuturesOrderRepo;
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
    BrokerRepo brokerRepo;

    @Autowired
    TraderRepo traderRepo;

    @Autowired
    FuturesOrderRepo futuresOrderRepo;


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
}

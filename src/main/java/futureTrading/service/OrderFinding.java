package futureTrading.service;

import futureTrading.dto.TraderHistoryOrder;
import futureTrading.dto.BrokerHistoryOrder;
import futureTrading.entities.FuturesOrder;
import futureTrading.entities.OrderInMD;

import java.util.List;

public interface OrderFinding {
    // trader： 自己完成的单子
    //          所有未完成的单子
    // broker： md里面所有的单子的详情信息
    //          broker里所有成交的单子
    //

    List<OrderInMD> getMyUnfinishedOrderInMD(String traderId, String brokerId, String productId);

    List<FuturesOrder> getMyFinishedOrders(Long traderId);

    List<OrderInMD> getAllOrderInMD(String brokerId, String productId);

    List<FuturesOrder> getAllFinishedOrdersInBroker(Long brokerId);

    List<TraderHistoryOrder> getTraderHistory(Long traderId);

    List<BrokerHistoryOrder> getBrokerHistory(Long brokerId);

}

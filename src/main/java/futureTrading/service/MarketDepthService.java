package futureTrading.service;

import futureTrading.entities.OrderInMD;

import java.util.List;

public interface MarketDepthService {

    // getMarketDepthByProduct

    // updateMarketDepth

    List<OrderInMD> getBuyOrdersInMD(String brokerId, String productId);
    List<OrderInMD> getSellOrdersInMD(String brokerId, String productId);
    List<OrderInMD> getMarketOrdersInMD(String brokerId, String productId);
    List<OrderInMD> getStopOrdersInMD(String brokerId, String productId);

}

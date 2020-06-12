package futureTrading.service;

import futureTrading.dto.OrderInMDDto;

public interface OrderProcessService {

    void processOrder(OrderInMDDto orderInMDDto);

    void notifyGateWay();

    // only for test
    void clearMD(String brokerId, String productId);
}

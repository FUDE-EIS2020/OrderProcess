package futureTrading.serviceImpl;

import futureTrading.entities.OrderInMD;
import futureTrading.service.MarketDepthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketDepthServiceImpl implements MarketDepthService {
    @Override
    public List<OrderInMD> getBuyOrdersInMD(String brokerId, String productId) {
        return null;
    }

    @Override
    public List<OrderInMD> getSellOrdersInMD(String brokerId, String productId) {
        return null;
    }

    @Override
    public List<OrderInMD> getMarketOrdersInMD(String brokerId, String productId) {
        return null;
    }

    @Override
    public List<OrderInMD> getStopOrdersInMD(String brokerId, String productId) {
        return null;
    }
}

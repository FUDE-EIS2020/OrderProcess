package futureTrading.serviceImpl;

import futureTrading.entities.OrderInMD;
import futureTrading.service.MarketDepthService;
import futureTrading.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketDepthServiceImpl implements MarketDepthService {

    @Autowired
    private RedisService redisService;

    @Override
    public List<OrderInMD> getBuyOrdersInMD(String brokerId, String productId) {
        return redisService.splitOrdersInMD(brokerId, productId).get(0);
    }

    @Override
    public List<OrderInMD> getSellOrdersInMD(String brokerId, String productId) {
        return redisService.splitOrdersInMD(brokerId, productId).get(1);
    }

    @Override
    public List<OrderInMD> getMarketOrdersInMD(String brokerId, String productId) {
        return new ArrayList<>();
    }

    @Override
    public List<OrderInMD> getStopOrdersInMD(String brokerId, String productId) {
        return new ArrayList<>();
    }
}

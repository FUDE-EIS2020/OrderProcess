package futureTrading.serviceImpl;

import futureTrading.dto.ProductPrice;
import futureTrading.entities.Broker;
import futureTrading.entities.FuturesOrder;
import futureTrading.entities.FuturesProduct;
import futureTrading.entities.OrderInMD;
import futureTrading.repositories.BrokerRepo;
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

    @Autowired
    private BrokerRepo brokerRepo;

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
        List<OrderInMD> orderInMDS1 = redisService.getMarketOrdersInMD(brokerId, productId).get(0);
        orderInMDS1.addAll(redisService.getMarketOrdersInMD(brokerId, productId).get(1));
        return orderInMDS1;
    }

    @Override
    public List<OrderInMD> getStopOrdersInMD(String brokerId, String productId) {
        List<OrderInMD> orderInMDS1 = redisService.getStopOrdersInMD(brokerId, productId).get(0);
        orderInMDS1.addAll(redisService.getStopOrdersInMD(brokerId, productId).get(1));
        return orderInMDS1;
    }

    @Override
    public List<ProductPrice> getAllProductPrice() {
        List<Broker> brokers = brokerRepo.findAll();
        List<ProductPrice> result = new ArrayList<>();
        for (Broker b:brokers) {
            List<FuturesProduct> products = b.getProducts();
            for (FuturesProduct product:products) {
                ProductPrice productPrice = new ProductPrice();
                productPrice.setBrokerName(b.getName());
                productPrice.setProductName(product.getName());
                productPrice.setProductPeriod(product.getPeriod());
                productPrice.setBrokerId(b.getId().toString());
                productPrice.setProductId(product.getId().toString());
                List<OrderInMD> buyOrders = redisService.splitOrdersInMD(b.getId().toString(), product.getId().toString()).get(0);
                List<OrderInMD> sellOrders = redisService.splitOrdersInMD(b.getId().toString(), product.getId().toString()).get(1);
                if (buyOrders.size() > 0) {
                    productPrice.setBuyPrice(buyOrders.get(0).getPrice());
                }
                if (sellOrders.size() > 0) {
                    productPrice.setSellPrice(sellOrders.get(0).getPrice());
                }
//                productPrice.setLastUpdate(redisService.getLastUpdate(b.getId().toString(), product.getId().toString()));
                result.add(productPrice);
            }
        }
        return result;
    }
}

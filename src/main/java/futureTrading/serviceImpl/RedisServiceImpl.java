package futureTrading.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import futureTrading.entities.OrderInMD;
import futureTrading.service.RedisService;
import org.aspectj.weaver.ast.Or;
import org.hibernate.criterion.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Sort by price: largest in head
class SortByPriceLH implements Comparator<OrderInMD> {
    @Override
    public int compare(OrderInMD o1, OrderInMD o2) {
        if (o1.getPrice() > o2.getPrice()) {
            return -1;
        }
        return 0;
    }
}

// Sort by price: smallest in head
class SortByPriceSH implements Comparator<OrderInMD> {
    @Override
    public int compare(OrderInMD o1, OrderInMD o2) {
        if (o1.getPrice() > o2.getPrice()) {
            return 0;
        }
        return -1;
    }
}

@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String, List<OrderInMD>> redisTemplate;

    @Override
    public void setOrder(String brokerId, String productId, List<OrderInMD> orderInMDList) {
        String key = brokerId + productId;
        redisTemplate.opsForValue().set(key, orderInMDList);
    }

    @Override
    public List<OrderInMD> getOrder(String brokerId, String productId) {
        String key = brokerId + productId;
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public List<List<OrderInMD>> splitOrdersInMD(String brokerId, String productId) {
        List<OrderInMD> allList = getOrder(brokerId, productId);
        List<OrderInMD> sellerOrderInMD = new ArrayList<>();
        List<OrderInMD> buyerOrderInMD = new ArrayList<>();
        for (OrderInMD tmpOrder : allList) {
            if ((!tmpOrder.getTag().equals("M")) && (!tmpOrder.getTag().equals("S"))) {
                if (tmpOrder.getType().equals("buy")) {
                    buyerOrderInMD.add(tmpOrder);
                }
                else sellerOrderInMD.add(tmpOrder);
            }
        }

        buyerOrderInMD.sort(new SortByPriceLH());
        sellerOrderInMD.sort(new SortByPriceSH());

        List<List<OrderInMD>> returnList = new ArrayList<>();
        returnList.add(buyerOrderInMD);
        returnList.add(sellerOrderInMD);
        return returnList;
    }
}

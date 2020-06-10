package futureTrading.serviceImpl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import futureTrading.entities.OrderInMD;
import futureTrading.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
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
        if (redisTemplate.opsForValue().get(key) == null){
            return new ArrayList<>();
        }
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

    @Override
    public JSONArray sendDataToFront(String brokerId, String productId) {
        JSONArray data = new JSONArray();
        JSONArray sellerArray = new JSONArray();
        JSONArray buyerArray = new JSONArray();

        List<List<OrderInMD>> splitList = splitOrdersInMD(brokerId, productId);
        List<OrderInMD> buyerOrderInMD = splitList.get(0);
        List<OrderInMD> sellerOrderInMD = splitList.get(1);
        // todo: now we send all the data to front end, front end decide how many level it need
        for (int i = sellerOrderInMD.size()-1; i >= 0; i--) {
            JSONObject sellerOrder = (JSONObject) JSONObject.toJSON(sellerOrderInMD.get(i));
            sellerOrder.remove("id");
            sellerOrder.remove("createTime");
            sellerOrder.remove("type");
            sellerOrder.remove("tag");
            sellerOrder.remove("traderId");
            //sellerOrder.put("level", i);
            sellerArray.add(sellerOrder);
        }
        for (int i = 0; i < buyerOrderInMD.size(); i++) {
            JSONObject buyerOrder = (JSONObject) JSONObject.toJSON(buyerOrderInMD.get(i));
            buyerOrder.remove("id");;
            buyerOrder.remove("createTime");
            buyerOrder.remove("type");
            buyerOrder.remove("tag");
            buyerOrder.remove("traderId");
            //buyerOrder.put("level", i);
            buyerArray.add(buyerOrder);
        }

        data.add(sellerArray);
        data.add(buyerArray);
        return data;
    }
}

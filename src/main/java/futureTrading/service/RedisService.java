package futureTrading.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import futureTrading.entities.OrderInMD;

import java.util.Date;
import java.util.List;

public interface RedisService {

    void setOrder(String brokerId, String productId, List<OrderInMD> orderInMDList);

    List<OrderInMD> getOrder(String brokerId, String productId);

    List<List<OrderInMD>> splitOrdersInMD(String brokerId, String productId);

    List<List<OrderInMD>> getMarketOrdersInMD(String brokerId, String productId);

    List<List<OrderInMD>> getStopOrdersInMD(String brokerId, String productId);

    JSONArray sendDataToFront(String brokerId, String productId);

}

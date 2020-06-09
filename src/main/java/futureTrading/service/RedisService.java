package futureTrading.service;

import com.alibaba.fastjson.JSONObject;
import futureTrading.entities.OrderInMD;

import java.util.List;

public interface RedisService {

    void setOrder(String brokerId, String productId, List<OrderInMD> orderInMDList);

    List<OrderInMD> getOrder(String brokerId, String productId);

    List<List<OrderInMD>> splitOrdersInMD(String brokerId, String productId);

}

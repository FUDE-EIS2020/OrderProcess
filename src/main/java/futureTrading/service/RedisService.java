package futureTrading.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface RedisService {
    void setOrder(String key, Object orderInMD);
    List<Object> getOrder(String key);
}

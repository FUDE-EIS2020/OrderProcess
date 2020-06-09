package futureTrading.service;

import com.alibaba.fastjson.JSONObject;

public interface RedisService {
    void setOrder(String key, JSONObject orderInMD);
    JSONObject getOrder(String key);
}

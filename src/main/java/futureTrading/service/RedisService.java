package futureTrading.service;

import java.util.List;

public interface RedisService {
    void setOrder(String key, Object orderInMD);

    List<Object> getOrder(String key);
}

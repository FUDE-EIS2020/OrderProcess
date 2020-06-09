package futureTrading.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import futureTrading.entities.OrderInMD;
import futureTrading.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisServiceImplement implements RedisService {

    //@Resource
    //private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void setOrder(String key, JSONObject orderInMD) {
        stringRedisTemplate.opsForValue().set(key, String.valueOf(orderInMD));
    }

    @Override
    public JSONObject getOrder(String key) {
        String jsonString =  stringRedisTemplate.opsForValue().get(key);
        return JSONObject.parseObject(jsonString);
    }
}

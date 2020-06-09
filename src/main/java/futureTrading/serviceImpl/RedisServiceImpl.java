package futureTrading.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import futureTrading.entities.OrderInMD;
import futureTrading.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String, List<Object>> redisTemplate;

    //@Resource
    //private StringRedisTemplate stringRedisTemplate;

    @Override
    public void setOrder(String brokerID_productID, List<Object> orderInMDList) {
        redisTemplate.opsForValue().set(brokerID_productID, orderInMDList);
    }

    @Override
    public List<Object> getOrder(String key) {
        //String jsonString =  stringRedisTemplate.opsForValue().get(key);
        return redisTemplate.opsForValue().get(key);
    }
}

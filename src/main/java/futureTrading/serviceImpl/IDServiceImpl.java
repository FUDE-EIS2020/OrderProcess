package futureTrading.serviceImpl;

import futureTrading.service.IDService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Objects;

@Service
public class IDServiceImpl implements IDService {

    @Resource
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public long generate(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key,
                Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        return counter.incrementAndGet();
    }
}

package futureTrading.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import futureTrading.dto.OrderInMDDto;
import futureTrading.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public int sendOrderRequestToKafka(OrderInMDDto orderInMDDto) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            kafkaTemplate.send("testTopic", mapper.writeValueAsString(orderInMDDto));
            return 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}

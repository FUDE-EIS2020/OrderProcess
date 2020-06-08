package futureTrading.serviceImpl;

import futureTrading.dto.OrderInMDDto;
import futureTrading.service.OrderProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessServiceImpl implements OrderProcessService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public void processOrder(OrderInMDDto orderInMDDto) {

    }

    @Override
    public void notifyGateWay() {
        kafkaTemplate.send("test", "order XX processed");
    }
}

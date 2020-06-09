package futureTrading.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import futureTrading.dto.OrderInMDDto;
import futureTrading.service.OrderProcessService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumer {

    @Autowired
    private OrderProcessService orderProcessService;

    @KafkaListener(topics = "testTopic", groupId = "test-group")
    public void consumer(ConsumerRecord consumerRecord) {
        Optional<Object> kafkaMassage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMassage.isPresent()) {
            Object o = kafkaMassage.get();
            String msg = (String) o;

            ObjectMapper mapper = new ObjectMapper();

            try {
                // get order from kafka
                OrderInMDDto dto = mapper.readValue(msg, OrderInMDDto.class);
                // process order
                orderProcessService.processOrder(dto);
                // notify gateway after processing
                orderProcessService.notifyGateWay();
            } catch (Exception e) {
                System.out.println("process order error");
            }
            System.out.println("receiving: " + msg);
        }
    }
}

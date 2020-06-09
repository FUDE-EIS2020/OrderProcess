package futureTrading.serviceImpl;

import futureTrading.dto.OrderInMDDto;
import futureTrading.service.MarketDepthService;
import futureTrading.service.OrderProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessServiceImpl implements OrderProcessService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private MarketDepthService marketDepthService;

    @Override
    public void processOrder(OrderInMDDto orderInMDDto) {

    }

    @Override
    public void notifyGateWay() {
        kafkaTemplate.send("test", "order XX processed");
    }

    private void processMarketOrder(OrderInMDDto orderInMDDto){

    }

    private void processLimitOrder(OrderInMDDto orderInMDDto){

    }

    private void processStopOrder(OrderInMDDto orderInMDDto){

    }

    private void processCancelOrder(){

    }
}

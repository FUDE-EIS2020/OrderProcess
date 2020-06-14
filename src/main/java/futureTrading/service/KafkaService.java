package futureTrading.service;

import futureTrading.dto.OrderInMDDto;

public interface KafkaService {

    int sendOrderRequestToKafka(OrderInMDDto orderInMDDto);
}

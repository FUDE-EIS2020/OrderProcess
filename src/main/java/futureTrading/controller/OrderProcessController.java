package futureTrading.controller;

import futureTrading.dto.AllProductsPrice;
import futureTrading.dto.OrderInMDDto;
import futureTrading.service.KafkaService;
import futureTrading.service.OrderProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderProcessController {

    @Autowired
    private OrderProcessService orderProcessService;

    @Autowired
    private KafkaService kafkaService;


    @PostMapping("/createOrder")
    public String createOrder(@RequestBody OrderInMDDto orderInMDDto) {
        Integer i = kafkaService.sendOrderRequestToKafka(orderInMDDto);
        if (i == 0) {
            return "OK";
        }
        else {
            return "BAD REQUEST";
        }
    }

    // only for test
    @GetMapping("/clearMarketDepth")
    public String clearMarketDepth(@RequestParam("brokerId") String brokerId, @RequestParam("productId")String productId) {
        orderProcessService.clearMD(brokerId, productId);
        return "OK";
    }

    @GetMapping("/prices")
    public AllProductsPrice getProductPrices() {
        return null;
    }
}

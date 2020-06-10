package futureTrading.controller;

import futureTrading.dto.OrderInMDDto;
import futureTrading.service.OrderProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderProcessController {

    @Autowired
    private OrderProcessService orderProcessService;

    @PostMapping("/createOrder")
    public String createOrder(@RequestBody OrderInMDDto orderInMDDto) {
        orderProcessService.processOrder(orderInMDDto);
        return "OK";
    }
}

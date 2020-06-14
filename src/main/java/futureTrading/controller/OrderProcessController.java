package futureTrading.controller;

import futureTrading.dto.OrderInMDDto;
import futureTrading.service.OrderProcessService;
import futureTrading.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderProcessController {

    @Autowired
    private OrderProcessService orderProcessService;

    @Autowired
    private RedisService redisService;

    @PostMapping("/createOrder")
    public String createOrder(@RequestBody OrderInMDDto orderInMDDto) {
        orderProcessService.processOrder(orderInMDDto);
        return "OK";
    }

    // only for test
    @GetMapping("/clearMarketDepth")
    public String clearMarketDepth(@RequestParam("brokerId") String brokerId, @RequestParam("productId")String productId) {
        orderProcessService.clearMD(brokerId, productId);
        return "OK";
    }

}

package futureTrading.controller;

import futureTrading.dto.AllProductsPrice;
import futureTrading.dto.OrderInMDDto;
import futureTrading.entities.FuturesOrder;
import futureTrading.entities.OrderInMD;
import futureTrading.service.KafkaService;
import futureTrading.service.OrderFinding;
import futureTrading.service.OrderProcessService;
import futureTrading.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderProcessController {

    @Autowired
    private OrderProcessService orderProcessService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private OrderFinding orderFinding;

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

    @GetMapping("/getMyUnfinishedOrders")
    public List<OrderInMD> getMyUnfinishedOrders(@RequestParam("traderId") String traderId, @RequestParam("brokerId") String brokerId, @RequestParam("productId")String productId) {
        return orderFinding.getMyUnfinishedOrderInMD(traderId,brokerId,productId);
    }

    @GetMapping("/getAllMyFinishedOrders")
    public List<FuturesOrder> getAllMyFinishedOrders(@RequestParam("traderId") Long traderId) {
        return orderFinding.getAllFinishedOrdersInBroker(traderId);
    }

    @GetMapping("/getAllOrderInMD")
    public List<OrderInMD> getAllOrderInMD(@RequestParam("brokerId") String brokerId, @RequestParam("productId")String productId) {
        return orderFinding.getAllOrderInMD(brokerId,productId);
    }

    @GetMapping("/getAllFinishedOrdersInBroker")
    public List<FuturesOrder> getAllFinishedOrdersInBroker(@RequestParam("brokerId") Long brokerId) {
        return orderFinding.getAllFinishedOrdersInBroker(brokerId);
    }

}

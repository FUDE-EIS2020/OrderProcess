package futureTrading.controller;

import com.alibaba.fastjson.JSONArray;
import futureTrading.dto.*;
import futureTrading.entities.FuturesOrder;
import futureTrading.entities.OrderInMD;
import futureTrading.service.*;
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
    @Autowired
    private MarketDepthService marketDepthService;

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
    public List<ProductPrice> getProductPrices() {
        return marketDepthService.getAllProductPrice();
    }

    @GetMapping("/getMyUnfinishedOrders")
    public List<OrderInMD> getMyUnfinishedOrders(@RequestParam("traderId") String traderId, @RequestParam("brokerId") String brokerId, @RequestParam("productId")String productId) {
        return orderFinding.getMyUnfinishedOrderInMD(traderId,brokerId,productId);
    }

    // get ALL ORDER: ALL TYPES return all orders
    // init?

    @GetMapping("/getAllMyFinishedOrders")
    public List<FuturesOrder> getAllMyFinishedOrders(@RequestParam("traderId") Long traderId) {
        return orderFinding.getAllFinishedOrdersInBroker(traderId);
    }

    // return all orders from this broker
    @GetMapping("/getAllOrderInMD")
    public List<OrderInMD> getAllOrderInMD(@RequestParam("brokerId") String brokerId, @RequestParam("productId")String productId) {
        return orderFinding.getAllOrderInMD(brokerId,productId);
    }

    @GetMapping("/getAllFinishedOrdersInBroker")
    public List<FuturesOrder> getAllFinishedOrdersInBroker(@RequestParam("brokerId") Long brokerId) {
        return orderFinding.getAllFinishedOrdersInBroker(brokerId);
    }

    @ResponseBody
    @GetMapping(path = "/sendFront")
    public JSONArray sendFront (@RequestParam("brokerId") String brokerId, @RequestParam("productId")String productId) {
        System.out.println("arrive controller sendFront! \n");
        return redisService.sendDataToFront(brokerId, productId);
    }

    @ResponseBody
    @GetMapping(path = "/traderOrderHistory")
    public List<TraderHistoryOrder> traderHistoryOrders(@RequestParam("traderId") Long traderId) {
        return orderFinding.getTraderHistory(traderId);
    }

    @ResponseBody
    @GetMapping(path = "/brokerOrderHistory")
    public List<BrokerHistoryOrder> brokerHistoryOrders(@RequestParam("brokerId") Long brokerId) {
        return orderFinding.getBrokerHistory(brokerId);
    }

}

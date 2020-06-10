package futureTrading.controller;



/* 2020/6/4.
 *
 * This controller is used to test dao.
 * Delete it when we finish the project.
 *
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import futureTrading.daos.TestDao;
import futureTrading.entities.FuturesProduct;
import futureTrading.entities.OrderInMD;
import futureTrading.service.RedisService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Controller
@RequestMapping(path = "/test")
public class TestController {

    @Resource
    TestDao testDao;

    @Resource
    RedisService redisService;

    @ResponseBody
    @GetMapping(path = "/getAllProducts")
    public List<FuturesProduct> getOneProduct() {
        System.out.println("arrive controller! \n");
        return testDao.getAll();
    }

    @ResponseBody
    @PostMapping(path = "/redisSet")
    public void redisSet(@RequestBody JSONObject jsonObject) {
        System.out.println("arrive controller redis set! \n");
        System.out.println(jsonObject);
        JSONArray jsonArray = jsonObject.getJSONArray("order");//jsonObject.getJSONObject("order");
        redisService.setOrder(jsonObject.getString("key1"), jsonObject.getString("key2"),
                JSONObject.parseArray(jsonArray.toString(), OrderInMD.class));

    }

    @ResponseBody
    @GetMapping(path = "/redisGet")
    public List<OrderInMD> redisGet(@RequestParam("key1") String key1, @RequestParam("key2") String key2) {
        System.out.println("arrive controller redis get! \n");
        return redisService.getOrder(key1,key2);
    }

    @ResponseBody
    @GetMapping(path = "/redisSplit")
    public List<List<OrderInMD>> redisSplit(@RequestParam("key1") String key1, @RequestParam("key2") String key2) {
        System.out.println("arrive controller redis split! \n");
        return redisService.splitOrdersInMD(key1,key2);
    }

    @ResponseBody
    @GetMapping(path = "/sendFront")
    public JSONArray sendFront (@RequestParam("key1") String key1, @RequestParam("key2") String key2) {
        System.out.println("arrive controller sendFront! \n");
        return redisService.sendDataToFront(key1, key2);
    }

    @ResponseBody
    @GetMapping(path = "/getMarketOrder")
    public List<List<OrderInMD>> getMarketOrdersInMD (@RequestParam("key1") String key1, @RequestParam("key2") String key2) {
        System.out.println("arrive controller getMarketOrder! \n");
        return redisService.getMarketOrdersInMD(key1, key2);
    }

    @ResponseBody
    @GetMapping(path = "/getStopOrder")
    public List<List<OrderInMD>> getStopOrdersInMD (@RequestParam("key1") String key1, @RequestParam("key2") String key2) {
        System.out.println("arrive controller getMarketOrder! \n");
        return redisService.getStopOrdersInMD(key1, key2);
    }
}

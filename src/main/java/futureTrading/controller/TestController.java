package futureTrading.controller;



/* 2020/6/4.
 *
 * This controller is used to test dao.
 * Delete it when we finish the project.
 *
 */

import com.alibaba.fastjson.JSONObject;
import futureTrading.daos.TestDao;
import futureTrading.entities.FuturesOrder;
import futureTrading.entities.FuturesProduct;
import futureTrading.entities.OrderInMD;
import futureTrading.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
        JSONObject jso = jsonObject.getJSONObject("order");
        redisService.setOrder(jsonObject.getString("key"), JSONObject.parseObject(String.valueOf(jso),OrderInMD.class));
    }

    @ResponseBody
    @GetMapping(path = "/redisGet")
    public List<Object> redisGet(@RequestParam("key") String key) {
        System.out.println("arrive controller redis get! \n");
        return redisService.getOrder(key);
    }
}

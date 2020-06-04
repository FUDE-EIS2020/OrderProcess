package futureTrading.controller;



/* 2020/6/4.
 *
 * This controller is used to test dao.
 * Delete it when we finish the project.
 *
 */

import futureTrading.daos.TestDao;
import futureTrading.entities.FuturesOrder;
import futureTrading.entities.FuturesProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/test")
public class TestController {

    @Autowired
    TestDao testDao;

    @ResponseBody
    @GetMapping(path = "/getAllProducts")
    public List<FuturesProduct> getOneProduct() {
        System.out.println("arrive controller! \n");
        return testDao.getAll();
    }

}

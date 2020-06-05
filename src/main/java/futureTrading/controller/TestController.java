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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.sleep;


@Slf4j
@Component
@ServerEndpoint("/connection/{username}")
@Controller
@RequestMapping(path = "/test")
public class TestController {

    // 保存 username --> session 的映射关系
    // TODO: 后面也许可以写进redis里
    private ConcurrentHashMap<String, Session> userSessionMap = new ConcurrentHashMap<>();

    @Autowired
    TestDao testDao;

    //  TODO: LOG
    @ResponseBody
    @OnOpen
    public String onOpen(Session session, @PathParam("username") String username) {
        if(userSessionMap.contains(username)) {return "connection failed";}
        userSessionMap.put(username,session);
        //log.info("Connection connected");
        //log.info("username: "+ username + "\t session: " + session.toString());
        return "connect";
    }

    //  TODO: LOG
    @OnClose
    public void onClose( @PathParam("username") String username) {
        userSessionMap.remove(username);
        //log.info("Connection closed");
    }

    // 传输消息错误调用的方法
    @OnError
    public void OnError(Throwable error) {
        log.info("Connection error");
    }

    // send message to every username
    @ResponseBody
    @GetMapping(path = "/sendTextTest")
    public void sendTextTest() throws IOException {
        //return "123";
        System.out.println("lined text test");
        for(Map.Entry<String, Session> entry:userSessionMap.entrySet()) {
                entry.getValue().getBasicRemote().sendText("尝试从后端发送消息");
        }
    }

    public void SendMessageTest() {

    }

    @ResponseBody
    @GetMapping(path = "/getAllProducts")
    public List<FuturesProduct> getOneProduct() {
        System.out.println("arrive controller! \n");
        return testDao.getAll();
    }


}

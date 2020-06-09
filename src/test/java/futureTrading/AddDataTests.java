package futureTrading;

import futureTrading.entities.Broker;
import futureTrading.entities.FuturesProduct;
import futureTrading.entities.Trader;
import futureTrading.repositories.BrokerRepo;
import futureTrading.repositories.FuturesProductRepo;
import futureTrading.repositories.TraderRepo;
import futureTrading.service.IDService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class AddDataTests {
    @Autowired
    private BrokerRepo brokerRepo;

    @Autowired
    private FuturesProductRepo futuresProductRepo;

    @Autowired
    private TraderRepo traderRepo;

    @Autowired
    private IDService idService;

    @Test
    public void addProduct() {
        FuturesProduct product = new FuturesProduct();
        product.setName("GOLD");
        product.setPeriod("OCT-2020");
        futuresProductRepo.save(product);

        FuturesProduct product1 = new FuturesProduct();
        product1.setName("WIT");
        product1.setPeriod("SPET-2020");
        futuresProductRepo.save(product1);

        FuturesProduct product2 = new FuturesProduct();
        product2.setName("GOLD");
        product2.setPeriod("JUNE-2020");
        futuresProductRepo.save(product2);

        Broker broker =  new Broker();
        broker.setName("Huaxin Security");
        broker.setToken("123123");
        broker.setProducts(Arrays.asList(product, product1, product2));
        brokerRepo.save(broker);

        Broker broker1 = new Broker();
        broker1.setName("MS");
        broker1.setToken("123123");
        broker.setProducts(Arrays.asList(product, product1, product2));
        brokerRepo.save(broker1);
    }

    @Test
    public void addTrader() {
        Trader trader = new Trader();
        trader.setCompName("MS");
        trader.setName("trader1");
        trader.setToken("123456");
        traderRepo.save(trader);

        Trader trader1 = new Trader();
        trader1.setCompName("MS");
        trader1.setName("trader2");
        trader1.setToken("123456");
        traderRepo.save(trader1);

        Trader trader2 = new Trader();
        trader2.setCompName("HX");
        trader2.setName("trader3");
        trader2.setToken("1234");
        traderRepo.save(trader2);
    }

    @Test
    public void getAllUUID() {
        /*
        * traders   71bbb202-b433-4ee3-a444-ffd56794aeed
                    9ae7c79d-3a76-40df-addd-ad2758949917
                    bfb32849-4fa8-481b-845b-1ea1f6fcc0d9
        *
        *
        List<Trader> traders = traderRepo.findAll();
        for (Trader t:traders
             ) {
            System.out.println(t.getId().toString());
        }
        * */

        /*
        * broker    b0cbee88-32c4-426a-bacc-cdf4345910dc
                    ee719d82-5630-42ab-8b64-5fefa1f11bea
        List<Broker> brokers = brokerRepo.findAll();
        for (Broker b: brokers) {
            System.out.println(b.getId().toString());
        }
        * */
    }
}

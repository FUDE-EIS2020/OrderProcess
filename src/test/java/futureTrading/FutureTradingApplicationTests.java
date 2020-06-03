package futureTrading;

import futureTrading.entities.Trader;
import futureTrading.repositories.TraderRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FutureTradingApplicationTests {

	@Autowired
	private TraderRepo traderRepo;

	@Test
	public void contextLoads() {
		Trader newGuy = new Trader();
		newGuy.setCompName("MS");
		newGuy.setName("WXM");
		newGuy.setToken("123123");
		this.traderRepo.save(newGuy);
	}

}

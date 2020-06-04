package futureTrading.daos;

import futureTrading.entities.Broker;
import futureTrading.entities.FuturesOrder;
import futureTrading.entities.FuturesProduct;
import futureTrading.repositories.FuturesOrderRepo;
import futureTrading.repositories.FuturesProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;



/* 2020/6/4.
 *
 * This dao is used to test dao.
 * Delete it when we finish the project.
 *
 */

@Repository
public class TestDao {
    @Autowired
    FuturesOrderRepo futuresOrderRepo;
    @Autowired
    FuturesProductRepo futuresProductRepo;

    public FuturesProduct getProductByID(UUID uuid) {
        return futuresProductRepo.getById(uuid);
    }

    public FuturesOrder getOrderByID(UUID uuid) {
        return futuresOrderRepo.getById(uuid);
    }

    public List<FuturesOrder> getAllOrderByTypeAndBroker
            (FuturesOrder.OrderType orderType, Broker broker) {
        return futuresOrderRepo.findAllByOrderTypeAndBroker(orderType, broker);
    }

    public List<FuturesProduct> getAll() {
        return futuresProductRepo.findAll();
    }
}

package futureTrading.repositories;

import futureTrading.entities.Broker;
import futureTrading.entities.FuturesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FuturesOrderRepo extends JpaRepository<FuturesOrder, String> {

    FuturesOrder getById(UUID id);

    List<FuturesOrder> findAllByOrderType(FuturesOrder.OrderType orderType);

    List<FuturesOrder> findAllByOrderState(FuturesOrder.OrderState orderState);

    List<FuturesOrder> findAllByBroker(Broker broker);

    List<FuturesOrder> findAllByOrderTypeAndBroker(FuturesOrder.OrderType orderType, Broker broker);

    List<FuturesOrder> findAllByOrderStateAndBroker(FuturesOrder.OrderState orderState, Broker broker);

}

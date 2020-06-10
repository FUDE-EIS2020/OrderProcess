package futureTrading.repositories;

import futureTrading.entities.Broker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokerRepo extends JpaRepository<Broker, Long> {

    Broker getById(Long id);
}

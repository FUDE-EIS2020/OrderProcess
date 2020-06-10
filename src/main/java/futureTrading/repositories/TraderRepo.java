package futureTrading.repositories;

import futureTrading.entities.Trader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraderRepo extends JpaRepository<Trader, Long> {
    Trader getById(Long id);
}

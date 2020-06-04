package futureTrading.repositories;

import futureTrading.entities.FuturesOrder;
import futureTrading.entities.FuturesProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FuturesProductRepo extends JpaRepository<FuturesProduct, String> {

    FuturesProduct getById(UUID uuid);

}

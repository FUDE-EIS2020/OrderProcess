package futureTrading.repositories;

import futureTrading.entities.FuturesProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FuturesProductRepo extends JpaRepository<FuturesProduct, Long> {

    FuturesProduct getById(Long id);

}

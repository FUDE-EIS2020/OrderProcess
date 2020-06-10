package futureTrading.entities;

import javax.persistence.*;
import java.util.List;


@Entity
@Table
public class Broker {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String token;

    @OneToMany(cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY,
            targetEntity = FuturesProduct.class)
    private List<FuturesProduct> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<FuturesProduct> getProducts() {
        return products;
    }

    public void setProducts(List<FuturesProduct> products) {
        this.products = products;
    }
}

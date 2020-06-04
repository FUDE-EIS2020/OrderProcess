package futureTrading.entities;

import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table
public class Broker {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private  String token;

  @OneToMany(cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY,
      targetEntity=FuturesProduct.class)
  private List<FuturesProduct> products;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
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

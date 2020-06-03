package FuturesTrading.entities;


import java.util.ArrayList;
import javax.persistence.*;


@Entity
@Table
public class Broker {
  @Id
  @GeneratedValue(generator = "jpa-uuid")
  private String id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private  String token;

  @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, targetEntity=FuturesProduct.class, mappedBy = "broker")
  @JoinTable
  private ArrayList<FuturesProduct> products;

  public String getId() {
    return id;
  }

  public void setId(String id) {
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

  public ArrayList<FuturesProduct> getProducts() {
    return products;
  }

  public void setProducts(ArrayList<FuturesProduct> products) {
    this.products = products;
  }
}

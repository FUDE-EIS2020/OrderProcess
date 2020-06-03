package futureTrading.entities;
import javax.persistence.*;

@Entity
@Table
public class FuturesProduct {
  @Id
  @GeneratedValue(generator = "jpa-uuid")
  private String id;

  @Column(nullable = false)
  String name;

  @Column(nullable = false)
  String period;

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

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }
}

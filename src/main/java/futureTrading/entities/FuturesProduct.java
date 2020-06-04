package futureTrading.entities;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class FuturesProduct {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID id;

  @Column(nullable = false)
  String name;

  @Column(nullable = false)
  String period;

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

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }
}

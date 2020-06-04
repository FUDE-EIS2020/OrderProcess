package futureTrading.entities;


import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class FuturesOrder {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID id;

  @ManyToOne(cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY,
      targetEntity=FuturesProduct.class)
  private FuturesProduct product;

  @Column(nullable = false)
  private Double price;

  @Column(nullable = false)
  private Integer amount;

  @Column(nullable = false)
  private OrderType orderType;

  @Column(nullable = false)
  private OrderState orderState;

  @Column(nullable = false)
  private Date createTime;

  @Column(nullable = true)
  private Date finishTime;

  @ManyToOne(cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY,
      targetEntity=Broker.class)
  private Broker broker;

  @ManyToOne(cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY,
      targetEntity=Trader.class)
  private Trader seller;

  @ManyToOne(cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY,
      targetEntity=Trader.class)
  private Trader buyer;

  @Column(nullable = false)
  private OrderInitiator initiator;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public FuturesProduct getProduct() {
    return product;
  }

  public void setProduct(FuturesProduct product) {
    this.product = product;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public void setOrderType(OrderType orderType) {
    this.orderType = orderType;
  }

  public OrderState getOrderState() {
    return orderState;
  }

  public void setOrderState(OrderState orderState) {
    this.orderState = orderState;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(Date finishTime) {
    this.finishTime = finishTime;
  }

  public Broker getBroker() {
    return broker;
  }

  public void setBroker(Broker broker) {
    this.broker = broker;
  }

  public Trader getSeller() {
    return seller;
  }

  public void setSeller(Trader seller) {
    this.seller = seller;
  }

  public Trader getBuyer() {
    return buyer;
  }

  public void setBuyer(Trader buyer) {
    this.buyer = buyer;
  }

  public OrderInitiator getInitiator() {
    return initiator;
  }

  public void setInitiator(OrderInitiator initiator) {
    this.initiator = initiator;
  }

  public enum OrderState {
    PENDING(false), FINISHED(true);
    private Boolean isFinished;
    private OrderState(Boolean isFinished) {
      this.isFinished = isFinished;
    }

    public Boolean isFinished() {
      return this.isFinished;
    }

    public Boolean isPending() {
      return !this.isFinished;
    }
  }

  public enum OrderInitiator {
    SELLER, BUYER
  }

  // todo: add more api about this enum
  public enum OrderType {
    MARKET(0), LIMIT(1), STOP(2), CANCEL(-1);
    private Integer index;
    private OrderType(Integer index) {
      this.index = index;
    }
  }
}

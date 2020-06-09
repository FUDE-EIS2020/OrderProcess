package futureTrading.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class OrderInMD implements Serializable {
    private Long id;
    private Double price;
    private Integer amount;
    private Date createTime;

    // 发起这个单的trader的id，是string类型的，需要转成UUID后使用
    private String traderId;

    // 表示这个单是买单或卖单，属性值'buy' or 'sell'
    private String type;

    // 表示这个是一个market order或者stop order，属性值为"M" or "S"
    private String tag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

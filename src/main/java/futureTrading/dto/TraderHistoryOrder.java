package futureTrading.dto;
/*
trader order history
[
{
                        'productName': 'WIT SPET-2020',
                        'broker': 'Everbright Securities Co., Ltd.',
                        'orderType': 'Limit Order',
                        'price': 15.12,
                        'volume': 10,
                        'totalPrice': 12341,
                        'state': 'Pending',
                        'updateTime': 'Fri Jun 12 2020 23:31:19 GMT+0800 (China Standard Time)',
                        'buyOrSell': "Buy",
                    },]

 */
public class TraderHistoryOrder {
    private String productName;
    private String broker;
    private String orderType;
    private String state;
    private String updateTime;
    private String buyOrSell;
    private int volume;
    private double price;
    private double totalPrice;
    private Long productId;
    private Long brokerId;
    private Long orderId;

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setBrokerId(Long brokerId) {
        this.brokerId = brokerId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getBrokerId() {
        return brokerId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public String getBroker() {
        return broker;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getState() {
        return state;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getBuyOrSell() {
        return buyOrSell;
    }

    public int getVolume() {
        return volume;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setBuyOrSell(String buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

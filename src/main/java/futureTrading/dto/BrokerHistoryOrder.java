package futureTrading.dto;


/*
broker order history
[
                    {
                        'productName': 'WIT SPET-2020',
                        'sellerComp': 'Everbright Securities Co., Ltd.',
                        'seller': 'iceFrog',
                        'orderType': 'Limit Order',
                        'price': 15.12,
                        'volume': 10,
                        'totalPrice': 12341,
                        'state': 'Pending',
                        'updateTime': 'Fri Jun 12 2020 23:31:19 GMT+0800 (China Standard Time)',
                        'buyOrSell': "Buy",
                    },]
 */
public class BrokerHistoryOrder {
    private String productName;
    private String sellerComp;  // in fact this is initiator's company
    private String seller;
    private String orderType;
    private double price;
    private int volume;
    private double totalPrice;
    private String state;
    private String updateTime;
    private String buyOrSell;

    public String getOrderType() {
        return orderType;
    }

    public String getProductName() {
        return productName;
    }

    public String getSellerComp() {
        return sellerComp;
    }

    public String getSeller() {
        return seller;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public double getTotalPrice() {
        return totalPrice;
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

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setSellerComp(String sellerComp) {
        this.sellerComp = sellerComp;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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
}

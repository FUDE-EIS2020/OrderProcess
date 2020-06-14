package futureTrading.dto;

import com.alibaba.fastjson.JSONArray;

public class MarketDepthChangeMsg {
    private String brokerId;
    private String productId;
    private JSONArray marketDepth;

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public JSONArray getMarketDepth() {
        return marketDepth;
    }

    public void setMarketDepth(JSONArray marketDepth) {
        this.marketDepth = marketDepth;
    }
}

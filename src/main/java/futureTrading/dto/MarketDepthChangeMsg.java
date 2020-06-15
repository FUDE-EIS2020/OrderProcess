package futureTrading.dto;

import com.alibaba.fastjson.JSONArray;

import java.util.Date;

public class MarketDepthChangeMsg {
    private String brokerId;
    private String productId;
    private JSONArray marketDepth;
    private Date lastUpdate;

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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

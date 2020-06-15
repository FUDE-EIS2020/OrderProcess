package futureTrading.dto;

import java.util.List;

public class AllProductsPrice {
    List<ProductPrice> productPrices;

    public List<ProductPrice> getProductPrices() {
        return productPrices;
    }

    public void setProductPrices(List<ProductPrice> productPrices) {
        this.productPrices = productPrices;
    }
}

package business;

import java.math.BigDecimal;

/** Immutable result shared by the preview, registration and report tests. */
public final class SaleResult {
    public final String model;
    public final BigDecimal unitPrice;
    public final int quantity;
    public final BigDecimal gross;
    public final BigDecimal discountRate;
    public final BigDecimal discount;
    public final BigDecimal total;
    public final String gift;
    public final int giftQuantity;

    SaleResult(Product product, int quantity, BigDecimal gross, BigDecimal rate,
            BigDecimal discount, String gift, int gifts) {
        this.model = product.name;
        this.unitPrice = product.price;
        this.quantity = quantity;
        this.gross = gross;
        this.discountRate = rate;
        this.discount = discount;
        this.total = gross.subtract(discount);
        this.gift = gift;
        this.giftQuantity = gifts;
    }
}

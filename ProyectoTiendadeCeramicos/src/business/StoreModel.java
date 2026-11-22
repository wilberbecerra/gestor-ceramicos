package business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Session data and sale rules. Deliberately no database or file persistence. */
public final class StoreModel {
    public static final int MAX_QUANTITY = 100000;
    private static final StoreModel SHARED = new StoreModel();
    private final List<Product> products = new ArrayList<>();
    private final int[] saleCounts = new int[5];
    private final int[] quantities = new int[5];
    private final BigDecimal[] amounts = new BigDecimal[5];
    private BigDecimal[] discounts = {decimal("7.5"), decimal("10"), decimal("12.5"), decimal("15")};
    private int[] gifts = {2, 3, 4};
    private String gift = "Lapicero";
    private int optimal = 10;
    private BigDecimal quota = decimal("30000.00");

    public StoreModel() {
        products.add(new Product("Cinza Plus", decimal("92.56"), 62, 62, 8, 6));
        products.add(new Product("Luxury", decimal("42.77"), 60, 60, 8.5, 4));
        products.add(new Product("Austria", decimal("52.45"), 45, 45, 6.5, 12));
        products.add(new Product("Yungay Mix", decimal("55.89"), 80, 120, 6.8, 9));
        products.add(new Product("Thalía", decimal("45.00"), 45, 11.8, 7.2, 10));
        java.util.Arrays.fill(amounts, decimal("0.00"));
    }

    public static StoreModel shared() { return SHARED; }
    public List<Product> products() { return Collections.unmodifiableList(products); }
    public Product product(int index) { return products.get(index); }

    public void updateProduct(int index, BigDecimal price, double width, double length,
            double thickness, int content) {
        Product previous = product(index);
        products.set(index, new Product(previous.name, price, width, length, thickness, content));
    }

    public SaleResult preview(int index, int quantity) {
        if (quantity < 1 || quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("Ingresa una cantidad entre 1 y 100 000 cajas.");
        }
        Product product = product(index);
        int range = quantity <= 5 ? 0 : quantity <= 10 ? 1 : quantity <= 15 ? 2 : 3;
        BigDecimal gross = product.price.multiply(BigDecimal.valueOf(quantity)).setScale(2);
        BigDecimal discount = gross.multiply(discounts[range])
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        int giftQuantity = Math.multiplyExact(quantity, gifts[Math.min(range, 2)]);
        return new SaleResult(product, quantity, gross, discounts[range], discount, gift, giftQuantity);
    }

    public SaleResult sell(int index, int quantity) {
        SaleResult result = preview(index, quantity);
        int nextCount = Math.incrementExact(saleCounts[index]);
        int nextQuantity = Math.addExact(quantities[index], quantity);
        BigDecimal nextAmount = amounts[index].add(result.total);
        saleCounts[index] = nextCount;
        quantities[index] = nextQuantity;
        amounts[index] = nextAmount;
        return result;
    }

    public int saleCount(int index) { return saleCounts[index]; }
    public int quantity(int index) { return quantities[index]; }
    public BigDecimal amount(int index) { return amounts[index]; }
    public int totalSales() { return java.util.Arrays.stream(saleCounts).sum(); }
    public int totalQuantity() { return java.util.Arrays.stream(quantities).sum(); }
    public BigDecimal totalAmount() {
        BigDecimal total = decimal("0.00");
        for (BigDecimal amount : amounts) total = total.add(amount);
        return total;
    }

    public BigDecimal quotaShare(int index) {
        return amounts[index].multiply(BigDecimal.valueOf(100)).divide(quota, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal totalQuotaShare() {
        return totalAmount().multiply(BigDecimal.valueOf(100)).divide(quota, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal averagePrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (Product product : products) total = total.add(product.price);
        return total.divide(BigDecimal.valueOf(products.size()), 8, RoundingMode.HALF_UP);
    }

    public BigDecimal minPrice() {
        return products.stream().map(p -> p.price).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    public BigDecimal maxPrice() {
        return products.stream().map(p -> p.price).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    public BigDecimal[] discounts() { return discounts.clone(); }
    public void setDiscounts(BigDecimal[] values) {
        if (values == null || values.length != 4) throw new IllegalArgumentException("Completa los cuatro porcentajes.");
        for (BigDecimal value : values) {
            if (value == null || value.signum() < 0 || value.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new IllegalArgumentException("Los descuentos deben estar entre 0 y 100 %.");
            }
        }
        discounts = values.clone();
    }

    public String gift() { return gift; }
    public int[] gifts() { return gifts.clone(); }
    public void setGifts(String name, int[] values) {
        if (name == null || name.trim().isEmpty() || name.trim().length() > 60
                || values == null || values.length != 3) {
            throw new IllegalArgumentException("Indica un obsequio y completa las tres cantidades.");
        }
        for (int value : values) {
            if (value < 0 || value > 1000) throw new IllegalArgumentException("Ingresa de 0 a 1 000 obsequios por caja.");
        }
        gift = name.trim();
        gifts = values.clone();
    }

    public int optimal() { return optimal; }
    public void setOptimal(int value) {
        if (value < 1) throw new IllegalArgumentException("La cantidad óptima debe ser mayor que cero.");
        optimal = value;
    }

    public BigDecimal quota() { return quota; }
    public void setQuota(BigDecimal value) {
        if (value == null || value.signum() <= 0 || value.setScale(2, RoundingMode.HALF_UP).signum() <= 0) {
            throw new IllegalArgumentException("La cuota debe ser mayor que cero.");
        }
        quota = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal decimal(String text) {
        if (text == null || !text.trim().matches("[+-]?[0-9]+(?:[.,][0-9]+)?")) {
            throw new IllegalArgumentException("Ingresa un número válido, sin separadores de miles.");
        }
        return new BigDecimal(text.trim().replace(',', '.'));
    }

    public static int integer(String text) {
        try { return Integer.parseInt(text.trim()); }
        catch (RuntimeException error) { throw new IllegalArgumentException("Ingresa una cantidad entera válida."); }
    }
}

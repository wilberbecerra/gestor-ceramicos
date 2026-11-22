package business;

import java.math.BigDecimal;

/** A model in the fixed, in-memory catalog. Dimensions remain in cm and mm. */
public final class Product {
    public final String name;
    public final BigDecimal price;
    public final double width;
    public final double length;
    public final double thickness;
    public final int content;

    public Product(String name, BigDecimal price, double width, double length,
            double thickness, int content) {
        if (name == null || name.trim().isEmpty() || price == null || price.signum() <= 0
                || !positive(width) || !positive(length) || !positive(thickness) || content < 1) {
            throw new IllegalArgumentException("Revisa los datos: todos los valores deben ser positivos.");
        }
        this.name = name;
        this.price = price.setScale(2, java.math.RoundingMode.HALF_UP);
        if (this.price.signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser de al menos S/ 0.01.");
        }
        this.width = width;
        this.length = length;
        this.thickness = thickness;
        this.content = content;
    }

    private static boolean positive(double value) {
        return Double.isFinite(value) && value > 0;
    }

    @Override public String toString() { return name; }
}

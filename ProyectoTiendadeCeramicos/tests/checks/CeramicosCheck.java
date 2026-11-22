package checks;

import business.*;
import ui.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.*;

/** No external test framework: golden calculations and real Swing event checks. */
public final class CeramicosCheck {
    private static int assertions;

    private static void check(boolean value, String description) {
        assertions++;
        if (!value) throw new AssertionError(description);
    }

    private static void amount(String expected, BigDecimal actual) {
        check(new BigDecimal(expected).compareTo(actual) == 0, expected + " != " + actual);
    }

    private static void invalid(Runnable action, String description) {
        boolean rejected = false;
        try { action.run(); } catch (IllegalArgumentException error) { rejected = true; }
        check(rejected, description);
    }

    private static Component find(Component root, String name) {
        if (name.equals(root.getName())) return root;
        if (root instanceof Container) {
            for (Component child : ((Container) root).getComponents()) {
                Component found = find(child, name);
                if (found != null) return found;
            }
        }
        return null;
    }

    private static JTextField input(Component root, String name) {
        Component field = find(root, name);
        check(field instanceof JTextField, "Field exists: " + name);
        return (JTextField) field;
    }

    private static void click(Component root, String name) {
        Component button = find(root, name);
        check(button instanceof JButton, "Button exists: " + name);
        ((JButton) button).doClick(0);
    }

    private static void businessChecks() {
        StoreModel store = new StoreModel();
        check(store.products().size() == 5, "Five original models");
        check(store.product(4).name.equals("Thalía"), "Fifth model preserved");
        check(store.product(4).length == 11.8, "Original dimensions preserved");
        int[] quantities = {1, 5, 6, 10, 11, 15, 16, 100};
        String[] totals = {"85.62", "428.09", "499.82", "833.04", "890.89", "1214.85", "1258.82", "7867.60"};
        int[] gifts = {2, 10, 18, 30, 44, 60, 64, 400};
        for (int i = 0; i < quantities.length; i++) {
            SaleResult result = store.preview(0, quantities[i]);
            amount(totals[i], result.total);
            check(result.giftQuantity == gifts[i], "Gift boundary " + quantities[i]);
        }
        check(store.totalSales() == 0, "Preview does not register a sale");
        invalid(() -> store.sell(0, 0), "Zero quantity rejected");
        invalid(() -> store.sell(0, -1), "Negative quantity rejected");
        invalid(() -> store.sell(0, 100001), "Excessive quantity rejected");
        store.sell(0, 10);
        amount("2.78", store.quotaShare(0));
        amount("833.04", store.totalAmount());
        store.sell(4, 1);
        check(store.saleCount(4) == 1 && store.quantity(4) == 1, "Fifth model contributes to reports");
        check(store.totalSales() == 2, "Registered sale count");
        amount("42.77", store.minPrice()); amount("92.56", store.maxPrice());
        check(store.averagePrice().setScale(2, java.math.RoundingMode.HALF_UP).toPlainString().equals("57.73"), "Average price");
        invalid(() -> store.setQuota(BigDecimal.ZERO), "Zero quota rejected");
        invalid(() -> store.setOptimal(0), "Zero optimal rejected");
        invalid(() -> store.setDiscounts(new BigDecimal[]{BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, new BigDecimal("101")}), "Invalid discount rejected atomically");
        amount("7.5", store.discounts()[0]);
        invalid(() -> store.setGifts("Muestra", new int[]{2, -1, 4}), "Negative gift rejected");
        check(store.gift().equals("Lapicero"), "Invalid gifts do not partially change state");
        invalid(() -> store.updateProduct(0, BigDecimal.ZERO, 62, 62, 8, 6), "Invalid price rejected");
        invalid(() -> store.updateProduct(0, new BigDecimal("92.56"), Double.NaN, 62, 8, 6), "NaN dimensions rejected");
        amount("92.56", store.product(0).price);
        invalid(() -> StoreModel.decimal("NaN"), "NaN input rejected");
        invalid(() -> StoreModel.decimal("1,2.3"), "Ambiguous decimal input rejected");
        amount("92.56", StoreModel.decimal("92,56"));
        Locale original = Locale.getDefault();
        try {
            Locale.setDefault(new Locale("es", "ES"));
            amount("833.04", new StoreModel().sell(0, 10).total);
            check(AppTheme.money(new BigDecimal("92.56")).equals("S/ 92.56"), "Display does not corrupt locale-independent calculations");
        } finally { Locale.setDefault(original); }
    }

    private static void layout(Container container) {
        container.doLayout();
        for (Component child : container.getComponents()) if (child instanceof Container) layout((Container) child);
    }

    private static void prepare(JFrame frame, int width, int height) {
        frame.setSize(width, height);
        if (!frame.isDisplayable()) frame.addNotify();
        frame.validate();
        for (int i = 0; i < 3; i++) layout(frame.getRootPane());
    }

    private static void capture(JFrame frame, Path directory, String file) throws Exception {
        prepare(frame, frame instanceof Login ? 1120 : 1280, frame instanceof Login ? 740 : 900);
        JRootPane root = frame.getRootPane();
        // Render the native hierarchy at a documented review size, independently
        // of the monitor's work area. Small live windows keep their scrollbars.
        if (!(frame instanceof Login)) {
            root.setSize(1264, Math.max(900, root.getPreferredSize().height));
            for (int i = 0; i < 3; i++) layout(root);
        }
        BufferedImage image = new BufferedImage(root.getWidth(), root.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics(); graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        RepaintManager.currentManager(root).setDoubleBufferingEnabled(false);
        root.printAll(graphics); graphics.dispose();
        ImageIO.write(image, "png", directory.resolve(file + ".png").toFile());
    }

    private static void swingChecks(Path directory) throws Exception {
        AppTheme.install();
        AtomicInteger authenticated = new AtomicInteger();
        Login login = new Login(new StoreModel(), user -> authenticated.incrementAndGet());
        try {
            prepare(login, 1120, 740);
            capture(login, directory, "01-acceso");
            input(login, "username").setText("WilberBecerra");
            input(login, "password").setText("incorrecta"); click(login, "login");
            check(authenticated.get() == 0, "Incorrect login rejected");
            check(((MessageBar) find(login, "feedback")).isVisible(), "Login error is visible");
            input(login, "password").setText("1234"); click(login, "login");
            check(authenticated.get() == 1, "Correct login accepted");
        } finally { login.dispose(); }

        StoreModel uiStore = new StoreModel();
        Tienda frame = new Tienda(uiStore, "WilberBecerra");
        try {
            prepare(frame, 1280, 840);
            click(frame, "navSALE"); check(frame.currentView() == ViewId.SALE, "Native navigation action");
            input(frame, "saleQuantity").setText("0"); click(frame, "sell");
            check(uiStore.totalSales() == 0, "UI rejects invalid quantity");
            input(frame, "saleQuantity").setText("10"); click(frame, "sell");
            check(uiStore.totalSales() == 1, "UI registers exactly one sale");
            check(((JLabel) find(frame, "receiptTotal")).getText().equals("S/ 833.04"), "Native receipt shows correct amount");
            frame.showView(ViewId.REPORTS);
            JTable report = (JTable) find(frame, "reportTable");
            check(report.getRowCount() == 5, "Native report contains five models");
            check(report.getValueAt(4, 0).equals("Thalía"), "Native report includes Thalía");
            check(report.getValueAt(0, 4).equals("2.78 %"), "Correct native contribution percentage");
            JComboBox<?> type = (JComboBox<?>) find(frame, "reportType");
            for (int i = 0; i < 4; i++) {
                type.setSelectedIndex(i); JTable table = (JTable) find(frame, "reportTable");
                check(table.getRowCount() == (i == 3 ? 3 : 5), "Report type " + i);
            }
            frame.showView(ViewId.QUOTA); input(frame, "quota").setText("0"); click(frame, "saveConfig");
            amount("30000.00", uiStore.quota());
            input(frame, "quota").setText("40000,50"); click(frame, "saveConfig"); amount("40000.50", uiStore.quota());
            frame.context().selected = 4; frame.showView(ViewId.EDIT);
            input(frame, "productContent").setText("0"); click(frame, "saveProduct");
            check(uiStore.product(4).content == 10, "Invalid edit preserves product");
            input(frame, "productContent").setText("10"); input(frame, "productPrice").setText("51,25"); click(frame, "saveProduct");
            amount("51.25", uiStore.product(4).price);
            prepare(frame, 1024, 720); frame.showView(ViewId.SALE); prepare(frame, 1024, 720);
            Component quantity = find(frame, "saleQuantity");
            check(quantity.getWidth() > 90 && quantity.getHeight() < 65, "Field usable at 1024px");
            frame.showView(ViewId.GIFTS); prepare(frame, 1024, 720);
            JComponent save = (JComponent) find(frame, "saveConfig");
            save.scrollRectToVisible(new Rectangle(0, 0, save.getWidth(), save.getHeight()));
            check(!save.getVisibleRect().isEmpty(), "Save action reachable by scrolling in a small window");
        } finally { frame.dispose(); }

        StoreModel captureStore = new StoreModel();
        Tienda screenshots = new Tienda(captureStore, "WilberBecerra");
        try {
            ViewId[] views = {ViewId.HOME, ViewId.CONSULT, ViewId.EDIT, ViewId.CATALOG, ViewId.SALE,
                    ViewId.REPORTS, ViewId.DISCOUNTS, ViewId.GIFTS, ViewId.OPTIMAL, ViewId.QUOTA, ViewId.ABOUT};
            String[] names = {"02-inicio", "03-consultar", "04-modificar", "05-listado", "06-venta",
                    "07-reportes", "08-descuentos", "09-obsequios", "10-cantidad-optima", "11-cuota-diaria", "12-acerca-de"};
            for (int i = 0; i < views.length; i++) {
                screenshots.showView(views[i]);
                prepare(screenshots, 1280, 840);
                if (views[i] == ViewId.SALE) {
                    input(screenshots, "saleQuantity").setText("10"); click(screenshots, "sell");
                }
                capture(screenshots, directory, names[i]);
            }
        } finally { screenshots.dispose(); }
    }

    public static void main(String[] args) throws Exception {
        businessChecks();
        Path images = Path.of(System.getProperty("ceramicos.captures", "build/capturas"));
        Files.createDirectories(images);
        final Throwable[] failure = new Throwable[1];
        SwingUtilities.invokeAndWait(() -> {
            try { swingChecks(images); } catch (Throwable error) { failure[0] = error; }
        });
        if (failure[0] != null) throw new AssertionError("Native Swing checks failed", failure[0]);
        System.out.println("PASS: " + assertions + " checks; 12 native Swing views rendered. Captures: " + images.toAbsolutePath());
    }
}

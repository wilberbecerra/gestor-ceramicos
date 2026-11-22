package ui;

import business.Product;
import business.SaleResult;
import business.StoreModel;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** The native panels shared by the main window and the original dialog classes. */
public final class StoreViews {
    private StoreViews() {}

    public static final class Context {
        public final StoreModel store;
        public final Consumer<ViewId> navigate;
        public int selected;
        public Context(StoreModel store, Consumer<ViewId> navigate) {
            this.store = store;
            this.navigate = navigate;
        }
    }

    public static JPanel create(ViewId view, Context context) {
        switch (view) {
            case HOME: return catalog(context, true);
            case CATALOG: return catalog(context, false);
            case CONSULT: return product(context, false);
            case EDIT: return product(context, true);
            case SALE: return sale(context);
            case REPORTS: return reports(context);
            case DISCOUNTS: case GIFTS: case OPTIMAL: case QUOTA: return configuration(context, view);
            case ABOUT: return about();
            default: throw new IllegalArgumentException("Vista desconocida");
        }
    }

    private static JPanel page(String title, String description, JComponent action) {
        JPanel page = new PagePanel();
        page.setName("page");
        page.setBorder(new EmptyBorder(24, 24, 24, 24));
        page.add(AppTheme.heading(title, description, action), BorderLayout.NORTH);
        return page;
    }

    private static JPanel pair(JComponent left, JComponent right, double leftWeight) {
        JPanel pair = AppTheme.panel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.weightx = leftWeight; c.weighty = 1;
        c.fill = GridBagConstraints.BOTH; c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, 0, 0, 20); pair.add(left, c);
        c.gridx = 1; c.weightx = 1 - leftWeight; c.insets = new Insets(0, 0, 0, 0); pair.add(right, c);
        left.setMinimumSize(new Dimension(0, 0)); right.setMinimumSize(new Dimension(0, 0));
        return pair;
    }

    private static JPanel grid(int columns, JComponent... fields) {
        JPanel grid = AppTheme.panel(new GridBagLayout());
        for (int i = 0; i < fields.length; i++) {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = i % columns; c.gridy = i / columns; c.weightx = 1;
            c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(0, 0, 16, i % columns == columns - 1 ? 0 : 20);
            grid.add(fields[i], c);
        }
        GridBagConstraints space = new GridBagConstraints();
        space.gridx = 0; space.gridy = (fields.length + columns - 1) / columns;
        space.gridwidth = columns; space.weighty = 1; space.fill = GridBagConstraints.BOTH;
        grid.add(Box.createVerticalGlue(), space);
        return grid;
    }

    private static JPanel withFeedback(JComponent body, MessageBar feedback, JPanel actions) {
        JPanel panel = AppTheme.panel(new BorderLayout(0, 18));
        panel.add(body, BorderLayout.CENTER);
        JPanel bottom = AppTheme.column();
        feedback.setAlignmentX(Component.LEFT_ALIGNMENT);
        feedback.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        bottom.add(feedback);
        bottom.add(Box.createVerticalStrut(12));
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        bottom.add(actions);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private static JPanel catalog(Context context, boolean home) {
        JPanel page = page(home ? "Catálogo de cerámicos" : "Listado de cerámicos",
                "Consulta los modelos, formatos y precios disponibles.",
                AppTheme.button("Nueva venta", "newSale", true, () -> context.navigate.accept(ViewId.SALE)));
        Object[][] data = new Object[context.store.products().size()][5];
        for (int i = 0; i < data.length; i++) {
            Product p = context.store.product(i);
            data[i] = new Object[]{p.name, AppTheme.money(p.price), AppTheme.number(p.width) + " × "
                    + AppTheme.number(p.length) + " cm", AppTheme.number(p.thickness) + " mm", p.content};
        }
        JTable table = AppTheme.table("catalogTable", new String[]{"Modelo", "Precio por caja", "Formato", "Espesor", "Piezas / caja"}, data);
        table.getColumnModel().getColumn(0).setPreferredWidth(170);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.getViewport().setBackground(AppTheme.CARD);
        tableScroll.setPreferredSize(new Dimension(home ? 590 : 900, 252));
        tableScroll.setMinimumSize(new Dimension(0, 100));
        JPanel left = AppTheme.panel(new BorderLayout(0, 14));
        left.add(tableScroll, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(AppTheme.button("Consultar selección", "consultSelected", false, () -> context.navigate.accept(ViewId.CONSULT)));
        buttons.add(AppTheme.button("Modificar selección", "editSelected", false, () -> context.navigate.accept(ViewId.EDIT)));
        left.add(buttons, BorderLayout.SOUTH);
        JPanel right = AppTheme.panel(new BorderLayout(0, 12));
        AppTheme.surface(right, AppTheme.CARD);
        right.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.LINE),
                new EmptyBorder(12, 12, 12, 12)));
        JPanel picture = AppTheme.panel(new BorderLayout(0, 8));
        picture.add(new AppTheme.MaterialPanel(), BorderLayout.CENTER);
        picture.add(AppTheme.label("Ambiente ilustrativo de cerámicos.", 11, false, AppTheme.MUTED), BorderLayout.SOUTH);
        picture.setPreferredSize(new Dimension(245, 150));
        right.add(picture, BorderLayout.NORTH);
        JPanel specs = AppTheme.column();
        right.add(specs, BorderLayout.CENTER);
        Runnable update = () -> {
            Product p = context.store.product(context.selected);
            specs.removeAll();
            specs.add(infoRow("Modelo seleccionado", p.name, false));
            specs.add(infoRow("Formato", AppTheme.number(p.width) + " × " + AppTheme.number(p.length) + " cm", false));
            specs.add(infoRow("Espesor", AppTheme.number(p.thickness) + " mm", false));
            specs.add(infoRow("Contenido", p.content + " piezas por caja", false));
            specs.revalidate(); specs.repaint();
        };
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                context.selected = table.getSelectedRow(); update.run();
            }
        });
        table.setRowSelectionInterval(context.selected, context.selected);
        update.run();
        page.add(home ? pair(left, right, .72) : left, BorderLayout.CENTER);
        return page;
    }

    private static JPanel product(Context context, boolean edit) {
        JPanel page = page(edit ? "Modificar cerámico" : "Consultar cerámico",
                edit ? "Actualiza las características del modelo seleccionado." : "Revisa la ficha del modelo seleccionado.", null);
        JPanel content = AppTheme.panel(new BorderLayout(0, 16));
        JPanel tabs = AppTheme.panel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tabs.add(AppTheme.button("Consultar", "goConsult", false, () -> context.navigate.accept(ViewId.CONSULT)));
        tabs.add(AppTheme.button("Modificar", "goEdit", false, () -> context.navigate.accept(ViewId.EDIT)));
        tabs.add(AppTheme.button("Ver listado", "goCatalog", false, () -> context.navigate.accept(ViewId.CATALOG)));
        content.add(tabs, BorderLayout.NORTH);
        JPanel form = AppTheme.formPanel();
        JComboBox<Product> model = AppTheme.combo("productModel", context.store.products().toArray(new Product[0]));
        model.setSelectedIndex(context.selected);
        JTextField price = AppTheme.input("productPrice", "", edit);
        JTextField count = AppTheme.input("productContent", "", edit);
        JTextField width = AppTheme.input("productWidth", "", edit);
        JTextField length = AppTheme.input("productLength", "", edit);
        JTextField thickness = AppTheme.input("productThickness", "", edit);
        Runnable fill = () -> {
            Product p = context.store.product(context.selected);
            price.setText(p.price.toPlainString()); count.setText(String.valueOf(p.content));
            width.setText(AppTheme.number(p.width)); length.setText(AppTheme.number(p.length));
            thickness.setText(AppTheme.number(p.thickness));
        };
        fill.run();
        model.addActionListener(e -> { context.selected = model.getSelectedIndex(); fill.run(); });
        JPanel fields = grid(3, AppTheme.field("Modelo", model),
                AppTheme.field("Precio por caja (S/)", price), AppTheme.field("Contenido por caja", count),
                AppTheme.field("Ancho (cm)", width), AppTheme.field("Largo (cm)", length),
                AppTheme.field("Espesor (mm)", thickness));
        MessageBar feedback = new MessageBar();
        JButton back = AppTheme.button(edit ? "Cancelar" : "Volver al listado", "cancelProduct", false,
                () -> context.navigate.accept(ViewId.CATALOG));
        JButton save = AppTheme.button("Guardar cambios", "saveProduct", true, () -> {
            try {
                context.store.updateProduct(context.selected, StoreModel.decimal(price.getText()),
                        StoreModel.decimal(width.getText()).doubleValue(), StoreModel.decimal(length.getText()).doubleValue(),
                        StoreModel.decimal(thickness.getText()).doubleValue(), StoreModel.integer(count.getText()));
                fill.run(); feedback.showMessage("Características actualizadas en esta sesión.", false);
            } catch (IllegalArgumentException error) { feedback.showMessage(error.getMessage(), true); }
        });
        form.add(withFeedback(fields, feedback, edit ? AppTheme.actions(back, save) : AppTheme.actions(back)), BorderLayout.CENTER);
        content.add(form, BorderLayout.CENTER); page.add(content, BorderLayout.CENTER);
        return page;
    }

    private static JPanel sale(Context context) {
        JPanel page = page("Registrar venta", "Selecciona el modelo y la cantidad de cajas.", null);
        JPanel form = AppTheme.formPanel();
        JComboBox<Product> model = AppTheme.combo("saleModel", context.store.products().toArray(new Product[0]));
        model.setSelectedIndex(context.selected);
        JTextField price = AppTheme.input("saleUnitPrice", AppTheme.money(context.store.product(context.selected).price), false);
        JTextField quantity = AppTheme.input("saleQuantity", "", true);
        JPanel fields = AppTheme.panel(new BorderLayout(0, 22));
        fields.add(AppTheme.field("Modelo", model), BorderLayout.NORTH);
        fields.add(grid(2, AppTheme.field("Precio por caja", price), AppTheme.field("Cantidad de cajas", quantity)), BorderLayout.CENTER);
        MessageBar feedback = new MessageBar();
        JPanel receipt = AppTheme.panel(new BorderLayout());
        receipt.setName("receipt");
        AppTheme.surface(receipt, AppTheme.CARD);
        receipt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.LINE), new EmptyBorder(20, 20, 20, 20)));
        Runnable preview = () -> {
            try {
                SaleResult result = context.store.preview(context.selected, StoreModel.integer(quantity.getText()));
                receipt(receipt, result, false);
            } catch (IllegalArgumentException error) { receipt(receipt, null, false); }
        };
        model.addActionListener(e -> {
            context.selected = model.getSelectedIndex();
            price.setText(AppTheme.money(context.store.product(context.selected).price)); preview.run();
        });
        quantity.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { preview.run(); }
            @Override public void removeUpdate(DocumentEvent e) { preview.run(); }
            @Override public void changedUpdate(DocumentEvent e) { preview.run(); }
        });
        JButton sell = AppTheme.button("Registrar venta", "sell", true, () -> {
            try {
                SaleResult result = context.store.sell(context.selected, StoreModel.integer(quantity.getText()));
                receipt(receipt, result, true);
                String message = "Venta registrada. Total a pagar: " + AppTheme.money(result.total) + ".";
                if (context.store.totalSales() % 5 == 0) {
                    message += " Ventas: " + context.store.totalSales() + ". Acumulado: "
                            + AppTheme.money(context.store.totalAmount()) + ". Aporte a cuota: "
                            + context.store.totalQuotaShare().toPlainString() + " %.";
                }
                feedback.showMessage(message, false);
            } catch (IllegalArgumentException error) { feedback.showMessage(error.getMessage(), true); }
            catch (ArithmeticException error) { feedback.showMessage("La cantidad supera el límite admitido.", true); }
        });
        quantity.addActionListener(e -> sell.doClick());
        form.add(withFeedback(fields, feedback, AppTheme.actions(sell)), BorderLayout.CENTER);
        preview.run(); page.add(pair(form, receipt, .46), BorderLayout.CENTER);
        return page;
    }

    private static void receipt(JPanel panel, SaleResult result, boolean registered) {
        panel.removeAll();
        JPanel body = AppTheme.column();
        body.add(AppTheme.label(registered ? "Venta registrada" : "Resumen de venta", 17, true, AppTheme.INK));
        body.add(Box.createVerticalStrut(16));
        if (result == null) {
            body.add(AppTheme.label("Ingresa una cantidad válida para calcular el importe.", 13, false, AppTheme.MUTED));
        } else {
            body.add(infoRow("Modelo", result.model, true));
            body.add(infoRow("Cajas", String.valueOf(result.quantity), true));
            body.add(infoRow("Importe de compra", AppTheme.money(result.gross), true));
            body.add(infoRow("Descuento (" + result.discountRate.stripTrailingZeros().toPlainString() + " %)", AppTheme.money(result.discount), true));
            body.add(infoRow("Obsequio", result.giftQuantity + " × " + result.gift, true));
            body.add(Box.createVerticalStrut(18));
            JPanel total = AppTheme.panel(new BorderLayout(16, 0));
            total.add(AppTheme.label("Total a pagar", 14, false, AppTheme.MUTED), BorderLayout.WEST);
            JLabel value = AppTheme.label(AppTheme.money(result.total), 27, true, AppTheme.PRIMARY);
            value.setName("receiptTotal"); total.add(value, BorderLayout.EAST);
            total.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); body.add(total);
            body.add(Box.createVerticalStrut(16));
            body.add(AppTheme.label("Resumen de cálculo de la operación.", 12, false, AppTheme.MUTED));
        }
        panel.add(body, BorderLayout.NORTH); panel.revalidate(); panel.repaint();
    }

    private static JPanel infoRow(String title, String value, boolean soft) {
        JPanel row = AppTheme.panel(new BorderLayout(18, 0));
        row.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.LINE),
                new EmptyBorder(8, 0, 8, 0)));
        row.add(AppTheme.label(title, 12, false, AppTheme.MUTED), BorderLayout.WEST);
        row.add(AppTheme.label(value, 13, true, AppTheme.INK), BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        return row;
    }

    private static JPanel reports(Context context) {
        JPanel page = page("Reportes de ventas", "Consulta los acumulados y las comparaciones del catálogo.", null);
        JPanel content = AppTheme.panel(new BorderLayout(0, 22));
        JComboBox<String> type = AppTheme.combo("reportType", new String[]{"Ventas por modelo", "Comparación de precios",
                "Cantidad vendida y óptima", "Estadísticas de precios"});
        JPanel top = AppTheme.panel(new BorderLayout(0, 24));
        JPanel selector = AppTheme.panel(new FlowLayout(FlowLayout.LEFT, 0, 0)); selector.add(type);
        top.add(selector, BorderLayout.NORTH);
        JPanel summary = AppTheme.panel(new GridLayout(1, 3, 0, 0));
        AppTheme.surface(summary, AppTheme.CARD);
        summary.setBorder(BorderFactory.createLineBorder(AppTheme.LINE));
        String[][] figures = {{"Ventas registradas", String.valueOf(context.store.totalSales())},
                {"Cajas vendidas", String.valueOf(context.store.totalQuantity())},
                {"Importe acumulado", AppTheme.money(context.store.totalAmount())}};
        for (String[] figure : figures) {
            JPanel metric = AppTheme.column();
            AppTheme.surface(metric, AppTheme.CARD);
            metric.setBorder(new EmptyBorder(14, 18, 14, 18));
            metric.add(AppTheme.label(figure[0], 12, false, AppTheme.MUTED));
            metric.add(Box.createVerticalStrut(5)); metric.add(AppTheme.label(figure[1], 24, true, AppTheme.INK));
            summary.add(metric);
        }
        top.add(summary, BorderLayout.CENTER); content.add(top, BorderLayout.NORTH);
        JPanel results = AppTheme.panel(new BorderLayout());
        Runnable refresh = () -> {
            String[] headers; Object[][] rows;
            StoreModel store = context.store;
            switch (type.getSelectedIndex()) {
                case 0:
                    headers = new String[]{"Modelo", "Ventas", "Cajas vendidas", "Importe", "Aporte a cuota"};
                    rows = new Object[store.products().size()][5];
                    for (int i = 0; i < rows.length; i++) rows[i] = new Object[]{store.product(i).name,
                            store.saleCount(i), store.quantity(i), AppTheme.money(store.amount(i)), store.quotaShare(i).toPlainString() + " %"};
                    break;
                case 1:
                    headers = new String[]{"Modelo", "Precio por caja", "Promedio", "Diferencia"};
                    rows = new Object[store.products().size()][4];
                    for (int i = 0; i < rows.length; i++) {
                        Product product = store.product(i); BigDecimal diff = product.price.subtract(store.averagePrice());
                        String comparison = diff.signum() == 0 ? "Al promedio" : (diff.signum() > 0 ? "+ " : "− ") + AppTheme.money(diff.abs());
                        rows[i] = new Object[]{product.name, AppTheme.money(product.price), AppTheme.money(store.averagePrice()), comparison};
                    }
                    break;
                case 2:
                    headers = new String[]{"Modelo", "Cajas vendidas", "Cantidad óptima", "Comparación"};
                    rows = new Object[store.products().size()][4];
                    for (int i = 0; i < rows.length; i++) {
                        int difference = store.quantity(i) - store.optimal();
                        String comparison = difference == 0 ? "En objetivo" : Math.abs(difference)
                                + (difference > 0 ? " por encima" : " por debajo");
                        rows[i] = new Object[]{store.product(i).name, store.quantity(i), store.optimal(), comparison};
                    }
                    break;
                default:
                    headers = new String[]{"Estadística", "Precio"};
                    rows = new Object[][]{{"Precio menor", AppTheme.money(store.minPrice())},
                            {"Precio mayor", AppTheme.money(store.maxPrice())}, {"Precio promedio", AppTheme.money(store.averagePrice())}};
            }
            JTable table = AppTheme.table("reportTable", headers, rows);
            table.getColumnModel().getColumn(0).setPreferredWidth(200);
            JScrollPane tableScroll = new JScrollPane(table);
            tableScroll.getViewport().setBackground(AppTheme.CARD);
            tableScroll.setPreferredSize(new Dimension(900, rows.length * table.getRowHeight() + 44));
            results.removeAll(); results.add(tableScroll, BorderLayout.CENTER);
            results.revalidate(); results.repaint();
        };
        type.addActionListener(e -> refresh.run()); refresh.run();
        content.add(results, BorderLayout.CENTER);
        content.add(AppTheme.label("Acumulados de esta sesión. Se reinician al cerrar el programa.", 12, false, AppTheme.MUTED), BorderLayout.SOUTH);
        page.add(content, BorderLayout.CENTER); return page;
    }

    private static JPanel configuration(Context context, ViewId view) {
        JPanel page = page("Configuración", "Ajusta las reglas que utiliza la aplicación.", null);
        JPanel nav = AppTheme.panel(new GridLayout(0, 1, 0, 6));
        ViewId[] types = {ViewId.DISCOUNTS, ViewId.GIFTS, ViewId.OPTIMAL, ViewId.QUOTA};
        String[] titles = {"Descuentos", "Obsequios", "Cantidad óptima", "Cuota diaria"};
        for (int i = 0; i < types.length; i++) {
            final ViewId target = types[i];
            JButton button = AppTheme.button(titles[i], "config" + target.name(), false, () -> context.navigate.accept(target));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            if (target == view) { button.setBackground(AppTheme.TINT); button.setForeground(AppTheme.PRIMARY); }
            nav.add(button);
        }
        JPanel navHolder = AppTheme.panel(new BorderLayout()); navHolder.add(nav, BorderLayout.NORTH);
        navHolder.setPreferredSize(new Dimension(170, 190));
        JPanel form = AppTheme.formPanel();
        JPanel fields = AppTheme.column();
        String title, description;
        JTextField[] inputs;
        StoreModel store = context.store;
        if (view == ViewId.DISCOUNTS) {
            title = "Descuentos por cantidad"; description = "Porcentajes aplicados según las cajas vendidas.";
            inputs = new JTextField[4]; BigDecimal[] values = store.discounts();
            String[] labels = {"De 1 a 5 cajas", "De 6 a 10 cajas", "De 11 a 15 cajas", "Más de 15 cajas"};
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = AppTheme.input("discount" + i, values[i].stripTrailingZeros().toPlainString(), true);
                fields.add(configRow(labels[i], inputs[i], "%"));
            }
        } else if (view == ViewId.GIFTS) {
            title = "Configurar obsequios"; description = "Nombre y unidades de obsequio por cada caja vendida.";
            inputs = new JTextField[4]; inputs[0] = AppTheme.input("giftName", store.gift(), true);
            fields.add(AppTheme.field("Tipo de obsequio", inputs[0])); fields.add(Box.createVerticalStrut(20));
            String[] labels = {"De 1 a 5 cajas", "De 6 a 10 cajas", "Desde 11 cajas"};
            for (int i = 0; i < 3; i++) {
                inputs[i + 1] = AppTheme.input("gift" + i, String.valueOf(store.gifts()[i]), true);
                fields.add(configRow(labels[i], inputs[i + 1], "uds."));
            }
        } else if (view == ViewId.OPTIMAL) {
            title = "Cantidad óptima"; description = "Cantidad de referencia que utilizan los reportes.";
            inputs = new JTextField[]{AppTheme.input("optimal", String.valueOf(store.optimal()), true)};
            fields.add(AppTheme.field("Cantidad óptima de cajas", inputs[0]));
        } else {
            title = "Cuota diaria"; description = "Importe de referencia para medir el aporte de las ventas.";
            inputs = new JTextField[]{AppTheme.input("quota", store.quota().toPlainString(), true)};
            fields.add(AppTheme.field("Cuota diaria (S/)", inputs[0]));
        }
        fields.add(Box.createVerticalGlue());
        JPanel body = AppTheme.panel(new BorderLayout(0, 18));
        JPanel formHeading = AppTheme.column();
        formHeading.add(AppTheme.label(title, 19, true, AppTheme.INK)); formHeading.add(Box.createVerticalStrut(8));
        formHeading.add(AppTheme.label(description, 13, false, AppTheme.MUTED));
        body.add(formHeading, BorderLayout.NORTH); body.add(fields, BorderLayout.CENTER);
        MessageBar feedback = new MessageBar();
        JButton save = AppTheme.button("Guardar configuración", "saveConfig", true, () -> {
            try {
                if (view == ViewId.DISCOUNTS) {
                    BigDecimal[] values = new BigDecimal[4];
                    for (int i = 0; i < 4; i++) values[i] = StoreModel.decimal(inputs[i].getText());
                    store.setDiscounts(values);
                } else if (view == ViewId.GIFTS) {
                    int[] values = new int[3]; for (int i = 0; i < 3; i++) values[i] = StoreModel.integer(inputs[i + 1].getText());
                    store.setGifts(inputs[0].getText(), values);
                } else if (view == ViewId.OPTIMAL) store.setOptimal(StoreModel.integer(inputs[0].getText()));
                else store.setQuota(StoreModel.decimal(inputs[0].getText()));
                feedback.showMessage("Configuración guardada para esta sesión.", false);
            } catch (IllegalArgumentException error) { feedback.showMessage(error.getMessage(), true); }
        });
        form.add(withFeedback(body, feedback, AppTheme.actions(
                AppTheme.button("Cancelar", "cancelConfig", false, () -> context.navigate.accept(ViewId.HOME)), save)), BorderLayout.CENTER);
        JPanel layout = AppTheme.panel(new BorderLayout(20, 0));
        layout.add(navHolder, BorderLayout.WEST); layout.add(form, BorderLayout.CENTER);
        page.add(layout, BorderLayout.CENTER); return page;
    }

    private static JPanel configRow(String title, JTextField input, String unit) {
        JPanel row = AppTheme.panel(new BorderLayout(14, 0));
        row.setBorder(new EmptyBorder(8, 0, 8, 0));
        JLabel label = AppTheme.label(title, 14, false, AppTheme.INK); label.setLabelFor(input);
        input.getAccessibleContext().setAccessibleName(title);
        row.add(label, BorderLayout.CENTER);
        JPanel value = AppTheme.panel(new BorderLayout(12, 0));
        input.setPreferredSize(new Dimension(100, 40)); value.add(input, BorderLayout.CENTER);
        value.add(AppTheme.label(unit, 13, false, AppTheme.MUTED), BorderLayout.EAST);
        row.add(value, BorderLayout.EAST); row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        return row;
    }

    private static JPanel about() {
        JPanel page = page("Acerca del proyecto", "Aplicación académica de escritorio.", null);
        JTextArea text = new JTextArea("Tienda de cerámicos es el primer proyecto en Java de Wilber Becerra. Reúne consulta y modificación de productos, cálculo de ventas con descuentos y obsequios, y reportes de la operación.\n\nLa aplicación utiliza Java Swing y mantiene los datos en memoria durante la ejecución. El catálogo y los acumulados se reinician cuando se cierra el programa.\n\nVersión original 1.0.0. Desarrollado por Wilber Becerra.");
        text.setEditable(false); text.setLineWrap(true); text.setWrapStyleWord(true);
        text.setFont(AppTheme.font(15, false)); text.setForeground(AppTheme.MUTED);
        text.setRows(10);
        text.setBackground(AppTheme.CARD);
        text.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.LINE), new EmptyBorder(20, 20, 20, 40)));
        text.setName("aboutText");
        page.add(text, BorderLayout.CENTER); return page;
    }

    private static final class PagePanel extends JPanel implements Scrollable {
        PagePanel() { super(new BorderLayout()); setBackground(AppTheme.CANVAS); }
        @Override protected void addImpl(Component component, Object constraints, int index) {
            if (BorderLayout.CENTER.equals(constraints)) {
                JPanel aligned = AppTheme.panel(new BorderLayout());
                aligned.add(component, BorderLayout.NORTH);
                super.addImpl(aligned, constraints, index);
            } else super.addImpl(component, constraints, index);
        }
        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle rect, int orientation, int direction) { return 20; }
        @Override public int getScrollableBlockIncrement(Rectangle rect, int orientation, int direction) { return rect.height - 24; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport && getPreferredSize().height < getParent().getHeight();
        }
    }
}

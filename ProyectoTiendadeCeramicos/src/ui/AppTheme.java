package ui;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/** Shared typography, materials and layout helpers for the native Swing views. */
public final class AppTheme {
    public static final Color PRIMARY = new Color(0x285C4E);
    public static final Color INK = new Color(0x242C29);
    public static final Color MUTED = new Color(0x58645E);
    public static final Color LINE = new Color(0xD9E0DB);
    public static final Color SURFACE = new Color(0xF5F7F5);
    public static final Color TINT = new Color(0xEDF4EF);
    public static final Color ERROR = new Color(0x9C3333);
    public static final Color ERROR_BACKGROUND = new Color(0xFAEDED);
    private static boolean installed;
    private static BufferedImage materialImage;

    private AppTheme() {}

    public static void install() {
        if (installed) return;
        FlatLightLaf.setup();
        UIManager.put("defaultFont", font(14, false));
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Label.foreground", INK);
        UIManager.put("TextField.foreground", INK);
        UIManager.put("TextField.inactiveBackground", SURFACE);
        UIManager.put("Component.focusColor", PRIMARY);
        UIManager.put("Component.focusedBorderColor", PRIMARY);
        UIManager.put("Component.borderColor", LINE);
        UIManager.put("Component.arc", 8);
        UIManager.put("Button.arc", 8);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Button.default.background", PRIMARY);
        UIManager.put("Button.default.foreground", Color.WHITE);
        UIManager.put("Table.selectionBackground", TINT);
        UIManager.put("Table.selectionForeground", INK);
        UIManager.put("ScrollPane.border", BorderFactory.createLineBorder(LINE));
        installed = true;
    }

    public static Font font(int size, boolean bold) {
        return new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size);
    }

    public static String money(BigDecimal value) {
        return "S/ " + value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    public static String number(double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }

    public static JLabel label(String text, int size, boolean bold, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font(size, bold));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public static JPanel panel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(Color.WHITE);
        return panel;
    }

    public static JPanel column() {
        JPanel panel = panel(null);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    public static JPanel brand() {
        JPanel brand = panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        brand.add(new CeramicMark(35));
        brand.add(Box.createHorizontalStrut(13));
        JPanel names = column();
        names.add(label("Tienda de cerámicos", 20, true, INK));
        names.add(label("Gestión de ventas", 12, false, MUTED));
        brand.add(names);
        return brand;
    }

    public static JButton button(String title, String name, boolean primary, Runnable action) {
        JButton button = new JButton(title);
        button.setName(name);
        button.setFont(font(14, primary));
        button.setMargin(new Insets(10, 17, 10, 17));
        button.setMinimumSize(new Dimension(90, 42));
        if (primary) {
            button.setBackground(PRIMARY);
            button.setForeground(Color.WHITE);
        }
        if (action != null) button.addActionListener(event -> action.run());
        return button;
    }

    public static JTextField input(String name, String value, boolean editable) {
        JTextField field = new JTextField(value, 16);
        field.setName(name);
        field.setEditable(editable);
        field.setFocusable(editable);
        field.setFont(font(14, false));
        field.setMargin(new Insets(10, 12, 10, 12));
        field.setPreferredSize(new Dimension(220, 44));
        if (!editable) field.setBackground(SURFACE);
        return field;
    }

    public static <T> JComboBox<T> combo(String name, T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setName(name);
        combo.setFont(font(14, false));
        combo.setPreferredSize(new Dimension(280, 44));
        return combo;
    }

    public static JPanel field(String title, JComponent control) {
        JPanel panel = panel(new BorderLayout(0, 8));
        JLabel label = label(title, 13, true, INK);
        label.setLabelFor(control);
        control.getAccessibleContext().setAccessibleName(title);
        panel.add(label, BorderLayout.NORTH);
        panel.add(control, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

    public static JPanel heading(String title, String subtitle, JComponent action) {
        JPanel panel = panel(new BorderLayout(20, 0));
        panel.setBorder(new EmptyBorder(0, 0, 24, 0));
        JPanel text = column();
        text.add(label(title, 27, true, INK));
        text.add(Box.createVerticalStrut(7));
        text.add(label(subtitle, 14, false, MUTED));
        panel.add(text, BorderLayout.CENTER);
        if (action != null) {
            JPanel holder = panel(new GridBagLayout());
            holder.add(action);
            panel.add(holder, BorderLayout.EAST);
        }
        return panel;
    }

    public static JPanel formPanel() {
        JPanel panel = panel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE), new EmptyBorder(24, 24, 24, 24)));
        return panel;
    }

    public static JPanel actions(JComponent... buttons) {
        JPanel panel = panel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, LINE), new EmptyBorder(20, 0, 0, 0)));
        for (JComponent button : buttons) panel.add(button);
        return panel;
    }

    public static JScrollPane scroll(JComponent content) {
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    public static JTable table(String name, String[] headers, Object[][] rows) {
        JTable table = new JTable(new DefaultTableModel(rows, headers) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        });
        table.setName(name);
        table.setFont(font(14, false));
        table.setRowHeight(46);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(LINE);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(font(12, false));
        table.getTableHeader().setPreferredSize(new Dimension(0, 42));
        table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setBorder(new EmptyBorder(0, 12, 0, 12));
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        right.setBorder(new EmptyBorder(0, 12, 0, 12));
        for (int i = 0; i < headers.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(i == 0 ? left : right);
        }
        return table;
    }

    public static BufferedImage materialImage() {
        if (materialImage == null) {
            try {
                java.net.URL url = AppTheme.class.getResource("/images/ceramicos-background.png");
                if (url == null) throw new IllegalStateException("No se encuentra el recurso de cerámicos.");
                materialImage = ImageIO.read(url);
            } catch (IOException error) {
                throw new IllegalStateException("No se pudo cargar la imagen de cerámicos.", error);
            }
        }
        return materialImage;
    }

    public static final class MaterialPanel extends JPanel {
        private final BufferedImage image = materialImage();
        public MaterialPanel() { setPreferredSize(new Dimension(320, 200)); }
        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            double scale = Math.max((double) getWidth() / image.getWidth(), (double) getHeight() / image.getHeight());
            int width = (int) Math.ceil(image.getWidth() * scale);
            int height = (int) Math.ceil(image.getHeight() * scale);
            g.drawImage(image, (getWidth() - width) / 2, (getHeight() - height) / 2, width, height, null);
            g.dispose();
        }
    }

    public static final class CeramicMark extends JComponent {
        public CeramicMark(int size) { setPreferredSize(new Dimension(size, size)); }
        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int cell = (Math.min(getWidth(), getHeight()) - 4) / 2;
            for (int i = 0; i < 4; i++) {
                g.setColor(i == 3 ? MUTED : PRIMARY);
                g.fillRoundRect((i % 2) * (cell + 4), (i / 2) * (cell + 4), cell, cell, 3, 3);
            }
            g.dispose();
        }
    }
}

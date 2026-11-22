package business;

import java.awt.*;
import java.util.EnumMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.*;

public class Tienda extends JFrame {
    private final StoreViews.Context context;
    private final JPanel workspace = AppTheme.panel(new BorderLayout());
    private final EnumMap<ViewId, JButton> navigation = new EnumMap<>(ViewId.class);
    private ViewId currentView;

    public Tienda() { this(StoreModel.shared(), "WilberBecerra"); }

    public Tienda(StoreModel store, String user) {
        AppTheme.install();
        context = new StoreViews.Context(store, this::showView);
        setTitle("Tienda de cerámicos | Gestión de ventas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Rectangle work = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(Math.min(1240, work.width), Math.min(800, work.height));
        setMinimumSize(new Dimension(Math.min(980, work.width), Math.min(640, work.height)));
        setLocationRelativeTo(null);
        JPanel root = AppTheme.panel(new BorderLayout());
        AppTheme.surface(root, AppTheme.CANVAS);
        root.add(sidebar(), BorderLayout.WEST);
        JPanel area = AppTheme.panel(new BorderLayout());
        JPanel header = AppTheme.panel(new BorderLayout(16, 0));
        AppTheme.surface(header, AppTheme.CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.LINE), new EmptyBorder(10, 24, 10, 24)));
        header.add(AppTheme.label("Gestión de ventas", 13, true, AppTheme.INK), BorderLayout.WEST);
        JPanel account = AppTheme.panel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        account.add(AppTheme.label(user, 12, false, AppTheme.MUTED));
        JButton logout = AppTheme.button("Cerrar sesión", "logout", false, () -> {
            new Login(store, null).setVisible(true); dispose();
        });
        logout.setFont(AppTheme.font(12, false));
        logout.setMargin(new Insets(6, 10, 6, 10));
        account.add(logout);
        header.add(account, BorderLayout.EAST); area.add(header, BorderLayout.NORTH);
        area.add(workspace, BorderLayout.CENTER);
        JPanel footer = AppTheme.panel(new BorderLayout(24, 0));
        AppTheme.surface(footer, AppTheme.CANVAS);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.LINE), new EmptyBorder(9, 24, 9, 24)));
        footer.add(AppTheme.label("Aplicación de escritorio", 12, false, AppTheme.MUTED), BorderLayout.WEST);
        footer.add(AppTheme.label("Los datos se conservan durante esta ejecución.", 12, false, AppTheme.MUTED), BorderLayout.EAST);
        area.add(footer, BorderLayout.SOUTH); root.add(area, BorderLayout.CENTER); setContentPane(root);
        setJMenuBar(menuBar()); showView(ViewId.HOME);
    }

    public StoreViews.Context context() { return context; }
    public ViewId currentView() { return currentView; }

    private JPanel sidebar() {
        JPanel sidebar = AppTheme.panel(new BorderLayout(0, 28));
        AppTheme.surface(sidebar, AppTheme.HEADER);
        sidebar.setPreferredSize(new Dimension(184, 0));
        sidebar.setBorder(new EmptyBorder(24, 14, 20, 14));
        JPanel brand = AppTheme.panel(new BorderLayout(10, 0));
        brand.setBorder(new EmptyBorder(0, 6, 0, 6));
        brand.add(new AppTheme.CeramicMark(26, true), BorderLayout.WEST);
        brand.add(AppTheme.label("Cerámicos", 17, true, AppTheme.PAPER), BorderLayout.CENTER);
        sidebar.add(brand, BorderLayout.NORTH);
        JPanel links = AppTheme.panel(new GridLayout(0, 1, 0, 6));
        ViewId[] destinations = {ViewId.HOME, ViewId.CATALOG, ViewId.SALE, ViewId.REPORTS, ViewId.DISCOUNTS, ViewId.ABOUT};
        String[] names = {"Inicio", "Productos", "Vender", "Reportes", "Configuración", "Acerca de"};
        for (int i = 0; i < destinations.length; i++) {
            final ViewId destination = destinations[i];
            JButton button = AppTheme.button(names[i], "nav" + destination.name(), false, () -> showView(destination));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            navigation.put(destination, button); links.add(button);
        }
        JPanel holder = AppTheme.panel(new BorderLayout()); holder.add(links, BorderLayout.NORTH);
        sidebar.add(holder, BorderLayout.CENTER);
        sidebar.add(AppTheme.label("Sesión local", 12, false, new Color(0xBAC7C1)), BorderLayout.SOUTH);
        return sidebar;
    }

    public void showView(ViewId view) {
        currentView = view;
        workspace.removeAll(); workspace.add(AppTheme.scroll(StoreViews.create(view, context)), BorderLayout.CENTER);
        ViewId group = view;
        if (view == ViewId.CONSULT || view == ViewId.EDIT) group = ViewId.CATALOG;
        if (view == ViewId.GIFTS || view == ViewId.OPTIMAL || view == ViewId.QUOTA) group = ViewId.DISCOUNTS;
        for (java.util.Map.Entry<ViewId, JButton> entry : navigation.entrySet()) {
            boolean selected = entry.getKey() == group;
            entry.getValue().setForeground(selected ? AppTheme.PAPER : new Color(0xC8D2CE));
            entry.getValue().setBackground(selected ? AppTheme.HEADER_LIGHT : AppTheme.HEADER);
            entry.getValue().setFont(AppTheme.font(14, selected));
            entry.getValue().setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 2, 0, 0, selected ? new Color(0x8EB39E) : AppTheme.HEADER),
                    new EmptyBorder(10, 12, 10, 8)));
        }
        workspace.revalidate(); workspace.repaint();
    }

    private JMenuBar menuBar() {
        JMenuBar bar = new JMenuBar();
        bar.setBackground(AppTheme.SURFACE);
        JMenu file = new JMenu("Archivo"); file.setMnemonic('A');
        JMenuItem exit = new JMenuItem("Salir"); exit.addActionListener(e -> dispose()); file.add(exit); bar.add(file);
        JMenu products = new JMenu("Mantenimiento"); products.setMnemonic('M');
        add(products, "Consultar cerámico", ViewId.CONSULT); add(products, "Modificar cerámico", ViewId.EDIT);
        add(products, "Listar cerámicos", ViewId.CATALOG); bar.add(products);
        JMenu sales = new JMenu("Ventas"); sales.setMnemonic('V');
        add(sales, "Vender", ViewId.SALE); add(sales, "Generar reportes", ViewId.REPORTS); bar.add(sales);
        JMenu settings = new JMenu("Configuración"); settings.setMnemonic('C');
        add(settings, "Configurar descuentos", ViewId.DISCOUNTS); add(settings, "Configurar obsequios", ViewId.GIFTS);
        add(settings, "Configurar cantidad óptima", ViewId.OPTIMAL); add(settings, "Configurar cuota diaria", ViewId.QUOTA); bar.add(settings);
        JMenu help = new JMenu("Ayuda"); add(help, "Acerca de la tienda", ViewId.ABOUT); bar.add(help);
        for (int i = 0; i < bar.getMenuCount(); i++) {
            bar.getMenu(i).setForeground(AppTheme.MUTED);
        }
        return bar;
    }

    private void add(JMenu menu, String title, ViewId view) {
        JMenuItem item = new JMenuItem(title); item.addActionListener(e -> showView(view)); menu.add(item);
    }

    public static void main(String[] args) { Login.main(args); }
}

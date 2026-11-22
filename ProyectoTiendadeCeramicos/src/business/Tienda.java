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
        setSize(Math.min(1200, work.width), Math.min(780, work.height));
        setMinimumSize(new Dimension(Math.min(980, work.width), Math.min(640, work.height)));
        setLocationRelativeTo(null);
        JPanel root = AppTheme.panel(new BorderLayout());
        JPanel top = AppTheme.panel(new BorderLayout());
        JPanel header = AppTheme.panel(new BorderLayout(24, 0));
        header.setBorder(new EmptyBorder(18, 28, 18, 28));
        header.add(AppTheme.brand(), BorderLayout.WEST);
        JPanel account = AppTheme.panel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        account.add(AppTheme.label(user, 13, false, AppTheme.MUTED));
        account.add(AppTheme.button("Cerrar sesión", "logout", false, () -> {
            new Login(store, null).setVisible(true); dispose();
        }));
        header.add(account, BorderLayout.EAST); top.add(header, BorderLayout.NORTH);
        JPanel nav = AppTheme.panel(new FlowLayout(FlowLayout.LEFT, 22, 0));
        nav.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, AppTheme.LINE), new EmptyBorder(0, 28, 0, 28)));
        ViewId[] destinations = {ViewId.HOME, ViewId.CATALOG, ViewId.SALE, ViewId.REPORTS, ViewId.DISCOUNTS, ViewId.ABOUT};
        String[] names = {"Inicio", "Productos", "Vender", "Reportes", "Configuración", "Acerca de"};
        for (int i = 0; i < destinations.length; i++) {
            final ViewId destination = destinations[i];
            JButton button = AppTheme.button(names[i], "nav" + destination.name(), false, () -> showView(destination));
            button.setContentAreaFilled(false); navigation.put(destination, button); nav.add(button);
        }
        top.add(nav, BorderLayout.SOUTH); root.add(top, BorderLayout.NORTH);
        root.add(workspace, BorderLayout.CENTER);
        JPanel footer = AppTheme.panel(new BorderLayout(24, 0));
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.LINE), new EmptyBorder(12, 28, 12, 28)));
        footer.add(AppTheme.label("Aplicación de escritorio", 12, false, AppTheme.MUTED), BorderLayout.WEST);
        footer.add(AppTheme.label("Los datos se conservan durante esta ejecución.", 12, false, AppTheme.MUTED), BorderLayout.EAST);
        root.add(footer, BorderLayout.SOUTH); setContentPane(root);
        setJMenuBar(menuBar()); showView(ViewId.HOME);
    }

    public StoreViews.Context context() { return context; }
    public ViewId currentView() { return currentView; }

    public void showView(ViewId view) {
        currentView = view;
        workspace.removeAll(); workspace.add(AppTheme.scroll(StoreViews.create(view, context)), BorderLayout.CENTER);
        ViewId group = view;
        if (view == ViewId.CONSULT || view == ViewId.EDIT) group = ViewId.CATALOG;
        if (view == ViewId.GIFTS || view == ViewId.OPTIMAL || view == ViewId.QUOTA) group = ViewId.DISCOUNTS;
        for (java.util.Map.Entry<ViewId, JButton> entry : navigation.entrySet()) {
            boolean selected = entry.getKey() == group;
            entry.getValue().setForeground(selected ? AppTheme.PRIMARY : AppTheme.MUTED);
            entry.getValue().setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, selected ? AppTheme.PRIMARY : Color.WHITE),
                    new EmptyBorder(12, 0, 12, 0)));
        }
        workspace.revalidate(); workspace.repaint();
    }

    private JMenuBar menuBar() {
        JMenuBar bar = new JMenuBar();
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
        return bar;
    }

    private void add(JMenu menu, String title, ViewId view) {
        JMenuItem item = new JMenuItem(title); item.addActionListener(e -> showView(view)); menu.add(item);
    }

    public static void main(String[] args) { Login.main(args); }
}

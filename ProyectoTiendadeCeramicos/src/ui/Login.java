package ui;

import business.StoreModel;
import business.Tienda;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** Local demonstration login; it does not authenticate against an external service. */
public class Login extends JFrame {
    private final JTextField username;
    private final JPasswordField password;
    private final MessageBar feedback = new MessageBar();

    public Login() { this(StoreModel.shared(), null); }

    public Login(StoreModel store, Consumer<String> authenticated) {
        AppTheme.install();
        setTitle("Tienda de cerámicos | Acceso");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Rectangle work = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(Math.min(1120, work.width), Math.min(740, work.height));
        setMinimumSize(new Dimension(Math.min(900, work.width), Math.min(620, work.height)));
        setLocationRelativeTo(null);
        JPanel content = AppTheme.panel(new GridLayout(1, 2));
        AppTheme.MaterialPanel material = new AppTheme.MaterialPanel(); material.setLayout(new BorderLayout());
        JPanel captionHolder = new JPanel(new BorderLayout()); captionHolder.setOpaque(false);
        captionHolder.setBorder(new EmptyBorder(26, 26, 26, 26));
        JPanel caption = AppTheme.column();
        AppTheme.surface(caption, AppTheme.HEADER);
        caption.setBorder(new EmptyBorder(20, 22, 20, 22));
        caption.add(AppTheme.label("Materiales para cada espacio.", 24, true, AppTheme.PAPER));
        caption.add(Box.createVerticalStrut(10));
        JTextArea description = new JTextArea("Un entorno de trabajo para consultar productos y registrar ventas.");
        description.setFont(AppTheme.font(13, false)); description.setForeground(AppTheme.PAPER);
        description.setOpaque(false); description.setAlignmentX(Component.LEFT_ALIGNMENT);
        description.setEditable(false); description.setFocusable(false); description.setLineWrap(true);
        description.setWrapStyleWord(true); description.setRows(2); caption.add(description);
        captionHolder.add(caption, BorderLayout.CENTER); material.add(captionHolder, BorderLayout.SOUTH);
        content.add(material);
        JPanel right = AppTheme.panel(new GridBagLayout());
        AppTheme.surface(right, AppTheme.CARD);
        JPanel form = AppTheme.panel(new GridBagLayout());
        AppTheme.surface(form, AppTheme.CARD);
        form.setBorder(new EmptyBorder(28, 44, 28, 44));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.NORTHWEST;
        c.gridy = 0; c.insets = new Insets(0, 0, 36, 0); form.add(AppTheme.brand(), c);
        c.gridy++; c.insets = new Insets(0, 0, 8, 0); form.add(AppTheme.label("Iniciar sesión", 26, true, AppTheme.INK), c);
        c.gridy++; c.insets = new Insets(0, 0, 26, 0); form.add(AppTheme.label("Ingresa a la aplicación de ventas.", 14, false, AppTheme.MUTED), c);
        username = AppTheme.input("username", "", true);
        c.gridy++; c.insets = new Insets(0, 0, 20, 0); form.add(AppTheme.field("Usuario", username), c);
        password = new JPasswordField(20); password.setName("password");
        password.setFont(AppTheme.font(14, false)); password.setMargin(new Insets(10, 12, 10, 12));
        password.setPreferredSize(new Dimension(330, 44));
        c.gridy++; c.insets = new Insets(0, 0, 10, 0); form.add(AppTheme.field("Contraseña", password), c);
        char echo = password.getEchoChar();
        JCheckBox reveal = new JCheckBox("Mostrar contraseña"); reveal.setName("showPassword");
        reveal.setOpaque(false); reveal.setForeground(AppTheme.MUTED);
        reveal.addActionListener(e -> password.setEchoChar(reveal.isSelected() ? (char) 0 : echo));
        c.gridy++; c.insets = new Insets(0, 0, 12, 0); form.add(reveal, c);
        c.gridy++; form.add(feedback, c);
        JButton login = AppTheme.button("Ingresar", "login", true, () -> {
            char[] secret = password.getPassword();
            try {
                String account = username.getText().trim();
                if (account.isEmpty() || secret.length == 0) {
                    feedback.showMessage("Completa el usuario y la contraseña.", true); return;
                }
                if (!authenticate(account, secret)) {
                    feedback.showMessage("Usuario o contraseña incorrectos.", true); return;
                }
                if (authenticated == null) new Tienda(store, account).setVisible(true);
                else authenticated.accept(account);
                dispose();
            } finally { Arrays.fill(secret, (char) 0); }
        });
        c.gridy++; c.insets = new Insets(8, 0, 24, 0); form.add(login, c);
        c.gridy++; c.insets = new Insets(0, 0, 0, 0);
        JLabel note = AppTheme.label("Proyecto académico de escritorio.", 12, false, AppTheme.MUTED);
        note.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.LINE),
                new EmptyBorder(18, 0, 0, 0)));
        form.add(note, c);
        right.add(form); content.add(right); setContentPane(content);
        getRootPane().setDefaultButton(login);
    }

    public static boolean authenticate(String user, char[] password) {
        return "WilberBecerra".equals(user) && Arrays.equals(password, new char[]{'1', '2', '3', '4'});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { AppTheme.install(); new Login().setVisible(true); });
    }
}

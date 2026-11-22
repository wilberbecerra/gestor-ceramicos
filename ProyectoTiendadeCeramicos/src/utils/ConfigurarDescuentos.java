package utils;

import ui.AppDialog;
import ui.AppTheme;
import ui.ViewId;
import javax.swing.SwingUtilities;

/** Preserves the original Eclipse entry point and shares the application panel. */
public class ConfigurarDescuentos extends AppDialog {
    public ConfigurarDescuentos() { super("Configurar descuentos", ViewId.DISCOUNTS); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { AppTheme.install(); new ConfigurarDescuentos().setVisible(true); });
    }
}

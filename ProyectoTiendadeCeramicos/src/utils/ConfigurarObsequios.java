package utils;

import ui.AppDialog;
import ui.AppTheme;
import ui.ViewId;
import javax.swing.SwingUtilities;

/** Preserves the original Eclipse entry point and shares the application panel. */
public class ConfigurarObsequios extends AppDialog {
    public ConfigurarObsequios() { super("Configurar obsequios", ViewId.GIFTS); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { AppTheme.install(); new ConfigurarObsequios().setVisible(true); });
    }
}

package ui;

import ui.AppDialog;
import ui.AppTheme;
import ui.ViewId;
import javax.swing.SwingUtilities;

/** Preserves the original Eclipse entry point and shares the application panel. */
public class DialogSell extends AppDialog {
    public DialogSell() { super("Registrar venta", ViewId.SALE); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { AppTheme.install(); new DialogSell().setVisible(true); });
    }
}

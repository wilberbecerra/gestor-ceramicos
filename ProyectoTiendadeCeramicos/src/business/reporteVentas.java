package business;

import ui.AppDialog;
import ui.AppTheme;
import ui.ViewId;
import javax.swing.SwingUtilities;

/** Preserves the original Eclipse entry point and shares the application panel. */
public class reporteVentas extends AppDialog {
    public reporteVentas() { super("Reportes de ventas", ViewId.REPORTS); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { AppTheme.install(); new reporteVentas().setVisible(true); });
    }
}

package ui;

import ui.AppDialog;
import ui.AppTheme;
import ui.ViewId;
import javax.swing.SwingUtilities;

/** Preserves the original Eclipse entry point and shares the application panel. */
public class jdCantidadOptima extends AppDialog {
    public jdCantidadOptima() { super("Cantidad óptima", ViewId.OPTIMAL); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { AppTheme.install(); new jdCantidadOptima().setVisible(true); });
    }
}

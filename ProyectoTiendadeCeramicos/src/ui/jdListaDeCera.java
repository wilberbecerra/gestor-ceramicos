package ui;

import ui.AppDialog;
import ui.AppTheme;
import ui.ViewId;
import javax.swing.SwingUtilities;

/** Preserves the original Eclipse entry point and shares the application panel. */
public class jdListaDeCera extends AppDialog {
    public jdListaDeCera() { super("Listado de cerámicos", ViewId.CATALOG); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { AppTheme.install(); new jdListaDeCera().setVisible(true); });
    }
}

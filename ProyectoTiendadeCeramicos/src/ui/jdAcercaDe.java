package ui;

import ui.AppDialog;
import ui.AppTheme;
import ui.ViewId;
import javax.swing.SwingUtilities;

/** Preserves the original Eclipse entry point and shares the application panel. */
public class jdAcercaDe extends AppDialog {
    public jdAcercaDe() { super("Acerca del proyecto", ViewId.ABOUT); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { AppTheme.install(); new jdAcercaDe().setVisible(true); });
    }
}

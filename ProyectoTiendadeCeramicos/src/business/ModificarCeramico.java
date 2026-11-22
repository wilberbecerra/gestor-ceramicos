package business;

import ui.AppDialog;
import ui.AppTheme;
import ui.ViewId;
import javax.swing.SwingUtilities;

/** Preserves the original Eclipse entry point and shares the application panel. */
public class ModificarCeramico extends AppDialog {
    public ModificarCeramico() { super("Modificar cerámico", ViewId.EDIT); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { AppTheme.install(); new ModificarCeramico().setVisible(true); });
    }
}

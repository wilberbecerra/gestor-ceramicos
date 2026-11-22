package ui;

import ui.AppDialog;
import ui.AppTheme;
import ui.ViewId;
import javax.swing.SwingUtilities;

/** Preserves the original Eclipse entry point and shares the application panel. */
public class ConsultarCeramico extends AppDialog {
    public ConsultarCeramico() { super("Consultar cerámico", ViewId.CONSULT); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { AppTheme.install(); new ConsultarCeramico().setVisible(true); });
    }
}

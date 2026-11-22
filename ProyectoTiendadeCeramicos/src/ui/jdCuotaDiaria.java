package ui;

import ui.AppDialog;
import ui.AppTheme;
import ui.ViewId;
import javax.swing.SwingUtilities;

/** Preserves the original Eclipse entry point and shares the application panel. */
public class jdCuotaDiaria extends AppDialog {
    public jdCuotaDiaria() { super("Cuota diaria", ViewId.QUOTA); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { AppTheme.install(); new jdCuotaDiaria().setVisible(true); });
    }
}

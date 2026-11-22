package ui;

import business.StoreModel;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.*;

/** Keeps the original Eclipse dialog entry points using the same native panels. */
public class AppDialog extends JDialog {
    protected AppDialog(String title, ViewId view) {
        super((Frame) null, title, true);
        AppTheme.install();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(1020, 690);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        StoreViews.Context context = new StoreViews.Context(StoreModel.shared(), destination -> dispose());
        setContentPane(AppTheme.scroll(StoreViews.create(view, context)));
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "close");
        getRootPane().getActionMap().put("close", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { dispose(); }
        });
    }
}

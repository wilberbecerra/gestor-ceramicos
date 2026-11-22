package ui;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

/** A non-blocking, keyboard-readable result or validation message. */
public final class MessageBar extends JTextArea {
    public MessageBar() {
        setName("feedback");
        setEditable(false);
        setFocusable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setFont(AppTheme.font(13, false));
        setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        setVisible(false);
    }

    public void showMessage(String text, boolean error) {
        setText(text);
        setBackground(error ? AppTheme.ERROR_BACKGROUND : AppTheme.TINT);
        setForeground(error ? AppTheme.ERROR : AppTheme.PRIMARY);
        setRows(text.length() > 95 ? 2 : 1);
        setVisible(true);
        getAccessibleContext().setAccessibleDescription(text);
        revalidate();
        repaint();
    }
}

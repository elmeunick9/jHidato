package presentation;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class AboutWindow extends JDialog {
    public AboutWindow(JFrame parent) {
        super(parent);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("About");
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextArea text = new JTextArea("JHidato 21.1\nDone by Robert Planas & Oscar Ochoa\nEnjoy!");
        text.setEditable(false);
        text.setCursor(null);
        text.setOpaque(false);
        text.setFocusable(false);
        text.setBorder(new EmptyBorder(5,5,5,5));
        text.setFont(text.getFont().deriveFont(18f));

        this.add(text);
        pack();

        // Center, to call after pack()
        setLocationRelativeTo(null);
    }
}

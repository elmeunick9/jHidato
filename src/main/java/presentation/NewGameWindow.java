package presentation;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class NewGameWindow extends JDialog {
    public NewGameWindow(JFrame parent) {
        super(parent);

        JLabel name = new JLabel("Notes, 1.23");
        setModalityType(ModalityType.APPLICATION_MODAL);

        setTitle("About Notes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());

        this.add(name);
        pack();
    }


}

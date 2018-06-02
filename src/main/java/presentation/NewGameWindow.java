package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NewGameWindow extends JDialog {
    private JButton buttonGenerate = new JButton("Generate");
    private JButton buttonMake = new JButton("Make From Scratch");
    private String difficulties[] = { "Easy", "Medium", "Hard", "Custom" };
    private JComboBox selectDifficulty = new JComboBox(difficulties);

    private boolean toGenerate = true;
    public int difficulty;

    NewGameWindow(JFrame parent) {
        super(parent);
        Container content = this.getContentPane();
        EmptyBorder border = new EmptyBorder(5,5,5,5);

        //Components
        JLabel labelDifficulty = new JLabel("Choose difficulty: ");

        JPanel rowDifficulty = new JPanel();
        JPanel rowButtons = new JPanel();

        //Layout
        rowDifficulty.setLayout(new BoxLayout(rowDifficulty, BoxLayout.X_AXIS));
        rowDifficulty.setBorder(border);
        rowDifficulty.add(labelDifficulty);
        rowDifficulty.add(selectDifficulty);
        rowButtons.setLayout(new BoxLayout(rowButtons, BoxLayout.X_AXIS));
        rowButtons.setBorder(border);
        rowButtons.add(buttonGenerate);
        rowButtons.add(buttonMake);

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(rowDifficulty);
        content.add(rowButtons);

        pack();
        setModalityType(ModalityType.APPLICATION_MODAL);

        setTitle("New Game");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());

        initActions();
    }

    private void initActions() {
        difficulty = selectDifficulty.getSelectedIndex();
        selectDifficulty.addActionListener(e -> difficulty = selectDifficulty.getSelectedIndex());
        buttonGenerate.addActionListener(e -> { toGenerate = true; dispose(); });
        buttonMake.addActionListener(e -> { toGenerate = false; dispose(); });
    }

    public boolean showDialog() {
        setVisible(true);
        return toGenerate;
    }
}

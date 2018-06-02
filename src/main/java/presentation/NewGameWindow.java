package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NewGameWindow extends JDialog {
    private JButton buttonGenerate = new JButton("Generate");
    private JButton buttonMake = new JButton("Make From Scratch");
    private String difficulties[] = { "Easy", "Medium", "Hard", "Custom" };
    private JComboBox selectDifficulty = new JComboBox(difficulties);
    private String types[] = { "Triangle", "Quads", "Hexagons" };
    private JComboBox selectType = new JComboBox(types);
    private JTextField filename = new JTextField();

    private boolean toGenerate = true;
    public int difficulty;
    public int type;

    NewGameWindow(JFrame parent) {
        super(parent);
        Container content = this.getContentPane();
        EmptyBorder border = new EmptyBorder(5,5,5,5);
        buttonMake.setEnabled(false);

        //Components
        JLabel labelDifficulty = new JLabel("Choose difficulty: ");
        JLabel labelType = new JLabel("Choose shape type: ");
        JLabel labelFile = new JLabel("Name: ");

        JPanel rowDifficulty = new JPanel();
        JPanel rowType = new JPanel();
        JPanel rowText = new JPanel();
        JPanel rowButtons = new JPanel();

        //Layout
        rowDifficulty.setLayout(new BoxLayout(rowDifficulty, BoxLayout.X_AXIS));
        rowDifficulty.setBorder(border);
        rowDifficulty.add(labelDifficulty);
        rowDifficulty.add(selectDifficulty);
        rowType.setLayout(new BoxLayout(rowType, BoxLayout.X_AXIS));
        rowType.setBorder(border);
        rowType.add(labelType);
        rowType.add(selectType);
        rowText.setLayout(new BoxLayout(rowText, BoxLayout.X_AXIS));
        rowText.setBorder(border);
        rowText.add(labelFile);
        rowText.add(filename);
        rowButtons.setLayout(new BoxLayout(rowButtons, BoxLayout.X_AXIS));
        rowButtons.setBorder(border);
        rowButtons.add(buttonGenerate);
        rowButtons.add(buttonMake);

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(rowDifficulty);
        content.add(rowType);
        content.add(rowText);
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
        selectDifficulty.addActionListener(e -> {
            difficulty = selectDifficulty.getSelectedIndex();
            if (difficulty == 3) {
                buttonGenerate.setEnabled(false);
                buttonMake.setEnabled(true);
            } else {
                buttonGenerate.setEnabled(true);
                buttonMake.setEnabled(false);
            }
        });
        type = selectType.getSelectedIndex();
        selectType.addActionListener(e -> type = selectType.getSelectedIndex());
        buttonGenerate.addActionListener(e -> { toGenerate = true; dispose(); });
        buttonMake.addActionListener(e -> { toGenerate = false; dispose(); });
    }

    public boolean showDialog() {
        setVisible(true);
        return toGenerate;
    }

    public String getFilename() {
        return filename.getText();
    }
}

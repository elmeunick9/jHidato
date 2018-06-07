package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class NewGameWindow extends JDialog {
    private JButton buttonGenerate = new JButton("Generate");
    private JButton buttonMake = new JButton("Make From Scratch");
    private String difficulties[] = { "Easy", "Medium", "Hard", "Custom" };
    private JComboBox selectDifficulty = new JComboBox(difficulties);
    private String types[] = { "Triangle", "Quads", "Hexagons" };
    private JComboBox selectType = new JComboBox(types);
    ButtonGroup chooseAdjacency  = new ButtonGroup();
    private JRadioButton rEdge = new JRadioButton("Edge");
    private JRadioButton rVertex = new JRadioButton("Vertex");
    private JRadioButton rBoth = new JRadioButton("Both");
    JComponent[] adjacencyList = {rEdge, rVertex, rBoth};
    private JTextField filename = new JTextField();

    private Container content;
    private EmptyBorder border;

    //True if when you click either generate o make buttons.
    private boolean ready = false;
    private int adjacency = 0;
    private int difficulty;
    private int type;

    public boolean toGenerate = true;


    NewGameWindow(JFrame parent) {
        super(parent);
        content = this.getContentPane();
        border = new EmptyBorder(5,5,5,5);
        buttonMake.setEnabled(false);

        addRow("Choose difficulty: ", selectDifficulty);
        addRow("Choose shape type: ", selectType);
        addRow("Adjacency: ", adjacencyList);
        addRow("Name: ", filename);
        JComponent[] list1 = {buttonGenerate, buttonMake};
        addRow(null, list1);

        chooseAdjacency.add(rEdge);
        chooseAdjacency.add(rVertex);
        chooseAdjacency.add(rBoth);
        chooseAdjacency.setSelected(rEdge.getModel(), true);

        //Layout
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        pack();
        setModalityType(ModalityType.APPLICATION_MODAL);

        setTitle("New Game");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());

        initActions();
    }

    private void addRow(String text, JComponent component) {
        JComponent[] c = {component};
        addRow(text, c);
    }

    private void addRow(String text, JComponent[] components) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setBorder(border);
        if (text != null) {
            JLabel label = new JLabel(text);
            row.add(label);
        }
        for (JComponent e : components) {
            row.add(e);
        }
        content.add(row);
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
        buttonGenerate.addActionListener(e -> { toGenerate = true; finish(); });
        buttonMake.addActionListener(e -> { toGenerate = false; finish(); });
    }

    private void finish() {
        adjacency = -1;
        int i = 0;
        for (JComponent c : adjacencyList) {
            JRadioButton b = (JRadioButton) c;
            if (b.isSelected()) adjacency = i;
            i++;
        }

        ready = true;
        dispose();
    }

    public boolean showDialog() {
        ready = false;
        setVisible(true);
        return ready;
    }

    public int getAdjacency() { return adjacency; }
    public int getDifficulty() { return difficulty; }
    public int getHType() { return type; }

    public String getFilename() {
        return filename.getText();
    }
}

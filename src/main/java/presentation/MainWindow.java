package presentation;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

public class MainWindow {
    private CtrlPresentation ctrlPresentation;
    private JFrame frame = new JFrame("jHidato 21.1");
    private JPanel panelContent = new JPanel();
    private JMenuBar menuMain = new JMenuBar();
    private JMenu menuFile = new JMenu("File");
    private JMenuItem menuitemQuit = new JMenuItem("Quit");

    public MainWindow(CtrlPresentation cPres) {
        ctrlPresentation = cPres;
        init();
    }

    private void init() {
        // Frame
        frame.setMinimumSize(new Dimension(700,700));
        frame.setPreferredSize(frame.getMinimumSize());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Menu
        menuFile.add(menuitemQuit);
        menuMain.add(menuFile);
        frame.setJMenuBar(menuMain);

        //Main Content
    }

    public void makeVisible() {
        System.out.println("isEventDispatchThread: " + SwingUtilities.isEventDispatchThread());
        frame.pack();
        frame.setVisible(true);
    }
}

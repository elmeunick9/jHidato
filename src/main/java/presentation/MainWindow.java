package presentation;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.*;

public class MainWindow {
    private CtrlPresentation ctrlPresentation;
    private JFrame frame = new JFrame("jHidato 21.1");
    private JPanel panelContent = new JPanel();
    private JMenuBar menuMain = new JMenuBar();
    private JMenu menuFile = new JMenu("File");
    private JMenuItem menuitemQuit = new JMenuItem("Quit");
    private JOptionPane loginDialog = new JOptionPane();

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

        menuitemQuit.addActionListener(e -> System.exit(0));

        //Main Content

        //Show Login
        String usrname = "";
        while (usrname == "") {
            String t = (String)JOptionPane.showInputDialog(
                    frame,
                    "Username:\n",
                    "Login / Register",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "KEK");
            if (t == null) {
                throw new ExceptionInInitializerError();
            }
            if (t.length() == 3 && t.matches("[A-Za-z0-9]+")) usrname = t.toUpperCase();
            else {
                JOptionPane.showMessageDialog(frame,
                        "Username must consist of exactly 3 alphanumeric values",
                        "Invalid Username",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void makeVisible() {
        System.out.println("isEventDispatchThread: " + SwingUtilities.isEventDispatchThread());
        frame.pack();
        frame.setVisible(true);
    }
}

package presentation;

import javax.swing.*;
import java.awt.Dimension;
import java.io.File;

public class MainWindow {
    private CtrlPresentation ctrlPresentation;
    private JFrame frame = new JFrame("jHidato 21.1");
    private JPanel panelContent = new JPanel();
    private JMenuBar menuMain = new JMenuBar();
    private JMenu menuFile = new JMenu("File");
    private JMenuItem menuitemNewGame = new JMenuItem("New Game");
    private JMenuItem menuitemLoadgame = new JMenuItem("Load Game");
    private JMenuItem menuitemSavegame = new JMenuItem("Save Game");
    private JMenuItem menuitemSaveAs = new JMenuItem("Save Game As Template...");
    private JMenuItem menuitemQuit = new JMenuItem("Quit");
    private JMenu menuRanking = new JMenu("Ranking");
    private JMenu menuHelp = new JMenu("Help");
    private JMenuItem menuitemAbout = new JMenuItem("About");
    private JMenuItem menuitemManual = new JMenuItem("Manual");

    private JOptionPane loginDialog = new JOptionPane();
    private NewGameWindow newGameWindow = new NewGameWindow(frame);
    private AboutWindow aboutWindow = new AboutWindow(frame);

    final JFileChooser fc = new JFileChooser();

    public MainWindow(CtrlPresentation cPres) {
        ctrlPresentation = cPres;
        initView();
        initActions();
        ctrlPresentation.initUser(askUser());
    }

    private void initView() {
        // Frame
        frame.setMinimumSize(new Dimension(700,700));
        frame.setPreferredSize(frame.getMinimumSize());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Menu
        menuFile.add(menuitemNewGame);
        menuFile.add(menuitemLoadgame);
        menuFile.add(menuitemSavegame);
        menuFile.add(menuitemSaveAs);
        menuFile.add(menuitemQuit);
        menuMain.add(menuFile);
        menuMain.add(menuRanking);
        menuHelp.add(menuitemManual);
        menuHelp.add(menuitemAbout);
        menuMain.add(menuHelp);
        frame.setJMenuBar(menuMain);

        //Main Content


    }

    private void initActions() {
        menuitemQuit.addActionListener(e -> System.exit(0));

        menuitemNewGame.addActionListener(e -> {
            boolean toGenerate = newGameWindow.showDialog();
            int d = newGameWindow.difficulty;
            int t = newGameWindow.type;
            String name = newGameWindow.getFilename();
            if (toGenerate) ctrlPresentation.getCtrlDomain().generateGame(name, d, t);
            else ctrlPresentation.getCtrlDomain().createGame(name);
        });

        menuitemSavegame.addActionListener(e -> {
            try {
                ctrlPresentation.getCtrlDomain().saveGame();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                ex.getMessage(),
                "Exception ocurred!",
                JOptionPane.ERROR_MESSAGE);
            }
        });

        menuitemLoadgame.addActionListener(e -> loadGameDialog());
        menuitemAbout.addActionListener(e -> aboutWindow.setVisible(true));
    }

    private void loadGameDialog() {
        String myuser = ctrlPresentation.getCtrlDomain().getUsername();
        String name = "";
        while (name.isEmpty()) {
            fc.setCurrentDirectory(new File(
                    System.getProperty("user.dir") + "/Usuaris/" + myuser + "/games/"));
            int c = fc.showDialog(frame, null);
            if (c == 0) {
                try {
                    name = fc.getSelectedFile().getName();
                    String user = fc.getSelectedFile().getParentFile().getParentFile().getName();
                    if (!user.equals(myuser)) {
                        throw new Exception("You must select a game within YOUR folder!");
                    }
                    ctrlPresentation.getCtrlDomain().getCtrlPersistence().loadGame(user, name);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            ex.getMessage(),
                            "Exception ocurred!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private String askUser() {
        //Show Login
        String usrname = "";
        while (usrname.isEmpty()) {
            String t = (String)JOptionPane.showInputDialog(
                    frame,
                    "Username:\n",
                    "Login / Register",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "YUUSHA");
            if (t == null) {
                throw new ExceptionInInitializerError();
            }
            if (t.length() > 1 && t.length() <= 6 && t.matches("[A-Za-z0-9]+")) {
                usrname = t.toUpperCase();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Username must consist of up to 6 alphanumeric values",
                        "Invalid Username",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return usrname;
    }

    public void makeVisible() {
        //System.out.println("isEventDispatchThread: " + SwingUtilities.isEventDispatchThread());
        frame.pack();
        frame.setVisible(true);
    }
}

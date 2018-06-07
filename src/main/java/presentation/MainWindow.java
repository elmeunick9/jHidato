package presentation;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainWindow {
    private JFrame frame = new JFrame("jHidato 21.1");
    private JMenuBar menuMain = new JMenuBar();
    private JMenu menuFile = new JMenu("File");
    private JMenuItem menuitemNewGame = new JMenuItem("New Game");
    private JMenuItem menuitemLoadgame = new JMenuItem("Load Game");
    private JMenuItem menuitemSavegame = new JMenuItem("Save Game");
    private JMenuItem menuitemSaveAs = new JMenuItem("Save Game As Template...");
    private JMenuItem menuitemLoadTemplate = new JMenuItem("Load Template");
    private JMenuItem menuitemQuit = new JMenuItem("Quit");
    
    private JMenu menuRanking = new JMenu("Ranking");
    private JMenu menuHelp = new JMenu("Help");
    private JMenuItem menuitemAbout = new JMenuItem("About");
    private JMenuItem menuitemManual = new JMenuItem("Manual");

    private NewGameWindow newGameWindow = new NewGameWindow(frame);
    private AboutWindow aboutWindow = new AboutWindow(frame);

    private ArrayList<ArrayList<String>> data;
    private Board boardHidato;

    final JFileChooser fc = new JFileChooser();
    final JFileChooser fcTemplates = new JFileChooser();

    public MainWindow() {
        initView();
        initActions();
        CtrlPresentation.getInstance().initUser(askUser());
    }

    private void initView() {
        // Frame
        frame.setMinimumSize(new Dimension(600,650));
        frame.setPreferredSize(frame.getMinimumSize());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Menu
        menuFile.add(menuitemNewGame);
        menuFile.add(menuitemLoadgame);
        menuFile.add(menuitemSavegame);
        menuFile.add(menuitemSaveAs);
        menuFile.add(menuitemLoadTemplate);
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
            if (!newGameWindow.showDialog()) return;
            boolean toGenerate = newGameWindow.toGenerate;
            int d = newGameWindow.getDifficulty();
            int t = newGameWindow.getHType();
            int a = newGameWindow.getAdjacency();
            String name = newGameWindow.getFilename();
            try {
                if (toGenerate)
                    CtrlPresentation.getInstance().getCtrlDomain().generateGame(name, d, t, a);
                else CtrlPresentation.getInstance().getCtrlDomain().createGame(name);
                initGame();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                        ex.getMessage(),
                        "Exception ocurred!",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        menuitemSavegame.addActionListener(e -> {
            try {
                String filename = CtrlPresentation.getInstance().getCtrlDomain().saveGame();
                JOptionPane.showMessageDialog(frame,
                        "Game saved with name " + filename,
                        "Game saved",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalStateException | IOException ex) {
                JOptionPane.showMessageDialog(frame,
                ex.getMessage(),
                "Exception ocurred!",
                JOptionPane.ERROR_MESSAGE);
            }
        });

        menuitemManual.addActionListener(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File("./Files/manual.pdf");
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                            ex.getMessage(),
                            "Exception ocurred!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        menuitemLoadgame.addActionListener(e -> loadGameDialog());
        menuitemSaveAs.addActionListener(e -> saveTemplateDialog());
        menuitemLoadTemplate.addActionListener(e -> loadTemplateDialog());
        menuitemAbout.addActionListener(e -> aboutWindow.setVisible(true));
    }

    private void loadGameDialog() {
        CtrlPresentation ctrlPresentation = CtrlPresentation.getInstance();
        String myuser = ctrlPresentation.getCtrlDomain().getUsername();
        String name = "";
        while (name.isEmpty()) {
            fc.setCurrentDirectory(new File(
                    System.getProperty("user.dir") + "/Users/" + myuser + "/games/"));
            int c = fc.showDialog(frame, null);
            if (c == 0) {
                try {
                    name = fc.getSelectedFile().getName();
                    String user = fc.getSelectedFile().getParentFile().getParentFile().getName();
                    if (!user.equals(myuser)) {
                        throw new IOException("You must select a game within YOUR folder!");
                    }
                    ctrlPresentation.getCtrlDomain().loadGame(name);
                    initGame();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                            ex.getClass().toString() + "\n" + ex.getMessage(),
                            "Exception ocurred!",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else break;
        }
    }

    private void saveTemplateDialog() {
        CtrlPresentation ctrlPresentation = CtrlPresentation.getInstance();
        String myuser = ctrlPresentation.getCtrlDomain().getUsername();

        fcTemplates.setDialogTitle("Save Game as Template");

        boolean loop = true;
        while (loop) {
            int c = fcTemplates.showSaveDialog(frame);
            if (c == 0) {
                try {
                    File file = fcTemplates.getSelectedFile();
                    ctrlPresentation.getCtrlDomain().getCtrlPersistence().exportHidato(file);
                    loop = false;
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                            ex.getMessage(),
                            "Exception ocurred!",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else loop = false;
        }
    }

    private void loadTemplateDialog() {
        CtrlPresentation ctrlPresentation = CtrlPresentation.getInstance();
        String myuser = ctrlPresentation.getCtrlDomain().getUsername();

        fcTemplates.setDialogTitle("Load Template");

        boolean loop = true;
        while (loop) {
            int c = fcTemplates.showOpenDialog(frame);
            if (c == 0) {
                try {
                    File file = fcTemplates.getSelectedFile();
                    ctrlPresentation.getCtrlDomain().getCtrlPersistence().importHidato(file);
                    initGame();
                    loop = false;
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                            ex.getClass().toString() + "\n" + ex.getMessage(),
                            "Exception ocurred!",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else loop = false;
        }
    }

    private void initGame() {
        //checkIfExists
        if(boardHidato != null) frame.remove(boardHidato);
        String tp = CtrlPresentation.getInstance().getCtrlDomain().getTypeHidato();
        NodeCell nc;
        switch(tp) {
            case "T":
                nc = new TriangleNode();
                break;
            case "Q":
                nc = new SquareNode();
                break;
            // case "H":
            //     nc = new HexagonNode();
            //     break;
            default:
                nc = new SquareNode();
                break;
        }

        boardHidato = new Board(nc,
                CtrlPresentation.getInstance().getCtrlDomain().getMatrix());
        frame.add(boardHidato);
        frame.pack();
        frame.setVisible(true);
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

        File templatesPath = new File(
                System.getProperty("user.dir") + "/Usuaris/" + usrname + "/templates/");
        templatesPath.mkdirs();

        fcTemplates.setCurrentDirectory(templatesPath);

        return usrname;
    }

    public Board getBoard() { return boardHidato; }

    public void makeVisible() {
        //System.out.println("isEventDispatchThread: " + SwingUtilities.isEventDispatchThread());
        frame.pack();
        frame.setVisible(true);

    }
}

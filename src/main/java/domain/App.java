package domain;

import presentation.CtrlPresentation;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                CtrlPresentation.getInstance().init();
            } catch (ExceptionInInitializerError e) {
                System.exit(0);
            }
        });
    }
}

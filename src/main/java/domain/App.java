package domain;

import persistance.CtrlPersistence;
import presentation.CtrlPresentation;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                CtrlPersistence ctrlPersistence = CtrlPersistence.getInstance();
                CtrlPresentation ctrlPresentation = CtrlPresentation.getInstance();
                CtrlDomain ctrlDomain = CtrlDomain.getInstance();

                ctrlPresentation.init();
            } catch (ExceptionInInitializerError e) {
                System.exit(0);
            }
        });
    }
}

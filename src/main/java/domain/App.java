package domain;

import persistance.CtrlPersistence;
import presentation.CtrlPresentation;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                CtrlPersistence ctrlPersistence = new CtrlPersistence();
                CtrlPresentation ctrlPresentation = new CtrlPresentation();
                CtrlDomain ctrlDomain = new CtrlDomain(ctrlPresentation, ctrlPersistence);

                ctrlPersistence.setCtrlDomain(ctrlDomain);
                ctrlPresentation.setCtrlDomain(ctrlDomain);

                ctrlPresentation.init();
            } catch (ExceptionInInitializerError e) {
                System.exit(0);
            }
        });
    }
}

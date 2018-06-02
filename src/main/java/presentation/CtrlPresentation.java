package presentation;

import domain.CtrlDomain;

public class CtrlPresentation {
    private MainWindow mainWindow = null;
    private CtrlDomain domain = new CtrlDomain();

    public CtrlPresentation() {
        if (mainWindow == null) {
            mainWindow = new MainWindow(this);
        }
    }

    public void init() {
        mainWindow.makeVisible();
    }

    public void initUser(String username) {
        domain.newPlayer(username);
    }

}

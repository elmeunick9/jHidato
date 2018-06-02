package presentation;

import domain.CtrlDomain;

public class CtrlPresentation {
    private MainWindow mainWindow = null;
    private CtrlDomain domain = null;

    public void init() {
        if (mainWindow == null) {
            mainWindow = new MainWindow(this);
        }
        mainWindow.makeVisible();
    }

    public void setCtrlDomain(CtrlDomain d) { domain = d; }
    CtrlDomain getCtrlDomain() {
        if (domain == null) throw new Error("Domain not initialized!");
        return domain;
    }

    public void initUser(String username) {
        getCtrlDomain().newPlayer(username);
    }

}

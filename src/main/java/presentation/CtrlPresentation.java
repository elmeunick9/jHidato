package presentation;

import domain.CtrlDomain;

public class CtrlPresentation {
    private MainWindow mainWindow = null;
    private static CtrlPresentation presentation = null;

    protected CtrlPresentation() {
        //Avoid instance.
    }

    public static CtrlPresentation getInstance() {
        if(presentation == null) {
            presentation = new CtrlPresentation();
        }
        return presentation;
    }

    public void init() {
        if (mainWindow == null) {
            mainWindow = new MainWindow();
        }
        mainWindow.makeVisible();
    }

    CtrlDomain getCtrlDomain() {
        return CtrlDomain.getInstance();
    }

    public void initUser(String username) {
        getCtrlDomain().newPlayer(username);
    }

    public boolean leftClick(int x, int y, int val) {
        CtrlDomain.getInstance().setVal(x+1, y+1, val);
        return true;
    }

}

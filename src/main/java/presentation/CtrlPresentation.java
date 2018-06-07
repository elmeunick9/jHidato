package presentation;

import domain.CtrlDomain;

public class CtrlPresentation {
    private MainWindow mainWindow = null;
    private static CtrlPresentation presentation = null;

    public boolean editorMode = false;

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
        int v = CtrlDomain.getInstance().getValue(x+1,y+1);
        if (v != -1) {
            if (CtrlDomain.getInstance().isNodeFixed(x+1,y+1)) val = v;
            else val = v+1;
        }

        int s = CtrlDomain.getInstance().getHidatoSize();
        mainWindow.getBoard().setNextMove(val);
        if (val < 0) val = 1;
        val = (val-1) % s;

        return CtrlDomain.getInstance().setVal(x+1, y+1, val + 1);
    }

    public boolean rightClick(int x, int y) {
        return CtrlDomain.getInstance().setVal(x+1, y+1, -1);
    }

    public boolean middleClick(int x, int y) {
        return getCtrlDomain().makeNodeABlock(x+1, y+1);
    }

}

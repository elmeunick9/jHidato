package presentation;

public class CtrlPresentation {
    private MainWindow mainWindow = null;

    public CtrlPresentation() {
        if (mainWindow == null) {
            mainWindow = new MainWindow(this);
        }
    }

    public void init() {
        mainWindow.makeVisible();
    }


}

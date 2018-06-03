package persistance;

import domain.CtrlDomain;
import domain.Hidato;

import java.io.IOException;
import java.util.ArrayList;

public class CtrlPersistence {
    private static CtrlPersistence persistence = null;
    protected CtrlPersistence() {

    }

    public static CtrlPersistence getInstance() {
        if(persistence == null)
            persistence = new CtrlPersistence();
        return persistence;
    }

    CtrlDomain getCtrlDomain() {
        return CtrlDomain.getInstance();
    }

    public static ArrayList<ArrayList<String>> getRanking() throws IOException {
        return CustomFile.getRanking();
    }

    public static Hidato importHidato(String file)  throws IOException {
        return CustomFile.importHidato(file);
    }

    public static void saveGame(String username, String hidatoName,
    ArrayList<String> data, String diff, long currTime) {
        CustomFile.saveGame(username, hidatoName, data, diff, currTime);
    }

    public static ArrayList<String> loadGame(String username, String hidatoName) {
        return CustomFile.loadGame(username, hidatoName);
    }
}

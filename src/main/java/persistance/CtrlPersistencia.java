package persistance;

import domain.Hidato;

import java.io.IOException;
import java.util.ArrayList;

public class CtrlPersistencia {
    public static ArrayList<ArrayList<String>> getRanking() throws IOException {
        return CustomFile.getRanking();
    }

    public static Hidato importHidato(String file)  throws IOException {
        return CustomFile.importHidato(file);
    }

    public static void saveGame(String username, String hidatoName, ArrayList<String> data, String diff, long currTime) {
        CustomFile.saveGame(username, hidatoName, data, diff, currTime);
    }

    public static ArrayList<String> loadGame(String username, String hidatoName) {
        return CustomFile.loadGame(username, hidatoName);
    }
}

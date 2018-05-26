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
}

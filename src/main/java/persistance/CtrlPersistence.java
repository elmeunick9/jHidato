package persistance;

import domain.CtrlDomain;
//import domain.Hidato;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CtrlPersistence {
    private CtrlDomain domain = null;

    public void setCtrlDomain(CtrlDomain d) { domain = d; }
    CtrlDomain getCtrlDomain() {
        if (domain == null) throw new Error("Domain not initialized!");
        return domain;
    }

    public static ArrayList<ArrayList<String>> getRanking() throws IOException {
        return CustomFile.getRanking();
    }

    public void importHidato(File file)  throws IOException {
        CustomFile.HidatoInStrings his = CustomFile.importHidato(file);
        domain.makeGameFromData(his.data, file.getName(), his.adjacency, his.type);
    }

    public void exportHidato(File file) throws IOException {
        ArrayList<String> data = getCtrlDomain().getClearHidatoData();
        CustomFile.saveTemplate(file, data);
    }

    public void saveGame(String username, String hidatoName, ArrayList<String> data,
                         String difficulty, long currTime) throws IOException {
        CustomFile.saveGame(username, hidatoName, data, difficulty, currTime);
    }

    public ArrayList<String> loadGame(String username, String hidatoName) throws IOException {
        return CustomFile.loadGame(username, hidatoName);
    }
}

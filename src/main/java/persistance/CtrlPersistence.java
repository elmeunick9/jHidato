package persistance;

import domain.CtrlDomain;
//import domain.Hidato;

import java.io.File;
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

    public ArrayList<ArrayList<String>> getRanking() throws IOException {
        return CustomFile.getRanking();
    }

    private void loadGameFromDomain(String name, CustomFile.GameInStrings g) {
        CtrlDomain.getInstance().makeGameFromData(
                g.data, name, g.adjacency, g.type, g.difficulty, g.time);
    }

    public void importHidato(File file)  throws IOException {
        CustomFile.GameInStrings his = CustomFile.importHidato(file);
        loadGameFromDomain(file.getName(), his);
    }

    public void exportHidato(File file) throws IOException {
        ArrayList<String> data = getCtrlDomain().getClearHidatoData();
        CustomFile.saveTemplate(file, data);
    }

    public void saveGame(String username, String hidatoName, ArrayList<String> data,
                         String difficulty, long currTime) throws IOException {
        CustomFile.saveGame(username, hidatoName, data, difficulty, currTime);
    }

    public void loadGame(String username, String hidatoName) throws IOException {
        CustomFile.GameInStrings g = CustomFile.loadGame(username, hidatoName);
        loadGameFromDomain(hidatoName, g);
    }
}

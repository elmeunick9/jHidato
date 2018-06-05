package persistance;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomFile {

    public static class GameInStrings {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        String adjacency;
        String type;
        String difficulty = "custom";
        String time = "0";
    }

    private static GameInStrings importHidato(BufferedReader b) throws IOException {
        GameInStrings his = new GameInStrings();
        String header = b.readLine();
        String[] params = header.split(",");
        if (params.length < 3) throw new IOException("Invalid header!");
        his.type = params[0];
        his.adjacency = params[1];
        int x = Integer.parseInt(params[2]);
        int y = Integer.parseInt(params[3]);

        for(int i = 0; i < x; i++) {
            String line = b.readLine();
            String[] values = line.split(",");
            his.data.add(new ArrayList<>());
            his.data.get(his.data.size()-1).addAll(Arrays.asList(values));
        }

        return his;
    }

    public static GameInStrings importHidato(String file) throws IOException {
        StringReader fr = new StringReader(file);
        BufferedReader b = new BufferedReader(fr);
        return importHidato(b);
    }

    public static GameInStrings importHidato(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader b = new BufferedReader(fr);
        return importHidato(b);
    }

    private static GameInStrings loadGame(BufferedReader b) throws IOException {
        GameInStrings g = importHidato(b);
        String footer = b.readLine();
        String[] params = footer.split(",");
        if (params.length < 2) throw new IOException("Invalid game! Is this a template?");
        g.difficulty = params[0];
        g.time = params[1];
        return g;
    }

    public static GameInStrings loadGame(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader b = new BufferedReader(fr);
        return loadGame(b);
    }

    public static GameInStrings loadGame(String file) throws IOException {
        StringReader fr = new StringReader(file);
        BufferedReader b = new BufferedReader(fr);
        return loadGame(b);
    }

    public static GameInStrings loadGame(String username, String hidatoName)
            throws IOException {
        FileReader fr = new FileReader("Usuaris/" + username
                + "/games/" + hidatoName);
        BufferedReader b = new BufferedReader(fr);
        return loadGame(b);
    }

    /* return the data of ranking from the file in form of matrix */
    public static ArrayList<ArrayList<String>> getRanking() throws IOException {
        String ruta = "Files/ranking.txt";
        FileReader fr = new FileReader(ruta);
        BufferedReader b = new BufferedReader(fr);
        String cadena = b.readLine();
        ArrayList<ArrayList<String>> ret = new ArrayList<>();


        while(cadena != null) {
            ret.add(new ArrayList<>());
            String[] line = cadena.split("/");
            for (String x : line) {
                ret.get(ret.size() - 1).add(x);
            }
            cadena = b.readLine();
        }

        return ret;
    }

    public static void saveTemplate(File file, ArrayList<String> data) {
        file.getParentFile().mkdirs();

        try {
            file.delete();
            file.createNewFile();

            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            PrintStream console = System.out;
            PrintStream o = new PrintStream(file);
            System.setOut(o);
            System.setOut(console);
            for(String line : data) {
                bw.write(line);
                bw.newLine();
            }
            bw.close();
            fileWriter.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void saveGame(File file, ArrayList<String> data, String diff, long currTime)
            throws IOException {
        file.getParentFile().mkdirs();

        file.delete();
        file.createNewFile();

        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        PrintStream console = System.out;
        PrintStream o = new PrintStream(file);
        System.setOut(o);
        System.setOut(console);
        for(String line : data) {
            bw.write(line);
            bw.newLine();
        }
        String line = diff + "," + currTime;
        bw.write(line);
        bw.newLine();
        bw.close();
        fileWriter.close();
    }

    //
    public static void saveGame(String username, String hidatoName,
    ArrayList<String> data, String diff, long currTime) throws IOException {
            File game = new File("Usuaris/" + username + "/games/", hidatoName);
            saveGame(game, data, diff, currTime);
    }
}

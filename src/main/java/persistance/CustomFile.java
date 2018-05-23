package persistance;
import domain.Hidato;
import domain.TriHidato;
import domain.QuadHidato;
import domain.HexHidato;
import domain.Node;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Scanner;

public class CustomFile {

    CustomFile() {}

    /* Import from a file given and returns the Hidato */
    public static Hidato importHidato(String file)  throws IOException {
        String ruta = "Files/";
        FileReader fr = new FileReader(ruta+file);
        BufferedReader b = new BufferedReader(fr);
        String cadena = b.readLine();
        Hidato.AdjacencyType adj = Hidato.AdjacencyType.BOTH;
        ArrayList<ArrayList<Node>> data = new ArrayList<>();
        //False init, if is not setted later, will return an empty hidato.
        Hidato hidato;
        String[] params;

        /* If cadena is null it means that the txt file is not correct,
         then a exception will be thrown. */
        if(cadena !=null) {
            params = cadena.split(",");
            int x = Integer.parseInt(params[2]);
            int y = Integer.parseInt(params[3]);
            switch (params[1]) {
                case "C":
                    adj = Hidato.AdjacencyType.EDGE;
                    break;
                case "CA":
                    adj = Hidato.AdjacencyType.BOTH;
                    break;
                default:
                    adj = Hidato.AdjacencyType.BOTH;
                    break;
            }

            for(int i = 0; i < x; i++) {
                String line = b.readLine();
                String[] values = line.split(",");
                data.add(new ArrayList<>());
                for(int j = 0; j < y; j++){
                    data.get(data.size() - 1).add(new Node(values[j]));
                }
            }

            switch(params[0]) {
                case "Q":
                    hidato = new QuadHidato(data, adj);
                    break;
                case "T":
                    hidato = new TriHidato(data, adj);
                    break;
                case "H":
                    hidato  = new HexHidato(data, adj);
                    break;
                default:
                    throw new IOException();
            }
        } else {
            throw new IOException();
        }
        b.close();

        return hidato;
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

    /*Wait a hidato entered by the terminal with the correct structure
      and return a Hidato*/
    public static Hidato enterHidato() throws IOException {
        Scanner s = new Scanner(System.in);
        String cadena = s.nextLine();
        String[] params;
        Hidato.AdjacencyType adj;
        ArrayList<ArrayList<Node>> data = new ArrayList<>();
        Hidato hidato;

        if(cadena.isEmpty()) {
            params = cadena.split(",");
            int x = Integer.parseInt(params[2]);
            int y = Integer.parseInt(params[3]);
            switch (params[1]) {
                case "C":
                    adj = Hidato.AdjacencyType.EDGE;
                    break;
                case "CA":
                    adj = Hidato.AdjacencyType.BOTH;
                    break;
                default:
                    adj = Hidato.AdjacencyType.BOTH;
                    break;
            }

            for(int i = 0; i < x; i++) {
                String line = s.nextLine();
                String[] values = line.split(",");
                data.add(new ArrayList<>());
                for(int j = 0; j < y; j++){
                    data.get(data.size() - 1).add(new Node(values[j]));
                }
            }

            switch(params[0]) {
                case "Q":
                    hidato = new QuadHidato(data, adj);
                    break;
                case "T":
                    hidato = new TriHidato(data, adj);
                    break;
                case "H":
                    hidato  = new HexHidato(data, adj);
                    break;
                default:
                    throw new IOException();
            }
        } else {
            throw new IOException();
        }

        return hidato;
    }

    //
    public static void saveGame(String username, String hidatoName, ArrayList<String> data){

        try {
            File folder = new File("Usuaris/" + username + "/Partida");
            if(!folder.exists()) {
                folder.mkdirs();
            }

            System.out.println("Folder created");
            File game = new File("Usuaris/" + username + "/Partida/", hidatoName);
            game.delete();
            game.createNewFile();
            FileWriter fileWriter = new FileWriter("Usuaris/" + username
                    + "/Partida/" + hidatoName, true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            PrintStream console = System.out;
            PrintStream o = new PrintStream(game);
            System.setOut(o);
            System.setOut(console);
            for(String line : data) {
                bw.write(line);
                bw.newLine();
            }
            bw.close();
            fileWriter.close();

        } catch(IOException ex){ex.printStackTrace();}

    }

}

package domain;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class File {

    File() {}

    /*Import from a file given and returns the Hidato */
    public static Hidato importHidato(String file)  throws IOException {
        String ruta = "src/files/";
        FileReader fr = new FileReader(ruta+file);
        BufferedReader b = new BufferedReader(fr);
        String cadena = b.readLine();
        Hidato.AdjacencyType adj = Hidato.AdjacencyType.BOTH;
        ArrayList<ArrayList<Node>> data = new ArrayList<>();
        //False init, if is not setted later, will return an empty hidato.
        Hidato hidato;
        String[] params;

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
                    hidato  = new TriHidato(data, adj);
                    break;
                default:
                    hidato = new TriHidato(data, adj);
                    break;
            }
        } else {
            //Modificar a una excepció custom
            throw new FileNotFoundException();
        }
        b.close();

        return hidato;
    }

    /* return the data of ranking from the file*/
    public static ArrayList<ArrayList<String>> getRanking() throws IOException {
        String ruta = "src/files/ranking.txt";
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
}

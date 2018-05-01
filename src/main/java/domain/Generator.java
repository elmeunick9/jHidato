package domain;

import java.util.ArrayList;
import java.util.Random;

public class Generator {

    //A filename is the FILE NAME, not the FILE NAME + EXTENSION.
    protected String filename;
    protected Hidato h;

    protected Generator() {}

    Generator(Game.Difficulty d, Game.HidatoType ht) {
        Hidato.AdjacencyType adj = Hidato.AdjacencyType.VERTEX;
        ArrayList<ArrayList<Node>> data = new ArrayList<>();
        int max;
        switch (d) {
            case EASY:
                max = 5;
                break;
            case MEDIUM:
                max = 10;
                break;
            case HARD:
                max = 20;
                break;
            case CUSTOM:
                max = 20;
                break;
            default:
                max = 10;
                break;
        }
        Random rn = new Random();
        int x = 5;//rn.nextInt() % max;
        int y = 5;//rn.nextInt() % max;

        for(int i = 0; i < x; i++) {
            data.add(new ArrayList<>());
            for(int j = 0; j < y; j++){
                String elem = "?";
                data.get(data.size() - 1).add(new Node(elem));
            }
        }

        switch (ht) {
            case TRIANGLE:
                h = new TriHidato(data, adj);
                break;
            case SQUARE:
                h = new QuadHidato(data, adj);
                break;
            case HEXAGON:
                h = new TriHidato(data, adj);
                break;
            default:
                h = new TriHidato(data, adj);
                break;
        }

        Solver s = new Solver (h);
        //h = s.generateSolution();
        filename = getHashedFilename();
    }

    /* Create random string*/
    /*
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }*/

    public Hidato getHidato() {
        return h;
    }

    public String getHashedFilename() {
        return Integer.toHexString(getHidato().hashCode());
    }

    public static void main(String[] args) throws Solver.SolutionNotFound {
        Generator a = new Generator(Game.Difficulty.EASY, Game.HidatoType.TRIANGLE);
        System.out.print(a.filename);
        System.out.print(a.h.getNode(1,1));
    }
}

package domain;

import java.util.ArrayList;
import java.util.Random;

public class Generator {

    //A filename is the FILE NAME, not the FILE NAME + EXTENSION.
    protected String filename;
    protected Hidato h;

    protected Generator() {}

    Generator(Game.Difficulty d, Game.HidatoType ht) {
        this(d, ht, Hidato.AdjacencyType.EDGE);
    }

    Generator(Game.Difficulty d, Game.HidatoType ht, Hidato.AdjacencyType adj) {
        Random rn = new Random();
        int z = getRangedRandom(d);
        ArrayList<ArrayList<Node>> data = makeEmptyDataMatrix(d);

        Hidato s = createHidato(data, adj, ht);
        int unsetNum = s.count();
        int minLen = unsetNum / 2;
        Solver solver = new Solver(s);
        try {
            h = solver.generateSolution(minLen);
        } catch (Solver.SolutionNotFound e) {
            throw new RuntimeException("Trying to generate hidato with no possible solutions!");
        }


        Hidato f = h;
        int c = f.count();
        for (int i = 0; i<20; i++) {
            try {
                h = solver.generateSolution(minLen);
                int c2 = h.count();
                if (c2 > c) {
                    c = c2;
                    f = h;
                }
            } catch (Solver.SolutionNotFound e) {
                i--; //If not found doesn't count.
            }
        }

        data = f.copyData();
        for(int i = 0; i < data.size(); i++) {
            for(int j = 0; j < data.get(i).size(); j++) {
                Node t = data.get(i).get(j);
                if (t.getType() == Node.Type.variable) {
                    Node n;
                    if (rn.nextInt(z/2+2) != 1) n = new Node("?");
                    else n = new Node(t.toString().replace("v", ""));
                    data.get(i).set(j, n);
                } else if (t.getType() == Node.Type.unset) {
                    data.get(i).set(j, new Node("*"));
                }
            }
        }

        h = createHidato(data, adj, ht);
        filename = getHashedFilename();
    }

    private Hidato createHidato(ArrayList<ArrayList<Node>> data,
                                Hidato.AdjacencyType adj,
                                Game.HidatoType ht) {
        switch (ht) {
            case TRIANGLE:
                return new TriHidato(data, adj);
            case SQUARE:
                return new QuadHidato(data, adj);
            case HEXAGON:
                return new HexHidato(data, adj);
            default:
                return new TriHidato(data, adj);
        }
    }

    public Hidato getHidato() {
        return h;
    }

    public String getHashedFilename() {
        return Integer.toHexString(getHidato().hashCode());
    }

    public static Pair<Integer, Integer> getInterval(Game.Difficulty difficulty) {
        Pair<Integer, Integer> interval;
        switch (difficulty) {
            case EASY: return new Pair<>(3, 5);
            case MEDIUM: return new Pair<>(6, 9);
            case HARD: return new Pair<>(10, 15);
            default: return new Pair<>(3, 6);
        }
    }

    public static int getRangedRandom(Game.Difficulty difficulty) {
        Random rn = new Random();
        Pair<Integer, Integer> interval = getInterval(difficulty);
        return rn.nextInt(interval.getValue() - interval.getKey()) + interval.getKey();
    }

    public static ArrayList<ArrayList<Node>> makeEmptyDataMatrix(Game.Difficulty difficulty) {
        int x = getRangedRandom(difficulty);
        int y = getRangedRandom(difficulty);
        return makeEmptyDataMatrix(x, y);
    }

    public static ArrayList<ArrayList<Node>> makeEmptyDataMatrix(int x, int y) {
        ArrayList<ArrayList<Node>> data = new ArrayList<>();
        for(int i = 0; i < x; i++) {
            data.add(new ArrayList<>());
            for(int j = 0; j < y; j++){
                String elem = "?";
                data.get(data.size() - 1).add(new Node(elem));
            }
        }
        return data;
    }

    public static void main(String[] args) throws Solver.SolutionNotFound {
        Generator a = new Generator(Game.Difficulty.EASY, Game.HidatoType.TRIANGLE,
                Hidato.AdjacencyType.EDGE);
        System.out.print(a.filename);
        System.out.print(a.h.getNode(1,1));
    }
}

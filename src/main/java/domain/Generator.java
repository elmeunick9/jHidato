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
        ArrayList<ArrayList<Node>> data = new ArrayList<>();
        Pair<Integer, Integer> interval;
        switch (d) {
            case EASY:
                interval = new Pair<>(3, 4);
                break;
            case MEDIUM:
                interval = new Pair<>(5, 7);
                break;
            case HARD:
                interval = new Pair<>(8, 10);
                break;
            default:
                interval = new Pair<>(3, 6);
                break;
        }
        Random rn = new Random();
        int x = rn.nextInt(interval.getValue() - interval.getKey()) + interval.getKey();
        int y = rn.nextInt(interval.getValue() - interval.getKey()) + interval.getKey();
        int z = rn.nextInt(interval.getValue() - interval.getKey()) + interval.getKey();

        for(int i = 0; i < x; i++) {
            data.add(new ArrayList<>());
            for(int j = 0; j < y; j++){
                String elem = "?";
                data.get(data.size() - 1).add(new Node(elem));
            }
        }

        Hidato s = createHidato(data, adj, ht);
        int unsetNum = s.count();
        int minLen = unsetNum / 2;
        System.out.println("MinLen: " + minLen);
        Solver solver = new Solver(s);
        try {
            h = solver.generateSolution(minLen);
            h.print();
            System.out.println("First try");
        } catch (Solver.SolutionNotFound e) {
            throw new RuntimeException("Trying to generate hidato with no possible solutions!");
        }

        boolean found = true;
        while(found && minLen <= unsetNum) {
            try {
                h = solver.generateSolution(minLen);
                h.print();
                System.out.println("More Tries");

                //Advance, try find a more filled solution, but not linearly.
                minLen += (int) ((double)minLen / 2);

                // Small random chance to consider the hidato OK if at least half unset nodes
                // are filled.
                if (minLen > unsetNum-5 && rn.nextInt(10) == 5) break;

            } catch (Solver.SolutionNotFound e) {
                //Once we find there is no solution, we stop and let h be the final choice.
                found = false;
            }
        }

        data = h.copyData();
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

    public static void main(String[] args) throws Solver.SolutionNotFound {
        Generator a = new Generator(Game.Difficulty.EASY, Game.HidatoType.TRIANGLE,
                Hidato.AdjacencyType.EDGE);
        System.out.print(a.filename);
        System.out.print(a.h.getNode(1,1));
    }
}

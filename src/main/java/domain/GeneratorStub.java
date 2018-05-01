package domain;

import java.util.ArrayList;

public class GeneratorStub {
    private Hidato h;
    private String filename;

    GeneratorStub(Game.Difficulty d) {
        ArrayList<ArrayList<Node>> data = new ArrayList<>();
        String[][] matrix = {
                {"#", "#", "#", "#", "#", "#", "#"},
                {"#", "#", "#", "*", "#", "#", "#"},
                {"#", "#", "?", "1", "?", "?", "?"},
                {"#", "?", "?", "16","?", "?", "?"},
                {"*", "?", "?", "7", "?", "?", "*"},
        };

        for (String[] list : matrix) {
            data.add(new ArrayList<>());
            for (String x : list) {
                data.get(data.size() - 1).add(new Node(x));
            }
        }

        h = new TriHidato(data, Hidato.AdjacencyType.EDGE);
        filename = "example.txt";
    }

    public Hidato getHidato() {
        return h;
    }

    public String getHashedFilename() {
        return filename;
    }
}

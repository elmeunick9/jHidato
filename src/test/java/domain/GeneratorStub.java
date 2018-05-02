package domain;

import java.util.ArrayList;

public class GeneratorStub extends Generator {

    GeneratorStub(Game.Difficulty d, Game.HidatoType t) {
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
        filename = "example-hash";
    }
}

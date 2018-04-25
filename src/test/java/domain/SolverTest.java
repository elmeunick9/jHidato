package domain;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SolverTest {
    private TriHidato hidato;
    private Solver solver;

    @Before
    public void before() {
        ArrayList<ArrayList<Node>> data = new ArrayList<>();
        String[][] matrix = {
                {"#", "#", "#", "#", "#", "#", "#"},
                {"#", "#", "#", "*", "#", "#", "#"},
                {"#", "#", "?", "1", "?", "2", "?"},
                {"#", "?", "?", "16","?", "?", "?"},
                {"*", "?", "?", "7", "?", "?", "*"},
        };

        for (String[] list : matrix) {
            data.add(new ArrayList<>());
            for (String x : list) {
                data.get(data.size() - 1).add(new Node(x));
            }
        }

        hidato = new TriHidato(data, Hidato.AdjacencyType.EDGE);
        solver = new Solver(hidato);
    }

    @Test
    public void nextMoveTest() throws Node.InvalidTypeException {
        Node n = hidato.getNode(3, 4);
        ArrayList<Pair<Node, Integer>> moves = solver.nextMove(n);

        System.out.println(moves);

        assertTrue( moves.contains(new Pair<>(hidato.getNode(3,3), 2)));
        assertTrue(!moves.contains(new Pair<>(hidato.getNode(3,3), 3)));
        assertTrue(!moves.contains(new Pair<>(hidato.getNode(4,4), 16)));

        n = hidato.getNode(3, 6);
        moves = solver.nextMove(n);

        assertTrue( moves.contains(new Pair<>(hidato.getNode(3,7), 3)));
    }

}

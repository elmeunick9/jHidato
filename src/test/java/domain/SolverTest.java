package domain;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SolverTest {
    private ArrayList<ArrayList<Node>> data = new ArrayList<>();

    @Before
    public void before() {
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
    }

    @Test
    public void nextMoveTest() throws Node.InvalidTypeException {
        Hidato hidato = new TriHidato(data, Hidato.AdjacencyType.EDGE);
        Solver solver = new Solver(hidato);

        Node n = hidato.getNode(3, 4);
        ArrayList<Pair<Node, Integer>> moves = solver.nextMove(n);

        assertTrue( moves.contains(new Pair<>(hidato.getNode(3,3), 2)));
        assertTrue(!moves.contains(new Pair<>(hidato.getNode(3,3), 3)));
        assertTrue(!moves.contains(new Pair<>(hidato.getNode(4,4), 16)));

        n = hidato.getNode(3, 6);
        moves = solver.nextMove(n);

        assertTrue( moves.contains(new Pair<>(hidato.getNode(3,7), 3)));
    }

    @Test
    public void findSolution1Test1() {
        data.get(2).set(5, new Node("?")); //Overriding Fixed Node!
        Hidato hidato = new TriHidato(data, Hidato.AdjacencyType.EDGE);
        Solver solver = new Solver(hidato);

        try {
            Hidato solution = solver.generateSolution();
        } catch (Solver.SolutionNotFound e) {
            throw new AssertionError();
        }

    }

    @Test
    public void findSolution1Test2() {
        data.get(2).set(2, new Node("2")); //Overriding Fixed Node!
        data.get(2).set(3, new Node("?"));
        data.get(2).set(4, new Node("14"));
        data.get(2).set(5, new Node("?"));

        Hidato hidato = new TriHidato(data, Hidato.AdjacencyType.EDGE);
        Solver solver = new Solver(hidato);

        try {
            Hidato solution = solver.generateSolution();
        } catch (Solver.SolutionNotFound e) {
            throw new AssertionError();
        }

    }

}

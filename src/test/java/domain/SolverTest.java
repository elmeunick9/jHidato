package domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

        assertTrue(moves.contains(new Pair<>(hidato.getNode(3,3), 2)));
        assertFalse(moves.contains(new Pair<>(hidato.getNode(3,3), 3)));
        assertFalse(moves.contains(new Pair<>(hidato.getNode(4,4), 16)));

        n = hidato.getNode(3, 6);
        moves = solver.nextMove(n);

        assertTrue(moves.contains(new Pair<>(hidato.getNode(3,7), 3)));
    }

    @Test
    public void findSolution1Test1() {
        data.get(2).set(5, new Node("?"));
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
        data.get(2).set(2, new Node("2"));
        data.get(2).set(3, new Node("?"));
        data.get(2).set(4, new Node("14"));
        data.get(2).set(5, new Node("?"));

        Hidato hidato = new TriHidato(data, Hidato.AdjacencyType.EDGE);
        Solver solver = new Solver(hidato);

        try {
            Hidato solution = solver.generateSolution();
            assertEquals(solution.getNode(4, 5).getValue(), 15);
        } catch (Solver.SolutionNotFound e) {
            throw new AssertionError();
        }

    }

    @Test
    public void findSolutionOn161() {
        //CHECKSTYLE:OFF
        String[][] matrix = {
            {"1v", "#"},
            {"2v", "3", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"5v", "4v", "*", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"6v", "7v", "*", "*", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"9v", "8v", "121v", "120v", "119", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"10v", "11v", "122v", "123v", "118v", "117v", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"*", "12", "13v", "124v", "125v", "116v", "115v", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"*", "15v", "14v", "*", "126v", "127v", "114v", "113v", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"17v", "16", "*", "*", "129v", "128v", "111v", "112v", "*", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"18", "19v", "20", "21v", "130v", "131v", "110v", "109", "108v", "107v", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"*", "*", "*", "22v", "23", "132v", "133v", "134v", "135v", "106v", "105v", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"*", "*", "*", "*", "24", "25", "26v", "27", "136v", "137", "104", "103v", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"*", "*", "*", "*", "31", "30v", "29v", "28v", "139v", "138v", "101v", "102v", "*", "#", "#", "#", "#", "#", "#", "#"},
            {"*", "*", "*", "*", "32", "33", "34", "35", "140v", "141v", "100v", "99v", "98v", "97v", "#", "#", "#", "#", "#", "#"},
            {"57", "56v", "55v", "54v", "53v", "*", "*", "36v", "37v", "142v", "143v", "*", "*", "96v", "95v", "#", "#", "#", "#", "#"},
            {"58v", "59v", "*", "51v", "52v", "*", "*", "39v", "38", "145v", "144v", "*", "*", "93v", "94v", "*", "#", "#", "#", "#"},
            {"61v", "60v", "*", "50v", "49v", "*", "41v", "40v", "147v", "146v", "151v", "152v", "153v", "92v", "91v", "*", "*", "#", "#", "#"},
            {"62v", "63v", "*", "*", "48v", "47v", "42v", "43v", "148v", "149v", "150v", "155", "154v", "*", "90", "89v", "88v", "87v", "#", "#"},
            {"*", "64", "65v", "66v", "67", "46", "45v", "44v", "73v", "74v", "75v", "156", "157v", "158v", "159v", "160v", "161v", "86v", "85v", "#"},
            {"*", "*", "*", "*", "68", "69v", "70v", "71", "72v", "*", "76v", "77v", "78v", "79v", "80v", "81v", "82v", "83v", "84v", "*"}
        };
        //CHECKSTYLE:ON

        data.clear();

        for (String[] list : matrix) {
            data.add(new ArrayList<>());
            for (String x : list) {
                if (x.endsWith("v")) x = "?";
                data.get(data.size() - 1).add(new Node(x));
            }
        }

        Hidato hidato = new TriHidato(data, Hidato.AdjacencyType.EDGE);
        Solver solver = new Solver(hidato);

        try {
            Hidato solution = solver.generateSolution();
            assertEquals(solution.getNode(5, 3).getValue(), 121);
            assertEquals(solution.getNode(6, 3).getValue(), 122);
        } catch (Solver.SolutionNotFound e) {
            throw new AssertionError();
        }
    }

    //FROM HERE ON SERIOUS TESTS ------------------------------------------------------------------

    @Test
    public void quadTest1() {
        //CHECKSTYLE:OFF
        String[][] matrix = {
                {"7v", "8",   "#",  "#",  "29",  "30v"},
                {"6",  "9v",  "#",  "#",  "28v", "31v"},
                {"5",  "10",  "#",  "#",  "27v",  "32v"},
                {"4v", "11v", "18v","19v","26v", "33v"},
                {"3v", "12v", "17v","20v","25",  "34v"},
                {"2v", "13v", "16v","21v","24v", "35"},
                {"1v", "14",  "15", "22", "23",  "*"},
        };
        //CHECKSTYLE:ON

        data.clear();

        for (String[] list : matrix) {
            data.add(new ArrayList<>());
            for (String x : list) {
                if (x.endsWith("v")) x = "?";
                data.get(data.size() - 1).add(new Node(x));
            }
        }

        Hidato hidato = new QuadHidato(data, Hidato.AdjacencyType.EDGE);
        Solver solver = new Solver(hidato);

        try {
            Hidato solution = solver.generateSolution();
            assertEquals(solution.getNode(1, 1).getValue(), 7);
            assertEquals(solution.getNode(2, 2).getValue(), 9);
            assertEquals(solution.getNode(4, 3).getValue(), 18);
            assertEquals(solution.getNode(4, 4).getValue(), 19);
            assertEquals(solution.getNode(5, 6).getValue(), 34);
        } catch (Solver.SolutionNotFound e) {
            throw new AssertionError();
        }
    }

    @Test
    public void quadTest2() {
        //CHECKSTYLE:OFF
        String[][] matrix = {
                {"7v", "8",   "#",  "#",  "27",  "26v"},
                {"6",  "9v",  "#",  "#",  "28v", "25v"},
                {"5",  "10",  "*",   "#",  "29v",  "24v"},
                {"4v", "11v", "32v","31v","30v", "23v"},
                {"3v", "12v", "*",   "*" ,"#",  "22v"},
                {"2v", "13v", "16v","17v","20v", "21"},
                {"1v", "14",  "15", "18", "19",  "*"},
        };
        //CHECKSTYLE:ON

        data.clear();

        for (String[] list : matrix) {
            data.add(new ArrayList<>());
            for (String x : list) {
                if (x.endsWith("v")) x = "?";
                data.get(data.size() - 1).add(new Node(x));
            }
        }

        Hidato hidato = new QuadHidato(data, Hidato.AdjacencyType.EDGE);
        Solver solver = new Solver(hidato);

        try {
            Hidato solution = solver.generateSolution();
            assertEquals(solution.getNode(1, 1).getValue(), 7);
            assertEquals(solution.getNode(2, 2).getValue(), 9);
            assertEquals(solution.getNode(4, 3).getValue(), 32);
            assertEquals(solution.getNode(4, 4).getValue(), 31);
            assertEquals(solution.getNode(5, 6).getValue(), 22);
        } catch (Solver.SolutionNotFound e) {
            throw new AssertionError();
        }
    }

}

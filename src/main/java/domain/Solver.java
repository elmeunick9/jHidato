package domain;

import javafx.util.Pair;
import java.util.Random;
import java.util.ArrayList;

public class Solver {
    private Hidato hidato;
    private Random rand = new Random();

    Solver(Hidato h) { hidato = h; }

    /* There is a hamiltonian strictly increasing by a order of 1 path from min to max node values. */
    /* To implement using nextMove */
    private boolean isSolution() {
        return false;
    }

    /* No nodes have the same value */
    /* Maybe not requiered
    boolean isValid() {
        return false;
    }
    */

    /* All nodes have some value */
    /* Maybe not requiered
    boolean isFilled() {
        return false;
    }
    */

    /* Generates a list of valid next movements, an empty list indicates that no movements can be done from this node */
    ArrayList<Pair<Node, Integer>> nextMove(Node a) {
        ArrayList<Node>                 nodes = hidato.adjacentNodes(a);
        ArrayList<Pair<Node, Integer>>  moves = new ArrayList<>();

        // Check if there is already an adjacent node of increasing or decreasing value.
        boolean upper = true;
        boolean lower = true;
        for (Node b : nodes) {
            if (b.hasValue()) {
                try {
                    if (b.getValue() == a.getValue()-1) lower = false;
                    if (b.getValue() == a.getValue()+1) upper = false;
                } catch (Node.InvalidTypeException e) {
                    System.err.println("We somehow reached an invalid node!");
                    System.err.println(e);
                    throw new Error();
                }
            }
        }

        // If not, all adjacent unset nodes may have an increasing or decreasing value.
        for (Node b : nodes) {
            if (b.getType() == Node.Type.unset) {
                try {
                    if (lower) moves.add(new Pair<>(b, a.getValue() - 1));
                    if (upper) moves.add(new Pair<>(b, a.getValue() + 1));
                } catch (Node.InvalidTypeException e) {
                    System.err.println("We somehow reached an invalid node!");
                    System.err.println(e);
                    throw new Error();
                }
            }
        }

        // We also want to count fixed adjacent nodes as a valid next movement as long as they have the correct value.
        // i.e. ignoring lower and upper.
        /* TODO: This aproax will fail. It could cause an unending loop of fixed nodes.
        We will need to mantain a lookup table of visited nodes, even if only for fixed ones.
        We can make the table a private member and clean it when generateSolution() finishes.
        */
        for (Node b : nodes) {
            if (b.getType() == Node.Type.fixed) {

            }
        }

        return moves;
    }

    class HidatoIsFilledWrongException extends Exception {}

    /* Generates a random solution  */
    // TODO: To guarentee one solution or unsolvable we must try every start node. Therefore maintain a list of already tried nodes.
    public Hidato generateSolution() throws HidatoIsFilledWrongException {
        ArrayList<Node> fixed_nodes = new ArrayList<>();
        for (Node n : hidato) if (n.getType() == Node.Type.fixed) fixed_nodes.add(n);
        ArrayList<Node> unset_nodes = new ArrayList<>();
        for (Node n : hidato) if (n.getType() == Node.Type.unset) unset_nodes.add(n);

        // Select a start node. If there is any of fixed type, one of them, otherwise any unset node.
        Node start_node;
        if (!fixed_nodes.isEmpty()) {
            int i = rand.nextInt(fixed_nodes.size()) + 1;
            start_node = fixed_nodes.get(i);
        } else {
            if (unset_nodes.isEmpty()) {
                if (isSolution()) return hidato.copy();
                else throw new HidatoIsFilledWrongException();
            }
            int i = rand.nextInt(unset_nodes.size()) + 1;
            start_node = unset_nodes.get(i);
        }

        Hidato original = hidato;
        hidato = hidato.copy();

        // Backtracking
        generateSolution(start_node, unset_nodes.size());

        Hidato copy = hidato;
        hidato = original;
        return copy;
    }

    private void generateSolution(Node n, int remaining_nodes) throws HidatoIsFilledWrongException {
        if (remaining_nodes == 0) {
            if (!isSolution()) throw new HidatoIsFilledWrongException();
            else return;
        }

        ArrayList<Pair<Node, Integer>> moves = nextMove(n);


        //Select random movement and try until no movements left.
        boolean notFound = true;
        while (!moves.isEmpty() && notFound) {
            int i = rand.nextInt(moves.size());
            Pair<Node, Integer> mov = moves.get(i);
            moves.remove(i);

            try {
                mov.getKey().setValue(mov.getValue());
                generateSolution(mov.getKey(), remaining_nodes--);
                notFound = false;
            } catch (Node.InvalidTypeException e) {
                System.err.println("We somehow reached an invalid node!");
                System.err.println(e);
                throw new Error();
            } catch (HidatoIsFilledWrongException e) {
                mov.getKey().clear();
            }
        }

        if (notFound) throw new HidatoIsFilledWrongException();
    }

    /* Generates all possible solutions */
    /*
    ArrayList<Hidato> generateAllSolution() {
        return new ArrayList;
    }*/

}

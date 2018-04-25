package domain;

import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.Map;

public class Solver {
    private Hidato hidato;
    private Random rand = new Random();
    private HashMap<Node, ArrayList<Node>> visited = new HashMap<>();

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
    ArrayList<Pair<Node, Integer>> nextMove(Node a) throws Node.InvalidTypeException {
        ArrayList<Node>                 nodes = hidato.adjacentNodes(a);
        ArrayList<Pair<Node, Integer>>  moves = new ArrayList<>();
        Integer nv = a.getValue();

        nodes.removeAll(visited.getOrDefault(a, new ArrayList<>()));

        // If there is any node that has value +1, that's the only possible movement, unless already taken.
        for (Node b : nodes) {
            if (b.hasValue()) {
                try {
                    if (b.getValue() == nv) {
                        moves.add(new Pair<>(b, nv));
                        return moves;
                    }
                } catch (Node.InvalidTypeException e) {
                    System.err.println("We somehow reached an invalid node!");
                    System.err.println(e);
                    throw new Error();
                }
            }
        }

        // If not, all adjacent unset nodes may have an increasing or decreasing value.
        for (Node b : nodes) {
            if (b.getType() == Node.Type.unset) moves.add(new Pair<>(b, nv+1));
        }

        return moves;
    }

    class HidatoIsFilledWrongException extends Exception {}

    /* Generates a random solution  */
    // TODO: To guarentee one solution or unsolvable we must try every start node. Therefore maintain a list of already tried nodes.
    public Hidato generateSolution() throws HidatoIsFilledWrongException, Node.InvalidTypeException {
        Node start_node = null;
        for (Node n : hidato) if (n.getType() == Node.Type.fixed && n.getValue() == 1) start_node = n;
        ArrayList<Node> unset_nodes = new ArrayList<>();
        for (Node n : hidato) if (n.getType() == Node.Type.unset) unset_nodes.add(n);

        // Select a start node. If there is any of fixed type, one of them, otherwise any unset node.

        if (start_node == null) {
            if (unset_nodes.isEmpty()) {
                if (isSolution()) return hidato.copy();
                else throw new HidatoIsFilledWrongException();
            }
            int i = rand.nextInt(unset_nodes.size()) + 1;
            start_node = unset_nodes.get(i);
            start_node.setValue(1);
        }

        Hidato original = hidato;
        hidato = hidato.copy();

        // Backtracking
        generateSolution(start_node, unset_nodes.size());
        visited.clear();

        Hidato copy = hidato;
        hidato = original;
        return copy;
    }

    private void generateSolution(Node n, int remaining_nodes) throws HidatoIsFilledWrongException {
        if (remaining_nodes == 0) {
            if (!isSolution()) throw new HidatoIsFilledWrongException();
            else return;
        }

        ArrayList<Pair<Node, Integer>> moves;
        try {
             moves = nextMove(n);
        } catch (Node.InvalidTypeException e) {
            System.err.println("We somehow reached an invalid node!");
            System.err.println(e);
            throw new Error();
        }

        //Select random movement and try until no movements left.
        boolean notFound = true;
        while (!moves.isEmpty() && notFound) {
            int i = rand.nextInt(moves.size());
            Pair<Node, Integer> mov = moves.get(i);
            moves.remove(i);

            try {
                mov.getKey().setValue(mov.getValue());
                visited.putIfAbsent(n, new ArrayList<>());
                visited.get(n).add(mov.getKey());
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

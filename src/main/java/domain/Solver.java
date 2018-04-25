package domain;

import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.Map;

public class Solver {
    private Hidato hidato;
    private Random rand = new Random();
    private ArrayList<Node> visited = new ArrayList<>();
    private int fill_size = 0;

    Solver(Hidato h) {
        hidato = h;
        for (Node n : hidato) {
            if (n.getType() == Node.Type.fixed || n.getType() == Node.Type.unset) fill_size++;
        }
    }

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
        Integer nv = a.getValue()+1;

        nodes.removeAll(visited);

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

        // If not, all adjacent unset nodes may have an increasing value.
        for (Node b : nodes) {
            if (b.getType() == Node.Type.unset) moves.add(new Pair<>(b, nv));
        }

        return moves;
    }

    private class HidatoIsFilledWrongException extends Exception {}
    public class SolutionNotFound extends Exception {}

    /* Generates a random solution  */
    // TODO: To guarentee one solution or unsolvable we must try every start node. Therefore maintain a list of already tried nodes.
    public Hidato generateSolution() throws SolutionNotFound {
        Node start_node = null;
        ArrayList<Node> candidate_nodes = new ArrayList<>();

        //The swap must be here or else we will get NullPointerExceptions. Why?
        //We would be selecting a start_node from the original hidato, which doesn't exist on the copy (it's a different object).
        Hidato original = hidato;
        hidato = hidato.copy();

        try {
            for (Node n : hidato) if (n.getType() == Node.Type.fixed && n.getValue() == 1) start_node = n;
            for (Node n : hidato) if (n.getType() == Node.Type.unset) candidate_nodes.add(n);
        } catch (Node.InvalidTypeException e) {
            throw new Error("Logic Error!");
        }

        boolean not_found = true;
        boolean fixed = start_node != null;
        do {
            try {
                // Select a start node. If there is any of fixed type, one of them, otherwise any unset node.
                if (!fixed) {
                    if (candidate_nodes.isEmpty() && not_found) {
                        if (isSolution()) return hidato.copy();
                        else throw new SolutionNotFound();
                    }
                    int i = rand.nextInt(candidate_nodes.size());
                    start_node = candidate_nodes.get(i);
                    start_node.setValue(1);
                }
            } catch (Node.InvalidTypeException e) {
                throw new Error("Logic Error!");
            }

            // Backtracking
            visited.add(start_node);
            try {
                generateSolution(start_node);
                not_found = false;
            } catch (HidatoIsFilledWrongException e) {
                candidate_nodes.remove(start_node);
            }
            visited.clear();
            start_node.clear();
            System.out.println("START NODE: " + start_node);
        } while (!candidate_nodes.isEmpty() && not_found && !fixed);

        if (not_found) throw new SolutionNotFound();

        Hidato copy = hidato;
        hidato = original;
        return copy;
    }

    private void generateSolution(Node n) throws HidatoIsFilledWrongException {
        System.out.println(visited);
         if (visited.size() == fill_size) return; //SOLUTION FOUND!

        //Get a list of possible movements.
        ArrayList<Pair<Node, Integer>> moves;
        try {
             moves = nextMove(n);
        } catch (Node.InvalidTypeException e) {
            System.err.println("We somehow reached an invalid node!");
            System.err.println(e);
            throw new Error();
        } catch (NullPointerException e) {
            System.err.println("NULL POINTER EXCEPTION ON ADJACENCY FOR NODE: " + n);
            System.err.println("Check that the solver is using the correct hidato!");
            throw e;
        }

        //Keep trying until no movements left.
        boolean notFound = true;
        while (!moves.isEmpty() && notFound) {
            int i = rand.nextInt(moves.size());
            Pair<Node, Integer> mov = moves.get(i);
            moves.remove(i);

            try {
                mov.getKey().setValue(mov.getValue());
                visited.add(mov.getKey());
                generateSolution(mov.getKey());
                notFound = false;
            } catch (Node.InvalidTypeException e) {
                System.err.println("We somehow reached an invalid node!");
                System.err.println(e);
                throw new Error();
            } catch (HidatoIsFilledWrongException e) {
                mov.getKey().clear();
                visited.remove(mov.getKey());
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

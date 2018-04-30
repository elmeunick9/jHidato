package domain;

import java.util.ArrayList;
import java.util.Random;


public class Solver {
    private Hidato hidato;
    private Random rand = new Random();
    private ArrayList<Node> visited = new ArrayList<>();
    private int fillSize = 0;
    private boolean printTraceOption = false;

    Solver(Hidato h) {
        hidato = h;
        for (Node n : hidato) {
            if (n.getType() == Node.Type.fixed || n.getType() == Node.Type.unset) fillSize++;
        }
    }

   /* There is a hamiltonian strictly increasing by a order of 1 path from min to max node values.*/
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

    /* Generates a list of valid next movements, an empty list indicates no movements */
    ArrayList<Pair<Node, Integer>> nextMove(Node a) throws Node.InvalidTypeException {
        ArrayList<Node>                 nodes = hidato.adjacentNodes(a);
        ArrayList<Pair<Node, Integer>>  moves = new ArrayList<>();
        Integer nv = a.getValue()+1;

        nodes.removeAll(visited);

        // If there is any node that has value +1, that's the only possible movement,
        // unless already taken.
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

    public Hidato generateSolution() throws SolutionNotFound{
        return generateSolution(fillSize);
    }

    /* Generates a random solution  */
    public Hidato generateSolution(int minLen) throws SolutionNotFound {
        Node startNode = null;
        ArrayList<Node> candidateNodes = new ArrayList<>();

        //The swap must be here or else we will get NullPointerExceptions. Why?
        //We would be selecting a startNode from the original hidato, not the copy.
        Hidato original = hidato;
        hidato = hidato.copy();

        for (Node n : hidato) if (n.hasValue() && n.getValue() == 1) startNode = n;
        for (Node n : hidato) if (n.getType() == Node.Type.unset) candidateNodes.add(n);

        boolean notFound = true;
        boolean fixed = startNode != null;
        do {
            // Select a start node. A fixed type or set to 1, otherwise any unset node.
            if (!fixed) {
                if (candidateNodes.isEmpty() && notFound) {
                    if (isSolution()) return hidato.copy();
                    else throw new SolutionNotFound();
                }
                int i = rand.nextInt(candidateNodes.size());
                startNode = candidateNodes.get(i);
                startNode.setValue(1);
            }

            // Backtracking
            visited.add(startNode);
            try {
                generateSolution(startNode, minLen);
                notFound = false;
            } catch (HidatoIsFilledWrongException e) {
                candidateNodes.remove(startNode);
                visited.clear();
                startNode.clear();
            }

            if (printTraceOption) System.out.println("START NODE: " + startNode);
        } while (!candidateNodes.isEmpty() && notFound && !fixed);

        if (notFound) throw new SolutionNotFound();

        Hidato copy = hidato;
        hidato = original;
        return copy;
    }

    private void generateSolution(Node n, int minLen) throws HidatoIsFilledWrongException {
        if (printTraceOption) System.out.println(visited);

        //SOLUTION FOUND!
        if (visited.size() == fillSize) return;

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
                generateSolution(mov.getKey(), minLen);
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

        if (notFound) {

            //PARTIAL SOLUTION FOUND. I would put this in the line below but fucking linter.
            if (minLen != -1 && visited.size() >= minLen) return;

            throw new HidatoIsFilledWrongException();
        }
    }

    /* Generates all possible solutions */
    /*
    ArrayList<Hidato> generateAllSolution() {
        return new ArrayList;
    }*/

}

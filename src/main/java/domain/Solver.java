package domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Solver {
    private Hidato hidato;
    private Random rand = new Random();
    private ArrayList<Node> visited = new ArrayList<>();
    private ArrayList<Node> fixedNodes = new ArrayList<>();
    private ArrayDeque<Node> fixedNodesDeque = new ArrayDeque<>();
    private HashMap<Node, HashMap<Node, Integer>> distance = new HashMap<>();
    private int fillSize = 0;
    private boolean printTraceOption = false;

    /** Generates a solver for a given hidato. On this call some precomputation is done.
     @param h Hidato. */
    Solver(Hidato h) {
        hidato = h;
        for (Node n : hidato) {
            if (n.getType() == Node.Type.unset || n.getType() == Node.Type.fixed) fillSize++;
        }
    }

    private void precompute() {
        fixedNodes.clear();
        fixedNodesDeque.clear();
        distance.clear();

        ArrayList<Pair<Node, Pair<Integer, Integer>>> fixedNodesPos = new ArrayList<>();
        for (Node n : hidato) {
            if (n.getType() == Node.Type.fixed) fixedNodes.add(n);
        }

        fixedNodes.sort(Node.compareByValue);

        for (int i = 1; i <= hidato.getSizeX(); i++) {
            for(int j = 1; j <= hidato.getSizeY(); j++) {
                if (!hidato.isIndexBounded(i, j)) continue;
                Node n = hidato.getNode(i, j);
                if(n.getType() != Node.Type.fixed) continue;
                fixedNodesPos.add(new Pair<>(n, new Pair<>(i, j)));
            }
        }

        for (int i = 1; i <= hidato.getSizeX(); i++) {
            for(int j = 1; j <= hidato.getSizeY(); j++) {
                if (!hidato.isIndexBounded(i, j)) continue;
                Node n = hidato.getNode(i, j);
                distance.put(n, new HashMap<>());
                for (Pair<Node, Pair<Integer, Integer>> f : fixedNodesPos) {
                    double x = i-f.getValue().getKey();
                    double y = j-f.getValue().getValue();
                    Double d = Math.sqrt(x*x + y*y);
                    distance.get(n).put(f.getKey(), d.intValue());
                }
            }
        }

        //Collections.reverse(fixedNodes);
        fixedNodesDeque.addAll(fixedNodes);
    }

   /* There is a hamiltonian strictly increasing by a order of 1 path from min to max node values.*/
    /* TODO: To implement using nextMove */
    private boolean isSolution() {
        return false;
    }

    //Based on euclidian distance. Only checks if two nodes are reacheble.
    //Rather than a heuristic, it actually is a restriction.
    private int heuristicFunction(Node n, Node target, int fromValue) {
        int diff = target.getValue() - fromValue;
        if (diff == 0) return 1;
        if (fromValue > target.getValue()) return 1;
        int mind = distance.get(n).get(target) / 2;
        return mind > diff ? 0 : 1;
    }

    /** Generates a list of valid next movements, an empty list indicates no movements.
     * Internally it filters out visited nodes. If you want a list of all possible
     * movements instead, use Hidato::adjacentNodes.
     * <p>
     * A movement is a pair of (Node, Integer) with the next node to go and the value it should have.
     * @param a The node to look from.
     * @return A list movements. */
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
            if (b.getType() == Node.Type.unset) {
                moves.add(new Pair<>(b, nv));
            }
        }

        return moves;
    }

    private class HidatoIsFilledWrongException extends Exception {}
    public class SolutionNotFound extends Exception {}

    /** Generates a random solution.
     * @return A copy of the Hidato to be solved, but solved.
     * @throws SolutionNotFound If a solution doesn't exist. */
    public Hidato generateSolution() throws SolutionNotFound{
        return generateSolution(fillSize);
    }

    /** Generates a random partial solution.
     * <p>
     * The algorithm does a random backtracking algorithm taking as the root node a node with
     * the value of 1. If such node doesn't exists it randomly tries to set to 1 a given unset Node
     * and continues from there.
     * <p>
     * Some optimizations (pruning) it does:
     * <ul>
     *     <li>If a node with a given value is too far from the next fixed node, discard it.</li>
     *     <li>If a fixed node is reached where there are still smaller fixed nodes, discard it.</li>
     *     <li>If a the current node value is equal to the next fixed node but it's not that node, discard it.</li>
     * </ul>
     * <p>
     * For more information consult the manual.
     * @param minLen The minimum length of the solution to find. If it's smaller than the amount of nodes
     *               that can hold a value, the solution will be partial.
     * @return A copy of the hidato to be solved, but solved.
     * @throws SolutionNotFound If a partial solution of the required minimum length doesn't exist.
      */
    public Hidato generateSolution(int minLen) throws SolutionNotFound {
        Node startNode = null;
        ArrayList<Node> candidateNodes = new ArrayList<>();

        //The swap must be here or else we will get NullPointerExceptions. Why?
        //We would be selecting a startNode from the original hidato, not the copy.
        Hidato original = hidato;
        hidato = hidato.copy();
        precompute();

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

            int h = 1;
            if (!fixedNodesDeque.isEmpty()) {
                h = heuristicFunction(startNode, fixedNodesDeque.getFirst(), 1);
            }
            if (h == 0) {
                candidateNodes.remove(startNode);
                startNode.clear();

            } else {
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

        //If we are fixed node, make sure to remove it from the queue.
        Node nodeRemovedFromQueue = null;
        if (n.getType() == Node.Type.fixed) {
            if (fixedNodesDeque.getFirst() == n) {
                fixedNodesDeque.removeFirst();
                nodeRemovedFromQueue = n;
            } else if (!fixedNodesDeque.isEmpty()) {
                //If we reached a fixed node ignoring middle ones we sure are on the wrong path.
                throw new HidatoIsFilledWrongException();
            }
        } else {
            // If the next fixed node value is equal to this one, which is not fixed, we are on a
            // wrong path.
            if (!fixedNodesDeque.isEmpty() && fixedNodesDeque.getFirst().getValue() == n.getValue()) {
                throw new HidatoIsFilledWrongException();
            }
        }

        //Get a list of possible movements.
        ArrayList<Pair<Node, Integer>> moves;
        try {
             moves = nextMove(n);
        } catch (NullPointerException e) {
            System.err.println("NULL POINTER EXCEPTION ON ADJACENCY FOR NODE: " + n);
            System.err.println("Check that the solver is using the correct hidato!");
            throw e;
        }

        //Keep trying movements until no movements left.
        boolean notFound = true;
        while (!moves.isEmpty() && notFound) {
            int i = rand.nextInt(moves.size());
            Pair<Node, Integer> mov = moves.get(i);
            moves.remove(i);

            //If the movement would bring us to a unreachable node (to next fixed) discard it.
            if (!fixedNodesDeque.isEmpty()) {
                int h = heuristicFunction(mov.getKey(), fixedNodesDeque.getFirst(), mov.getValue());
                if (h == 0) continue;
            }

            try {
                mov.getKey().setValue(mov.getValue());
                visited.add(mov.getKey());
                generateSolution(mov.getKey(), minLen);
                notFound = false;
            } catch (HidatoIsFilledWrongException e) {
                mov.getKey().clear();
                visited.remove(mov.getKey());
            }

        }

        if (notFound) {

            //If failed, and we just removed a node from fixed queue, reinsert it.
            if (nodeRemovedFromQueue != null) fixedNodesDeque.addFirst(nodeRemovedFromQueue);

            //PARTIAL SOLUTION FOUND.
            if (minLen != -1 && visited.size() >= minLen) return;

            throw new HidatoIsFilledWrongException();
        }
    }

}

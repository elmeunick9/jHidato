package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Hidato {
    public enum AdjacencyType { VERTEX, EDGE, BOTH };
    protected AdjacencyType adjacency;
    private ArrayList<ArrayList<Node>> nodes;
    private Map<Node, ArrayList<Node>> map = new HashMap<>();

    Hidato(ArrayList<ArrayList<Node>> data, AdjacencyType t) {
        nodes = data;
        adjacency = t;

        for (int i = 1; i <= data.size(); i++)
            for (int j = 1; j <= data.size(); j++) {
                Node n = getNode(i, j);
                if (!n.valid()) continue;
                map.put(n, adjacentNodes(i, j));
            }
    }

    protected abstract ArrayList<Node> adjacentNodes(int i, int j);
    public ArrayList<Node> adjacentNodes(Node n) {
        ArrayList<Node> t = new ArrayList<>(map.get(n));
        t.remove(n);
        return t;
    }

    public boolean isIndexBounded(int i, int j) {
        return i > 0 && j > 0 && i <= nodes.size() && j <= nodes.get(i-1).size();
    }

    //Get a node from the matrix.
    public Node getNode(int x, int y) throws IndexOutOfBoundsException {
        return nodes.get(x-1).get(y-1);
    }

    //Get a node based on it's cell geometry and position.
    public abstract Node getNodeByCoord(float i, float j);

    public abstract void draw();

    public void clear() {
        for (ArrayList<Node> list : nodes) { for (Node n : list) n.clear(); }
    }

    public abstract Hidato copy();
    public abstract Hidato copy(AdjacencyType t);

    protected ArrayList<ArrayList<Node>> copyData() {
        ArrayList<ArrayList<Node>> copy = new ArrayList(nodes.size());
        for (ArrayList<Node> list : nodes) {
            copy.add(new ArrayList<>());
            for (Node x : list) {
                copy.get(copy.size()-1).add(x.copy());
            }
        }
        return copy;
    }

}

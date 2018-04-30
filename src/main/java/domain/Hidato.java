package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class Hidato implements Iterable<Node> {
    public enum AdjacencyType { VERTEX, EDGE, BOTH };
    protected AdjacencyType adjacency;
    private ArrayList<ArrayList<Node>> nodes;
    private Map<Node, ArrayList<Node>> map = new HashMap<>();


    Hidato(ArrayList<ArrayList<Node>> data, AdjacencyType t) {
        nodes = data;
        adjacency = t;

        for (int i = 1; i <= data.size(); i++) {
            for (int j = 1; j <= data.get(i-1).size(); j++) {
                Node n = getNode(i, j);
                if (n.valid()) map.put(n, adjacentNodes(i, j));
            }
        }
    }

    @Override
    public Iterator<Node> iterator() {
        return new Iterator<>() {
            private int i = -1;
            private int j = 0;

            //CHECKSTYLE:OFF
            @Override
            public boolean hasNext() {
                int k = i;
                int s = j;
                if (k < nodes.get(s).size()-1) { k++; }
                else                           { k = 0; s++; }
                return s < nodes.size() && k < nodes.get(s).size();
            }

            @Override
            public Node next() {
                if (!hasNext()) throw new NoSuchElementException();
                if (i < nodes.get(j).size()-1)  { i++; }
                else                            { i = 0; j++; }
                return nodes.get(j).get(i);
            }
            //CHECKSTYLE:ON

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
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

    ArrayList<ArrayList<Node>> copyData() {
        ArrayList<ArrayList<Node>> copy = new ArrayList<>(nodes.size());
        for (ArrayList<Node> list : nodes) {
            copy.add(new ArrayList<>());
            for (Node x : list) {
                copy.get(copy.size()-1).add(x.copy());
            }
        }
        return copy;
    }

    public void print() {
        for (ArrayList<Node> a : nodes) {
            System.out.print("{");
            for (Node n : a) {
                String node = n.toString();
                String space = "    ".substring(node.length());

                System.out.print('"' + node + '"' + (a.get(a.size()-1) != n ? "," + space : ""));
            }
            System.out.print("}");
            if (nodes.get(nodes.size()-1) != a) System.out.print(",");
            System.out.println();
        }
    }

}

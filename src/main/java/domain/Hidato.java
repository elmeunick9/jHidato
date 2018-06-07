package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Datastructure to represent an hidato. As an abstract class,
 * you should call some subclass constructor.
 */
public abstract class Hidato implements Iterable<Node> {
    public enum AdjacencyType { VERTEX, EDGE, BOTH };
    protected AdjacencyType adjacency;
    private ArrayList<ArrayList<Node>> nodes;
    private Map<Node, ArrayList<Node>> map = new HashMap<>();
    private int sizeY = 0;
    private int sizeX = 0;

    Hidato(ArrayList<ArrayList<Node>> data, AdjacencyType t) {
        nodes = data;
        adjacency = t;

        for (int i = 1; i <= data.size(); i++) {
            int j;
            for (j = 1; j <= data.get(i-1).size(); j++) {
                Node n = getNode(i, j);
                if (n.valid()) map.put(n, adjacentNodes(i, j));
            }

            // The size_y of the matrix is the max. Why? You shouldn't, but what if data is not
            // a uniform matrix but rather a bunch of lists of different sizes?
            if (j+1 > sizeY) sizeY = j+1;
        }
        sizeX = data.size();
    }

    /** @return Returns the size of the matrix representing this hidato on the X axis.
     * All nodes count, even invisible ones!
     */
    public int getSizeX() { return sizeX; }
    /** @return Returns the size of the matrix representing this hidato on the Y axis.
     * All nodes count, even invisible ones!
     */
    public int getSizeY() { return sizeY; }

    /** Helps itarting all nodes of an hidato. Doesn't use axis. Useful for writing for each loops.
     * @return Node iterator.
     */
    @Override
    public Iterator<Node> iterator() {
        return new Iterator<Node>() {
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

    /** Generates a list of all adjacent nodes from a given node. The list takes into account
     * the adjacency type. Non valid nodes are filtered out.
     * <p>
     * This function cost is constant, internally it uses an adjacency lookup table created on
     * this class constructor. Therefore changes on nodes that may change their adjacency should
     * not be allowed. In case you find a way, abstain to do so.
     * @param n The node to look from.
     * @return List of adjacent nodes. */
    public ArrayList<Node> adjacentNodes(Node n) {
        ArrayList<Node> t = new ArrayList<>(map.get(n));
        t.remove(n);
        return t;
    }

    /**
     * @param i Coordinate on the X axis. Starting from 1.
     * @param j Coordinate on the Y axis. Starting from 1.
     * @return True if the given coordinates are inside the matrix limits */
    public boolean isIndexBounded(int i, int j) {
        return i > 0 && j > 0 && i <= nodes.size() && j <= nodes.get(i-1).size();
    }

    /** Get a node from the matrix
     * @param x Coordinate on the X axis. Starting from 1.
     * @param y Coordinate on the Y axis. Starting from 1.
     * @return The node.
     * **/
    public Node getNode(int x, int y) throws IndexOutOfBoundsException {
        return nodes.get(x-1).get(y-1);
    }

    public AdjacencyType getAdjacency() {
        return adjacency;
    }

    /** Like the name implies, clears all end every node on this hidato. Calls Node::clear()
     * @see Node
     */
    public void clear() {
        for (ArrayList<Node> list : nodes) { for (Node n : list) n.clear(); }
    }

    /** Makes a deep copy of this hidato.
     * @return A copied instance of the hidato. */
    public abstract Hidato copy();

    /** Makes a deep copy of this hidato. Also allows to change the adjacency type of this copy.
     * @param t New adjacency type.
     * @return A copied instance of the hidato. */
    public abstract Hidato copy(AdjacencyType t);

    /** Deep copies the hidato matrix and returns it. Use to create new hidatos based on this one.
     * @return The new data matrix. */
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

    /** Gets a portable representation of this hidato. Why we do this here, or why it returns
     * a list of strings is beyond me.
     * @param type The Hidato.Type of this? hidato.
     * @return A list of strings, each one representing one line, of a representation of the
     * hidato. */
    public ArrayList<String> getRawData(Game.HidatoType type) {
        ArrayList<String> data = new ArrayList<>();
        String typeString = "";
        if(type == Game.HidatoType.TRIANGLE) {
            typeString = "T";
        } else if (type == Game.HidatoType.SQUARE) {
            typeString = "Q";
        } else if (type == Game.HidatoType.HEXAGON) {
            typeString = "H";
        }
        String header = typeString +",";
        if(AdjacencyType.EDGE == adjacency) {
            header += "C,";
        } else if(AdjacencyType.BOTH == adjacency) {
            header += "CA,";
        }
        //else if(AdjacencyType.VERTEX == adjacency) {
            //CANNOT BE A VERTEX TYPE
        //}
        header+= nodes.size()+","+nodes.get(0).size();
        data.add(header);

        for (ArrayList<Node> a : nodes) {
            String line = "";
            for (Node n : a) {
                String node = n.toString();
                if(a.get(a.size()-1) != n){
                    node += ",";
                }
                line += node;
            }
            data.add(line);
        }
        return data;
    };

    /** This helper function prints on the standard output a good fomatted text representation
     * of the hidato. Of course the format goes to hell if not a QuadHidato. Also it can only
     * keep the proper format for hidatos of size smaller than 1000. */
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

    /** This helper function returns the size of the hidato as in cells with values */
    public int count() {
        int c = 0;
        for (Node n : this) {
            if (n.editable() || n.hasValue()) c++;
        }
        return c;
    }

    /** This helper function returns whether the hidato has any Node with a value set. */
    public boolean isCleared() {
        for (Node n : this) {
            if (n.getType() == Node.Type.variable) return false;
        }
        return true;
    }
}

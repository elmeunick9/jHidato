package domain;

import java.util.ArrayList;

public class TriHidato extends Hidato {
    public TriHidato(ArrayList<ArrayList<Node>> data, AdjacencyType t) { super(data, t); }

    public TriHidato copy() { return copy(adjacency); }
    public TriHidato copy(AdjacencyType t) {
        ArrayList<ArrayList<Node>> nodes = copyData();
        return new TriHidato(nodes, t);
    }

    private void addNodeIfValid(int x, int y, ArrayList<Node> list) {
        if (isIndexBounded(x, y)) {
            Node n = getNode(x, y);
            if (n.valid()) list.add(n);
        }
    }

    protected ArrayList<Node> adjacentNodes(int i, int j) {
        ArrayList<Node> nodes = new ArrayList<>();
        if (adjacency == AdjacencyType.EDGE || adjacency == AdjacencyType.BOTH) {
            addNodeIfValid(i, j-1, nodes);
            addNodeIfValid(i, j+1, nodes);
            addNodeIfValid((i%2 == j%2) ? i+1 : i-1, j, nodes);
        }
        if (adjacency == AdjacencyType.VERTEX || adjacency == AdjacencyType.BOTH) {
            if (i%2 == j%2) {
                addNodeIfValid(i-1, j, nodes);
                addNodeIfValid(i+1, j-2, nodes);
                addNodeIfValid(i+1, j+2, nodes);
            } else {
                addNodeIfValid(i+1, j, nodes);
                addNodeIfValid(i-1, j+2, nodes);
                addNodeIfValid(1-1, j-2, nodes);
            }
        }

        return nodes;
    }

    public void draw() {}

    //TODO: This is a mock implementation.
    public Node getNodeByCoord(float i, float j) { return new Node("#");}
}

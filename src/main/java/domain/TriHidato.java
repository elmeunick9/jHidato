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
        int x, y;
        if (adjacency == AdjacencyType.EDGE || adjacency == AdjacencyType.BOTH) {
            x = i; y = j-1; addNodeIfValid(x, y, nodes);
            x = i; y = j+1; addNodeIfValid(x, y, nodes);
            x = (i%2 == j%2) ? i+1 : i-1; y = j; addNodeIfValid(x, y, nodes);
        }
        if (adjacency == AdjacencyType.VERTEX || adjacency == AdjacencyType.BOTH) {
            if (i%2 == j%2) {
                x = i-1; y = j;     addNodeIfValid(x, y, nodes);
                x = i+1; y = j-2;   addNodeIfValid(x, y, nodes);
                x = i+1; y = j+2;   addNodeIfValid(x, y, nodes);
            }
            else {
                x = i+1; y = j;     addNodeIfValid(x, y, nodes);
                x = i-1; y = j+2;   addNodeIfValid(x, y, nodes);
                x = i-1; y = j-2;   addNodeIfValid(x, y, nodes);
            }
        }

        return nodes;
    }

    public void draw() {}

    //TODO: This is a mock implementation.
    public Node getNodeByCoord(float i, float j) { return new Node("#");}
}

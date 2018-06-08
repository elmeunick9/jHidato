package domain;

import java.util.ArrayList;

public class HexHidato extends Hidato {
    public HexHidato(ArrayList<ArrayList<Node>> data, Hidato.AdjacencyType t) { super(data, t); }

    public HexHidato copy() { return copy(adjacency); }
    public HexHidato copy(Hidato.AdjacencyType t) {
        ArrayList<ArrayList<Node>> nodes = copyData();
        return new HexHidato(nodes, t);
    }

    private void addNodeIfValid(int x, int y, ArrayList<Node> list) {
        if (isIndexBounded(x, y)) {
            Node n = getNode(x, y);
            if (n.valid()) list.add(n);
        }
    }

    /**
     * Depends on the parity the value that are adjacent in the matrix is
     * the next or the previous for the previous and next row.
     */
    protected ArrayList<Node> adjacentNodes(int i, int j) {
        ArrayList<Node> nodes = new ArrayList<>();

        if (adjacency == Hidato.AdjacencyType.EDGE) {
            addNodeIfValid(i+1, j, nodes);
            addNodeIfValid(i, j-1, nodes);
            addNodeIfValid(i, j+1, nodes);
            addNodeIfValid(i-1, j, nodes);
            if(i%2==1){
                addNodeIfValid(i-1, j-1, nodes);
                addNodeIfValid(i+1, j-1, nodes);

            } else {
                addNodeIfValid(i-1, j+1, nodes);
                addNodeIfValid(i+1, j+1, nodes);
            }

        }

        return nodes;
    }

}

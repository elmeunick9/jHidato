package domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HexHidatoTest {
    private HexHidato hidato;
    @Before
    public void before() {
        ArrayList<ArrayList<Node>> data = new ArrayList<>();
        String[][] matrix = {
                {"#", "#", "#", "#", "#", "#", "#"},
                {"#", "#", "#", "*", "#", "#", "#"},
                {"#", "#", "?", "1", "?", "?", "?"},
                {"#", "?", "?", "16","?", "?", "?"},
                {"*", "?", "?", "7", "?", "?", "*"},
        };

        for (String[] list : matrix) {
            data.add(new ArrayList<>());
            for (String x : list) {
                data.get(data.size() - 1).add(new Node(x));
            }
        }

        hidato = new HexHidato(data, Hidato.AdjacencyType.EDGE);
    }

    @Test
    public void setValuesTest() throws Node.InvalidTypeException {
        Hidato c = hidato.copy(Hidato.AdjacencyType.VERTEX);
        c.getNode(4, 2).setValue(8);
        hidato.getNode(4, 2).setValue(9);
        assertEquals(c.getNode(4,2).getValue(), 8);
        assertEquals(hidato.getNode(4,2).getValue(), 9);
    }

    @Test
    public void typeNodeTest() throws Node.InvalidTypeException {
        hidato.getNode(4, 2).setValue(8);
        assertEquals(hidato.getNode(1,1).getType(), Node.Type.invisible);
        assertEquals(hidato.getNode(5,1).getType(), Node.Type.block);
        assertEquals(hidato.getNode(5,7).getType(), Node.Type.block);
        assertEquals(hidato.getNode(3,4).getType(), Node.Type.fixed);
        assertEquals(hidato.getNode(3,4).getValue(), 1);
        assertEquals(hidato.getNode(4,2).getValue(), 8);
    }

    @Test
    public void adjacencyEdgeTest() throws Node.InvalidTypeException {
        Node a = hidato.getNode(3, 4);
        Node b = hidato.getNode(4, 4);
        Node c = hidato.getNode(5, 4);

        ArrayList<Node> list = hidato.adjacentNodes(hidato.getNode(4, 5));
        assertTrue(list.contains(a));
        assertTrue(list.contains(b));
        assertTrue(list.contains(c));
    }
}

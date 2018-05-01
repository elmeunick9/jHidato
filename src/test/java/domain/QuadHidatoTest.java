package domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class QuadHidatoTest {
    private QuadHidato hidato;
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

        hidato = new QuadHidato(data, Hidato.AdjacencyType.EDGE);
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
        Node a = hidato.getNode(5, 4);
        assertEquals(a.getValue(), 7);

        Node b = hidato.getNode(4, 3);
        b.setValue(3);
        Node c = hidato.getNode(3, 4);
        Node d = hidato.getNode(4, 6);

        ArrayList<Node> list = hidato.adjacentNodes(hidato.getNode(4, 4));
        assertTrue(list.contains(a));
        assertTrue(list.contains(b));
        assertTrue(list.contains(c));
        assertTrue(!list.contains(d));
        assertEquals(list.get(list.indexOf(b)).getValue(), 3);
    }

    @Test
    public void adjacencyVertexTest() throws Node.InvalidTypeException {
        Hidato c = hidato.copy(Hidato.AdjacencyType.VERTEX);
        Node a1 = c.getNode(3, 3);
        Node a2 = c.getNode(3, 5);
        Node a3 = c.getNode(5, 3);
        Node a4 = c.getNode(5, 5);
        ArrayList<Node> list = c.adjacentNodes(c.getNode(4, 4));
        assertTrue(list.contains(a1));
        assertTrue(list.contains(a2));
        assertTrue(list.contains(a3));
        assertTrue(list.contains(a4));

    }
}

package domain;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import java.util.ArrayList;

public class QuadHidatoTest {
	/*private QuadHidato hidato;

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
    public void generalTest() throws Node.InvalidTypeException {
        Hidato c = hidato.copy(Hidato.AdjacencyType.VERTEX);
        c.getNode(4, 2).setValue(8);
        hidato.getNode(4, 2).setValue(9);
        Assert.assertEquals(c.getNode(1,1).getType(), Node.Type.invisible);
        Assert.assertEquals(c.getNode(5,1).getType(), Node.Type.block);
        Assert.assertEquals(c.getNode(5,7).getType(), Node.Type.block);
        Assert.assertEquals(c.getNode(3,4).getType(), Node.Type.fixed);
        Assert.assertEquals(c.getNode(3,4).getValue(), 1);
        Assert.assertEquals(c.getNode(4,2).getValue(), 8);
        Assert.assertEquals(hidato.getNode(4,2).getValue(), 9);
    }

    @Test
    public void typeNodeTest() throws Node.InvalidTypeException {
        hidato.getNode(4, 2).setValue(8);
        Assert.assertEquals(hidato.getNode(1,1).getType(), Node.Type.invisible);
        Assert.assertEquals(hidato.getNode(5,1).getType(), Node.Type.block);
        Assert.assertEquals(hidato.getNode(5,7).getType(), Node.Type.block);
        Assert.assertEquals(hidato.getNode(3,4).getType(), Node.Type.fixed);
        Assert.assertEquals(hidato.getNode(3,4).getValue(), 1);
        Assert.assertEquals(hidato.getNode(4,2).getValue(), 8);
    }

    @Test
    public void adjacencyTest() throws Node.InvalidTypeException {
        //Test by EDGE
        Node a = hidato.getNode(5, 4);
        Assert.assertEquals(a.getValue(), 7);

        Node b = hidato.getNode(4, 3);
        b.setValue(3);

        ArrayList<Node> list = hidato.adjacentNodes(hidato.getNode(4, 4));
        Assert.assertTrue(list.contains(a));
        Assert.assertTrue(list.contains(b));
        Assert.assertEquals(list.get(list.indexOf(b)).getValue(), 3);

        //Test by VERTEX
        Hidato c = hidato.copy(Hidato.AdjacencyType.VERTEX);
        a = hidato.getNode(5, 4);
        b = hidato.getNode(3, 4);
        list = hidato.adjacentNodes(hidato.getNode(4, 4));
        Assert.assertTrue(list.contains(a));
        Assert.assertTrue(list.toString().contains("?"));
    }*/
}

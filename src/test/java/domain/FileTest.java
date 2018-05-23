package domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import persistance.CustomFile;

import java.util.ArrayList;



public class FileTest {
    @Test
    public void gettingTriHidato() throws IOException, Node.InvalidTypeException {
        Hidato hidato = CustomFile.importHidato("example.txt");
        assertEquals(1, hidato.getNode(3,4).getValue());

        Node a = hidato.getNode(5, 4);
        assertEquals(a.getValue(), 7);

        Node b = hidato.getNode(4, 3);
        b.setValue(3);

        ArrayList<Node> list = hidato.adjacentNodes(hidato.getNode(4, 4));
        assertTrue(list.contains(a));
        assertTrue(list.contains(b));
        assertEquals(list.get(list.indexOf(b)).getValue(), 3);
    }

    @Test
    public void gettingQuadHidato() throws IOException, Node.InvalidTypeException {
        Hidato hidato = CustomFile.importHidato("exampleQuadHidato.txt");
        assertEquals(1, hidato.getNode(3,4).getValue());

        Node a = hidato.getNode(5, 4);
        assertEquals(a.getValue(), 7);

        Node b = hidato.getNode(4, 3);
        b.setValue(3);

        ArrayList<Node> list = hidato.adjacentNodes(hidato.getNode(4, 4));
        assertTrue(list.contains(a));
        assertTrue(list.contains(b));
        assertEquals(list.get(list.indexOf(b)).getValue(), 3);
    }
}

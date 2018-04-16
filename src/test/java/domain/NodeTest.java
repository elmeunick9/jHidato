package domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class NodeTest {

    @Test
    public void testMe() throws Node.InvalidTypeException {
        Node a = new Node("#");
        assertEquals(a.toString(),"#");
        assertEquals(a.getType(), Node.Type.invisible);
        Node b = new Node("?");
        b.setValue(5);
        assertEquals(b.toString(),"5v");
        b.clear();
        assertEquals(b.toString(),"?");
        Node c = new Node("98");
        assertEquals(c.toString(),"98");
    }

    @Test
    public void copyTest1() throws Node.InvalidTypeException {
        Node a = new Node("4");
        Node b = a.copy();
        assertEquals(a.getValue(), 4);
        assertEquals(b.getValue(), 4);
    }

    @Test
    public void copyTest2() throws Node.InvalidTypeException {
        Node a = new Node("?");
        Node b = a.copy();
        a.setValue(91);
        assertEquals(a.getValue(), 91);
        assertEquals(b.getType(), Node.Type.unset);
    }
}

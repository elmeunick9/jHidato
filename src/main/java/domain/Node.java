package domain;

import java.util.Comparator;

/** A node, cell, block, item, name it, of an hidato.
 * <p>
 * Extra effort has been put on making those comfy to use and constant. The string representation
 * of a node depends on its type. Check the enum Type for more info.
 */
public class Node {
    /** The type of node. */
    public enum Type {
        variable, /** Has a value and can be modified. As a string, it's represented with an integer number and the letter 'v' attached. */
        fixed, /** Has a value but can not be modified. As a string, it's represented with an integer number. */
        block, /** Doesn't have a value, can't have one. Is visible. Represented with the symbol * */
        invisible, /** Doesn't have a value, can't have one. Is not visible. Represented with the symbol # */
        unset /** Doesn't have a value, if it had, it would be Variable. Is visible. Represented with the symbol ? */
    }
    private int value = 0;
    private Type type;

    /** Constructs a node given a string that represents it.
     * @param data The string representation of the node.
     */
    public Node(String data) {
        if      ("#".equals(data)) type = Type.invisible;
        else if ("*".equals(data)) type = Type.block;
        else if ("?".equals(data)) type = Type.unset;
        else if ("v".equals(data.substring(data.length()-1))) {
            value = Integer.parseInt(data.substring(0, data.length()-1));
            type = Type.variable;
        } else {
            type = Type.fixed;
            value = Integer.parseInt(data);
        }
    }

    /** Look at the Type enum definition.
     * @see Type
     * @return The string representation of the node.*/
    @Override
    public String toString() {
        if      (type == Type.invisible) return "#";
        else if (type == Type.block) return "*";
        else if (type == Type.unset) return "?";
        else {
            String v = Integer.toString(value);
            if (type == Type.variable) v += "v";
            return v;
        }
    }

    public Node copy() {
        String s = toString();
        if (s.endsWith("v")) {
            s = s.substring(0, s.length()-1);
            Node k = new Node("?");
            k.value = Integer.parseInt(s);
            k.type = Type.variable;
            return k;
        } else return new Node(s);
    }

    public Type getType(){ return type; }
    public boolean editable()   { return type == Type.unset || type == Type.variable; }
    public boolean valid()      { return type != Type.invisible && type != Type.block; }
    public boolean hasValue()   { return type == Type.variable || type == Type.fixed; }

    /** Usually thrown when you try to apply an operation over an incompatible type. */
    class InvalidTypeException extends RuntimeException {}

    public int getValue() throws InvalidTypeException {
        if (hasValue()) return value;
        else throw new InvalidTypeException();
    }
    public void setValue(int v) throws InvalidTypeException {
        if (type == Type.unset)     type = Type.variable;
        if (type == Type.variable)  value = v;
        else if (!(type == Type.fixed && value == v)) throw new InvalidTypeException();
    }

    /** Makes a variable unset again. */
    public void clear() {
        if (type == Type.variable) type = Type.unset;
    }

    public static Comparator<Node> compareByValue = new Comparator<Node>() {
        public int compare(Node a, Node b) {
            return a.getValue() - b.getValue();
        }
    };
}

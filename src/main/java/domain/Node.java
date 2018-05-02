package domain;

import java.util.Comparator;

public class Node {
    public enum Type { variable, fixed, block, invisible, unset }
    private int value = 0;
    private Type type;

    Node(String data) {
        if      ("#".equals(data)) type = Type.invisible;
        else if ("*".equals(data)) type = Type.block;
        else if ("?".equals(data)) type = Type.unset;
        else {
            type = Type.fixed;
            value = Integer.parseInt(data);
        }
    }

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

    public void clear() {
        if (type == Type.variable) type = Type.unset;
    }

    public static Comparator<Node> COMPARE_BY_VALUE = new Comparator<Node>() {
        public int compare(Node a, Node b) {
            return a.getValue() - b.getValue();
        }
    };

    public static Comparator<Node> COMPARE_BY_VALUE_DESCENDING = new Comparator<Node>() {
        public int compare(Node a, Node b) {
            return b.getValue() - a.getValue();
        }
    };
}

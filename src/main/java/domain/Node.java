package domain;

public class Node {
    public enum Type { variable, fixed, block, invisible, unset }
    Node(String data) {
        if      (data.equals("#")) type = Type.invisible;
        else if (data.equals("*")) type = Type.block;
        else if (data.equals("?")) type = Type.unset;
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
            return k;
        } else return new Node(s);
    }

    public Type getType(){ return type; }
    public boolean editable() { return type == Type.unset || type == Type.variable; }
    public boolean valid() { return type != Type.invisible && type != Type.block; }

    private int value = 0;
    private Type type;

    class InvalidTypeException extends Exception {};

    public int getValue() throws InvalidTypeException {
        if (type == Type.variable || type == Type.fixed) return value;
        else throw new InvalidTypeException();
    }
    public void setValue(int v) throws InvalidTypeException {
        if (type == Type.unset) type = Type.variable;
        if (type == Type.variable) value = v;
        else throw new InvalidTypeException();
    }

    public void clear() {
        if (type == Type.variable) type = Type.unset;
    }
}

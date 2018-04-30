package domain;

public class Pair<T, G> {
    private T first;
    private G second;

    Pair(T a, G b) {
        first = a;
        second = b;
    }

    public T getKey()   { return first; }
    public G getValue() { return second; }

    public void setKey(T k)     { first = k; }
    public void setValue(G v)   { second = v; }

    @Override
    public String toString() {
        return "<" + first + ", " + second + ">";
    }

    @Override
    public boolean equals(Object that){
        if (that.getClass() == this.getClass()) {
            Pair<T, G> inst = (Pair<T, G>) that;
            return inst.getKey() == this.getKey() && inst.getValue() == this.getValue();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return first.hashCode() * second.hashCode() + first.hashCode();
    }
}

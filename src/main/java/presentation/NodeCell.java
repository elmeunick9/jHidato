package presentation;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;


abstract class NodeCell {

    abstract public void setSize(double givenSize);

    abstract public void drawNode(int i, int j, Graphics2D g2);

    abstract public void paintNode(int i, int j, String n, Graphics2D g2);


    abstract public Point pixelsToCoord(int posx, int posy);
    abstract public void setBorderLeft(int border);
    abstract public void setBorderTop(int border);

    abstract public Vector<Double> screenProperties(int screenWidth, int screenHeight, int boardHeight, int boardWidth);

    protected int roundToInt(double x) {
        int r = (int)Math.round(x);
        return r;
    }
}

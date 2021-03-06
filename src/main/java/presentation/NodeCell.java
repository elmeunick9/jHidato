package presentation;

import java.awt.*;
import java.util.Vector;

/**
 * The interface of the Type of Cells used in board.
 */
abstract class NodeCell {

    protected Color FIXED_NODE_BGCOLOR = new Color(195, 198, 198);
    protected Color VALOR_NODE_BGCOLOR = new Color(222, 222, 222);
    protected Color BLOCK_NODE_BGCOLOR = new Color(175, 174, 156);


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

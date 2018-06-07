package presentation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Vector;

import static java.lang.Math.sqrt;


public class TriangleNode extends NodeCell{
    private int borderLeft=0;
    private int borderTop=0;
    private double size=0;
    private double x2=0;
    private double x1=0;


    @Override
    public void setSize(double size) {
        this.x2 = size*2/sqrt(3);
        this.x1 = x2/2;
        this.size = size;
    }

    private Polygon node(int x0, int y0, int i, int j) {
        int y = y0 + borderTop;
        int x = x0 + borderLeft;

        int[] cx;
        int[] cy;

        cx = new int[] {x, x+roundToInt(x1), x+roundToInt(x2)};
        if (isVertical(i,j)) {
            cy = new int[] {y+roundToInt(size), y, y+roundToInt(size)};
        } else {
            cy = new int[] {y, y+roundToInt(size), y};
        }

        return new Polygon(cx,cy,3);
    }

    @Override
    public void drawNode(int i, int j, Graphics2D g2) {
        int x = j * roundToInt(x1);
        int y = i * roundToInt(size);
        g2.setColor(new Color(222, 222, 222));
        g2.fillPolygon(node(x,y,i,j));
        g2.setColor(Color.BLACK);
        g2.drawPolygon(node(x,y,i,j));
    }

    @Override
    public void paintNode(int i, int j, String n, Graphics2D g2) {
        int x = j * roundToInt(x1);
        int y = i * roundToInt(size);
        //block
        if (n.equals("*")) {
            g2.setColor(BLOCK_NODE_BGCOLOR);
            g2.fillPolygon(node(x,y,i,j));
            g2.setColor(Color.BLACK);
            g2.drawPolygon(node(x,y,i,j));
        } else if(n.equals("?")) {
            g2.setColor(VALOR_NODE_BGCOLOR);
            g2.fillPolygon(node(x,y,i,j));
            g2.setColor(Color.BLACK);
            g2.drawPolygon(node(x,y,i,j));
        } else if(n.contains("v")){
            g2.setColor(VALOR_NODE_BGCOLOR);
            g2.fillPolygon(node(x,y,i,j));
            g2.setColor(Color.BLACK);
            g2.drawPolygon(node(x,y,i,j));
            g2.setColor(Color.BLACK);
            g2.drawString(n.substring(0,n.length()-1), x+(int)Math.round(x1)/2+borderLeft+((int)Math.round(x1)/4), (y+(int)Math.round(size)/2)+(borderTop+10));
        } else {
            g2.setColor(FIXED_NODE_BGCOLOR);
            g2.fillPolygon(node(x,y,i,j));
            g2.setColor(Color.BLACK);
            g2.drawPolygon(node(x,y,i,j));
            g2.setColor(Color.BLACK);
            g2.drawString(n, x+(int)Math.round(x1)/2+borderLeft+((int)Math.round(x1)/4), (y+(int)Math.round(size)/2)+(borderTop+10));
        }
    }

    @Override
    public Point pixelsToCoord(int posx, int posy) {
        Point p = new Point(-1,-1);

        posx -= borderLeft;
        posy -= borderTop;

        int y = (int)Math.floor(posy/size);
        int x = (int)Math.floor(posx/x1);

        //half
        double half = size/x1;

        double auxX;
        if (isVertical(y,x)) auxX = posx - ((x) * x1);
        else auxX = ((x+1) * x1) - posx;
        double auxY = ((y+1) * size) - posy;

        if(y%2==0) {
            if(x%2==0 && (auxY/auxX > half)) {
                --x;
            } else if(x%2!=0 && (auxY/auxX < half)) {
                --x;
            }
        } else {
            if(x%2!=0 && (auxY/auxX > half)) {
                --x;
            } else if(x%2==0 && (auxY/auxX < half)) {
                --x;
            }
        }

        p.x=x;
        p.y=y;
        return p;
    }

    @Override
    public void setBorderTop(int border) {
        this.borderTop = border;
    }
    @Override
    public void setBorderLeft(int border) {
        this.borderLeft = border;

    }

    @Override
    public Vector<Double> screenProperties(int screenWidth, int screenHeight, int boardHeight, int boardWidth) {


        double nodeHeight = boardHeight*1.05;
        double nodeWidth = boardWidth*1.05;

        double nodeSize;

        //screen with ra higher than the hidato
        if ((double)screenHeight/(double)screenWidth >= nodeHeight/nodeWidth) {
            nodeSize = (double)screenWidth/nodeWidth;
        } else {
            nodeSize = (double)screenHeight/nodeHeight;
        }

        int bTop = 0;
        int bLeft = 0;

        bLeft = (int) (screenWidth - nodeWidth*nodeSize)/2;
        bTop = (int) (screenHeight - nodeHeight*nodeSize)/2+1;

        Vector<Double> properties = new Vector<Double>();
        properties.add(nodeSize);
        properties.add((double)bTop);
        properties.add((double)bLeft);

        return properties;
    }
    //Returns if the triangle is vertical or not
    private boolean isVertical(int i, int j) {
       return i%2 == j%2;
    }

    private boolean isPair(int i){
        return i%2==0;
    }
}



package presentation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Vector;


public class HexagonNode extends NodeCell{
    private int borderLeft=0;
    private int borderTop=0;
    private double size=0;
    private double x2=0;
    private double x1=0;
    private double y1=0;
    private double y2=0;


    @Override
    public void setSize(double size) {
        this.size = size;
        double radi = size/2;
        y1 = size/4;
        y2 = size/4 * 3;

        x1 = radi*Math.sqrt(3)/2;
        x2 = radi*Math.sqrt(3);
    }

    private Polygon node(int x0, int y0, int i, int j) {
        int y = y0 + borderTop;
        int x = x0 + borderLeft;

        int[] cx;
        int[] cy;

        cx = new int[] {x+(int)Math.round(x1),x+(int)Math.round(x2),x+(int)Math.round(x2),x+(int)Math.round(x1),x,x};
        cy = new int[] {y,y+(int)Math.round(y1),y+(int)Math.round(y2),y+(int)Math.round(size),y+(int)Math.round(y2),y+(int)Math.round(y1)};

        return new Polygon(cx,cy,6);
    }

    @Override
    public void drawNode(int i, int j, Graphics2D g2) {
        int x = j * roundToInt(x2);
        int y = i * roundToInt(y2);
        if(i%2==1) {
            x+=x1;
        }
        g2.setColor(new Color(222, 222, 222));
        g2.fillPolygon(node(x,y,i,j));
        g2.setColor(Color.BLACK);
        g2.drawPolygon(node(x,y,i,j));
    }

    @Override
    public void paintNode(int i, int j, String n, Graphics2D g2) {
        int x = j * roundToInt(x2);
        int y = i * roundToInt(y2);
        if(i%2==1) {
            x+=x1;
        }

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

        int y = (int) (posy / y2);

        double xleft = ((posx - ((y%2)*x1))/ (x2)); //per controlar que si cliques a la esquerra de les filles parelles, no sigui x = 0;
        int x = -1;
        if (xleft >= 0) x = (int) xleft;

        double distxf = ((x+1)*x2 - posx + (y%2)*x1);
        double distxi = (posx - x*x2 - (y%2)*x1);
        double distxc = (distxf - distxi)/2;
        double distyi = (posy - y*y2);

        if (y%2 == 0) { //files parelles, on l'hexagon esta desplaçat cap a la dreta mig hexagon
            int aux = (int) (posx - x1);

        }

        if (distyi <= y1){                 //quadrat de dalt de cada hexagon, incloent un triangle de cadascun dels hexagons adjacents per dalt
            if (Math.abs(distyi/distxc) < 0.5){
                --y;
                if (y%2 == 0){
                    if (distxc < 0) ++x;
                } else{
                    if (distxc > 0) --x;
                }
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



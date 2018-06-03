package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import static java.lang.Math.sqrt;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;


public class Board extends JPanel {

    NodeCell node;

    private double nodeHeight;
    private int border = 0;
    Vector<Integer> nombresPerDefecte;
    ArrayList<ArrayList<String>> matrixHidato;

    private double screenWidth;
    private double screenHeight;
    private int boardWidth;
    private int boardHeight;
    private int nextMove = -1;
    private int last = 1;

    //necessita que el creador faci panel.setPreferredSize(dim);
    //Aquesta constructora es fa servir per jugar una nova partida
    public Board(NodeCell node, ArrayList<ArrayList<String>> matrix){
        //setBorder(new LineBorder(new Color(0, 0, 0)));
        this.node = node;
        this.matrixHidato = matrix;

        this.nombresPerDefecte = new Vector <Integer>();
        nombresPerDefecte.add(1);
        nombresPerDefecte.add(11);
        this.last = nombresPerDefecte.get(nombresPerDefecte.size()-1);

        boardWidth = matrixHidato.get(0).size();
        boardHeight = matrixHidato.size();

        setOpaque(false);

        this.screenWidth = 600;
        this.screenHeight = 600;
        nodeSize();
    }

    public void calcSizeAndRepaint(int screenWidth, int screenHeight) {
        this.screenWidth = 500;
        this.screenHeight = 500;


        nodeSize();
    }

    private void nodeSize(){
        Vector<Double> properties = node.screenProperties((int)screenWidth, (int)screenHeight, boardHeight, boardWidth);
        double borderLeft = properties.get(2);
        double borderTop = properties.get(1);
        nodeHeight = properties.get(0);

        node.setBorderLeft((int)borderLeft);
        node.setBorderTop((int)borderTop);
        node.setSize(nodeHeight);

    }

    public void updateMatriu(ArrayList<ArrayList<String>> m) {
        this.matrixHidato = m;
        repaint();
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //millora linies diagonals dibuixades
        g.setFont(new Font("Arial", Font.BOLD, (int)nodeHeight/3));
        super.paintComponent(g2);

        for (int i=0;i<matrixHidato.size();i++) {
            for (int j=0;j<matrixHidato.get(0).size();j++) {
                String aux = matrixHidato.get(i).get(j);
                if (aux != "#"){
                    node.drawNode(i,j,g2);
                    if (isInteger(aux)) node.paintNode(i,j,aux, last, g2);
                    else node.paintNode(i, j, aux, last, g2);
                }
            }
        }
    }

    public void setNextMove(int nextM) {
        this.nextMove = nextM;
    }

    public void setLast(int l) {
        this.last = l;
        repaint();
    }

    public void setPossiblesMoviments(Vector<Integer> nombresPerDefecte) {
        this.nombresPerDefecte = nombresPerDefecte;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}

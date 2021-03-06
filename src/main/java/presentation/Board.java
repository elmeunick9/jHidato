package presentation;

import domain.CtrlDomain;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

/**
 * Creates a matrix of NodeCells paint in the screen.
 */
public class Board extends JPanel {

    NodeCell node;

    private double nodeHeight;
    private int border = 0;
    ArrayList<ArrayList<String>> matrixHidato;

    private double screenWidth;
    private double screenHeight;
    private int boardWidth;
    private int boardHeight;
    private int nextMove = 1;

    /**
     * Generates a board of NodeCell depending on the Subclass of the NodeCell.
     *
     */
    public Board(NodeCell node, ArrayList<ArrayList<String>> matrix){
        //setBorder(new LineBorder(new Color(0, 0, 0)));
        this.node = node;
        this.matrixHidato = matrix;

        boardWidth = matrixHidato.get(0).size();
        boardHeight = matrixHidato.size();

        setOpaque(false);

        MouseListener mouseListener = new MouseListener();
        addMouseListener(mouseListener);

        this.screenWidth = 700;
        this.screenHeight = 700;
        nodeSize();
    }

    /**
     * Calculates the size of each cell.
     */
    private void nodeSize(){
        Vector<Double> properties = node.screenProperties(
                (int)screenWidth,
                (int)screenHeight,
                boardHeight,
                boardWidth
        );
        double borderLeft = properties.get(2);
        double borderTop = properties.get(1);
        nodeHeight = properties.get(0);

        node.setBorderLeft((int)borderLeft);
        node.setBorderTop((int)borderTop);
        node.setSize(nodeHeight);

    }

    public void updateMatrix(ArrayList<ArrayList<String>> m) {
        this.matrixHidato = m;
        repaint();
    }

    /**
     * Paint the board. Paint each cell except if is a invisible node.
     */
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(new Font("Arial", Font.BOLD, (int)nodeHeight/3));
        super.paintComponent(g2);

        for (int i=0;i<matrixHidato.size();i++) {
            for (int j=0;j<matrixHidato.get(0).size();j++) {
                String aux = matrixHidato.get(i).get(j);
                if (!aux.equals("#")){
                    node.drawNode(i,j,g2);
                    node.paintNode(i, j, aux, g2);
                }
            }
        }
    }

    public void setNextMove(int nextM) {
        this.nextMove = nextM;
    }

    class MouseListener extends MouseAdapter {
        //CtrlPresentation ctrlP = CtrlPresentation.getInstance();
        public void mousePressed(MouseEvent e){
            Point p = new Point(node.pixelsToCoord(e.getX(),e.getY()));
            if (p.x < 0 || p.y < 0 || p.x >= matrixHidato.get(0).size() || p.y >= matrixHidato.size()) return;

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (CtrlPresentation.getInstance().leftClick(p.y,p.x, nextMove))
                    matrixHidato = CtrlDomain.getInstance().getMatrix();
                nextMove++;
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                if (CtrlPresentation.getInstance().rightClick(p.y, p.x))
                    matrixHidato = CtrlDomain.getInstance().getMatrix();
                nextMove--;
            }
            boolean editorMode = CtrlPresentation.getInstance().editorMode;
            if (SwingUtilities.isMiddleMouseButton(e) && editorMode) {
                boolean ct = e.isControlDown();
                if (CtrlPresentation.getInstance().middleClick(p.y, p.x, ct))
                    matrixHidato = CtrlDomain.getInstance().getMatrix();
            }
            repaint();

            //Check if you just solved the hidato!
            if (CtrlDomain.getInstance().isSolved()) {
                CtrlPresentation.getInstance().finishGame();
            }
        }
    }
}

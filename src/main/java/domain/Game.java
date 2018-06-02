package domain;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private CtrlDomain domain;

    public enum Difficulty { EASY, MEDIUM, HARD, CUSTOM };
    public enum HidatoType { TRIANGLE, SQUARE, HEXAGON};
    private int score = 0;
    private HidatoType ht;
    private Difficulty dif;
    private Hidato h;
    private String filename;
    private User user;
    private long timeInit;
    private long currTime = 0;

    Game(CtrlDomain domain, Difficulty d, User u, HidatoType htype) {
        this(domain, d, u, htype, new Generator(d, htype));
    }

    Game(CtrlDomain dom, Difficulty d, User u, Hidato hidato, HidatoType hidatoType,
         String filename) {
        domain = dom;
        h = hidato;
        dif = d;
        this.filename = filename;
        user = u;
        ht = hidatoType;
        timeInit = System.currentTimeMillis();
    }

    Game(CtrlDomain dom, Difficulty d, User u, HidatoType htype, Generator g) {
        this(dom, d, u, g.getHidato(), htype, g.getHashedFilename());
    }

    public int getScore() { return score; }
    public User getPlayer() {
        return user;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String fn) { filename = fn; }

    public Difficulty getDif() {
        return dif;
    }

    public int getValue(int x, int y) throws Node.InvalidTypeException{
        return h.getNode(x,y).getValue();
    }

    public HidatoType getHt() {
        return ht;
    }

    /*Add or substract the score with minimum of 0*/
    private void changeScore(int s) {
        if(s > 0  || (this.score + s) >= 0)
            this.score += s;
        else if(s < 0) {
            this.score = 0;
        }
    }

    private boolean moveIsvalid(Node n) {
        boolean ret = false;
        if(n.editable())
            ret = true;
        return ret;
    }

    public void setDif(Difficulty d) {
        this.dif = d;
    }

    public void move(int x, int y, int value) throws Node.InvalidTypeException {
        if(moveIsvalid(this.h.getNode(x, y))) {
            this.h.getNode(x, y).setValue(value);
            changeScore(1);
        } else {
            changeScore(-1);
        }

    }

    /*Save the stats of the game when user pause or leave the game*/
    public void saveGame() throws IOException {
        String dificult;
        switch (dif){
            case EASY:
                dificult = "easy";
                break;
            case MEDIUM:
                dificult = "medium";
                break;
            case HARD:
                dificult = "hard";
                break;
            default:
                dificult = "custom";
                break;
        }
        domain.getCtrlPersistence().saveGame(user.getName(),
                filename, h.getRawData(ht), dificult, currTime);
    }

    public void loadGame(String file) {

    }

    public static Hidato.AdjacencyType getAdjacencyType(String at) {
        Hidato.AdjacencyType ret = null;
        switch (at) {
            case "C":
                ret = Hidato.AdjacencyType.EDGE;
                break;
            case "CA":
                ret = Hidato.AdjacencyType.BOTH;
                break;
            default:
                break;
        }
        return ret;
    }

    private Hidato loadHidato(ArrayList<ArrayList<Node>> data, Hidato.AdjacencyType adj) {
        switch (ht) {
            case TRIANGLE:
                return new TriHidato(data, adj);
            case SQUARE:
                return new QuadHidato(data, adj);
            case HEXAGON:
                return new HexHidato(data, adj);
            default:
                return new TriHidato(data, adj);
        }
    }

    public static HidatoType getHidatoType(String t) {
        HidatoType ret = null;
        switch (t) {
            case "T":
                ret = HidatoType.TRIANGLE;
                break;
            case "Q":
                ret = HidatoType.SQUARE;
                break;
            case "H":
                ret = HidatoType.HEXAGON;
                break;
            default:
                ret = HidatoType.SQUARE;
        }
        return ret;
    }

    /*Refresh stats from user and ranking when a game is over
    * return the time in miliseconds */
    public long finishGame() {
        user.gameFinished();
        currTime += System.currentTimeMillis() - timeInit;
        return currTime;
    }

    public void print() {
        h.print();
    }

    public void solve() throws Solver.SolutionNotFound {
        Solver s = new Solver(h);
        h = s.generateSolution();
    }

    public Hidato getHidato() { return h; }
}

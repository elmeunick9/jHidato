package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    private CtrlDomain domain;

    public enum Difficulty { EASY, MEDIUM, HARD, CUSTOM };
    public enum HidatoType { TRIANGLE, SQUARE, HEXAGON};

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

    private boolean moveIsvalid(Node n) {
        boolean ret = false;
        if(n.editable())
            ret = true;
        return ret;
    }

    public boolean move(int x, int y, int value) throws Node.InvalidTypeException {
        if(value == -1) {
            this.h.getNode(x,y).clear();
            return true;
        }
        if(moveIsvalid(this.h.getNode(x, y))) {
            this.h.getNode(x, y).setValue(value);
            return true;
        }
        return false;
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

    public static Difficulty getDifficultyType(String dif) {
        switch (dif) {
            case "easy": return Difficulty.EASY;
            case "medium": return Difficulty.MEDIUM;
            case "hard": return Difficulty.HARD;
            case "custom": return Difficulty.CUSTOM;
            default: return Difficulty.CUSTOM;
        }
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

    public void clear() {
        h.clear();
    }

    public ArrayList<ArrayList<String>> getRawData() {
        ArrayList<String> a = h.getRawData(ht);
        //FirstLine of Info Unused on Presentation.
        a.remove(0);
        ArrayList<ArrayList<String>> ret = new ArrayList<>();
        for(String d : a) {
            ArrayList aux= new ArrayList(Arrays.asList(d.split(",")));
            ret.add(aux);
        }
        return ret;
    }
}

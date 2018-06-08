package domain;

import persistance.CtrlPersistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Game is the class that allows to the user
 * to play the Hidato. This class loads in memory the Hidato
 * from the disk or trigger to generate a random one.
 */
public class Game {
    public enum Difficulty { EASY, MEDIUM, HARD, CUSTOM };
    public enum HidatoType { TRIANGLE, SQUARE, HEXAGON};

    private HidatoType ht;
    private Difficulty dif;
    private Hidato h;
    private String filename;
    private User user;
    private long timeInit;
    private long timeElapsed;
    private long currTime = 0;

    Game(Difficulty d, User u, HidatoType htype, Hidato.AdjacencyType adj) {
        this(d, u, htype, new Generator(d, htype, adj));
    }

    Game(Difficulty d, User u, Hidato hidato, HidatoType hidatoType,
         String filename) {
        h = hidato;
        dif = d;
        this.filename = filename;
        user = u;
        ht = hidatoType;
        timeInit = System.currentTimeMillis();
        timeElapsed = 0;
    }

    Game(Difficulty d, User u, HidatoType htype, Generator g) {
        this(d, u, g.getHidato(), htype, g.getHashedFilename());
    }

    public User getPlayer() {
        return user;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String fn) { filename = fn; }

    public void setTime(long t) { timeElapsed = t; }

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
    /**
     * Check if the given move it's possible.
     * If it's correct the given possition of the node
     *  is setted with the value.
     */
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

    /**
     * Save the stats of the game in disk when user pause or leave the game.
     * */
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
        currTime = timeElapsed + System.currentTimeMillis() - timeInit;
        CtrlDomain.getInstance().getCtrlPersistence().saveGame(user.getName(),
                filename, h.getRawData(ht), dificult, currTime);
    }

    /**
     * Returns the equivalent of the string passed
     * to the Enum Difficulty.
     */
    public static Difficulty getDifficultyType(String dif) {
        switch (dif) {
            case "easy": return Difficulty.EASY;
            case "medium": return Difficulty.MEDIUM;
            case "hard": return Difficulty.HARD;
            case "custom": return Difficulty.CUSTOM;
            default: return Difficulty.CUSTOM;
        }
    }
    /**
     * Returns the equivalent of the string passed
     * to the Enum of Hidato AdjacencyType.
     */
    public static Hidato.AdjacencyType getAdjacencyType(String at) {
        Hidato.AdjacencyType ret = null;
        switch (at) {
            case "C":
                ret = Hidato.AdjacencyType.EDGE;
                break;
            case "CA":
                ret = Hidato.AdjacencyType.BOTH;
                break;
            case "A":
                ret = Hidato.AdjacencyType.VERTEX;
                break;
            default:
                break;
        }
        return ret;
    }

    /**
     * Returns the equivalent of the string passed
     * to the Enum of HidatoType.
     */
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

    /**
     * Returns the equivalent of the Enum of Type
     * to the equivalent string for the presentation.
     */
    public String getTypeToPresentation(){
        String ret;
        switch(ht) {
            case TRIANGLE:
                ret = "T";
                break;
            case SQUARE:
                ret = "Q";
                break;
            case HEXAGON:
                ret = "H";
                break;
            default:
                ret = "Q";
                break;
        }
        return ret;
    }

    /**Refresh stats from user and ranking when a game is over
    * return score */
    public long finishGame() throws IOException {
        user.gameFinished();
        currTime = timeElapsed + System.currentTimeMillis() - timeInit;

        //At 1p = Speed of 30s/Node.
        long score = ((long)getHidato().count()*300000) / currTime;

        CtrlDomain ctrl = CtrlDomain.getInstance();
        ctrl.addScoreToRanking((int)score);
        CtrlPersistence.getInstance().saveRanking(ctrl.getRankingData());

        return score;
    }

    public void print() {
        h.print();
    }

    /**
     * Creates a new Instance of Solver with the hidato
     * and will return the Hidato with one of the possible solution
     * (if exists).
     */
    public void solve() throws Solver.SolutionNotFound {
        Solver s = new Solver(h);
        h = s.generateSolution();
    }

    public Hidato getHidato() { return h; }

    public void clear() {
        h.clear();
    }

    /**
     * Returns the equivalent data of the Hidato
     * to a matrix of Strings.
     */
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

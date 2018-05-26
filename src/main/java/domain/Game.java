package domain;

import persistance.CtrlPersistencia;

import java.util.ArrayList;

public class Game {
    public enum Difficulty { EASY, MEDIUM, HARD, CUSTOM };
    public enum HidatoType { TRIANGLE, SQUARE, HEXAGON};
    private int score;
    private HidatoType ht;
    private Difficulty dif;
    private Hidato h;
    private String filename;
    private User user;
    private long timeInit;

    Game(Difficulty d, User u, HidatoType htype) {
        this(d, u, htype, new Generator(d, htype));
    }

    Game(Difficulty d, User u, HidatoType htype, Generator g) {
        score = 0;
        h = g.getHidato();
        dif = d;
        filename = g.getHashedFilename();
        user = u;
        ht = htype;
        timeInit = System.currentTimeMillis();
    }

    public int getScore() {
        return score;
    }

    public User getPlayer() {
        return user;
    }

    public String getFilename() {
        return filename;
    }

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
    public void saveGame() {
        CtrlPersistencia.saveGame(user.getName(), filename, h.getRawData(ht));
    }

    public void loadGame(String file) {
        ArrayList<String> infoLoaded = CtrlPersistencia.loadGame(user.getName(), file);
        System.out.print(infoLoaded);
        String line = infoLoaded.get(0);
        String[] params = line.split(",");
        ArrayList<ArrayList<Node>> dataHidato = new ArrayList<>();
        int x = Integer.parseInt(params[2]);
        System.out.println("LOAD?");

        //Generate the Matrix of <Node> to import the Hidato.
        for(int i = 1; i <= x; i++) {
            line = infoLoaded.get(i);
            String[] data = line.split(",");
            dataHidato.add(new ArrayList<>());
            for(String d : data) {
                dataHidato.get(dataHidato.size() - 1).add(new Node(d));
            }
        }
        ht = getHidatoType(params[0]);
        h = loadHidato(dataHidato, getAdjacencyType(params[1]));
        filename = file;
        /*
        dif = d;
        timeInit = System.currentTimeMillis();*/
    }

    private Hidato.AdjacencyType getAdjacencyType(String at) {
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

    private HidatoType getHidatoType(String t) {
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
        long timePlayed = System.currentTimeMillis() - timeInit;
        return timePlayed;
    }

    public void print() {
        h.print();
    }

    public void solve() throws Solver.SolutionNotFound {
        Solver s = new Solver(h);
        h = s.generateSolution();
    }

    public void clear() {
        h.clear();
    }

    public static void main(String[] args) {
        User u = new User("Oscar");
        Game game = new Game(Difficulty.EASY, u, HidatoType.SQUARE);
        game.print();
        game.loadGame("5acf9800");
        game.print();
        game.clear();
    }
}

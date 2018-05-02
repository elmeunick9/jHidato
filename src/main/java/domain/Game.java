package domain;

import java.time.Instant;

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

    }

    /*Refresh stats from user and ranking when a game is over
    * return the time in miliseconds */
    public long finishGame() {
        user.gameFinished();
        long timePlayed = System.currentTimeMillis() - timeInit;
        return timePlayed;
    }
}

package domain;

import persistance.CtrlPersistence;
import presentation.CtrlPresentation;

import java.util.ArrayList;

public class CtrlDomain {
    private Game game;
    private User user;
    private static CtrlDomain domain = null;
    protected CtrlDomain() {

    }

    public static CtrlDomain getInstance() {
        if(domain == null)
            domain = new CtrlDomain();
        return domain;
    }

    public void newPlayer(String username) {
        user = new User(username);
    }

    //Generate a new hidato and game.
    public ArrayList<ArrayList<String>> generateGame(int difficulty, int type) {
        Game.Difficulty d;
        switch (difficulty) {
            case 0: d = Game.Difficulty.EASY; break;
            case 1: d = Game.Difficulty.MEDIUM; break;
            case 2: d = Game.Difficulty.HARD; break;
            default: d = Game.Difficulty.EASY;
        }

        Game.HidatoType t;
        switch (type) {
            case 0: t = Game.HidatoType.TRIANGLE; break;
            case 1: t = Game.HidatoType.SQUARE; break;
            case 2: t = Game.HidatoType.HEXAGON; break;
            default: t = Game.HidatoType.SQUARE;
        }
        game = new Game(d, user, t);
        game.print();
        return game.getRawData();
    }

    public ArrayList<ArrayList<String>> getMatrix() {
        return game.getRawData();
    }

    //Create a custom game from scratch.
    public void createGame() {
        System.out.println("NOT IMPLEMENTED!");
    }

    public void setVal(int x, int y, int val) {
        game.move(x,y,val);
    }

    public CtrlPersistence getCtrlPersistence() { return CtrlPersistence.getInstance(); }
}

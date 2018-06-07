package domain;

import persistance.CtrlPersistence;
import presentation.CtrlPresentation;

import java.util.ArrayList;
import java.io.IOException;

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

    public CtrlPersistence getCtrlPersistence() { return CtrlPersistence.getInstance(); }
    public CtrlPresentation getCtrlPresentation() { return CtrlPresentation.getInstance(); }

    public void newPlayer(String username) {
        user = new User(username);
    }

    public String getUsername() { return user.getName(); }

    //Generate a new hidato and game.
    public void generateGame(String name, int difficulty, int type) {
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
        if (!name.isEmpty()) game.setFilename(name);
        game.print();
    }

    public ArrayList<ArrayList<String>> getMatrix() {
        return game.getRawData();
    }

    //Create a custom game from scratch.
    public void createGame(String name) {
        System.out.println("NOT IMPLEMENTED!");
    }

    /** Saves the current game.
     * @return Name of the game saved.
     * @throws IOException If there has been an error with IO
     * @throws IllegalStateException If user is not currently playing any game. */
    public String saveGame() throws IOException {
        if (game == null) throw new IllegalStateException("You must start a game first!");
        game.saveGame();
        return game.getFilename();
    }

    public void loadGame(String name) throws IOException {
        getCtrlPersistence().loadGame(user.getName(), name);
    }

    public void makeGameFromData(ArrayList<ArrayList<String>> data, String name, String adjacency,
                                 String type, String difficulty, String time) {
        ArrayList<ArrayList<Node>> nodes = new ArrayList<>();
        for (ArrayList<String> l : data) {
            nodes.add(new ArrayList<>());
            for (String s : l) {
                nodes.get(nodes.size()-1).add(new Node(s));
            }
        }

        Game.HidatoType t = Game.getHidatoType(type);
        Hidato.AdjacencyType a = Game.getAdjacencyType(adjacency);
        Game.Difficulty d = Game.getDifficultyType(difficulty);
        Hidato h = makeNewHidato(t, a, nodes);
        game = new Game(d, user, h, t, name);

        game.print();
    }

    public ArrayList<String> getClearHidatoData() {
        Hidato t = game.getHidato().copy();
        t.clear();
        return t.getRawData(game.getHt());
    }

    Hidato makeNewHidato(Game.HidatoType t, Hidato.AdjacencyType adj,
                                 ArrayList<ArrayList<Node>> nodes) {
        switch (t) {
            case TRIANGLE: return new TriHidato(nodes, adj);
            case SQUARE: return new QuadHidato(nodes, adj);
            case HEXAGON: return new HexHidato(nodes, adj);
            default: return new QuadHidato(nodes, adj);
        }
    }

    public boolean setVal(int x, int y, int val) {
        return game.move(x,y,val);
    }

    public String getTypeHidato() {
        return game.getTypeToPresentation();
    }
}

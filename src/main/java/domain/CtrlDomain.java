package domain;

import persistance.CtrlPersistence;
import presentation.CtrlPresentation;

import java.io.IOException;
import java.util.ArrayList;

public class CtrlDomain {
    private Game game;
    private User user;
    private CtrlPersistence persistence = null;
    private CtrlPresentation presentation = null;

    public CtrlDomain(CtrlPresentation present, CtrlPersistence persist) {
        persistence = persist;
        presentation = present;
    }

    public CtrlPersistence getCtrlPersistence() { return persistence; }
    public CtrlPresentation getCtrlPresentation() { return presentation; }

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
        game = new Game(this, d, user, t);
        if (!name.isEmpty()) game.setFilename(name);
        game.print();
    }

    //Create a custom game from scratch.
    public void createGame(String name) {
        System.out.println("NOT IMPLEMENTED!");
    }

    public void saveGame() throws IOException {
        if (game == null) throw new IllegalStateException("You must start a game first!");
        game.saveGame();
    }

    public void makeGameFromData(ArrayList<ArrayList<String>> data, String name, String adjacency,
                                 String type) {
        ArrayList<ArrayList<Node>> nodes = new ArrayList<>();
        for (ArrayList<String> l : data) {
            nodes.add(new ArrayList<>());
            for (String s : l) {
                nodes.get(nodes.size()-1).add(new Node(s));
            }
        }

        Game.HidatoType t = Game.getHidatoType(type);
        Hidato.AdjacencyType a = Game.getAdjacencyType(adjacency);
        Hidato h = makeNewHidato(t, a, nodes);
        game = new Game(this, Game.Difficulty.CUSTOM, user, h, t, name);

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
}

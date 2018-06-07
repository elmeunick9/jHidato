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
    public void generateGame(String name, int difficulty, int type, int adj) {
        generateGame(name, difficulty, type, adj, false);
    }

    public void generateGame(String name, int difficulty, int type, int adj, boolean empty) {
        Game.Difficulty d;
        switch (difficulty) {
            case 0: d = Game.Difficulty.EASY; break;
            case 1: d = Game.Difficulty.MEDIUM; break;
            case 2: d = Game.Difficulty.HARD; break;
            default: d = Game.Difficulty.HARD;
        }

        Game.HidatoType t;
        switch (type) {
            case 0: t = Game.HidatoType.TRIANGLE; break;
            case 1: t = Game.HidatoType.SQUARE; break;
            case 2: t = Game.HidatoType.HEXAGON; break;
            default: t = Game.HidatoType.SQUARE;
        }

        Hidato.AdjacencyType adjT;
        switch (adj) {
            case 0: adjT = Hidato.AdjacencyType.EDGE; break;
            case 1: adjT = Hidato.AdjacencyType.VERTEX; break;
            case 2: adjT = Hidato.AdjacencyType.BOTH; break;
            default: adjT = Hidato.AdjacencyType.EDGE;
        }

        if (!empty) {
            game = new Game(d, user, t, adjT);
            if (!name.isEmpty()) game.setFilename(name);
        } else {
            ArrayList<ArrayList<Node>> nodes = Generator.makeEmptyDataMatrix(d);
            Hidato h = makeNewHidato(t, adjT, nodes);
            String filename = name.isEmpty() ? Integer.toHexString(h.hashCode()) : name;
            game = new Game(d, user, h, t, filename);
        }
        game.print();
    }

    public ArrayList<ArrayList<String>> getMatrix() {
        return game.getRawData();
    }

    //Create a custom game from scratch.
    public void createGame(String name, int difficulty, int type, int adj) {
        generateGame(name, difficulty, type, adj, true);
    }

    /** Saves the current game.
     * @return Name of the game saved.
     * @throws IOException If there has been an error with IO
     * @throws IllegalStateException If user is not currently playing any game. */
    public String saveGame() throws IOException {
        if (game == null) throw new IllegalStateException("No game no life.");
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

    public int getHidatoSize() { return game.getHidato().count(); }

    public boolean setVal(int x, int y, int val) {
        return game.move(x,y,val);
    }

    public int getValue(int x, int y) {
        if (game == null) return 0;
        Node n = game.getHidato().getNode(x, y);
        try {
            return n.getValue();
        } catch (Node.InvalidTypeException e) {
            return -1;
        }
    }

    public boolean isNodeFixed(int x, int y) {
        if (game == null) return false;
        return game.getHidato().getNode(x, y).getType() == Node.Type.fixed;
    }

    public String getTypeHidato() {
        return game.getTypeToPresentation();
    }

    public boolean solve() {
        if (game == null) return false;
        Solver s = new Solver(game.getHidato());
        try {
            Hidato h = s.generateSolution();
            game = new Game(game.getDif(), user, h, game.getHt(), game.getFilename());
        } catch (Solver.SolutionNotFound e) {
            return false;
        }
        return true;
    }

    public boolean isClearerd() {
        if (game == null) throw new RuntimeException("No game no life.");
        return game.getHidato().isCleared();
    }

    public boolean clear() {
        if (game == null) throw new RuntimeException("No game no life.");
        game.getHidato().clear();
        return true;
    }

    public void convertToFixed() {
        if (game == null) throw new RuntimeException("No game no life.");
        ArrayList<ArrayList<Node>> data = game.getHidato().copyData();
        for (int j = 0; j < data.size(); j++) {
            ArrayList<Node> l = data.get(j);
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getType() == Node.Type.variable) {
                    String n = Integer.toString(l.get(i).getValue());
                    l.set(i, new Node(n));
                }
            }
            data.set(j, l);
        }

        Hidato h = makeNewHidato(game.getHt(), game.getHidato().getAdjacency(), data);
        game = new Game(game.getDif(), user, h, game.getHt(), game.getFilename());
    }
}

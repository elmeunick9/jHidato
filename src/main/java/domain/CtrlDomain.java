package domain;

public class CtrlDomain {
    private Game game;
    private User user;

    public void newPlayer(String username) {
        user = new User(username);
    }

    //Generate a new hidato and game.
    public void generateGame(int difficulty, int type) {
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
    }

    //Create a custom game from scratch.
    public void createGame() {
        System.out.println("NOT IMPLEMENTED!");
    }
}

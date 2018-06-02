package domain;

public class CtrlDomain {
    private Game game;
    private User user;

    public void newPlayer(String username) {
        user = new User(username);
    }

    //Generate a new hidato and game.
    public void generateGame(int difficulty) {
        Game.Difficulty d;
        switch (difficulty) {
            case 1: d = Game.Difficulty.EASY; break;
            case 2: d = Game.Difficulty.MEDIUM; break;
            case 3: d = Game.Difficulty.HARD; break;
            default: d = Game.Difficulty.EASY;
        }
        Game game = new Game(d, user, Game.HidatoType.SQUARE);
        game.print();
    }

    //Create a custom game from scratch.
    public void createGame() {
        System.out.println("NOT IMPLEMENTED!");
    }
}

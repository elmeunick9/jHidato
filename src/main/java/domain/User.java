package domain;

public class User {
    private String name;
    private int totalGames;

    public User(String name) {
        this.name = name;
        this.totalGames = 0;
    }

    public String getName() {
        return this.name;
    }

    public int getTotalGames() {
        return this.totalGames;
    }

    public void gameFinished() {
        this.totalGames += 1;
    }
}

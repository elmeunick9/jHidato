package domain;

public class CtrlDomain {
    private Game game;
    private User user;

    public void newPlayer(String username) {
        user = new User(username);
    }
}

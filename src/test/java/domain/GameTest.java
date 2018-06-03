package domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class GameTest {
    private Game game;
    private User user;
    private Generator generator;

    @Before
    public void before() {
        user = new User("test");
        generator = new GeneratorStub(Game.Difficulty.EASY, Game.HidatoType.TRIANGLE);
        game = new Game(Game.Difficulty.EASY, user, Game.HidatoType.TRIANGLE, generator);
    }

    @Test
    public void initialState() {
        assertEquals(user, game.getPlayer());
        assertEquals(Game.Difficulty.EASY, game.getDif());
        assertEquals(Game.HidatoType.TRIANGLE, game.getHt());
    }

    @Test
    public void validMove() throws Node.InvalidTypeException {
        game.move(4, 2, 9);
        assertEquals(9, game.getValue(4,2));
    }
}

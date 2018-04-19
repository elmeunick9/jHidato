package domain;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class UserTest {
    private User user;

    @Before
    public void before() {
        this.user = new User("Dummy");
    }

    @Test
    public void testGetName() {
        assertEquals("Dummy", user.getName());
    }

    @Test
    public void testTotalGames(){
        assertEquals(0, user.getTotalGames());
    }

    @Test
    public void testIncreaseTotalGames(){
        user.gameFinished();
        assertEquals(1, user.getTotalGames());
        user.gameFinished();
        assertEquals(2, user.getTotalGames());
    }
}
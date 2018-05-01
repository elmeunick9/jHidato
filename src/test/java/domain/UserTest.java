package domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private User user;

    @Before
    public void before() {
        this.user = new User("Dummy");
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("Dummy", user.getName());
    }

    @Test
    public void testTotalGames() {
        Assert.assertEquals(0, user.getTotalGames());
    }

    @Test
    public void testIncreaseTotalGames() {
        user.gameFinished();
        Assert.assertEquals(1, user.getTotalGames());
        user.gameFinished();
        Assert.assertEquals(2, user.getTotalGames());
    }
}

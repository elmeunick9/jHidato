package domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AppTest {

    @Test
    public void testAppHasAGreeting() {
        assertEquals("Projecte dels hidatos fet", App.getMessage());
    }
}

package domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {

    @Test
    public void testAppHasAGreeting() {
        assertEquals("Projecte dels hidatos fet", App.getMessage());
    }
}

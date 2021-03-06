package domain;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RankingTest {
    private Ranking rank;
    @Before
    public void before() throws IOException{
        rank = new Ranking();
        rank.clear();
        rank.addScore("ExampleUser2", 820);
        rank.addScore("ExampleUser", 920);
        rank.addScore("ExampleUse3", 598);
        rank.addScore("ExampleUse4", 498);
    }

    @Test
    public void gettingUserNames() {
        ArrayList<ArrayList<String>> r = rank.getRankingData();
        String aux = r.get(0).get(0);
        assertEquals("ExampleUser", aux);
        aux = r.get(1).get(0);
        assertEquals("ExampleUser2", aux);
    }

    @Test
    public void gettingScores() {
        ArrayList<ArrayList<String>> r = rank.getRankingData();
        String aux = r.get(0).get(1);
        assertEquals("920", aux);
        aux = r.get(3).get(1);
        assertEquals("498", aux);
    }
}

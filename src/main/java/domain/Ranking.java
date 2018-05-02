package domain;

import java.io.IOException;
import java.util.ArrayList;

public class Ranking {
    private ArrayList<ArrayList<String>> r;
    Ranking() throws IOException {
        r = File.getRanking();
    }

    public ArrayList<ArrayList<String>> getRanking() {
       return r;
    }
}

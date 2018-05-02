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

    public void mostraRanking() {
        System.out.print("RANKING:  \n");
        for(int i = 0; i < r.size(); i++){
            System.out.print(r.get(i));
            System.out.print("\n");
        }
    }
}

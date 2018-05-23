package domain;

import persistance.customFile;

import java.io.IOException;
import java.util.ArrayList;

public class Ranking {
    private ArrayList<ArrayList<String>> r;
    Ranking() throws IOException {
        r = customFile.getRanking();
    }

    public ArrayList<ArrayList<String>> getRanking() {
       return r;
    }

    public void showRanking() {
        System.out.print("RANKING:  \n");
        for(int i = 0; i < r.size(); i++){
            System.out.print(r.get(i));
            System.out.print("\n");
        }
    }
}

package domain;

import persistance.CtrlPersistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Ranking {
    private TreeMap<Integer, String> map = new TreeMap<>();
    private ArrayList<ArrayList<String>> r;
    Ranking() throws IOException {
        r = CtrlPersistence.getInstance().getRanking();
        for (ArrayList<String> t : r) {
            int pts = Integer.parseInt(t.get(1));
            map.put(pts, t.get(0));
        }
    }

    public ArrayList<ArrayList<String>> getRankingData() {
        return r;
    }

    public String getRanking() {
        String out = "";
        Integer p = 1;
        for (ArrayList<String> line : r) {
            String pos = (p++).toString();
            String pts = line.get(1);
            String username = line.get(0);
            String ptss = "";
            int n = 47 - pos.length() - username.length() - pts.length();
            for (int i = 0; i<n; i++) ptss += ".";
            out += pos + ". " + username.toUpperCase() + ptss + pts + "\n";
        }

        return out;
    }

    public void addScore(String username, Integer score) {
        //Get index
        int i = 0;
        for (int scr : map.descendingMap().keySet()) {
            if (score >= scr) break;
            i++;
        }

        map.put(score, username);

        //Add to ranking
        ArrayList<String> s = new ArrayList<>();
        s.add(username);
        s.add(score.toString());
        s.add("0:00");
        r.add(i, s);

        //ONLY TOP 10
        if (r.size() > 10) r.remove(10);
    }

    public void showRanking() {
        System.out.print("RANKING:  \n");
        for(int i = 0; i < r.size(); i++){
            System.out.print(r.get(i));
            System.out.print("\n");
        }
    }
}

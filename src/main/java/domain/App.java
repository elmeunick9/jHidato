package domain;

import java.io.IOException;
import java.util.Scanner;

public class App {
    private static void getWelcome() {
        String msg = "Welcome to the project of the group 21.1: \n"
                + "If you want to see all the options available please press enter.";

        System.out.print(msg);
    }

    private static void leaveMsg() {
        String msg = "\nProject of the group 21.1, Thank you for try it.";

        System.out.print(msg);
    }

    private static void getOptions() {
        String msg = "If you want to exit just press enter.\n"
                + "If you want to test a specific class please enter the number"
                + " corresponding to the class you want to test: \n"
                + "1: Class Generator \n"
                + "2: Class Game \n"
                + "3: Class Ranking \n";

        System.out.print(msg);
    }

    private static void testingGenerator() {
        String msg = "This is the generator class. What would you like to do?\n"
                + "1: ";
        System.out.print(msg);
    }

    private static void testingGame() throws Solver.SolutionNotFound {
        Scanner s = new Scanner(System.in);
        String msg = "\n\n\n"
                + "Please enter a username to create a user for the game: \n";
        System.out.print(msg);
        String username = s.nextLine();
        User u = new User(username);

        msg ="Now choose a difficulty: \n"
                + "Easy(e)\n"
                + "Medium(m)\n"
                + "Hard(h)\n"
                + "Enter one of the characters inside the parenthesis.\n";
        System.out.print(msg);
        String dif = s.nextLine();
        Game.Difficulty d;
        switch(dif){
            case "e":
                d = Game.Difficulty.EASY;
                break;
            case "m":
                d = Game.Difficulty.MEDIUM;
                break;
            case "h":
                d = Game.Difficulty.HARD;
                break;
            default:
                d = Game.Difficulty.EASY;
                break;
        }

        msg = "Finally choose a type of cell: \n"
                + "Triangle (t) \n"
                + "Square (s) \n"
                + "Hexagon (h) \n";
        System.out.print(msg);

        String type = s.nextLine();
        Game.HidatoType ht;
        switch (type) {
            case "t":
                ht = Game.HidatoType.TRIANGLE;
                break;
            case "s":
                ht = Game.HidatoType.SQUARE;
                break;
            case "h":
                ht = Game.HidatoType.HEXAGON;
                break;
            default:
                ht = Game.HidatoType.SQUARE;
                break;
        }

        Game g = new Game(d, u, ht);

        msg = "Game Created. This is the Hidato: \n";
        System.out.print(msg);
        g.print();

        String msg2 = "\nNow what do you want to do? \n"
                + "1: Enter a move\n"
                + "2: Solve it\n"
                + "3: Clear \n"
                + "4: Finish Game\n";
        System.out.print(msg2);
        String act = s.nextLine();
        boolean keep = true;
        while(keep) {
            switch(act) {
                case "1":
                    msg = "Enter the x,y position of the node starting from (1,1) at the"
                            + "Left-Top side. The first elem choose the column, the second the row."
                            + "Finally add the value all three parameters separated by coma.\n"
                            + "Example: 1,1,2 \n";
                    System.out.print(msg);
                    String param = s.nextLine();
                    String[] params = param.split(",");
                    if(params.length == 3) {
                        int x = Integer.parseInt(params[0]);
                        int y = Integer.parseInt(params[1]);
                        int v = Integer.parseInt(params[2]);
                        g.move(x,y,v);
                    }
                    break;
                case "2":
                    g.solve();
                    break;
                case "3":
                    g.clear();
                    break;
                case "4":
                    long res = g.finishGame();
                    msg = "Game finished. The time played in miliseconds is: ";
                    System.out.print(msg);
                    System.out.print(res);
                    System.out.print("\n");
                    keep = false;
                    break;
                default:
                    keep = false;
                    break;
            }
            if(keep) {
                msg = "Hidato resultant: \n";
                System.out.print(msg);
                g.print();

                System.out.print(msg2);
                act = s.nextLine();
            }
        }
    }

    private static void testingTriHidato() {
        String msg = "WIP";
        System.out.print(msg);
    }

    private static void testingQuadHidato() {
        String msg = "WIP";
        System.out.print(msg);
    }

    private static void testingHexHidato() {
        String msg = "WIP";
        System.out.print(msg);
    }

    private static void testingRanking() throws IOException {
        String msg = "We import the file ranking.txt in /files. "
                + "This is the basic implementation, you will see it now"
                + " in terminal ";
        System.out.print(msg);
        Ranking r = new Ranking();
        r.showRanking();
    }

    public static void main(String[] args) throws IOException, Solver.SolutionNotFound {
        boolean keepPlaying = true;
        Scanner s = new Scanner(System.in);
        getWelcome();
        System.in.read();
        getOptions();
        while (keepPlaying) {
            String val = s.nextLine();
            switch(val) {
                case "1":
                    testingGenerator();
                    break;
                case "2":
                    testingGame();
                    break;
                case "3":
                    testingRanking();
                    break;
                case "4":
                    testingTriHidato();
                    break;
                case "5":
                    testingQuadHidato();
                    break;
                case "6":
                    testingHexHidato();
                    break;
                default:
                    keepPlaying = false;
                    break;
            }

            if(keepPlaying){
                System.out.print("\nPress enter to continue");
                s.nextLine();
                getOptions();
            } else {
                leaveMsg();
            }
        }
    }
}

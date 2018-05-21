package domain;

import persistance.File;

import java.io.IOException;
import java.util.Scanner;

public class App {
    private static void getWelcome() {
        String msg = "Welcome to the project of the group 21.1: \n";
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
                + "3: Class Ranking \n"
                + "4: Enter Hidato \n"
                + "To try the solver enter into the class game and let it to him"
                + " or enter your own Hidato!\n";

        System.out.print(msg);
    }

    private static void testingGenerator() {
        Scanner s = new Scanner(System.in);
        String msg = "This is the generator class. What would you like to do?\n"
                + "1: Generate a Easy Hidato \n"
                + "2: Generate a Medium Hidato \n"
                + "3: Generate a Hard Hidato \n";
        System.out.print(msg);
        String val = s.nextLine();
        Generator g;

        msg = "This is the generated Hidato. Note that we only implemented "
                + "the SQUARE cell type due to the implementation in the terminal"
                + "is the same. The diferences between the cell types you could see"
                + "it in the tests. \n";
        System.out.print(msg);
        switch (val) {
            case "1":
                g = new Generator(Game.Difficulty.EASY, Game.HidatoType.SQUARE);
                g.getHidato().print();
                break;
            case "2":
                g = new Generator(Game.Difficulty.MEDIUM, Game.HidatoType.SQUARE);
                g.getHidato().print();
                break;
            case "3":
                g = new Generator(Game.Difficulty.HARD, Game.HidatoType.SQUARE);
                g.getHidato().print();
                break;
            default:
                break;
        }

    }

    private static void testingGame() throws Solver.SolutionNotFound {
        Scanner s = new Scanner(System.in);
        String msg = "Please enter a username to create a user for the game: \n";
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

    private static void testingRanking() throws IOException {
        String msg = "We import the file ranking.txt in /files. "
                + "This is the basic implementation, you will see it now"
                + " in terminal ";
        System.out.print(msg);
        Ranking r = new Ranking();
        r.showRanking();
    }

    private static void enterHidato() throws IOException, Solver.SolutionNotFound {
        String msg = "Copy and paste the hidato into the terminal "
                + "with the structure given in the pdf. "
                + "You could find a example inside the 'files' folder called 'example.txt'\n";
        System.out.print(msg);
        Hidato h = File.enterHidato();

        msg = "This is the representation of your Hidato.\n";
        System.out.print(msg);
        h.print();

        msg = "A possible solution for the Hidato given is: \n";
        System.out.print(msg);

        Solver s = new Solver(h);
        s.generateSolution().print();
    }

    public static void main(String[] args) throws IOException, Solver.SolutionNotFound {
        boolean keepPlaying = true;
        Scanner s = new Scanner(System.in);
        getWelcome();
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
                    enterHidato();
                    break;
                default:
                    keepPlaying = false;
                    break;
            }

            if(keepPlaying){
                System.out.print("\nPress enter to continue\n");
                s.nextLine();
                getOptions();
            } else {
                leaveMsg();
            }
        }
    }
}

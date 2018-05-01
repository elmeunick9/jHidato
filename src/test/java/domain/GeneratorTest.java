package domain;

import org.junit.Test;

public class GeneratorTest {
    private Generator gen;

    @Test
    public void basicTriHidatoTest() {
        gen = new Generator(Game.Difficulty.EASY, Game.HidatoType.TRIANGLE);
        Hidato h = gen.getHidato();

        Solver solver = new Solver(h);
        try {
            Hidato s = solver.generateSolution();
        } catch (Solver.SolutionNotFound e) {
            throw new AssertionError();
        }
    }

    @Test
    public void mediumQuadHidatoTest() {
        gen = new Generator(Game.Difficulty.MEDIUM, Game.HidatoType.SQUARE);
        Hidato h = gen.getHidato();
        h.print();

        Solver solver = new Solver(h);
        try {
            Hidato s = solver.generateSolution();
            s.print();
        } catch (Solver.SolutionNotFound e) {
            throw new AssertionError();
        }
    }



}

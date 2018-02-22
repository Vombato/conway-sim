package core.model;

import java.util.Objects;
import java.util.stream.IntStream;
import static core.model.Status.ALIVE;
import static core.model.Status.DEAD;
import core.utils.Matrix;

/**
 * Utility class for computation and editing of {@link Generation}.
 *
 */
public final class Generations {

    private  Generations() { }

    /**
     * Computes a new {@link Generation} from the given one.
     * @param start that is the previous {@link Generation}
     * @return the new computed {@link Generation}
     */
    public static Generation compute(final Generation start) {
        Objects.requireNonNull(start);
        final Environment env = start.getEnviroment();
        final Matrix<Cell> previous = start.getCellMatrix();
        final Matrix<Cell> result = GenerationFactory.copyOf(start).getCellMatrix();
        //Iteration of the cell matrix
        IntStream.range(0, previous.getHeight()).forEach(row -> {
            IntStream.range(0, previous.getWidth()).forEach(column -> {
                //Alive neighbors count
                int neighbors = 0;
                for (int h = -1; h <= 1; h++) {
                    for (int w = -1; w <= 1; w++) {
                        if (row + h >= 0 && row + h < previous.getHeight() && column + w >= 0 && column + w < previous.getWidth() && !(h == 0 && w == 0)) {
                            neighbors += previous.get(row + h, column + w).getStatus().equals(ALIVE) ? 1 : 0;
                        }
                    }
                }
                //Next Status evaluation
                if (previous.get(row, column).getStatus().equals(ALIVE) && env.getCellEnvironment(row, column).checkCellDeath(neighbors)) {
                    result.get(row, column).setStatus(DEAD);
                } else if (previous.get(row, column).getStatus().equals(DEAD) && env.getCellEnvironment(row, column).checkCellBorn(neighbors)) {
                    result.get(row, column).setStatus(ALIVE);
                }
            });
        });
        return GenerationFactory.from(result, env);
    }

    /**
     * Computes n generations from start. 
     * @param start is the first {@link Generation}
     * @param number is the number of generation to be computed sequentially
     * @return the result of the computations
     */
    public static Generation compute(final Generation start, final int number) {
        Objects.requireNonNull(start);
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative.");
        }
        Generation temp = start;
        for (int i = 0; i < number; i++) {
            temp = Generations.compute(temp);
        }
        return temp;
    }

    /**
     * A method to modify a {@link Generation} by applying a certain alive cell pattern. Note that in order to do this it creates a new generation without modifying the given one.
     * @param generation is the {@link Generation} to be modified
     * @param x is the row of the top left cell of the pattern
     * @param y is the column of the top left cell of the pattern
     * @param patternAliveCells is the alive cells {@link Matrix} of the pattern
     * @return the modified generation with the pattern applied in the given position
     */
    public static Generation mergePatternXY(final Generation generation, final int x, final int y, final Matrix<Boolean> patternAliveCells) {
        Objects.requireNonNull(generation);
        Objects.requireNonNull(patternAliveCells);
        if (x < 0 || y < 0 || x + patternAliveCells.getHeight() > generation.getHeight() || y + patternAliveCells.getWidth() > generation.getWidth()) {
            throw new IllegalArgumentException("Invalid position or invalid pattern dimension.");
        }
        final Matrix<Cell> gen = GenerationFactory.copyOf(generation).getCellMatrix();
        final Matrix<Cell> toApply = patternAliveCells.map(b -> new CellImpl(b ? ALIVE : DEAD));
        IntStream.range(0, toApply.getHeight()).forEach(row -> {
            IntStream.range(0, toApply.getWidth()).forEach(column -> {
                gen.set(row + x, column + y, toApply.get(row, column));
            });
        });
        return GenerationFactory.from(gen, generation.getEnviroment());
    }

}


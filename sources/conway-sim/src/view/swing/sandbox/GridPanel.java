package view.swing.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import core.utils.ListMatrix;
import core.utils.Matrix;


/**
 * 
 * 
 */
public class GridPanel extends JScrollPane {

    private static final long serialVersionUID = -1;
    private static final int INITIAL_SIZE = 20;
    private static final int INITIAL_BORDER_WIDTH = 1;
    private static final Color INITIAL_BORDER_COLOR = Color.darkGray;

    private final Matrix<JLabel> labelMatrix;
    private final Dimension cellSize = new Dimension(INITIAL_SIZE, INITIAL_SIZE);
    private int borderWidth = INITIAL_BORDER_WIDTH;
    private final Color borderColor = INITIAL_BORDER_COLOR;

    /**
     * 
     * @param width of the matrix
     * @param height of the matrix
     */
    public GridPanel(final int width, final int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Arguments must be greater than 1.");
        }
        this.labelMatrix = new ListMatrix<>(width, height, () -> {
            final JLabel l = new JLabel("");
            l.setSize(cellSize);
            l.setPreferredSize(cellSize);
            l.setBackground(Color.white);
            l.setOpaque(true);
            return l;
        });
        final JPanel grid = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 0.5;
        for (int i = 0; i < this.labelMatrix.getHeight(); i++) {
            for (int j = 0; j < this.labelMatrix.getWidth(); j++) {
                c.gridx = j;
                c.gridy = i;
                setBorder(this.labelMatrix.get(i, j), i, j, this.borderColor, this.borderWidth);
                grid.add(this.labelMatrix.get(i, j), c);
            }
        }
        this.setViewportView(grid);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.getVerticalScrollBar().setUnitIncrement(this.cellSize.height);
    }
    /**
     * Alters Cell size value.
     * @param byPixels to add
     */
    public void alterCellSize(final int byPixels) {
        if (this.cellSize.getWidth() + byPixels <= 0 || this.cellSize.getHeight() + byPixels <= 0) {
            throw new IllegalStateException("Final Dimensions are 0 or less.");
        }
        this.cellSize.setSize(this.cellSize.getWidth() + byPixels, this.cellSize.getHeight() + byPixels);
        this.labelMatrix.forEach(label -> {
            label.setSize(this.cellSize);
            label.setPreferredSize(this.cellSize);
        });
        this.getVerticalScrollBar().setUnitIncrement(this.cellSize.height);
    }

    /**
     * Alters Border width value.
     * @param byPixels to add
     */
    public void alterBorderWidth(final int byPixels) {
        if (this.borderWidth + byPixels < 1) {
            throw new IllegalStateException("Final Border Width is 0 or less.");
        }
        this.borderWidth += byPixels;
        for (int i = 0; i < this.labelMatrix.getHeight(); i++) {
            for (int j = 0; j < this.labelMatrix.getWidth(); j++) {
                setBorder(this.labelMatrix.get(i, j), i, j, this.borderColor, this.borderWidth);
            }
        }
    }

    private void setBorder(final JLabel label, final int row, final int col, final Color c, final int borderWidth)  {
        if (row == 0) {
            if (col == 0) {
                // Top left corner, draw all sides
                label.setBorder(BorderFactory.createLineBorder(c, borderWidth));
            } else {
                // Top edge, draw all sides except left edge
                label.setBorder(BorderFactory.createMatteBorder(borderWidth, 0, borderWidth,
                        borderWidth, c));
            }
        } else {
            if (col == 0) {
                // Left-hand edge, draw all sides except top
                label.setBorder(BorderFactory.createMatteBorder(0, borderWidth, borderWidth,
                        borderWidth, c));
            } else {
                // Neither top edge nor left edge, skip both top and left lines
                label.setBorder(BorderFactory.createMatteBorder(0, 0, borderWidth, borderWidth,
                        c));
            }
        }
    }

    /**
     * A fr nvrogòwn  ng .
     * @param boolMatrix is the to.
     */
    public void paintCells(final Matrix<Boolean> boolMatrix) {

        if (boolMatrix.getHeight() != this.labelMatrix.getHeight() || boolMatrix.getWidth() != this.labelMatrix.getWidth()) {
            throw new IllegalArgumentException("Matrix shuld be as high and wide as the current one");
        }

        final Map<Boolean, Color> booltocolor = new HashMap<>();
        booltocolor.put(false, Color.WHITE);
        booltocolor.put(true, Color.BLACK);
        final List<List<Color>> colors = new ArrayList<>();
        IntStream.range(0, boolMatrix.getHeight()).forEach(line -> {
            colors.add(line, new ArrayList<>(boolMatrix.getWidth()));
            IntStream.range(0, boolMatrix.getWidth()).forEach(column -> {
                colors.get(line).add(column, booltocolor.get(boolMatrix.get(line, column)));
            });
        });
        this.displayColors(new ListMatrix<>(colors));
    }

    private void displayColors(final Matrix<Color> colorMatrix) {
        final JPanel grid = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 0.5;
        IntStream.range(0, colorMatrix.getHeight()).forEach(line -> {
            IntStream.range(0, colorMatrix.getWidth()).forEach(column -> {
                final JLabel lab = new JLabel();
                lab.setBackground(colorMatrix.get(line, column));
                lab.setSize(this.cellSize);
                lab.setPreferredSize(this.cellSize);
                lab.setOpaque(true);
                c.gridx = line;
                c.gridy = column;
                grid.add(lab, c);
            });
        });
        this.setViewportView(grid);
    }
}

package view.swing.sandbox;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.editor.LevelGridEditorImpl;
import controller.editor.PatternEditor;
import controller.io.LevelLoader;
import core.campaign.Level;
import view.DesktopGUI;
import view.swing.level.LevelComplete;

/**
 * Factory that creates sandbox JPanels.
 */
public final class SandboxBuilder {

    private SandboxBuilder() {
    }

    /**
     * Creates the sandbox for level mode.
     * 
     * @param gui
     *            the gui that hosts the sandbox
     * @param level
     *            the integer corresponding to the level to load
     * @return the builded sandbox as JPanel on success
     */
    public static JPanel buildLevelSandbox(final DesktopGUI gui, final int level) {
        final LevelLoader levelLoader = new LevelLoader(level);
        final Level l = levelLoader.getLevel();
        final int h = l.getEnvironmentMatrix().getHeight();
        final int w = l.getEnvironmentMatrix().getWidth();
        return new AbstractSandbox(gui) {
            private static final long serialVersionUID = 1L;
            private GenerationPanel generationPanel;

            @Override
            protected JGridPanel buildGrid(final int cellSize) {
                return new JGridPanel(w, h, cellSize);
            }

            @Override
            protected GenerationPanel buildGenerationPanel() {
                generationPanel = new GenerationPanel(this);
                return generationPanel;
            }

            private GenerationPanel getGenerationPanel() {
                return this.generationPanel;
            }

            @Override
            protected PatternEditor buildEditor(final GridPanel gridp) {
                return new LevelGridEditorImpl(gridp, l, () -> {
                    SwingUtilities.invokeLater(() -> {
                        gui.popUpFrame(new LevelComplete());
                        this.getGenerationPanel().end();
                    });
                });
            }
        };
    }

    /**
     * Creates the sandbox for free mode.
     * 
     * @param gui
     *            the gui that hosts the sandbox
     * @return the builded sandbox as JPanel on success
     */
    public static JPanel buildSandbox(final DesktopGUI gui) {
        return new SimpleSandbox(gui);
    }

}

package view.swing.book;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import controller.book.RecipeImpl;
import controller.editor.PatternEditor;
import controller.io.IOLoader;
import controller.io.RLEConvert;
import controller.io.RecipeLoader;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;
import view.swing.sandbox.GridPanel;
/**
 * 
 *
 */

public class BookFrame extends JInternalFrame {
/**
     * 
     */
    private static final long serialVersionUID = -1045414565623185058L;



    private static final int FRAME_WIDTH = 250;
    //private static final int HEIGHT = 280;
    private static final int HEIGHTOFCELL = 30;
    private String selectedItem = null;

    /**
     * 
     * @return selectedItem
     */
    public final String getSelectedItem() {
        return selectedItem;
    }
    /**
     * 
     * @param selectedItem the item to select
     */
    public void setSelectedItem(final String selectedItem) {
        this.selectedItem = selectedItem;
    }
    /**
     * @param patternE the PatternManager
     * 
     */

    public BookFrame(final PatternEditor patternE) {
        super("Book", false, true);

        final RecipeLoader rl = new RecipeLoader();

        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        // SIZE BY DIMENSION
        this.setSize(FRAME_WIDTH, HEIGHTOFCELL * rl.getRecipeBook().getRecipeBookSize());
        //this.setSize(WIDTH, HEIGHT);

        //PATTERN PREVIEW GRID
        //The final GridPanel constructor will have as 3rd argument int SIZE_OF_CELL
        final GridPanel pg = new GridPanel(FRAME_WIDTH / 3, FRAME_WIDTH / 3, null);

        // TEST FOR THE JList WITH A TEMP ARRAY
        final List<String> arrList = new ArrayList<String>();

        for (final RecipeImpl recipe : rl.getRecipeBook().getBookList()) {
            arrList.add(recipe.getName());
        }

        final JList<String> list = new JList<String>(arrList.toArray(new String[arrList.size()]));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        list.addMouseListener(new MouseListener() {
            public void mousePressed(final MouseEvent e) {
                setSelectedItem(list.getSelectedValue());
                System.out.println("DEBUG | Selected Item: " + list.getSelectedValue());
            }

            @Override
            public void mouseClicked(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseEntered(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseExited(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }


            @Override
            public void mouseReleased(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

        });

        this.add(list);

        //BUTTON PANEL
        final JPanel ioPanel = new JPanel();
        this.add(ioPanel);
        final JButton placeBtn = new JButton("Place");

        //ACTION LISTENER TESSSSST
        ActionListener alPlace = e -> {
            System.out.println("DEBUG | PLACE Button pressed, handling the pattern placement.");
            final Matrix<Status> mat = new RLEConvert(rl.getRecipeBook().getRecipeByName(getSelectedItem()).getContent()).convert();
            patternE.addPatternToPlace(mat);
            final Matrix<Color> matC = new ListMatrix(mat.getHeight(), mat.getWidth(), () -> Color.WHITE);
            //Cleaning the grid by painting a white matrix
            pg.paintGrid(matC);
            for (int i = 0; i < mat.getHeight(); i++) {
                for (int k = 0; k < mat.getWidth(); k++) {
                    if (mat.get(i, k).equals(Status.DEAD)) {
                        matC.set(i, k, Color.BLACK);
                    }
                }
            }
            //Painting the selected pattern
            pg.paintGrid(matC);
            this.doDefaultCloseAction();
        };
        placeBtn.addActionListener(alPlace);
        ioPanel.add(placeBtn);

        //JFILECHOOSER
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select the file you want to load");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        JButton loadBtn = new JButton("Load");

        //ACTION LISTENER TESSSSST
        ActionListener alLoad = e -> {
            final JButton jb = (JButton) e.getSource();
            if (fc.showOpenDialog(jb) == JFileChooser.APPROVE_OPTION) {
                final String filepath;
                filepath = fc.getSelectedFile().getAbsolutePath();
                System.out.println("File selected:" + filepath);
                IOLoader ioLoader = new IOLoader();
                try {
                    ArrayList<String> al = ioLoader.load(filepath);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        };

        loadBtn.addActionListener(alLoad);
        //ADD THE BUTTON
        ioPanel.add(loadBtn);

    }
}


package view.swing.book;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
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
import controller.io.IOLoader;
import controller.io.RecipeLoader;
import core.model.Status;
/**
 * 
 *
 */
public class BookFrame extends JInternalFrame {
/**
     * 
     */
    private static final long serialVersionUID = -1045414565623185058L;


    private static final int WIDTH = 150;
    //private static final int HEIGHT = 280;
    private static final int HEIGHTOFCELL = 20;
    /**
     * 
     */
    public BookFrame() {
        super("Book", false, true);

        final RecipeLoader rl = new RecipeLoader();

        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        // SIZE BY DIMENSION
        this.setSize(WIDTH, HEIGHTOFCELL * rl.getRecipeBook().getRecipeBookSize());
        //this.setSize(WIDTH, HEIGHT);

        // TEST FOR THE JList WITH A TEMP ARRAY
        final List<String> arrList = new ArrayList<String>();

        for (RecipeImpl recipe : rl.getRecipeBook().getBookList()) {
            arrList.add(recipe.getName());
        }

        JList<String> list = new JList<String>(arrList.toArray(new String[arrList.size()]));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        this.add(list);

        //BUTTON PANEL
        JPanel ioPanel = new JPanel();
        this.add(ioPanel);
        JButton placeBtn = new JButton("Place");
        ioPanel.add(placeBtn);

        //JFILECHOOSER
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select the file you want to load");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        JButton loadBtn = new JButton("Load");

        //ACTION LISTENER TESSSSST
        ActionListener ac = e -> {
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

        loadBtn.addActionListener(ac);
        //ADD THE BUTTON
        ioPanel.add(loadBtn);

    }
}


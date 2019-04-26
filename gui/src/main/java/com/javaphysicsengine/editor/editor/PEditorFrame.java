/*
 * Purpose: To create the PhysicsAPI Editor Window that is able to define objects by the user, simulate them, and save them.
 * Original Creation Date: January 11 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.editor.editor;

import com.javaphysicsengine.api.PWorld;
import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.editor.codegenerator.PCodeGenerator;
import com.javaphysicsengine.editor.io.PFileWriter;
import com.javaphysicsengine.editor.io.PFileReader;
import com.javaphysicsengine.editor.simulation.PSimulationWindow;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PEditorFrame extends JFrame implements ActionListener {
    private PEditorPanel editorPanel = null;

    /**
     * Creates a PEditorFrame window object
     */
    public PEditorFrame() {
        super("Physics API Editor");
        this.setFocusable(true);

        // Setting up the GUI components and link it to ActionListener
        setupMenuBar();
        addPanels();
    }

    /**
     * Post-condition: Starts the PhysicsAPI Editor window, and sets the look and feel
     */
    public static void main(String[] args) {
        // Setting the new look and feel of the window
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (Exception ignored) {  }

        // Setting the look and feel of GUI components
        UIManager.put("nimbusBase", new Color(50, 50, 50));
        UIManager.put("nimbusBlueGrey", new Color(50, 50, 50));
        UIManager.put("control", new Color(50, 50, 50));
        UIManager.put("TextField.background", new Color(40, 40, 40));
        UIManager.put("TextField.foreground", new Color(150, 150, 150));
        UIManager.put("OptionPane.messagebackground", new Color(200, 200, 200));
        UIManager.put("OptionPane.foreground", new Color(200, 200, 200));

        // Create the window with properties
        PEditorFrame newWindow = new PEditorFrame();
        newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newWindow.setSize(1000, 600);
        newWindow.setResizable(true);
        newWindow.setVisible(true);
    }

    /**
     * Adds the properties pane and the editor pane onto the window
     */
    private void addPanels() {
        // Set up the properties pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // Set up the pane where users can draw on it
        editorPanel = new PEditorPanel(tabbedPane);

        // Create the split pane to divide the window pane to two
        JSplitPane windowSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorPanel, tabbedPane);
        windowSplitPanel.setResizeWeight(0.7);  //  Makes the right side smaller than the left side

        // Add the split pane to the window
        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new BorderLayout());
        windowPanel.add(windowSplitPanel, BorderLayout.CENTER);
        this.getContentPane().add(windowPanel);
    }

    /**
     * Creates the Menu Bar with its menu buttons on the window
     */
    private void setupMenuBar() {
        // An array storing all the JMenuItems and JMenu and all that inherit from it
        JMenuItem[] menuItems = new JMenuItem[9];
        JMenuItem[] menus = new JMenu[4];

        // Create the "File" menu tab
        menuItems[0] = new JMenuItem("New");
        menuItems[1] = new JMenuItem("Load");
        menuItems[2] = new JMenuItem("Save");
        menus[0] = new JMenu("File");
        for (int i = 0; i < 3; i++)
            menus[0].add(menuItems[i]);

        // Creating the "Options" menu tab
        menuItems[3] = new JCheckBoxMenuItem("Toggle Anti-Aliasing");
        menus[1] = new JMenu("Options");
        menus[1].add(menuItems[3]);

        // Create the "Run" menu tab
        menuItems[4] = new JMenuItem("Run Simulation");
        menus[2] = new JMenu("Run");
        menus[2].add(menuItems[4]);

        // Create the "View" menu tab
        menuItems[5] = new JCheckBoxMenuItem("View Bounding Box");
        ((JCheckBoxMenuItem) menuItems[5]).setState(true);
        menuItems[6] = new JCheckBoxMenuItem("View Shape Outline");
        ((JCheckBoxMenuItem) menuItems[6]).setState(true);
        menuItems[7] = new JCheckBoxMenuItem("View Shape Fill");
        menuItems[8] = new JMenuItem("View Generated Java Code");
        menus[3] = new JMenu("View");
        for (int i = 5; i < menuItems.length; i++)
            menus[3].add(menuItems[i]);

        // Add the menus to the menu bar
        JMenuBar menuBar = new JMenuBar();
        for (JMenuItem menu : menus) {
            menuBar.add(menu);
        }
        this.setJMenuBar(menuBar);

        for (JMenuItem menu : menuItems) {
            menu.addActionListener(this);
        }
    }

    /**
     * It will save the bodies from the editor to a file.
     * It will show a prompt to the user asking for where to save the file.
     * Note that it must be a full file path.
     */
    private void loadBodiesFromFile() {
        // Get the file path from user
        String filePath = JOptionPane.showInputDialog("Enter File Path");
        try {
            InputStream inputStream = new FileInputStream(filePath);

            PFileReader fileReader = new PFileReader(inputStream);
            List<PBody> bodies = fileReader.getBodies();
            List<PConstraints> constraints = fileReader.getConstraints();

            // Adding the bodies and constraints to the editor
            for (PBody body : bodies) {
                if (body != null)
                    editorPanel.addBody(body);
            }
            for (PConstraints constraint : constraints) {
                if (constraint != null)
                    editorPanel.addConstraint(constraint);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * It will pop up a dialog window asking the user to enter the file path
     * that will save the bodies to.
     * Note: the file path must be a full file path.
     */
    private void saveBodiesToFile() {
        String filePath = JOptionPane.showInputDialog("Enter File Path:");

        try {
            FileOutputStream fileWriter = new FileOutputStream(filePath);

            PFileWriter pFileWriter = new PFileWriter(fileWriter);
            pFileWriter.saveBodies(editorPanel.getBodies());
            pFileWriter.saveConstraints(editorPanel.getConstraints());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * View the java code generated from the bodies made in the editor.
     * It will open up a window with the code.
     */
    private void viewJavaCode() {
        List<PBody> bodies = editorPanel.getBodies();
        List<PConstraints> constraints = editorPanel.getConstraints();
        PCodeGenerator codeGenerator = new PCodeGenerator();
        List<String> codeLines = codeGenerator.generateApiCode(bodies, constraints);

        // Add them to the text area
        StringBuilder singularLine = new StringBuilder();
        for (String line : codeLines) {
            singularLine.append(line)
                    .append("\n");
        }
        JTextArea textArea = new JTextArea(singularLine.toString());

        // Show the info to a JOptionPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        JOptionPane.showMessageDialog(null, scrollPane, "Bodies Summary", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Gets the bodies created in the editorPanel, opens a new window, and simulates it
     */
    private void runSimulation() {
        List<PBody> bodies = editorPanel.getBodies();
        List<PConstraints> constraints = editorPanel.getConstraints();

        // Relink the bodies attached to constraints to the bodies[] because the bodies attached to constraints[] are copies
        for (PConstraints constraint : constraints) {
            System.out.println("Constraint: " + constraint.getAttachedBodies()[0] + "\n" + constraint.getAttachedBodies()[1]);

            PBody[] reattachedBodies = new PBody[2];
            for (int i = 0; i < reattachedBodies.length; i++) {
                System.out.println("Performing BS");
                // Do a binary search (already sorted by name in ascending order)
                int left = 0;
                int right = bodies.size() - 1;
                PBody attachedBody = constraint.getAttachedBodies()[i];

                while (right >= left) {
                    int mid = (left + right) / 2;
                    PBody curBody = bodies.get(mid);

                    if (curBody.getName().compareTo(attachedBody.getName()) < 0)
                        left = mid + 1;
                    else if (curBody.getName().compareTo(attachedBody.getName()) > 0)
                        right = mid - 1;
                    else {
                        reattachedBodies[i] = curBody;
                        break;
                    }
                }
                System.out.println("Finished BS");
            }

            // Set the legitamate bodies to the constraints
            constraint.setAttachedBodies(reattachedBodies[0], reattachedBodies[1]);
        }

        System.out.println("Created Spring");

        // Create the world and add the constraints and the bodies
        PWorld world = new PWorld();
        for (PBody body : bodies)
            world.getBodies().add(body);
        for (PConstraints constraint : constraints)
            world.getConstraints().add(constraint);

        // Create the window
        new PSimulationWindow(world, 30, editorPanel.isShapeFillDisplayed(),
                editorPanel.isShapeOutlineDisplayed(), editorPanel.isAntiAliasingToggled())
                .setVisible(true);
    }

    /**
     * Certain actions called when clicked on a Menu Item in the Menu Bar of the window
     * @param e the event triggered
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JCheckBoxMenuItem) {
            JCheckBoxMenuItem curItem = (JCheckBoxMenuItem) e.getSource();
            switch (curItem.getText()) {
                case "Toggle Anti-Aliasing":
                    System.out.println("Toggled Anti-Aliasing");
                    editorPanel.setAntiAliasing(curItem.isSelected());
                    break;
                case "View Bounding Box":
                    editorPanel.displayBoundingBox(curItem.getState());
                    System.out.println("Displayed Bounding Box");
                    break;
                case "View Shape Outline":
                    editorPanel.displayShapeOutline(curItem.getState());
                    System.out.println("Displayed Shape Outline");
                    break;
                case "View Shape Fill":
                    editorPanel.displayShapeFill(curItem.getState());
                    System.out.println("Displayed Shape Fill");
                    break;
            }
        } else if (e.getSource() instanceof JMenuItem) {
            JMenuItem curItem = (JMenuItem) e.getSource();
            switch (curItem.getText()) {
                case "New":
                    System.out.println("Created a new file");
                    editorPanel.clearBodies();
                    editorPanel.clearConstraints();
                    break;
                case "Load":
                    System.out.println("Opened a file");
                    loadBodiesFromFile();
                    break;
                case "Save":
                    System.out.println("Saved file");
                    saveBodiesToFile();
                    break;
                case "Run Simulation":
                    System.out.println("Ran Simulation");
                    runSimulation();
                    break;
                case "View Generated Java Code":
                    System.out.println("Displayed Java Code");
                    viewJavaCode();
                    break;
            }
        }
    }
}
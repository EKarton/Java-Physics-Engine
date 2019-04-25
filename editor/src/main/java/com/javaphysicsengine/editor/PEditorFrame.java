/*
  Purpose: To create the PhysicsAPI Editor Window that is able to define objects by the user, simulate them, and save them.
  Original Creation Date: January 11 2016
  @author Emilio Kartono
  @version January 15 2016
*/

package com.javaphysicsengine.editor;

import com.javaphysicsengine.api.PWorld;
import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.api.body.PString;
import com.javaphysicsengine.utils.File;
import com.javaphysicsengine.utils.Vector;
import com.sun.tools.javac.util.Pair;

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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PEditorFrame extends JFrame implements ActionListener {
    private PEditorPanel editorPanel = null;

    /*
      Post-condition: Creates a PEditorFrame window object
    */
    public PEditorFrame() {
        super("Physics API Editor");
        this.setFocusable(true);

        // Setting up the GUI components and link it to ActionListener
        setupMenuBar();
        addPanels();
    }

    /*
      Post-condition: Starts the PhysicsAPI Editor window, and sets the look and feel
    */
    public static void main(String[] args) {
        // Setting the new look and feel of the window
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (Exception e) {
        }

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

    /*
      Post-condition: Adds the properties pane and the editor pane onto the window
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

    /*
       Post-condition: Creates the Menu Bar with its menu buttons on the window
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
        for (int i = 0; i < menus.length; i++)
            menuBar.add(menus[i]);
        this.setJMenuBar(menuBar);

        // Link the ActionListeners to the JMenuItems
        for (int i = 0; i < menuItems.length; i++)
            menuItems[i].addActionListener(this);
    }

    /*
      Pre-condition: Global Variable "editorPanel" must not be null; file path asked to user must be the full file path
      Post-condition: Loads all the bodies stored in a ".txt" file (Will be asked to user VIA JOptionPane)
   */
    private void loadBodiesFromFile() {
        // Get the file path from user
        String filePath = JOptionPane.showInputDialog("Enter File Path");

        PBodyFileReader fileReader = new PBodyFileReader();
        Pair<List<PBody>, List<PConstraints>> results = fileReader.loadBodiesFromFile(filePath);
        List<PBody> bodies = results.fst;
        List<PConstraints> constraints = results.snd;

        // Adding the bodies and constraints to the editor
        for (PBody body : bodies)
            if (body != null)
                editorPanel.addBody(body);
        for (PConstraints constraint : constraints)
            if (constraint != null)
                editorPanel.addConstraint(constraint);
    }

    /*
       Pre-condition: Global variable "editorPanel" must not be null; file path asked to user must be the full file path
       Post-condition: Saves all the bodies in a file
    */
    private void saveBodiesToFile() {
        String filePath = JOptionPane.showInputDialog("Enter File Path:");
        ArrayList<String> fileLines = new ArrayList<String>();

        // Adding all the bodies to an array of strings
        for (PBody body : editorPanel.getBodies()) {
            String line = "";
            if (body instanceof PPolygon)
                line += "PPolygon";
            else if (body instanceof PCircle)
                line += "PCircle";

            // Adding the properties of the body
            line += "{";
            line += body.toString();
            line += "}";
            fileLines.add(line);
        }

        // Adding the constraints
        for (PConstraints constraint : editorPanel.getConstraints()) {
            String line = "";
            if (constraint instanceof PSpring)
                line += "PSpring";
            else if (constraint instanceof PString)
                line += "PString";

            // Adding the properties of the constraint
            line += "{";
            line += constraint.toString();
            line += "}";
            fileLines.add(line);
        }

        // Save them to a text file
        String[] fileLines_Array = new String[fileLines.size()];
        fileLines_Array = fileLines.toArray(fileLines_Array);
        File.write(fileLines_Array, filePath);
    }

    private void viewJavaCode() {
        // Create the "lines of code"
        ArrayList<String> codeLines = new ArrayList<String>();

        // Getting the bodies and the constraints
        ArrayList<PBody> bodies = editorPanel.getBodies();
        ArrayList<PConstraints> constraints = editorPanel.getConstraints();

        // Create the java code for the importing libraries
        codeLines.add("import PhysicsEngine.*;");
        codeLines.add("import ToolBox.Vector;");

        // Create the java code to make the function (function that will handle creating the world and its bodies and constraints)
        codeLines.add("");
        codeLines.add("public void initialisePhysics()");
        codeLines.add("{");

        // Create the java code to create the world
        codeLines.add("    PWorld world = new PWorld();");
        codeLines.add("");

        // Create the java code for bodies
        for (PBody body : bodies) {
            String bodyName = body.getName().replaceAll(" ", "_");
            String bodyType = "PPolygon";
            if (body instanceof PCircle)
                bodyType = "PCircle";

            // Write the code for all the PBody objects
            codeLines.add("    " + bodyType + " " + bodyName + " = new " + bodyType + "(\"" + bodyName + "\")");
            codeLines.add("    " + bodyName + ".setMass(" + body.getMass() + ");");
            codeLines.add("    " + bodyName + ".setMoveable(" + body.isMoving() + ");");
            codeLines.add("    " + bodyName + ".setVelocity(" + body.getVelocity() + ");");
            codeLines.add("    " + bodyName + ".setOutlineColor(" + body.getOutlineColor() + ");");
            codeLines.add("    " + bodyName + ".setFillColor(" + body.getFillColor() + ");");

            // Adding lines of code special for PPolygon
            if (body instanceof PPolygon) {
                PPolygon bodyPoly = (PPolygon) body;

                // Add the vertices
                for (Vector vertices : bodyPoly.getVertices())
                    codeLines.add("    " + bodyName + ".getVertices().add(new Vector(" + vertices.toString() + "));");
                codeLines.add("    " + bodyName + ".computeCenterOfMass();");
            }

            // Adding lines of code special for PCircle
            else if (body instanceof PCircle) {
                PCircle bodyCircle = (PCircle) body;
                codeLines.add("    " + bodyName + ".setRadius(" + bodyCircle.getRadius() + ");");
                codeLines.add("    " + bodyName + ".setCenterPt(new Vector(" + bodyCircle.getCenterPt() + "));");
            }

            codeLines.add("    world.getBodies().add(" + bodyName + ");");

            // Add a space in between different objects
            codeLines.add("");
        }

        // Create the java code for the constraints
        for (PConstraints constraint : editorPanel.getConstraints()) {
            String constraintName = "Constraint_" + (int) (Math.random() * (1000));
            String attachedBody1Name = constraint.getAttachedBodies()[0].getName();
            String attachedBody2Name = constraint.getAttachedBodies()[1].getName();
            String constraintType = "PSpring";
            if (constraint instanceof PString)
                constraintType = "PString";

            // Writing lines of code for all PConstraints
            codeLines.add("    " + constraintType + " " + constraintName + " = new " + constraintType + "(" + attachedBody1Name + ", " + attachedBody2Name + ")");


            // Writing lines of code for all PSprings
            if (constraint instanceof PSpring) {
                PSpring springConstraint = (PSpring) constraint;
                codeLines.add("    " + constraintName + ".setLengthOfSpring(new Vector(" + springConstraint.getLength() + "));");
                codeLines.add("    " + constraintName + ".setKValue(" + springConstraint.getKValue() + ");");
            }

            // Writing lines of code for all PStrings
            if (constraint instanceof PString) {
                PString stringConstraint = (PString) constraint;
                codeLines.add("    " + constraintName + ".setLengthOfSpring(new Vector(" + stringConstraint.getLength() + "));");
            }
            codeLines.add("    world.getConstraints().add(" + constraintName + ");");

            // Add a space in between different objects
            codeLines.add("");
        }

        // End the function call with the last curly braces
        codeLines.add("}");

        // Add them to the text area
        String singularLine = "";
        for (String line : codeLines)
            singularLine += line + "\n";
        JTextArea textArea = new JTextArea(singularLine);

        // Show the info to a JOptionPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        JOptionPane.showMessageDialog(null, scrollPane, "Bodies Summary", JOptionPane.PLAIN_MESSAGE);
    }

    /*
       Pre-condition: Global variable "editorPanel" must not be null
       Post-condition: Gets the bodies created in the editorPanel, opens a new window, and simulates it
    */
    private void runSimulation() {
        ArrayList<PBody> bodies = editorPanel.getBodies();
        ArrayList<PConstraints> constraints = editorPanel.getConstraints();

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

            // If one of them are nulls
            for (int i = 0; i < reattachedBodies.length; i++)
                if (reattachedBodies[i] == null)
                    continue;

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
        PSimulationWindow simulationWindow = new PSimulationWindow(world, 30, editorPanel.isShapeFillDisplayed(),
                editorPanel.isShapeOutlineDisplayed(), editorPanel.isAntiAliasingToggled());
    }

    /*
       Pre-condition: "e" must not be null
       Post-condition: Certain actions called when clicked on a Menu Item in the Menu Bar of the window
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
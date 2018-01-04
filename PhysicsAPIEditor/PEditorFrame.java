/*
  Purpose: To create the PhysicsAPI Editor Window that is able to define objects by the user, simulate them, and save them.
  Original Creation Date: January 11 2016
  @author Emilio Kartono
  @version January 15 2016
*/

package PhysicsAPIEditor;

import java.util.StringTokenizer;
import java.util.ArrayList;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import ToolBox.File;
import ToolBox.Vector;
import PhysicsEngine.*;

public class PEditorFrame extends JFrame implements ActionListener
{    
  private PEditorPanel editorPanel = null;
  
  /*
    Post-condition: Creates a PEditorFrame window object
  */
  public PEditorFrame()
  {
    super("Physics API Editor");
    this.setFocusable(true);
    
    // Setting up the GUI components and link it to ActionListener
    setupMenuBar();
    addPanels();
  }
  
  /*
    Post-condition: Adds the properties pane and the editor pane onto the window
  */
  private void addPanels()
  {
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
  private void setupMenuBar()
  {
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
    ((JCheckBoxMenuItem) menuItems[5]). setState(true);
    menuItems[6] = new JCheckBoxMenuItem("View Shape Outline");
    ((JCheckBoxMenuItem) menuItems[6]). setState(true);
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
     Pre-condition: "properitesTokenizer" and "bodies" must not be null
     Post-condition: Creates a PSpring object based on the properties listed in the StringTokenizer
     @param bodies A list of bodies already created
     @param propertiesTokenizer The StringTokenizer containing the properties of the PPolygon 
     @return PSpring The generated PSpring
  */  
  private PSpring createSpringConstraint(StringTokenizer propertiesTokenizer, ArrayList<PBody> bodies)
  {
    PSpring createdSpring = null;
    while(propertiesTokenizer.hasMoreTokens())
    {
      // Grab the properties and its values
      StringTokenizer propertyTokenizer = new StringTokenizer(propertiesTokenizer.nextToken(), ":");
      String propertyType = propertyTokenizer.nextToken();
      String propertyValue = propertyTokenizer.nextToken();
      
      // Set the appropriate properties
      switch(propertyType)
      {
        case "KValue":
          createdSpring.setKValue(Double.parseDouble(propertyValue));
          break;
        case "Length":
          createdSpring.setLength(Double.parseDouble(propertyValue));
          break;
        case "BodiesAttached":
          StringTokenizer bodiesTokenizer = new StringTokenizer(propertyValue, "[]");
          PBody[] bodiesAttached = new PBody[2];
          int curBodyAttached = 0;
          while(bodiesTokenizer.hasMoreTokens())
          {
            // Search for the body that has that name (note! bodies[] is not in order!)
            String targetName = bodiesTokenizer.nextToken();
            for (PBody body : bodies)
              if (body.getName().equals(targetName))
                bodiesAttached[curBodyAttached] = body;
                  
            curBodyAttached++;            
          }
          
          // Add the bodies to the spring
          createdSpring = new PSpring(bodiesAttached[0], bodiesAttached[1]);
          break;
      }
    }
    
    return createdSpring;
  }
  
   /*
     Pre-condition: "properitesTokenizer" and "bodies" must not be null
     Post-condition: Creates a PString object based on the properties listed in the StringTokenizer
     @param bodies A list of bodies already created
     @param propertiesTokenizer The StringTokenizer containing the properties of the PPolygon 
     @return PString The generated PString
  */  
  private PString createStringConstraint(StringTokenizer propertiesTokenizer, ArrayList<PBody> bodies)
  {
    PString createdString = null;
    while(propertiesTokenizer.hasMoreTokens())
    {
      // Grab the properties and its values
      StringTokenizer propertyTokenizer = new StringTokenizer(propertiesTokenizer.nextToken(), ":");
      String propertyType = propertyTokenizer.nextToken();
      String propertyValue = propertyTokenizer.nextToken();
      
      // Set the appropriate properties
      switch(propertyType)
      {
        case "Length":
          createdString.setLength(Double.parseDouble(propertyValue));
          break;
        case "BodiesAttached":
          StringTokenizer bodiesTokenizer = new StringTokenizer(propertyValue, "[]");
          PBody[] bodiesAttached = new PBody[2];
          int curBodyAttached = 0;
          while(bodiesTokenizer.hasMoreTokens())
          {
            // Search for the body that has that name (note! bodies[] is not in order!)
            String targetName = bodiesTokenizer.nextToken();
            for (PBody body : bodies)
              if (body.getName().equals(targetName))
                bodiesAttached[curBodyAttached] = body;
                  
            curBodyAttached++;            
          }
          
          // Add the bodies to the spring
          createdString = new PString(bodiesAttached[0], bodiesAttached[1]);
          break;
      }
    }
    
    return createdString;
  }
  
  /*
     Pre-condition: "properitesTokenizer" must not be null; it must have valid property syntaxes (i.e. propertyName:propertyValue)
     Post-condition: Creates a PPolygon object based on the properties listed in the StringTokenizer
     @param propertiesTokenizer The StringTokenizer containing the properties of the PPolygon 
     @return PPolygon The generated PPolygon
  */
  private PPolygon createPolygonBody(StringTokenizer propertiesTokenizer)
  {
    PPolygon createdPoly = null;
    
    while(propertiesTokenizer.hasMoreTokens())
    {
      // Grab the properties and its values
      StringTokenizer propertyTokenizer = new StringTokenizer(propertiesTokenizer.nextToken(), ":");
      String propertyType = propertyTokenizer.nextToken();
      String propertyValue = propertyTokenizer.nextToken();
      
      // Set the appropriate properties
      switch(propertyType)
      {
        case "Name":
          createdPoly = new PPolygon(propertyValue);
          break;
        case "Velocity":
          StringTokenizer velocityTokenizer = new StringTokenizer(propertyValue, " ");
          double xVelocity = Double.parseDouble(velocityTokenizer.nextToken());
          double yVelocity = Double.parseDouble(velocityTokenizer.nextToken());
          createdPoly.getVelocity().setXY(xVelocity, yVelocity);  
          break;        
        case "Is Moveable":
          createdPoly.setMoveable(Boolean.parseBoolean(propertyValue));
          break;
        case "Mass":
          createdPoly.setMass(Double.parseDouble(propertyValue));
          break;
        case "Angle":
          createdPoly.rotate(Double.parseDouble(propertyValue));
          break;
        case "Vertices":
          StringTokenizer verticesTokenizer = new StringTokenizer(propertyValue, ",");
          while(verticesTokenizer.hasMoreTokens())
          {
            StringTokenizer vertexTokenizer = new StringTokenizer(verticesTokenizer.nextToken(), " ");
            double xVertexComponent = Double.parseDouble(vertexTokenizer.nextToken());
            double yVertexComponent = Double.parseDouble(vertexTokenizer.nextToken());
            createdPoly.getVertices().add(new Vector(xVertexComponent, yVertexComponent));
          }
          break;
      }
    }
    
    // Initialise the center of mass
    if (createdPoly != null)
      createdPoly.computeCenterOfMass();
    
    System.out.println("    Successfully Created PPolygon !!!");
    return createdPoly;
  }
  
  /*
     Pre-condition: "properitesTokenizer" must not be null; it must have valid property syntaxes (i.e. propertyName:propertyValue)
     Post-condition: Creates a PCircle object based on the properties listed in the StringTokenizer
     @param propertiesTokenizer The StringTokenizer containing the properties of the PCircle 
     @return The generated PCircle
  */
  private PCircle createCircleBody(StringTokenizer propertiesTokenizer)
  {
    PCircle createdCircle = null;
    String name = "";
    double radius = 10;
    Vector centerPt = new Vector(10, 10);
    boolean isMoveable = false;
    double mass = 10;
    double angle = 0;
    
    while(propertiesTokenizer.hasMoreTokens())
    {
      StringTokenizer propertyTokenizer = new StringTokenizer(propertiesTokenizer.nextToken(), ":");
      String propertyType = propertyTokenizer.nextToken();
      String propertyValue = propertyTokenizer.nextToken();
      
      switch (propertyType)
      {
        case "Name":
          createdCircle = new PCircle(propertyValue);
          break;
        case "Velocity":
          StringTokenizer velocityTokenizer = new StringTokenizer(propertyValue, " ");
          double xVelocity = Double.parseDouble(velocityTokenizer.nextToken());
          double yVelocity = Double.parseDouble(velocityTokenizer.nextToken());
          createdCircle.getVelocity().setXY(xVelocity, yVelocity);  
          break;   
        case "Is Moveable":
          createdCircle.setMoveable(Boolean.parseBoolean(propertyValue));
          break;
        case "Mass":
          createdCircle.setMass(Double.parseDouble(propertyValue));
          break;
        case "Angle":
          createdCircle.rotate(Double.parseDouble(propertyValue));
          break;
        case "Radius":
          createdCircle.setRadius(Double.parseDouble(propertyValue));
          break;
        case "CenterPoint":
          StringTokenizer centerPtTokenizer = new StringTokenizer(propertyValue, " ");
          double xCenterPt = Double.parseDouble(centerPtTokenizer.nextToken());
          double yCenterPt = Double.parseDouble(centerPtTokenizer.nextToken());
          createdCircle.getCenterPt().setXY(xCenterPt, yCenterPt);  
          break;
      }
    }
    
    System.out.println("   Successfully Loaded PCircle !!!!!!!");
    return createdCircle;
  }
  
   /*
     Pre-condition: Global Variable "editorPanel" must not be null; file path asked to user must be the full file path
     Post-condition: Loads all the bodies stored in a ".txt" file (Will be asked to user VIA JOptionPane)
  */
  private void loadBodiesFromFile()
  {
    // Get the file path from user
    String filePath = JOptionPane.showInputDialog("Enter File Path");
    
    // Start storing the bodies and constraints coming from the text file
    editorPanel.clearConstraints();
    editorPanel.clearBodies();
    ArrayList<PBody> bodies = new ArrayList<PBody>();
    ArrayList<PConstraints> constraints = new ArrayList<PConstraints>();
    
    // Get the string of lines 
    String[] lines = File.readAllLines(filePath);
    System.out.println(lines.length);
    
    for (String line : lines)
    {
      StringTokenizer outerBracketTokenizer = new StringTokenizer(line, "{}");
      String bodyType = outerBracketTokenizer.nextToken();
      String bodyProperties = outerBracketTokenizer.nextToken();      
      StringTokenizer propertiesTokenizer = new StringTokenizer(bodyProperties, ";");
      
      if (bodyType.equals("PPolygon"))
      {
        System.out.println("Loading Polygon:");
        bodies.add(createPolygonBody(propertiesTokenizer));
      }
      else if (bodyType.equals("PCircle"))
      {
        System.out.println("Loading Circle:");
        bodies.add(createCircleBody(propertiesTokenizer));
      }
      else if (bodyType.equals("PSpring"))
      {
        System.out.println("Loading Spring");
        constraints.add(createSpringConstraint(propertiesTokenizer, bodies));
      }
      else if (bodyType.equals("PString"))
      {
        System.out.println("Loading String");
        constraints.add(createStringConstraint(propertiesTokenizer, bodies));
      }      
    }
    
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
  private void saveBodiesToFile()
  {
    String filePath = JOptionPane.showInputDialog("Enter File Path:");
    ArrayList<String> fileLines = new ArrayList<String>();
    
    // Adding all the bodies to an array of strings
    for (PBody body : editorPanel.getBodies())
    {
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
    for (PConstraints constraint : editorPanel.getConstraints())
    {
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
  
  private void viewJavaCode()
  {
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
    for (PBody body : bodies)
    {
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
      if (body instanceof PPolygon)
      {
        PPolygon bodyPoly = (PPolygon) body;  
        
        // Add the vertices
        for (Vector vertices : bodyPoly.getVertices())
          codeLines.add("    " + bodyName + ".getVertices().add(new Vector(" + vertices.toString() + "));");
        codeLines.add("    " + bodyName + ".computeCenterOfMass();");
      }
      
      // Adding lines of code special for PCircle
      else if (body instanceof PCircle)
      {
        PCircle bodyCircle = (PCircle) body;
        codeLines.add("    " + bodyName + ".setRadius(" + bodyCircle.getRadius() + ");");
        codeLines.add("    " + bodyName + ".setCenterPt(new Vector(" + bodyCircle.getCenterPt() + "));");
      }
      
      codeLines.add("    world.getBodies().add(" + bodyName + ");");
      
      // Add a space in between different objects
      codeLines.add("");
    }
    
    // Create the java code for the constraints
    for (PConstraints constraint : editorPanel.getConstraints())
    {
      String constraintName = "Constraint_" + (int) (Math.random() * (1000));
      String attachedBody1Name = constraint.getAttachedBodies()[0].getName();
      String attachedBody2Name = constraint.getAttachedBodies()[1].getName();
      String constraintType = "PSpring";
      if (constraint instanceof PString)
        constraintType = "PString";
      
      // Writing lines of code for all PConstraints
      codeLines.add("    " + constraintType + " " + constraintName + " = new " + constraintType + "(" + attachedBody1Name + ", " + attachedBody2Name + ")");
      
      
      // Writing lines of code for all PSprings
      if (constraint instanceof PSpring)
      {
        PSpring springConstraint = (PSpring) constraint;
        codeLines.add("    " + constraintName + ".setLengthOfSpring(new Vector(" + springConstraint.getLength() + "));");
        codeLines.add("    " + constraintName + ".setKValue(" + springConstraint.getKValue() + ");");
      }
      
      // Writing lines of code for all PStrings
      if (constraint instanceof PString)
      {
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
    scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
    JOptionPane.showMessageDialog(null, scrollPane, "Bodies Summary", JOptionPane.PLAIN_MESSAGE);
  }
  
  /*
     Pre-condition: Global variable "editorPanel" must not be null
     Post-condition: Gets the bodies created in the editorPanel, opens a new window, and simulates it
  */
  private void runSimulation()
  {    
    ArrayList<PBody> bodies = editorPanel.getBodies();
    ArrayList<PConstraints> constraints = editorPanel.getConstraints();
    
    // Relink the bodies attached to constraints to the bodies[] because the bodies attached to constraints[] are copies
    for (PConstraints constraint : constraints)
    {
      System.out.println("Constraint: " + constraint.getAttachedBodies()[0] + "\n" + constraint.getAttachedBodies()[1]);
      
      PBody[] reattachedBodies = new PBody[2];
      for (int i = 0; i < reattachedBodies.length; i++)
      {
        System.out.println("Performing BS");
        // Do a binary search (already sorted by name in ascending order)
        int left = 0;
        int right = bodies.size() - 1;
        PBody attachedBody = constraint.getAttachedBodies()[i];
        
        while (right >= left)
        {
          int mid = (left + right) / 2;
          PBody curBody = bodies.get(mid);
          
          if (curBody.getName().compareTo(attachedBody.getName()) < 0)
            left = mid + 1; 
          else if (curBody.getName().compareTo(attachedBody.getName()) > 0)
            right = mid - 1;
          else
          {
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
  public void actionPerformed (ActionEvent e)
  {
    if (e.getSource() instanceof JCheckBoxMenuItem)
    {
      JCheckBoxMenuItem curItem = (JCheckBoxMenuItem) e.getSource();
      switch (curItem.getText())
      {
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
    }
    
    else if (e.getSource() instanceof JMenuItem)
    {
      JMenuItem curItem = (JMenuItem) e.getSource();
      switch (curItem.getText())
      {
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
  
  /*
     Purpose: Opens a new window, and simulates the bodies in real time
     Original Creation Date: January 14 2016
     @author Emilio Kartono
     @version January 15 2016
  */
  private class PSimulationWindow extends JFrame
  {
    private SimulationPanel panel = null;
    
    /*
      Pre-condition: "world" must not be a null value. Frame rate must be greater than 0
      Post-condition: Creates a PSimulationWindow object
      @param world The world which holds all the bodies to be simulated
      @param frameRate The FPS for rendering and simulating the bodies
      @param isShapeFillVisible Determines whether the bodies should have its shape fill rendered
        @param isShapeOutlineVisible Determines whether the bodies should have its shape outlines rendered
        @param isAntiAliasingToggled Determines whether AntiAliasing is turned on or off
    */
    public PSimulationWindow(PWorld world, double frameRate, boolean isShapeFillVisible, boolean isShapeOutlineVisible, boolean isAntiAliasingToggled)
    {
      super("Simulation Window");
      panel = new SimulationPanel(world, frameRate, isShapeFillVisible, isShapeOutlineVisible, isAntiAliasingToggled);
      this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      
      // Add the simulation panel to the window and make the window visible
      this.getContentPane().add(panel);
      this.setVisible(true);
      this.setSize(1000, 600);
    }
    
    
    /*
     Post-condition: Destroys the object, clears the memory the object is used in the RAM and turns off the game timer
    */
    @Override
    public void dispose()
    {
      panel.turnOffTimer();
      super.dispose();
    }
    
    /*
     Purpose: A JPanel which will simulate and draw the objects stored in a PWorld object
     Original Creation Date: January 14 2016
     @author Emilio Kartono
     @version January 15 2016
    */
    private class SimulationPanel extends JPanel implements ActionListener
    {
      private PWorld world;
      private double frameRate;
      private boolean isShapeFillVisible = true;
      private boolean isShapeOutlineVisible = true;
      private boolean isAntiAliasingToggled = false;
      private Timer gameTimer;
      
      /*
        Pre-condition: "world" must not be null. Frame rate must be greater than 0
        Post-condition: Creates a SimulationPanel
        @param world The PWorld object that contains all the objects to be simulated
        @param frameRate The FPS for rendering and simulating the bodies
        @param isShapeFillVisible Determines whether the bodies should have its shape fill rendered
        @param isShapeOutlineVisible Determines whether the bodies should have its shape outlines rendered
        @param isAntiAliasingToggled Determines whether AntiAliasing is turned on or off
        
      */
      public SimulationPanel(PWorld world, double frameRate, boolean isShapeFillVisible, boolean isShapeOutlineVisible, boolean isAntiAliasingToggled)
      {
        super();
        this.world = world;
        this.frameRate = frameRate;
        this.isShapeFillVisible = isShapeFillVisible;
        this.isShapeOutlineVisible = isShapeOutlineVisible;
        this.isAntiAliasingToggled = isAntiAliasingToggled;
        
        // Initialise the game loop
        gameTimer = new Timer((int) (1000 / frameRate), this);
        gameTimer.start();
      }
      
      /*
        Post-condition: Shuts down the physics simulation
      */
      public void turnOffTimer() { this.gameTimer.stop(); }
      
      @Override
      /*
        Pre-condition: Global variable "world" must not be null
        Post-condition: Draws the objects on the screen
        @param world The PWorld object that contains all the objects to be simulated
        @param frameRate The FPS for rendering and simulating the bodies
        @param isShapeFillVisible Determines whether the bodies should have its shape fill rendered
        @param isShapeOutlineVisible Determines whether the bodies should have its shape outlines rendered
      */
      public void paintComponent(Graphics g)
      {
        super.paintComponent(g);
        
         // If antialiasing toggled
        if (isAntiAliasingToggled)
        {
          // Set antialiasing (for smoother but slower graphics)
          Graphics2D g2 = (Graphics2D) g;
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }   
        
        // Draw the bodies
        for (PBody body : world.getBodies())
        {
          if (isShapeFillVisible)
            body.drawFill(g, this.getHeight());
          if (isShapeOutlineVisible)
            body.drawOutline(g, this.getHeight());
        }
        
        // Draw the springs
        for (PConstraints constraint : world.getConstraints())
          constraint.drawConstraints(g, this.getHeight());
      }
      
      /*
        Pre-condition: Global variable "world" must not be null. "e" must not be null.
        Post-condition: Simulates the bodies in the world and renders the objects
        @param e The ActionEvent object that called this method
      */
      public void actionPerformed(ActionEvent e)
      {
        if (e.getSource() instanceof Timer)
        {
          // Simulate the objects and draw them
          world.simulate(1000 / frameRate / 1000.0);
          repaint();
        }
      }
    }   
  }
  
  /*
    Post-condition: Starts the PhysicsAPI Editor window, and sets the look and feel
  */
  public static void main(String[] args)
  {
     // Setting the new look and feel of the window
    try 
    {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
        if ("Nimbus".equals(info.getName())) 
        {
            UIManager.setLookAndFeel(info.getClassName());
            break;
        }
    }
    catch (Exception e) {}
    
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
}
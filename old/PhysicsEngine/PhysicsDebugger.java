/*
  Name: PhysicsDebugger
  A class that uses the physics engine to simulate and draw simple shapes in a window
  This class is only used for debugging purposes. No instance of this can be created outside the Physics Engine package
  @author Emilio Kartono
  @version December 21, 2015
*/

package PhysicsEngine;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import ToolBox.Vector;

class PhysicsDebuggerPanel extends JPanel implements ActionListener
{
  // Fields controlling the animation and graphics of the JPanel
  private static final int WIDTH = 1000;
  private static final int HEIGHT = 600;
  private final double FPS = 60;
  private final double TIME_MULTIPLIER = 1;
  private final boolean enableAntiAliasing = true;
  
  // Field storing the efficiency of the physics engine (based on the amount of time it takes to simulate the objects)
  private long simulationDuration = 0;
  private long maxSimulationDuration = Long.MIN_VALUE;
  private long renderingDuration = 0;
  
  // Fields representing the physic engine and its bodies
  private PWorld pEngine = new PWorld();
  private PPolygon polygon;
  
  /*
    Post-condition: Creates a PhysicsDebuggerPanel object
  */
  public PhysicsDebuggerPanel()
  {    
    // Initialise the physic engine
    /*
    polygon = new PPolygon("Hehe");
    polygon.getVertices().add(new Vector(110, 510));
    polygon.getVertices().add(new Vector(110, 600));
    polygon.getVertices().add(new Vector(200, 600));
    polygon.getVertices().add(new Vector(200, 510));
    polygon.computeCenterOfMass();
    polygon.rotate(10);
    pEngine.getBodies().add(polygon);
    polygon.rotate(45);
    */
    
    /*
    PPolygon poly3 = new PPolygon("JJ");
    poly3.getVertices().add(new Vector(100, 600));
    poly3.getVertices().add(new Vector(100, 700));
    poly3.getVertices().add(new Vector(200, 700));
    poly3.getVertices().add(new Vector(200, 600));
    poly3.computeCenterOfMass();
    pEngine.getBodies().add(poly3);
    */
    
    /*
    PPolygon poly4 = new PPolygon("JJ");
    poly4.getVertices().add(new Vector(100, 900));
    poly4.getVertices().add(new Vector(100, 1000));
    poly4.getVertices().add(new Vector(200, 1000));
    poly4.getVertices().add(new Vector(200, 900));
    poly4.computeCenterOfMass();
    pEngine.getBodies().add(poly4);
    */
    
    /*
    for (int i = 0; i < 1; i++)
    {
      PPolygon poly = new PPolygon("blocks");
      poly.getVertices().add(new Vector(500, 500 + (i * 10)));
      poly.getVertices().add(new Vector(550, 500 + (i * 10)));
      poly.getVertices().add(new Vector(550, 550 + (i * 10)));
      poly.getVertices().add(new Vector(500, 550 + (i * 10)));
      poly.computeCenterOfMass();
      pEngine.getBodies().add(poly);
    }
    */
    
    /*
    PPolygon poly90 = new PPolygon("GG");
    poly90.getVertices().add(new Vector(170, 1100));
    poly90.getVertices().add(new Vector(250, 1000));
    poly90.getVertices().add(new Vector(250, 1100));
    poly90.computeCenterOfMass();
    pEngine.getBodies().add(poly90);
    */
    
    PPolygon ground = new PPolygon("Ground");
    ground.getVertices().add(new Vector(0, 0));
    ground.getVertices().add(new Vector(0, 100));
    ground.getVertices().add(new Vector(1000, 100));
    ground.getVertices().add(new Vector(1000, 0));
    ground.computeCenterOfMass();
    ground.setMoveable(false);
    pEngine.getBodies().add(ground);
    
    
    PCircle circle3 = new PCircle("KIJIJI");
    circle3.setCenterPt(new Vector(600, 500));
    circle3.setMoveable(false);
    circle3.setRadius(90);
    pEngine.getBodies().add(circle3);
    
    PCircle circle4 = new PCircle("KIJIJI");
    circle4.setCenterPt(new Vector(400, 500));
    circle4.setRadius(90);
    pEngine.getBodies().add(circle4);
    
    PString string2 = new PString(circle3, circle4);
    pEngine.getConstraints().add(string2);
    
    
    // Create the game timer
    Timer gameTimer = new Timer((int) (1000.0 / FPS / TIME_MULTIPLIER), this);
    gameTimer.start();
  }
  
   /*
    Pre-condition: The "g" must not be null.
    Post-condition: Draws the bodies on the screen
    @param g The Graphics Object
  */
  @Override
  public void paintComponent(Graphics g)
  {
    // Clear the screen
    super.paintComponent(g);
    
    // Set the antialiasing (if enabled)
    if (enableAntiAliasing)
    {
      Graphics2D graphics2D = (Graphics2D) g;
      graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    // Draw the effiency of physics engine simulation
    g.setColor(Color.black);
    g.drawString("Max Simulation Time: " + maxSimulationDuration, 50, 20);
    g.drawString("Time Ellapsed for Simulation: " + simulationDuration, 50, 40);
    
    // Get the efficiency of drawing the objects to the screen
    renderingDuration = System.currentTimeMillis();
    pEngine.draw(g);
    renderingDuration = System.currentTimeMillis() - renderingDuration;
    
    // Drawing the rendering efficiency
    g.setColor(Color.black);
    g.drawString("Time Ellapsed for Rendering: " + renderingDuration, 50, 60);
    
    // Drawing the time
    g.drawString("Current Program Time: " + System.currentTimeMillis(), 50, 80);
    g.drawString("Time Speed: " + TIME_MULTIPLIER + "x", 50, 100);
  }
  
  /*
     Pre-condition: "e" must not be null
     Post-condition: Simulates the bodies when a Timer triggers this event
  */
  public void actionPerformed(ActionEvent e)
  {
    // Keeping track of effiency
    simulationDuration = System.currentTimeMillis();
    pEngine.simulate((1000.0 / FPS) / 1000.0);
    simulationDuration = System.currentTimeMillis() - simulationDuration;
    
    // Keeping track of the max simulation duration
    if (simulationDuration > maxSimulationDuration)
      maxSimulationDuration = simulationDuration;
    
    // Redraw the entire screen
    repaint();
  }
  
   /*
     Post-condition: Start the debugger, open the window, and simulate the bodies
  */
  public static void main(String[] args)
  {
    // Create the window with certain properties
    JFrame debugWindow = new JFrame("Physic Engine Debugger");
    debugWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    debugWindow.setSize(WIDTH, HEIGHT);
    debugWindow.setResizable(false);    
    
    // Insert the JPanel to the window
    debugWindow.getContentPane().add(new PhysicsDebuggerPanel());
    
    // Make the window visible (MUST BE PLACED AFTER INSRTING JPANEL!!)
    debugWindow.setVisible(true);    
  }
}
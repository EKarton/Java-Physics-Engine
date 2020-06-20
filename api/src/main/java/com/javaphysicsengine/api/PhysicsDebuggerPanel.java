/*
 * A class that uses the physics engine to simulate and draw simple shapes in a window
 * This class is only used for debugging purposes. No instance of this can be created outside the Physics Engine package
 * @author Emilio Kartono
 * @version December 21, 2015
 */

package com.javaphysicsengine.api;

import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.utils.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhysicsDebuggerPanel extends JPanel implements ActionListener {

    // Fields controlling the animation and graphics of the JPanel
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private final double FPS = 25;
    private final double TIME_MULTIPLIER = 1;

    // Field storing the efficiency of the physics engine (based on the amount of time it takes to simulate the objects)
    private long simulationDuration = 0;
    private long maxSimulationDuration = Long.MIN_VALUE;

    // Fields representing the physic engine and its bodies
    private PWorld pEngine = new PWorld();
    private PPolygon polygon;

    /**
     * Creates a PhysicsDebuggerPanel object
     */
    public PhysicsDebuggerPanel() {
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

//        PPolygon ground = new PPolygon("Ground");
//        ground.getVertices().add(new Vector(0, 0));
//        ground.getVertices().add(new Vector(0, 100));
//        ground.getVertices().add(new Vector(1000, 100));
//        ground.getVertices().add(new Vector(1000, 0));
//        ground.computeCenterOfMass();
//        ground.setMoveable(false);
//        pEngine.getBodies().add(ground);
//
//
//        PCircle circle3 = new PCircle("KIJIJI");
//        circle3.setCenterPt(new Vector(600, 500));
//        circle3.setMoveable(false);
//        circle3.setRadius(90);
//        pEngine.getBodies().add(circle3);
//
//        PCircle circle4 = new PCircle("KIJIJI");
//        circle4.setCenterPt(new Vector(400, 500));
//        circle4.setRadius(90);
//        pEngine.getBodies().add(circle4);
//
//        PString string2 = new PString(circle3, circle4);
//        pEngine.getConstraints().add(string2);

//        PPolygon triangle = new PPolygon("Triangle");
//        triangle.setMoveable(false);
//        triangle.getVertices().add(new Vector(600, 500));
//        triangle.getVertices().add(new Vector(240, 240));
//        triangle.getVertices().add(new Vector(600, 240));
//        triangle.computeCenterOfMass();
//        pEngine.getBodies().add(triangle);

//        PCircle circle = new PCircle("Circle");
//        circle.setCenterPt(Vector.of(420, 640));
//        circle.setRadius(20);
//        circle.setMass(1);
//        pEngine.getBodies().add(circle);

//        PPolygon polygon1 = new PPolygon(("Box1"));
//        polygon1.getVertices().add(new Vector(400, 100));
//        polygon1.getVertices().add(new Vector(600, 100));
//        polygon1.getVertices().add(new Vector(600, 300));
//        polygon1.getVertices().add(new Vector(400, 300));
//        polygon1.computeCenterOfMass();
//        polygon1.setMoveable(false);
//        pEngine.getBodies().add(polygon1);
//
//        PPolygon polygon2 = new PPolygon(("Box2"));
//        polygon2.getVertices().add(new Vector(450, 200));
//        polygon2.getVertices().add(new Vector(550, 200));
//        polygon2.getVertices().add(new Vector(550, 400));
//        polygon2.getVertices().add(new Vector(450, 400));
//        polygon2.computeCenterOfMass();
//        polygon2.setMoveable(true);
//        polygon2.setVelocity(Vector.of(0, -10));
//        polygon2.translate(Vector.of(0, 10));
//        pEngine.getBodies().add(polygon2);

        PCircle circle = new PCircle("Circle");
        circle.setCenterPt(Vector.of(522, 440));
        circle.setRadius(20);
        circle.setMass(1);
        pEngine.getBodies().add(circle);

        PCircle circle5 = new PCircle("Circle");
        circle5.setCenterPt(Vector.of(510, 240));
        circle5.setRadius(20);
        circle5.setMass(1);
        circle5.setVelocity(Vector.of(0, 10));
        pEngine.getBodies().add(circle5);

        PPolygon polygon = new PPolygon("Box");
        polygon.getVertices().add(Vector.of(300, 400));
        polygon.getVertices().add(Vector.of(340, 400));
        polygon.getVertices().add(Vector.of(340, 440));
        polygon.getVertices().add(Vector.of(300, 440));
        polygon.computeCenterOfMass();
        pEngine.getBodies().add(polygon);

        for (int i = 0; i < 20; i++) {
            PCircle circle2 = new PCircle("Circle2");
            circle2.setMoveable(false);
            circle2.setCenterPt(Vector.of(100 + 40 * i, 140));
            circle2.setRadius(40);
            circle2.setMass(1);
            pEngine.getBodies().add(circle2);
        }

        for (int i = 0; i < 20; i++) {
            PCircle circle2 = new PCircle("Circle2");
            circle2.setMoveable(false);
            circle2.setCenterPt(Vector.of(100, 140 + 40 * i));
            circle2.setRadius(40);
            circle2.setMass(1);
            pEngine.getBodies().add(circle2);
        }


        // Create the game timer
        Timer gameTimer = new Timer((int) (1000.0 / FPS / TIME_MULTIPLIER), this);
        gameTimer.start();
    }

    /**
     * Start the debugger, open the window, and simulate the bodies
     */
    public static void main(String[] args) {

        // Create the window with certain properties
        JFrame debugWindow = new JFrame("Physic Engine Debugger");
        debugWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        debugWindow.setSize(WIDTH, HEIGHT);
        debugWindow.setResizable(false);

        // Insert the JPanel to the window
        debugWindow.getContentPane().add(new PhysicsDebuggerPanel());

        // Make the window visible
        debugWindow.setVisible(true);
    }

    /**
     * Draws the bodies on the screen
     * @param g The Graphics Object
     */
    @Override
    public void paintComponent(Graphics g) {
        // Clear the screen
        super.paintComponent(g);

        // Set the antialiasing (if enabled)
        boolean enableAntiAliasing = true;
        if (enableAntiAliasing) {
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        // Draw the efficiency of physics engine simulation
        g.setColor(Color.black);
        g.drawString("Max Simulation Time: " + maxSimulationDuration, 50, 20);
        g.drawString("Time Ellapsed for Simulation: " + simulationDuration, 50, 40);

        // Get the efficiency of drawing the objects to the screen
        long renderingDuration = System.currentTimeMillis();
        pEngine.draw(g);
        renderingDuration = System.currentTimeMillis() - renderingDuration;

        // Drawing the rendering efficiency
        g.setColor(Color.black);
        g.drawString("Time Ellapsed for Rendering: " + renderingDuration, 50, 60);

        // Drawing the time
        g.drawString("Current Program Time: " + System.currentTimeMillis(), 50, 80);
        g.drawString("Time Speed: " + TIME_MULTIPLIER + "x", 50, 100);
    }

    /**
     * Simulates the bodies when a Timer triggers this event
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
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
}
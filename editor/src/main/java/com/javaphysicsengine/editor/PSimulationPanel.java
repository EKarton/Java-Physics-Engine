/*
 * Purpose: A JPanel which will simulate and draw the objects stored in a PWorld object
 * Original Creation Date: January 14 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */
package com.javaphysicsengine.editor;

import com.javaphysicsengine.api.PWorld;
import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PConstraints;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PSimulationPanel extends JPanel implements ActionListener {
    private PWorld world;
    private double frameRate;
    private boolean isShapeFillVisible = true;
    private boolean isShapeOutlineVisible = true;
    private boolean isAntiAliasingToggled = false;
    private Timer gameTimer;

    /**
     * Pre-condition: "world" must not be null. Frame rate must be greater than 0
     * Post-condition: Creates a PSimulationPanel
     * @param world The PWorld object that contains all the objects to be simulated
     * @param frameRate The FPS for rendering and simulating the bodies
     * @param isShapeFillVisible Determines whether the bodies should have its shape fill rendered
     * @param isShapeOutlineVisible Determines whether the bodies should have its shape outlines rendered
     * @param isAntiAliasingToggled Determines whether AntiAliasing is turned on or off
     */
    public PSimulationPanel(PWorld world, double frameRate, boolean isShapeFillVisible, boolean isShapeOutlineVisible, boolean isAntiAliasingToggled) {
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

    /**
     * Post-condition: Turns off the physics simulation
     */
    public void turnOffTimer() {
        this.gameTimer.stop();
    }

    /**
     * Draws the objects on the screen
     * @param g The graphics object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // If antialiasing toggled
        if (isAntiAliasingToggled) {
            // Set antialiasing (for smoother but slower graphics)
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        // Draw the bodies
        for (PBody body : world.getBodies()) {
            if (isShapeFillVisible)
                body.drawFill(g, this.getHeight());
            if (isShapeOutlineVisible)
                body.drawOutline(g, this.getHeight());
        }

        // Draw the springs
        for (PConstraints constraint : world.getConstraints())
            constraint.drawConstraints(g, this.getHeight());
    }

    /**
     * Simulates the bodies in the world and renders the objects
     * @param e The ActionEvent object that called this method
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            // Simulate the objects and draw them
            world.simulate(1000 / frameRate / 1000.0);
            repaint();
        }
    }
}

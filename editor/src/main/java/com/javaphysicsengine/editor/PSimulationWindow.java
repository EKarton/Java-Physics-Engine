/*
 * Purpose: Opens a new window, and simulates the bodies in real time
 * Original Creation Date: January 14 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.editor;

import com.javaphysicsengine.api.PWorld;

import javax.swing.JFrame;

public class PSimulationWindow extends JFrame {
    private PSimulationPanel panel;

    /**
     * Creates a PSimulationWindow object
     * Note: Frame rate must be greater than 0
     * @param world The world which holds all the bodies to be simulated
     * @param frameRate The FPS for rendering and simulating the bodies
     * @param isShapeFillVisible Determines whether the bodies should have its shape fill rendered
     * @param isShapeOutlineVisible Determines whether the bodies should have its shape outlines rendered
     * @param isAntiAliasingToggled Determines whether AntiAliasing is turned on or off
     */
    public PSimulationWindow(PWorld world, double frameRate, boolean isShapeFillVisible, boolean isShapeOutlineVisible, boolean isAntiAliasingToggled) {
        super("Simulation Window");
        panel = new PSimulationPanel(world, frameRate, isShapeFillVisible, isShapeOutlineVisible, isAntiAliasingToggled);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add the simulation panel to the window and make the window visible
        this.getContentPane().add(panel);
        this.setVisible(true);
        this.setSize(1000, 600);
    }

    /**
     * Destroys the object, and turns off the game timer
     */
    @Override
    public void dispose() {
        panel.turnOffTimer();
        super.dispose();
    }

}

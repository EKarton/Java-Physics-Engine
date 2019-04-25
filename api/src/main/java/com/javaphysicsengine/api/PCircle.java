/*
 * Purpose: To represent a PBody object with a circle shape
 * Original Creation Date: January 1 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */
package com.javaphysicsengine.api;

import com.javaphysicsengine.utils.Vector;

import java.awt.Graphics;

public class PCircle extends PBody {

    // Geometric properties about the circle
    private double radius = 10;

    /**
     * Creates a PCircle object with a certain name attached
     * @param name The name of the circle
     */
    public PCircle(String name) {
        super(name);
    }

    /**
     * Creates a PCircle object from a pre-existing PCircle object
     * @param existingCircle A pre-existing PCircle object
     */
    public PCircle(PCircle existingCircle) {
        super(existingCircle);
        this.radius = existingCircle.radius;
    }

    /**
     * Returns the radius of the circle
     * @return Returns the radius of the circle
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the circle
     * @param newRadius The new radius of the circle
     */
    public void setRadius(double newRadius) {
        radius = newRadius;
    }

    /**
     * Rotates the circle
     * @param newAngle The angle of the body
     */
    public void rotate(double newAngle) {
        super.angle = newAngle;
    }

    /**
     * Moves the circle to its new center point
     * @param newCenterPt The new center point
     */
    public void move(Vector newCenterPt) {
        centerPt = newCenterPt;
    }

    /**
     * Moves the circle based on a certain displacement
     * @param displacement The displacement to move the body by a certain amount
     */
    public void translate(Vector displacement) {
        centerPt.setY(centerPt.getY() + displacement.getY());
        centerPt.setX(centerPt.getX() + displacement.getX());
    }

    /**
     * Draws the fill of the circle
     * Pre-condition: The "windowHeight" must be greater than 0
     * @param g The Graphics Object
     * @param windowHeight The height of the window containing the body being displayed
     */
    @Override
    public void drawFill(Graphics g, int windowHeight) {
        // Draws a circle in middle of coordinate
        int topLeftX = (int) (centerPt.getX() - radius);
        int topLeftY = windowHeight - (int) (centerPt.getY() + radius);
        g.setColor(getFillColor());
        g.fillOval(topLeftX, topLeftY, (int) (radius * 2), (int) (radius * 2));

        // Draw the center of mass
        super.drawFill(g, windowHeight);
    }

    /**
     * Draws the outline of the circle
     * Pre-condition: "windowHeight" must be greater than 0
     * @param g The Graphics Object
     * @param windowHeight The height of the window containing the body being displayed
     */
    @Override
    public void drawOutline(Graphics g, int windowHeight) {
        // Draws a circle in middle of coordinate
        int topLeftX = (int) (centerPt.getX() - radius);
        int topLeftY = windowHeight - (int) (centerPt.getY() + radius);
        g.setColor(getOutlineColor());
        g.drawOval(topLeftX, topLeftY, (int) (radius * 2), (int) (radius * 2));

        // Draw the center of mass
        super.drawOutline(g, windowHeight);
    }

    /**
     * Returns the properties of the circle in a string where each property is
     * stored in format { propertyType:propertyValue; } (excluding the curly brackets)
     * @return Returns the properties of the circle in a string
     */
    @Override
    public String toString() {
        return super.toString() + "Radius:" + radius + ";";
    }
}
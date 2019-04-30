/*
 * Purpose: To represent the bounding box of a shape
 * Original Creation Date: January 1 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.api.body;

import com.javaphysicsengine.utils.Vector;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class PBoundingBox {
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    /**
     * Creates a bounding box based on a list of vertices from a polygon shape
     * @param vertices The vertices of a polygon
     */
    public PBoundingBox(List<Vector> vertices) {
        recomputeBoundaries(vertices);
    }

    /**
     * Constructs a Bounding Box given known minX, minY, maxX, maxY values
     * @param minX the min X value
     * @param maxX the max X value
     * @param minY the min Y value
     * @param maxY the max Y value
     */
    public PBoundingBox(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     * Recomputes a bounding box based on a list of vertices from a polygon shape
     * @param vertices The vertices of a polygon
     */
    public void recomputeBoundaries(List<Vector> vertices) {
        minX = Double.MAX_VALUE;
        maxX = -Double.MIN_VALUE;
        minY = Double.MAX_VALUE;
        maxY = -Double.MIN_VALUE;

        // Getting the min/max x and y values from vertices[]
        for (Vector vertex : vertices) {
            if (vertex.getX() < minX) minX = vertex.getX();
            if (vertex.getY() < minY) minY = vertex.getY();
            if (vertex.getX() > maxX) maxX = vertex.getX();
            if (vertex.getY() > maxY) maxY = vertex.getY();
        }
    }

    /**
     * Returns the minimum x value of the bounding box
     * @return Returns the minimum x value
     */
    public double getMinX() {
        return minX;
    }

    /**
     * Sets the minimum x value of the bounding box
     * @param minX The new minimum x value
     */
    public void setMinX(double minX) {
        this.minX = minX;
    }

    /**
     * Returns the minimum y value of the bounding box
     * @return Returns the minimum y value
     */
    public double getMinY() {
        return minY;
    }

    /**
     * Sets the minimum y value of the bounding box
     * @param minY The new minimum y value
     */
    public void setMinY(double minY) {
        this.minY = minY;
    }

    /**
     * Returns the maximum x value of the bounding box
     * @return Returns the maximum x value
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * Sets the maximum x value of the bounding box
     * @param maxX The new maximum x value
     */
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    /**
     * Returns the maximum y value of the bounding box
     * @return Returns the maximum y value
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * Sets the maximum y value of the bounding box
     * @param maxY The new maximum y value
     */
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    /**
     * Draws the bounding box outline on the screen
     *
     * @param g The Graphics Object
     * @param windowHeight The height of the window that will draw the bounding box
     */
    public void drawBoundingBox(Graphics g, double windowHeight) {
        // Draw an outline of the bounding box
        g.setColor(Color.red);
        g.drawLine((int) minX, (int) (windowHeight - minY), (int) minX, (int) (windowHeight - maxY));
        g.drawLine((int) minX, (int) (windowHeight - maxY), (int) maxX, (int) (windowHeight - maxY));
        g.drawLine((int) maxX, (int) (windowHeight - maxY), (int) maxX, (int) (windowHeight - minY));
        g.drawLine((int) maxX, (int) (windowHeight - minY), (int) minX, (int) (windowHeight - minY));
    }

    @Override
    public String toString() {
        return "{ (" + minX + ", " + maxX + "), (" + minY + ", " + maxY + ") }";
    }
}
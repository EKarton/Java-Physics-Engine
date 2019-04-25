/*
  Purpose: To represent the bounding box of a shape
  Original Creation Date: January 1 2016
  @author Emilio Kartono
  @version January 15 2016
*/

package com.javaphysicsengine.api;

import com.javaphysicsengine.utils.Vector;

import java.awt.*;
import java.util.ArrayList;

class PBoundingBox {
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    /*
       Pre-condition: "vertices" must not be null
       Post-condition: Creates a bounding box based on a list of vertices from a polygon shape
       @param vertices The vertices of a polygon
    */
    public PBoundingBox(ArrayList<Vector> vertices) {
        recomputeBoundaries(vertices);
    }

    /*
       Pre-condition: "vertices" must not be null
       Post-condition: Recomputes a bounding box based on a list of vertices from a polygon shape
       @param vertices The vertices of a polygon
    */
    public void recomputeBoundaries(ArrayList<Vector> vertices) {
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

    /*
      Post-condition: Returns the minimum x value of the bounding box
      @return Returns the minimum x value
    */
    public double getMinX() {
        return minX;
    }

    /*
      Post-condition: Sets the minimum x value of the bounding box
      @param minX The new minimum x value
    */
    public void setMinX(double minX) {
        this.minX = minX;
    }

    /*
      Post-condition: Returns the minimum y value of the bounding box
      @return Returns the minimum y value
    */
    public double getMinY() {
        return minY;
    }

    /*
      Post-condition: Sets the minimum y value of the bounding box
      @param minY The new minimum y value
    */
    public void setMinY(double minY) {
        this.minY = minY;
    }

    /*
      Post-condition: Returns the maximum x value of the bounding box
      @return Returns the maximum x value
    */
    public double getMaxX() {
        return maxX;
    }

    /*
      Post-condition: Sets the maximum x value of the bounding box
      @param maxX The new maximum x value
    */
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    /*
      Post-condition: Returns the maximum y value of the bounding box
      @return Returns the maximum y value
    */
    public double getMaxY() {
        return maxY;
    }

    /*
      Post-condition: Sets the maximum y value of the bounding box
      @param maxY The new maximum y value
    */
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    /*
      Pre-condition: The "g" must not be null and the "windowHeight" must be greater than 0
      Post-condition: Draws the bounding box outline on the screen
      @param g The Graphics Object
      @param windowHeight The height of the window that will draw the bounding box
    */
    public void drawBoundingBox(Graphics g, double windowHeight) {
        // Draw an outline of the bounding box
        g.setColor(Color.red);
        g.drawLine((int) minX, (int) (windowHeight - minY), (int) minX, (int) (windowHeight - maxY));
        g.drawLine((int) minX, (int) (windowHeight - maxY), (int) maxX, (int) (windowHeight - maxY));
        g.drawLine((int) maxX, (int) (windowHeight - maxY), (int) maxX, (int) (windowHeight - minY));
        g.drawLine((int) maxX, (int) (windowHeight - minY), (int) minX, (int) (windowHeight - minY));
    }
}
package com.javaphysicsengine.api;

import com.javaphysicsengine.utils.Trigonometry;
import com.javaphysicsengine.utils.Vector;

import java.awt.*;
import java.util.ArrayList;

public class PPolygon extends PBody {
    // Fields representing the geometric and graphic structure of a polygon
    private ArrayList<Vector> vertices = new ArrayList<Vector>();
    private PBoundingBox boundingBox;

    public PPolygon(String name) {
        super(name);
    }

    public PPolygon(PPolygon existingPolygon) {
        super(existingPolygon);

        // Make a copy of its vertices
        for (Vector vertexCopy : existingPolygon.vertices)
            vertices.add(new Vector(vertexCopy.getX(), vertexCopy.getY()));
        this.computeCenterOfMass();
    }

    public ArrayList<Vector> getVertices() {
        return vertices;
    }

    public PBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void computeCenterOfMass() {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Vector vertex : vertices) {
            if (vertex.getX() < minX) minX = vertex.getX();
            if (vertex.getX() > maxX) maxX = vertex.getX();
            if (vertex.getY() < minY) minY = vertex.getY();
            if (vertex.getY() > maxY) maxY = vertex.getY();
        }

        centerPt.setX((minX + maxX) / 2);
        centerPt.setY((minY + maxY) / 2);

        boundingBox = new PBoundingBox(vertices);
    }

    public void translate(Vector displacement) {
        // Moving all the vertices based on the displacement
        for (Vector vertex : vertices) {
            vertex.setX(vertex.getX() + displacement.getX());
            vertex.setY(vertex.getY() + displacement.getY());
        }

        // Move the centerPt
        centerPt.setX(centerPt.getX() + displacement.getX());
        centerPt.setY(centerPt.getY() + displacement.getY());

        // Move the bounding box
        boundingBox.setMinX(boundingBox.getMinX() + displacement.getX());
        boundingBox.setMaxX(boundingBox.getMaxX() + displacement.getX());
        boundingBox.setMinY(boundingBox.getMinY() + displacement.getY());
        boundingBox.setMaxY(boundingBox.getMaxY() + displacement.getY());
    }

    public void rotate(double newAngle) {
        // Rotate all the vertices around its center of mass
        for (Vector vertex : vertices) {
            // Shifting the vertex so that the centerPt is (0, 0)
            vertex.setX(vertex.getX() - centerPt.getX());
            vertex.setY(vertex.getY() - centerPt.getY());

            // Getting the angle made by the vertex and the origin
            double betaAngle = Math.abs(Trigonometry.inverseOfTan(vertex.getY() / vertex.getX()));
            double alphaAngle = Trigonometry.convertBetaToThetaAngle(vertex.getX(), vertex.getY(), betaAngle);

            // Getting the new rotated x and y coordinates based on the unit circle
            double angleToRotateBy = alphaAngle - angle + newAngle;
            vertex.setY(Trigonometry.sin(angleToRotateBy) * vertex.getLength() + centerPt.getY());
            vertex.setX(Trigonometry.cos(angleToRotateBy) * vertex.getLength() + centerPt.getX());
        }

        if (boundingBox != null)
            boundingBox.recomputeBoundaries(vertices);
        super.angle = newAngle;
    }

    public void move(Vector newCenterPt) {
        // Compute the displacement from the old centerPt to the new centerPt and call the translate()
        Vector displacement = Vector.subtract(newCenterPt, centerPt);
        translate(displacement);
    }

    @Override
    public void drawBoundingBox(Graphics g, int windowHeight) {
        boundingBox.drawBoundingBox(g, windowHeight);
        super.drawBoundingBox(g, windowHeight);
    }

    @Override
    public void drawFill(Graphics g, int windowHeight) {
        // Convert the vertices to x and y coordinates
        int[] xCoords = new int[vertices.size()];
        int[] yCoords = new int[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            xCoords[i] = (int) vertices.get(i).getX();
            yCoords[i] = windowHeight - (int) vertices.get(i).getY();
        }

        // Draw the polygon onto the screen
        g.setColor(getFillColor());
        g.fillPolygon(xCoords, yCoords, xCoords.length);

        // Draw the center of mass
        super.drawFill(g, windowHeight);
    }

    @Override
    public void drawOutline(Graphics g, int windowHeight) {
        // Convert the vertices to x and y coordinates
        int[] xCoords = new int[vertices.size()];
        int[] yCoords = new int[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            xCoords[i] = (int) vertices.get(i).getX();
            yCoords[i] = windowHeight - (int) vertices.get(i).getY();
        }

        // Draw the polygon onto the screen
        g.setColor(getOutlineColor());
        g.drawPolygon(xCoords, yCoords, xCoords.length);

        // Draw the center of mass
        super.drawOutline(g, windowHeight);
    }

    @Override
    public String toString() {
        String propertiesLine = super.toString() + "Vertices:";
        for (int i = 0; i < vertices.size(); i++) {
            propertiesLine += vertices.get(i).getX() + " " + vertices.get(i).getY();
            if (i < vertices.size() - 1)
                propertiesLine += ",";
        }
        return propertiesLine;
    }
}
package com.javaphysicsengine.api.body;

import com.javaphysicsengine.utils.Vector;

import java.awt.Color;
import java.awt.Graphics;

public class PSpring extends PConstraints {
    private double kValue = 100;  // <- In Newtons/meter

    /**
     * Creates a PSpring object with "body1" and "body2" attached
     * @param body1 The first body to be attached to.
     * @param body2 The second body to be attached to.
     */
    public PSpring(PBody body1, PBody body2) {
        super(body1, body2);
        setLength(50);
    }

    /**
     * The k value of the spring
     * @return The k value of the spring
     */
    public double getKValue() {
        return this.kValue;
    }

    /**
     * Sets the k value of the spring
     * @param kValue The new k value (k > 0)
     */
    public void setKValue(double kValue) {
        this.kValue = kValue;
    }

    /**
     * Adds the tension forces to the attached bodies
     */
    public void addTensionForce() {
        // Computing the center of the spring
        PBody[] bodies = super.getAttachedBodies();
        Vector equilCenter = new Vector((bodies[0].getCenterPt().getX() + bodies[1].getCenterPt().getX()) / 2,
                (bodies[0].getCenterPt().getY() + bodies[1].getCenterPt().getY()) / 2);

        // Adding tension to each body separately
        addTensionForceToBody(getAttachedBodies()[0], equilCenter);
        addTensionForceToBody(getAttachedBodies()[1], equilCenter);
    }

    /**
     * Adds the tension forces to a body
     * @param body The body
     * @param equilPt The equilibruim point of the spring
     */
    private void addTensionForceToBody(PBody body, Vector equilPt) {
        // Computing the distance of the body to the centerpt in vector form
        double xMinus = body.getCenterPt().getX() - equilPt.getX();
        double yMinus = body.getCenterPt().getY() - equilPt.getY();

        // If it is at the dead center of spring's equillibruim point, there no tension force
        if (xMinus == 0 && yMinus == 0)
            return;

        double distance = Math.sqrt((xMinus * xMinus) + (yMinus * yMinus));
        distance /= 100;

        // Computing the tension force
        double displacementFromSpring = distance - (getLength() / 2);
        double tensionForce_Scalar = -kValue * (distance);
        Vector tensionForce = new Vector(xMinus, yMinus);
        tensionForce.setLength(tensionForce_Scalar);

        // Add the tension force to the body
        body.setNetForce((Vector.add(body.getNetForce(), tensionForce)));
    }

    /**
     * Draws a line between the two attached bodies
     * @param g The Graphics Object
     * @param windowHeight The height of the window that is containing the body being displayed
     */
    public void drawConstraints(Graphics g, int windowHeight) {
        // Draw a line in between the two objects
        g.setColor(Color.GREEN);
        PBody[] bodies = super.getAttachedBodies();

        int x1 = (int) bodies[0].getCenterPt().getX();
        int y1 = (int) bodies[0].getCenterPt().getY();
        int x2 = (int) bodies[1].getCenterPt().getX();
        int y2 = (int) bodies[1].getCenterPt().getY();
        g.drawLine(x1, windowHeight - y1, x2, windowHeight - y2);
    }

    /**
     * Returns string representation of a spring
     * @return Returns the string rep. of this PSpring
     */
    @Override
    public String toString() {
        return super.toString() + "KValue:" + kValue;
    }
}
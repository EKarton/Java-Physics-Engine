/*
  Purpose: To represent a PConstraints object that has spring-like physics
  Original Creation Date: January 1 2016
  @author Emilio Kartono
  @version January 15 2016
*/

package com.javaphysicsengine.api;

import com.javaphysicsengine.utils.Vector;

import java.awt.*;

public class PSpring extends PConstraints {
    // Physic properties
    private double kValue = 100;  // <- In Newtons/meter

    /*
       Pre-condition: "body1" and "body2" must not be null
       Post-condition: Creates a PSpring object with "body1" and "body2" attached
       @param body1 The first body to be attached to.
       @param body2 The second body to be attached to.
    */
    public PSpring(PBody body1, PBody body2) {
        super(body1, body2);
        setLength(50);
    }

    /*
      Post-condition: Returns the k value of the spring
      @return The k value of the spring
    */
    public double getKValue() {
        return this.kValue;
    }

    /*
      Pre-condition: The kValue must be greater than 0
      Post-condition: Sets the k value of the spring
      @param kValue The new k value
    */
    public void setKValue(double kValue) {
        this.kValue = kValue;
    }

    /*
       Post-condition: Adds the tension forces to a body
       @param body The body
       @param equilPt The equilibruim point of the spring
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

        //System.out.println("  Force: " + tensionForce_Scalar);

        // Add the tension force to the body
        body.setNetForce((Vector.add(body.getNetForce(), tensionForce)));
    }

    /*
       Post-condition: Adds the tension forces to the attached bodies
    */
    public void addTensionForce() {
        // Computing the center of the spring
        PBody[] bodies = super.getAttachedBodies();
        Vector equilCenter = new Vector((bodies[0].getCenterPt().getX() + bodies[1].getCenterPt().getX()) / 2,
                (bodies[0].getCenterPt().getY() + bodies[1].getCenterPt().getY()) / 2);

        // Adding tension to each body separately
        //System.out.println("Body 1:");
        addTensionForceToBody(getAttachedBodies()[0], equilCenter);
        //System.out.println("Body 2:");
        addTensionForceToBody(getAttachedBodies()[1], equilCenter);
    }

    /*
      Pre-condition: The "g" must not be null and the "windowHeight" must be greater than 0
      Post-condition: Draws a line between the two attached bodies
      @param g The Graphics Object
      @param windowHeight The height of the window that is containing the body being displayed
    */
    public void drawConstraints(Graphics g, int windowHeight) {
        // Draw a line in between the two objects
        g.setColor(Color.GREEN);
        PBody[] bodies = super.getAttachedBodies();
        g.drawLine((int) bodies[0].getCenterPt().getX(), windowHeight - (int) bodies[0].getCenterPt().getY(),
                (int) bodies[1].getCenterPt().getX(), windowHeight - (int) bodies[1].getCenterPt().getY());
    }

    /*
      Post-condition: Returns the properties of the PSpring in a string where each property is stored in format { propertyType:propertyValue; } (excluding the curly brackets)
      @return Returns the properties of the PSpring in a string
    */
    @Override
    public String toString() {
        return super.toString() + "KValue:" + kValue;
    }
}
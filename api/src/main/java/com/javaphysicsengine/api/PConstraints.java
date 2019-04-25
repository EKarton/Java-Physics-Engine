/*
  Purpose: To represent the basic properties of all constraints attached to two bodies
  Original Creation Date: January 1 2016
  @author Emilio Kartono
  @version January 15 2016
*/

package com.javaphysicsengine.api;

import java.awt.*;

public abstract class PConstraints {
    protected double length = 0;
    private PBody[] bodies = new PBody[2];

    /*
       Pre-condition: "body1" and "body2" must not be null
       Post-condition: Creates a PConstraints object with "body1" and "body2" attached
       @param body1 The first body to be attached to.
       @param body2 The second body to be attached to.
    */
    public PConstraints(PBody body1, PBody body2) {
        bodies[0] = body1;
        bodies[1] = body2;
    }

    /*
       Post-condition: Returns the bodies that are attached to the constraint.
       @return Returns the bodies that are attached to the constraint.
    */
    public PBody[] getAttachedBodies() {
        return bodies;
    }

    /*
       Post-condition: Set the bodies that are attached to the constraint.
       Pre-condition: "body1" and "body2" should not be null.
       @param body1 The first body to be attached to.
       @param body2 The secod body to be attached to.
    */
    public void setAttachedBodies(PBody body1, PBody body2) {
        bodies[0] = body1;
        bodies[1] = body2;
    }

    /*
       Post-condition: Returns the length of the constraint
       @return Returns the length of the constraint
    */
    public double getLength() {
        return length;
    }

    /*
       Post-condition: Set the length of the string
       Pre-condition: "newLength" must be greater than 0
       @param newLength The new length
    */
    public void setLength(double newLength) {
        this.length = newLength;
    }

    /*
       Post-condition: Adds the tension forces to the attached bodies
    */
    public abstract void addTensionForce();

    /*
      Pre-condition: The "g" must not be null and the "windowHeight" must be greater than 0
      Post-condition: Draws a line between the two attached bodies
      @param g The Graphics Object
      @param windowHeight The height of the window that is containing the body being displayed
    */
    public abstract void drawConstraints(Graphics g, int windowHeight);

    /*
     Post-condition: Returns the properties of the constraints in a string where each property is stored in format { propertyType:propertyValue; } (excluding the curly brackets)
     @return Returns the properties of the constraints in a string
   */
    @Override
    public String toString() {
        return "BodiesAttached:[" + bodies[0].getName() + "][" + bodies[1].getName() + "];Length:" + length + ";";
    }
}
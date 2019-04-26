/*
 * Purpose: To generate API code from the bodies made in the editor
 * Original Creation Date: January 11 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.editor;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.api.body.PString;
import com.javaphysicsengine.utils.Vector;

import java.util.ArrayList;
import java.util.List;

public class PCodeGenerator {

    /**
     * Generates Api Java code.
     * It returns a list of lines of text.
     *
     * @param bodies the bodies made
     * @param constraints the constraints made
     * @return a list of lines
     */
    public List<String> generateApiCode(List<PBody> bodies, List<PConstraints> constraints) {

        // Create the "lines of code"
        List<String> codeLines = new ArrayList<>();

        // Create the java code for the importing libraries
        codeLines.add("import PhysicsEngine.*;");
        codeLines.add("import ToolBox.Vector;");

        // Create the java code to make the function (function that will handle creating the world and its bodies and constraints)
        codeLines.add("");
        codeLines.add("public void initialisePhysics()");
        codeLines.add("{");

        // Create the java code to create the world
        codeLines.add("    PWorld world = new PWorld();");
        codeLines.add("");

        // Create the java code for bodies
        for (PBody body : bodies) {
            String bodyName = body.getName().replaceAll(" ", "_");
            String bodyType = "PPolygon";
            if (body instanceof PCircle)
                bodyType = "PCircle";

            // Write the code for all the PBody objects
            codeLines.add("    " + bodyType + " " + bodyName + " = new " + bodyType + "(\"" + bodyName + "\")");
            codeLines.add("    " + bodyName + ".setMass(" + body.getMass() + ");");
            codeLines.add("    " + bodyName + ".setMoveable(" + body.isMoving() + ");");
            codeLines.add("    " + bodyName + ".setVelocity(" + body.getVelocity() + ");");
            codeLines.add("    " + bodyName + ".setOutlineColor(" + body.getOutlineColor() + ");");
            codeLines.add("    " + bodyName + ".setFillColor(" + body.getFillColor() + ");");

            // Adding lines of code special for PPolygon
            if (body instanceof PPolygon) {
                PPolygon bodyPoly = (PPolygon) body;

                // Add the vertices
                for (Vector vertices : bodyPoly.getVertices())
                    codeLines.add("    " + bodyName + ".getVertices().add(new Vector(" + vertices.toString() + "));");
                codeLines.add("    " + bodyName + ".computeCenterOfMass();");
            }

            // Adding lines of code special for PCircle
            else if (body instanceof PCircle) {
                PCircle bodyCircle = (PCircle) body;
                codeLines.add("    " + bodyName + ".setRadius(" + bodyCircle.getRadius() + ");");
                codeLines.add("    " + bodyName + ".setCenterPt(new Vector(" + bodyCircle.getCenterPt() + "));");
            }

            codeLines.add("    world.getBodies().add(" + bodyName + ");");

            // Add a space in between different objects
            codeLines.add("");
        }

        // Create the java code for the constraints
        for (PConstraints constraint : constraints) {
            String constraintName = "Constraint_" + (int) (Math.random() * (1000));
            String attachedBody1Name = constraint.getAttachedBodies()[0].getName();
            String attachedBody2Name = constraint.getAttachedBodies()[1].getName();
            String constraintType = "PSpring";
            if (constraint instanceof PString)
                constraintType = "PString";

            // Writing lines of code for all PConstraints
            codeLines.add("    " + constraintType + " " + constraintName + " = new " + constraintType + "(" + attachedBody1Name + ", " + attachedBody2Name + ")");


            // Writing lines of code for all PSprings
            if (constraint instanceof PSpring) {
                PSpring springConstraint = (PSpring) constraint;
                codeLines.add("    " + constraintName + ".setLengthOfSpring(new Vector(" + springConstraint.getLength() + "));");
                codeLines.add("    " + constraintName + ".setKValue(" + springConstraint.getKValue() + ");");
            }

            // Writing lines of code for all PStrings
            if (constraint instanceof PString) {
                PString stringConstraint = (PString) constraint;
                codeLines.add("    " + constraintName + ".setLengthOfSpring(new Vector(" + stringConstraint.getLength() + "));");
            }
            codeLines.add("    world.getConstraints().add(" + constraintName + ");");

            // Add a space in between different objects
            codeLines.add("");
        }

        // End the function call with the last curly braces
        codeLines.add("}");

        return codeLines;
    }
}

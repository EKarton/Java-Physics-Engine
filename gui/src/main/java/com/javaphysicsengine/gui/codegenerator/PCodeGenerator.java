/*
 * Purpose: To generate API code from the bodies made in the gui
 * Original Creation Date: January 11 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.gui.codegenerator;

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
        codeLines.add("import com.javaphysicsengine.api.*;");
        codeLines.add("import com.javaphysicsengine.utils.*;");

        // Create the java code to make the function (function that will handle creating the world and its bodies and constraints)
        codeLines.add("public class MainClass {");
        codeLines.add("\tpublic static void main(String[] args) {");

        // Create the java code to create the world
        codeLines.add("\t\tPWorld world = new PWorld();");
        codeLines.add("");

        // Create the java code for bodies
        for (PBody body : bodies) {
            String bodyName = body.getName().replaceAll(" ", "_");
            String bodyType = "PPolygon";
            if (body instanceof PCircle)
                bodyType = "PCircle";

            // Write the code for all the PBody objects
            codeLines.add("\t\t" + bodyType + " " + bodyName + " = new " + bodyType + "(\"" + bodyName + "\")");
            codeLines.add("\t\t" + bodyName + ".setMass(" + body.getMass() + ");");
            codeLines.add("\t\t" + bodyName + ".setMoveable(" + body.isMoving() + ");");
            codeLines.add("\t\t" + bodyName + ".setVelocity(" + body.getVelocity() + ");");
            codeLines.add("\t\t" + bodyName + ".setOutlineColor(" + body.getOutlineColor() + ");");
            codeLines.add("\t\t" + bodyName + ".setFillColor(" + body.getFillColor() + ");");

            // Adding lines of code special for PPolygon
            if (body instanceof PPolygon) {
                PPolygon bodyPoly = (PPolygon) body;

                // Add the vertices
                for (Vector vertices : bodyPoly.getVertices())
                    codeLines.add("\t\t" + bodyName + ".getVertices().add(new Vector(" + vertices.toString() + "));");
                codeLines.add("\t\t" + bodyName + ".computeCenterOfMass();");
            }

            // Adding lines of code special for PCircle
            else if (body instanceof PCircle) {
                PCircle bodyCircle = (PCircle) body;
                codeLines.add("\t\t" + bodyName + ".setRadius(" + bodyCircle.getRadius() + ");");
                codeLines.add("\t\t" + bodyName + ".setCenterPt(new Vector(" + bodyCircle.getCenterPt() + "));");
            }

            codeLines.add("\t\tworld.getBodies().add(" + bodyName + ");");

            // Add a space in between different objects
            codeLines.add("");
        }

        // Create the java code for the constraints
        for (PConstraints constraint : constraints) {
            String constraintName = "constraint_" + (int) (Math.random() * (1000));
            String attachedBody1Name = constraint.getAttachedBodies()[0].getName();
            String attachedBody2Name = constraint.getAttachedBodies()[1].getName();
            String constraintType = "PSpring";
            if (constraint instanceof PString)
                constraintType = "PString";

            // Writing lines of code for all PConstraints
            codeLines.add("\t\t" + constraintType + " " + constraintName + " = new " + constraintType + "(" + attachedBody1Name + ", " + attachedBody2Name + ")");


            // Writing lines of code for all PSprings
            if (constraint instanceof PSpring) {
                PSpring springConstraint = (PSpring) constraint;
                codeLines.add("\t\t" + constraintName + ".setLengthOfSpring(new Vector(" + springConstraint.getLength() + "));");
                codeLines.add("\t\t" + constraintName + ".setKValue(" + springConstraint.getKValue() + ");");
            }

            // Writing lines of code for all PStrings
            if (constraint instanceof PString) {
                PString stringConstraint = (PString) constraint;
                codeLines.add("\t\t" + constraintName + ".setLengthOfSpring(new Vector(" + stringConstraint.getLength() + "));");
            }
            codeLines.add("\t\tworld.getConstraints().add(" + constraintName + ");");

            // Add a space in between different objects
            codeLines.add("");
        }

        // End the function call with the last curly braces
        codeLines.add("\t}");
        codeLines.add("}");

        return codeLines;
    }
}

/*
 * Purpose: To store and modify all bodies and constraints made by the gui.
 *          It also stores temporary data for bodies and constraints that are currently being made.
 *
 * @author Emilio Kartono
 * @version April 26 2019
 */

package com.javaphysicsengine.gui.editor.store;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.utils.Vector;

import java.util.ArrayList;

public class PEditorStore {
    private ArrayList<Vector> polyVertices = new ArrayList<Vector>();
    private Vector circleCenterPt = new Vector(-1, -1); // <- If it is -1, it is not defined yet
    private double circleRadius = -1;
    private PBody selectedBody = null;
    private PBody attachedBody1 = null;  // Used for springs / strings

    // Arrays storing the bodies / constraints made by the user
    private ArrayList<PBody> createdBodies = new ArrayList<PBody>();
    private ArrayList<PConstraints> createdConstraints = new ArrayList<PConstraints>();

    public ArrayList<Vector> getPolyVertices() {
        return polyVertices;
    }

    public Vector getCircleCenterPt() {
        return circleCenterPt;
    }

    public double getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(double circleRadius) {
        this.circleRadius = circleRadius;
    }

    public PBody getSelectedBody() {
        return selectedBody;
    }

    public void setSelectedBody(PBody newSelectedBody) {
        if (!createdBodies.contains(newSelectedBody)) {
            throw new IllegalArgumentException("The body " + newSelectedBody + " does not exist in store!");
        }
        this.selectedBody = newSelectedBody;
    }

    public void clearSelectedBody() {
        this.selectedBody = null;
    }

    public PBody getAttachedBody1() {
        return attachedBody1;
    }

    public void setAttachedBody1(PBody newAttachedBody) {
        if (!createdBodies.contains(newAttachedBody)) {
            throw new IllegalArgumentException("The body " + newAttachedBody + " does not exist in store!");
        }
        this.attachedBody1 = newAttachedBody;
    }

    public void clearAttachedBody1() {
        this.attachedBody1 = null;
    }

    public ArrayList<PBody> getCreatedBodies() {
        return createdBodies;
    }

    public ArrayList<PConstraints> getCreatedConstraints() {
        return createdConstraints;
    }

    public void reset() {
        polyVertices.clear();
        circleCenterPt.setXY(-1, -1);
        circleRadius = -1;
        attachedBody1 = null;
        selectedBody = null;
    }

    public void addBody(PBody body) {
        for (PBody createdBody : createdBodies) {
            if (createdBody.getName().equals(body.getName())) {
                throw new IllegalArgumentException("Name " + body.getName() + " already exists!");
            }
        }
        createdBodies.add(body);
    }

    /*
      Post-condition: Add a constraint to the EditorPanel
      @param constraint The PContraints object
    */
    public void addConstraint(PConstraints constraint) {
        for (PBody body : constraint.getAttachedBodies()) {
            if (!createdBodies.contains(body)) {
                throw new IllegalArgumentException("The body " + body + " is not in store!");
            }
        }

        createdConstraints.add(constraint);
    }

    public void deleteBody(String objectName) {
        PBody body = getBodyFromName(objectName);
        if (body == null) {
            throw new IllegalArgumentException("Object name does not exist!");
        }

        // Search for any constraints attached to the body to be deleted. If there is, delete it
        for (int i = 0; i < createdConstraints.size(); i++) {
            PConstraints currentConstraint = createdConstraints.get(i);
            System.out.println("Has constraint");

            for (int j = 0; j < currentConstraint.getAttachedBodies().length; j++) {
                String nameOfAttachedBody = currentConstraint.getAttachedBodies()[j].getName();
                System.out.println("  AB: " + nameOfAttachedBody);
                if (nameOfAttachedBody.equals(objectName)) {
                    System.out.println("Found a constraint!");
                    createdConstraints.remove(i);
                    i--;
                    break;
                }
            }
        }

        // Remove the body from the arraylist of bodies
        createdBodies.remove(body);
    }

    /**
     * Clears all the created bodies and constraints.
     */
    public void clearBodies() {
        createdConstraints.clear();
        createdBodies.clear();
    }

    public void clearConstraints() {
        createdConstraints.clear();
    }

    /*
      Post-condition: Changes the name of a known PBody object
      Pre-condition: The "body" must not be null
      @param newName The new name of the body
      @param body The PBody that is wished to have its name changed
      @returns Returns true if the name was changed successfully; else false.
    */
    public void changeBodyName(String newName, PBody body) {
        for (PBody createdBody : createdBodies) {
            if (createdBody.getName().equals(newName)) {
                throw new IllegalArgumentException("Name already exists");
            }
        }

        body.setName(newName);
    }

    public PBody getBodyFromName(String bodyName) {
        for (PBody createdBody : createdBodies) {
            if (createdBody.getName().equals(bodyName)) {
                return createdBody;
            }
        }
        return null;
    }
}

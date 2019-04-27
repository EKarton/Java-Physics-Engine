package com.javaphysicsengine.editor.editor.store;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.api.body.PString;
import com.javaphysicsengine.utils.Vector;

import java.util.ArrayList;
import java.util.List;

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

    public void setSelectedBody(PBody selectedBody) {
        this.selectedBody = selectedBody;
    }

    public PBody getAttachedBody1() {
        return attachedBody1;
    }

    public void setAttachedBody1(PBody attachedBody1) {
        this.attachedBody1 = attachedBody1;
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

    public List<PBody> getCopiesOfBodies() {
        List<PBody> copyOfBodies = new ArrayList<>();
        for (PBody body : createdBodies) {
            PBody copiedBody = null;
            if (body instanceof PPolygon)
                copiedBody = new PPolygon((PPolygon) body);
            else if (body instanceof PCircle)
                copiedBody = new PCircle((PCircle) body);

            if (copiedBody != null)
                copyOfBodies.add(copiedBody);
        }
        return copyOfBodies;
    }

    /*
      Post-condition: Returns a copy of all the PConstraints objects created in this panel
      @return List of all copied PConstraints objects created in this panel
    */
    public List<PConstraints> getCopiesOfConstraints() {
        List<PConstraints> copyOfConstraints = new ArrayList<>();
        for (PConstraints constraint : createdConstraints) {
            PBody[] bodiesAttached_Copy = new PBody[2];

            // Making a copy of the bodies attached
            for (int i = 0; i < bodiesAttached_Copy.length; i++) {
                PBody bodyAttached = constraint.getAttachedBodies()[i];
                if (bodyAttached instanceof PPolygon)
                    bodiesAttached_Copy[i] = new PPolygon((PPolygon) bodyAttached);
                else if (bodyAttached instanceof PCircle)
                    bodiesAttached_Copy[i] = new PCircle((PCircle) bodyAttached);
            }

            // Making a copy of the constraints
            if (constraint instanceof PSpring) {
                PSpring springCopy = new PSpring(bodiesAttached_Copy[0], bodiesAttached_Copy[1]);
                springCopy.setKValue(((PSpring) constraint).getKValue());
                springCopy.setLength(constraint.getLength());
                copyOfConstraints.add(springCopy);
            } else if (constraint instanceof PString) {
                PString stringCopy = new PString(bodiesAttached_Copy[0], bodiesAttached_Copy[1]);
                stringCopy.setLength(constraint.getLength());
                copyOfConstraints.add(stringCopy);
            }
        }
        return copyOfConstraints;
    }

    public void addBody(PBody body) {
        createdBodies.add(body);
        sortBodyByName();
    }

    /*
      Post-condition: Add a constraint to the EditorPanel
      @param constraint The PContraints object
    */
    public void addConstraint(PConstraints constraint) {
        createdConstraints.add(constraint);
    }

    public boolean canDeleteBody(String objectName) {
        // Search for the index of the object with the object name
        int bodyIndex = getBodyIndexByName(objectName, createdBodies);

        // If there was a -1, then there is an error
        return bodyIndex != -1;
    }

    public void deleteBody(String objectName) {
        // Search for the index of the object with the object name
        int bodyIndex = this.getBodyIndexByName(objectName, createdBodies);

        // If there was a -1, then there is an error
        if (bodyIndex == -1)
            return;

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
        createdBodies.remove(bodyIndex);
    }

    public void clearBodies() {
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
        sortBodyByName();
    }

    public boolean canChangeBodyName(String newName, PBody body) {
        // Check if the body name already exists in the list of bodies
        int bodyFoundIndex = getBodyIndexByName(newName, createdBodies);

        // If the body was found
        return bodyFoundIndex == -1 || createdBodies.get(bodyFoundIndex).equals(body);
    }

    /*
      Post-condition: Sorts the createdBodies[] list in alphabethical order according to body name
    */
    public void sortBodyByName() {
        // Sort the bodies list by name in ascending order using insertion sort
        for (int i = 1; i < createdBodies.size(); i++) {
            PBody curBody = createdBodies.get(i);
            int j = i;
            while (j > 0 && createdBodies.get(j - 1).getName().compareTo(curBody.getName()) > 0) {
                createdBodies.set(j, createdBodies.get(j - 1));
                j--;
            }
            createdBodies.set(j, curBody);
        }
    }

    /*
      Post-condition: Returns the index in a list of bodies when found a body with the name name
      @param name The name of the body to search for
      @param bodies The list of bodies
      @return Returns the index of the body with the same name
    */
    public int getBodyIndexByName(String name, List<PBody> bodies) {
        // Do a binary search (already sorted by name in ascending order)
        int left = 0;
        int right = bodies.size() - 1;

        while (right >= left) {
            int mid = (left + right) / 2;
            PBody curBody = bodies.get(mid);

            if (curBody.getName().compareTo(name) < 0)
                left = mid + 1;
            else if (curBody.getName().compareTo(name) > 0)
                right = mid - 1;
            else
                return mid;
        }
        return -1;
    }
}

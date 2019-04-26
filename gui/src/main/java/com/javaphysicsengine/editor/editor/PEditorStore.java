package com.javaphysicsengine.editor.editor;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PConstraints;
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

    public void setPolyVertices(ArrayList<Vector> polyVertices) {
        this.polyVertices = polyVertices;
    }

    public Vector getCircleCenterPt() {
        return circleCenterPt;
    }

    public void setCircleCenterPt(Vector circleCenterPt) {
        this.circleCenterPt = circleCenterPt;
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

    public void setCreatedBodies(ArrayList<PBody> createdBodies) {
        this.createdBodies = createdBodies;
    }

    public ArrayList<PConstraints> getCreatedConstraints() {
        return createdConstraints;
    }

    public void setCreatedConstraints(ArrayList<PConstraints> createdConstraints) {
        this.createdConstraints = createdConstraints;
    }

    public void reset() {
        polyVertices.clear();
        circleCenterPt.setXY(-1, -1);
        circleRadius = -1;
        attachedBody1 = null;
        selectedBody = null;
    }

    /*
      Post-condition: Changes the name of a known PBody object
      Pre-condition: The "body" must not be null
      @param newName The new name of the body
      @param body The PBody that is wished to have its name changed
      @returns Returns true if the name was changed successfully; else false.
    */
    public void changeBodyName(String newName, PBody body) {
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

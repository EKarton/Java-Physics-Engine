package com.javaphysicsengine.editor.editor;

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
}

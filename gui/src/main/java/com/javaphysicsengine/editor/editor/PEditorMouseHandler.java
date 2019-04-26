package com.javaphysicsengine.editor.editor;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.api.body.PString;
import com.javaphysicsengine.utils.Vector;

import java.awt.event.MouseEvent;

import static com.javaphysicsengine.editor.editor.PEditorPanel.EDIT_MODE_CIRCLE;
import static com.javaphysicsengine.editor.editor.PEditorPanel.EDIT_MODE_CURSOR;
import static com.javaphysicsengine.editor.editor.PEditorPanel.EDIT_MODE_POLYGON;
import static com.javaphysicsengine.editor.editor.PEditorPanel.EDIT_MODE_SPRING;
import static com.javaphysicsengine.editor.editor.PEditorPanel.EDIT_MODE_STRING;

public class PEditorMouseHandler {

    public static final int SNAP_TOOL_POINT_RANGE = 4;

    private final PEditorStore store;
    private int mouseX = 0;
    private int mouseY = 0;
    private boolean isMouseSnappedToPoint = false;
    private String editMode;

    public PEditorMouseHandler(PEditorStore store, String defaultEditMode) {
        this.store = store;
        this.editMode = defaultEditMode;
    }

    public void mouseClicked(MouseEvent mouseEvent, int height) {
        // If it selected an object
        if (editMode.equals(EDIT_MODE_CURSOR) || editMode.equals(EDIT_MODE_SPRING) || editMode.equals(EDIT_MODE_STRING)) {
            store.setSelectedBody(null);
            if (isMouseSnappedToPoint)
                for (PBody body : store.getCreatedBodies())
                    if ((int) body.getCenterPt().getX() == mouseX && (int) (height - body.getCenterPt().getY()) == mouseY) {
                        store.setSelectedBody(body);
                        break;
                    }
        }

        if (editMode.equals(EDIT_MODE_SPRING) || editMode.equals(EDIT_MODE_STRING) && store.getSelectedBody() != null) {
            if (store.getAttachedBody1() == null) {
                store.setAttachedBody1(store.getSelectedBody());

            } else if (store.getSelectedBody() != null) {
                // Create the object
                PConstraints constraint = null;
                if (editMode.equals(EDIT_MODE_SPRING)) {
                    constraint = new PSpring(store.getAttachedBody1(), store.getSelectedBody());
                }
                else {
                    constraint = new PString(store.getAttachedBody1(), store.getSelectedBody());
                }

                store.getCreatedConstraints().add(constraint);
                store.setAttachedBody1(null);
                store.setSelectedBody(null);
            }

        } else if (editMode.equals(EDIT_MODE_CIRCLE)) {
            // If the center point is not defined yet, define it
            if (store.getCircleCenterPt().getX() == -1)
                store.getCircleCenterPt().setXY(mouseX, mouseY);

                // If the user selects the radius, create the circle object
            else {
                // Generate the circle name (the name must be unique from the other bodies)
                String circleName;
                do
                    circleName = "Circle " + (int) (Math.random() * (10000));
                while (store.getBodyIndexByName(circleName, store.getCreatedBodies()) != -1);

                PCircle circle = new PCircle(circleName);
                circle.setRadius(store.getCircleRadius());
                circle.setCenterPt(new Vector(store.getCircleCenterPt().getX(), height - store.getCircleCenterPt().getY()));

                store.addBody(circle);
                store.reset();
            }
        } else if (editMode.equals(EDIT_MODE_POLYGON)) {
            store.getPolyVertices().add(new Vector(mouseX, mouseY));

            // Check if it closed the polygon
            if (store.getPolyVertices().size() > 2)
                if (store.getPolyVertices().get(0).equals(store.getPolyVertices().get(store.getPolyVertices().size() - 1))) {
                    // Generate the body name (the body name must be unique from all the rest)
                    String polyName = "";
                    do
                        polyName = "Polygon " + (int) (Math.random() * (10000));
                    while (store.getBodyIndexByName(polyName, store.getCreatedBodies()) != -1);

                    // Create the body
                    PPolygon polygon = new PPolygon(polyName);
                    for (int i = 0; i < store.getPolyVertices().size() - 1; i++) // -1 since the last vertex is a copy of the first vertex
                    {
                        store.getPolyVertices().get(i).setY(height - store.getPolyVertices().get(i).getY());  // Translate the point since the origin for polygon is bottom left
                        polygon.getVertices().add(store.getPolyVertices().get(i));
                    }
                    polygon.computeCenterOfMass();

                    store.addBody(polygon);
                    store.reset();
                }
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {

    }

    public void mouseReleased(MouseEvent mouseEvent) {

    }

    public void mouseEntered(MouseEvent mouseEvent) {

    }

    public void mouseExited(MouseEvent mouseEvent) {

    }

    public void mouseDragged(MouseEvent mouseEvent, int height) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
        isMouseSnappedToPoint = false;

        if (store.getSelectedBody() != null && editMode.equals(EDIT_MODE_CURSOR))
            store.getSelectedBody().move(new Vector(mouseX, height - mouseY));
    }

    public void mouseMoved(MouseEvent mouseEvent, int height) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
        isMouseSnappedToPoint = false;

        // If the mouse is on a certain point on the polygon not made yet
        if (editMode.equals(EDIT_MODE_POLYGON))
            for (Vector pt : store.getPolyVertices())
                if (isMouseNearPoint(getMouseX(), getMouseY(), (int) pt.getX(), (int) pt.getY())) {
                    // Save the point it is snapped to
                    setMouseSnappedToPoint(true);

                    mouseX = (int) pt.getX();
                    mouseY = (int) pt.getY();
                    break;
                }

        // Check if it snapped to any of the made bodies
        if (!isMouseSnappedToPoint())
            for (PBody body : store.getCreatedBodies()) {
                // Check if it is on its center pt
                Vector bodyCenterPt = body.getCenterPt();
                if (isMouseNearPoint(getMouseX(), getMouseY(), (int) bodyCenterPt.getX(), (int) (height - bodyCenterPt.getY()))) {
                    // Save the point it is snapped to
                    setMouseSnappedToPoint(true);
                    mouseX = (int) bodyCenterPt.getX();
                    mouseY = (int) (height - bodyCenterPt.getY());
                    break;
                }
            }

        // If the mouse is adjusting the radius of the circle
        if (editMode.equals(EDIT_MODE_CIRCLE) && store.getCircleCenterPt().getX() != -1) {
            double xMinus = mouseX - store.getCircleCenterPt().getX();
            double yMinus = mouseY - store.getCircleCenterPt().getY();
            store.setCircleRadius(Math.sqrt((xMinus * xMinus) + (yMinus * yMinus)));
        }
    }

    /*
      Post-condition: Returns true if the mouse is within a range of 4 pixels of a specified point
      @param mouseX The x mouse coodinate
      @param mouseY The y mouse coordinate
      @param posX The x coordinate of a specified point
      @param posY The y coordinate of a specified point
      @return Returns true if the mouse is close to a specified point; else false
    */
    private boolean isMouseNearPoint(int mouseX, int mouseY, int posX, int posY) {
        double distToPoint = Math.pow(mouseX - posX, 2) + Math.pow(mouseY - posY, 2);
        return distToPoint < SNAP_TOOL_POINT_RANGE * SNAP_TOOL_POINT_RANGE;
    }

    public PEditorStore getStore() {
        return store;
    }

    public int getMouseX() {
        return mouseX;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public boolean isMouseSnappedToPoint() {
        return isMouseSnappedToPoint;
    }

    public void setMouseSnappedToPoint(boolean mouseSnappedToPoint) {
        isMouseSnappedToPoint = mouseSnappedToPoint;
    }

    public String getEditMode() {
        return editMode;
    }

    public void setEditMode(String editMode) {
        this.editMode = editMode;
    }
}

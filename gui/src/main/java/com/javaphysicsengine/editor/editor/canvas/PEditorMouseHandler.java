/*
 * Purpose: To handle all mouse interactions made from the PEditorPanel
 * @author Emilio Kartono
 * @version April 26 2019
 */

package com.javaphysicsengine.editor.editor.canvas;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.api.body.PString;
import com.javaphysicsengine.editor.editor.store.PEditorStore;
import com.javaphysicsengine.utils.Vector;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_CIRCLE;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_CURSOR;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_POLYGON;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_SPRING;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_STRING;

public class PEditorMouseHandler implements MouseMotionListener, MouseListener {

    private static final int SNAP_TOOL_POINT_RANGE = 4;

    private final PEditorStore store;
    private int mouseX = 0;
    private int mouseY = 0;
    private boolean isMouseSnappedToPoint = false;
    private String editMode;

    public PEditorMouseHandler(PEditorStore store, String defaultEditMode) {
        this.store = store;
        this.editMode = defaultEditMode;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int height = mouseEvent.getComponent().getHeight();

        // If it selected an object
        if (editMode.equals(EDIT_MODE_CURSOR) || editMode.equals(EDIT_MODE_SPRING) || editMode.equals(EDIT_MODE_STRING)) {
            store.setSelectedBody(null);
            if (isMouseSnappedToPoint) {
                for (PBody body : store.getCreatedBodies())
                    if ((int) body.getCenterPt().getX() == mouseX && (int) (height - body.getCenterPt().getY()) == mouseY) {
                        store.setSelectedBody(body);
                        break;
                    }
            }
        }

        if (editMode.equals(EDIT_MODE_SPRING) || editMode.equals(EDIT_MODE_STRING) && store.getSelectedBody() != null) {
            if (store.getAttachedBody1() == null) {
                store.setAttachedBody1(store.getSelectedBody());

            } else if (store.getSelectedBody() != null) {
                // Create the object
                PConstraints constraint;
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
                store.getCircleCenterPt().setXY(mouseX, height - mouseY);

                // If the user selects the radius, create the circle object
            else {
                // Generate the circle name (the name must be unique from the other bodies)
                String circleName;
                do
                    circleName = "Circle " + (int) (Math.random() * (10000));
                while (store.getBodyIndexByName(circleName, store.getCreatedBodies()) != -1);

                PCircle circle = new PCircle(circleName);
                circle.setRadius(store.getCircleRadius());
                circle.setCenterPt(new Vector(store.getCircleCenterPt().getX(), store.getCircleCenterPt().getY()));

                store.addBody(circle);
                store.reset();
            }
        } else if (editMode.equals(EDIT_MODE_POLYGON)) {

            boolean isClosingPolygon = store.getPolyVertices().size() > 0 && store.getPolyVertices().get(0).equals(new Vector(mouseX, height - mouseY));
            boolean isClosingPolygonValid = store.getPolyVertices().size() >= 2;

            // See if the mouse is closing the polygon
            if (isClosingPolygon && isClosingPolygonValid) {

                // Generate the body name (the body name must be unique from all the rest)
                String polyName;
                do
                    polyName = "Polygon " + (int) (Math.random() * (10000));
                while (store.getBodyIndexByName(polyName, store.getCreatedBodies()) != -1);

                // Create the body
                PPolygon polygon = new PPolygon(polyName);
                polygon.getVertices().addAll(store.getPolyVertices());
                polygon.computeCenterOfMass();

                store.addBody(polygon);
                store.reset();
            } else {
                store.getPolyVertices().add(new Vector(mouseX, height - mouseY));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        int height = mouseEvent.getComponent().getHeight();

        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
        isMouseSnappedToPoint = false;

        if (editMode.equals(EDIT_MODE_CURSOR)) {
            if (store.getSelectedBody() != null) {
                store.getSelectedBody().move(new Vector(mouseX, height - mouseY));
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        int height = mouseEvent.getComponent().getHeight();

        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
        isMouseSnappedToPoint = false;

        // Snap to an edge in WIP Polygon if it is creating a polygon
        if (editMode.equals(EDIT_MODE_POLYGON)) {
            for (Vector pt : store.getPolyVertices()) {
                if (areTwoPointsWithinSnapRange(mouseX, mouseY, (int) pt.getX(), (int) (height - pt.getY()))) {
                    setMouseSnappedToPoint(true);
                    mouseX = (int) pt.getX();
                    mouseY = (int) (height - pt.getY());
                    break;
                }
            }
        } else if (editMode.equals(EDIT_MODE_CIRCLE)) {

            // If the mouse is adjusting the radius of the circle
            if (store.getCircleCenterPt().getX() != -1) {
                double xMinus = mouseX - store.getCircleCenterPt().getX();
                double yMinus = (height - mouseY) - store.getCircleCenterPt().getY();
                store.setCircleRadius(Math.sqrt((xMinus * xMinus) + (yMinus * yMinus)));
            }
        }

        // Check if it snapped to any of the made bodies
        if (!isMouseSnappedToPoint()) {
            for (PBody body : store.getCreatedBodies()) {
                // Check if it is on its center pt
                Vector bodyCenterPt = body.getCenterPt();
                if (areTwoPointsWithinSnapRange(getMouseX(), getMouseY(), (int) bodyCenterPt.getX(), (int) (height - bodyCenterPt.getY()))) {
                    // Save the point it is snapped to
                    setMouseSnappedToPoint(true);
                    mouseX = (int) bodyCenterPt.getX();
                    mouseY = (int) (height - bodyCenterPt.getY());
                    break;
                }
            }
        }
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isMouseSnappedToPoint() {
        return isMouseSnappedToPoint;
    }

    public void setMouseSnappedToPoint(boolean mouseSnappedToPoint) {
        isMouseSnappedToPoint = mouseSnappedToPoint;
    }

    public void setEditMode(String editMode) {
        this.editMode = editMode;
    }

    /*
      Post-condition: Returns true if a point is within a range of 4 pixels of another point
      @param x1 The x mouse coodinate
      @param y1 The y mouse coordinate
      @param x2 The x coordinate of a specified point
      @param y2 The y coordinate of a specified point
      @return Returns true if the mouse is close to a specified point; else false
    */
    private boolean areTwoPointsWithinSnapRange(int x1, int y1, int x2, int y2) {
        double distToPoint = Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
        return distToPoint < SNAP_TOOL_POINT_RANGE * SNAP_TOOL_POINT_RANGE;
    }
}

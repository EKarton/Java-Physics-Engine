package com.javaphysicsengine.editor.editor.canvas;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.editor.editor.store.PEditorStore;
import com.javaphysicsengine.utils.Vector;
import org.junit.Test;

import java.awt.*;
import java.awt.event.MouseEvent;

import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_CIRCLE;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_CURSOR;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_POLYGON;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_SPRING;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_STRING;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class PEditorMouseHandlerTest {

    @Test
    public void mouseClicked_when_editmode_is_cursor_and_clicks_on_centerpt_of_circle_should_set_circle_as_selected_body() {
        PCircle circle = new PCircle("My circle");
        circle.setCenterPt(new Vector(10, 10));
        circle.setRadius(10);

        PEditorStore store = spy(new PEditorStore());
        store.addBody(circle);

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(10).when(mouseEvent).getX();
        doReturn(490).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CURSOR);

        // Snap the point to the circle
        handler.mouseMoved(mouseEvent);

        handler.mouseClicked(mouseEvent);

        assertEquals(circle, store.getSelectedBody());
    }

    @Test
    public void mouseClicked_should_set_spring_to_circle_when_adding_a_spring_and_selected_body_is_circle() {
        PCircle circle = new PCircle("My circle");
        circle.setCenterPt(new Vector(10, 10));
        circle.setRadius(10);

        PEditorStore store = spy(new PEditorStore());
        store.addBody(circle);

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(10).when(mouseEvent).getX();
        doReturn(490).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_SPRING);

        // Snap the point to the circle
        handler.mouseMoved(mouseEvent);

        handler.mouseClicked(mouseEvent);

        assertEquals(circle, store.getSelectedBody());
        assertEquals(circle, store.getAttachedBody1());
        assertEquals(0, store.getCreatedConstraints().size());
    }

    @Test
    public void mouseClicked_should_create_spring_to_two_circles_when_attachedBody1_is_one_circle_and_selected_body_is_another_circle() {
        PCircle circle1 = new PCircle("My first circle");
        circle1.setCenterPt(new Vector(10, 10));
        circle1.setRadius(10);

        PCircle circle2 = new PCircle("My second circle");
        circle2.setCenterPt(new Vector(10, 10));
        circle2.setRadius(10);

        PEditorStore store = spy(new PEditorStore());
        store.addBody(circle1);
        store.addBody(circle2);
        store.setAttachedBody1(circle1);
        store.setSelectedBody(circle2);

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(10).when(mouseEvent).getX();
        doReturn(490).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_SPRING);

        // Move mouse to center of circle 2
        handler.mouseMoved(mouseEvent);

        handler.mouseClicked(mouseEvent);

        assertEquals(1, store.getCreatedConstraints().size());
        assertNull(store.getSelectedBody());
        assertNull(store.getAttachedBody1());
    }

    @Test
    public void mouseClicked_should_set_string_to_circle_when_adding_a_spring_and_selected_body_is_circle() {
        PCircle circle = new PCircle("My circle");
        circle.setCenterPt(new Vector(10, 10));
        circle.setRadius(10);

        PEditorStore store = spy(new PEditorStore());
        store.addBody(circle);

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(10).when(mouseEvent).getX();
        doReturn(490).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_STRING);

        // Snap the point to the circle
        handler.mouseMoved(mouseEvent);

        handler.mouseClicked(mouseEvent);

        assertEquals(circle, store.getSelectedBody());
        assertEquals(circle, store.getAttachedBody1());
        assertEquals(0, store.getCreatedConstraints().size());
    }

    @Test
    public void mouseClicked_should_create_string_to_two_circles_when_attachedBody1_is_one_circle_and_selected_body_is_another_circle() {
        PCircle circle1 = new PCircle("My first circle");
        circle1.setCenterPt(new Vector(10, 10));
        circle1.setRadius(10);

        PCircle circle2 = new PCircle("My second circle");
        circle2.setCenterPt(new Vector(10, 10));
        circle2.setRadius(10);

        PEditorStore store = spy(new PEditorStore());
        store.addBody(circle1);
        store.addBody(circle2);
        store.setAttachedBody1(circle1);
        store.setSelectedBody(circle2);

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(10).when(mouseEvent).getX();
        doReturn(490).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_STRING);

        // Move mouse to center of circle 2
        handler.mouseMoved(mouseEvent);

        handler.mouseClicked(mouseEvent);

        assertEquals(1, store.getCreatedConstraints().size());
        assertNull(store.getSelectedBody());
        assertNull(store.getAttachedBody1());
    }

    @Test
    public void mouseClicked_should_set_center_point_of_circle_when_creating_a_circle_and_center_point_not_defined() {
        PEditorStore store = spy(new PEditorStore());

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(10).when(mouseEvent).getX();
        doReturn(400).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CIRCLE);

        // Note that this is needed to save the mouseX and mouseY in the handler.
        handler.mouseMoved(mouseEvent);

        handler.mouseClicked(mouseEvent);

        assertEquals(10, store.getCircleCenterPt().getX(), 0.00001);
        assertEquals(100, store.getCircleCenterPt().getY(), 0.00001);
    }

    @Test
    public void mouseClicked_should_create_circle_when_creating_a_circle_and_center_point_already_defined() {
        PEditorStore store = spy(new PEditorStore());
        store.getCircleCenterPt().setXY(10, 10);

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(10).when(mouseEvent).getX();
        doReturn(400).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CIRCLE);

        // Note that this is needed to save the mouseX and mouseY in the handler.
        handler.mouseMoved(mouseEvent);

        handler.mouseClicked(mouseEvent);

        assertEquals(1, store.getCreatedBodies().size());

        PBody createdBody = store.getCreatedBodies().get(0);
        assertTrue(createdBody instanceof PCircle);

        PCircle createdCircle = (PCircle) createdBody;
        assertEquals(new Vector(10, 10), createdCircle.getCenterPt());
        assertEquals(90, createdCircle.getRadius(), 0.0001);

        verify(store, times(1)).reset();
    }

    @Test
    public void mouseClicked_should_add_mouse_coords_as_vertex_when_creating_polygon_without_closing_polygon() {
        PEditorStore store = spy(new PEditorStore());

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(10).when(mouseEvent).getX();
        doReturn(400).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_POLYGON);

        // Note that this is needed to save the mouseX and mouseY in the handler.
        handler.mouseMoved(mouseEvent);

        handler.mouseClicked(mouseEvent);

        assertEquals(10, store.getPolyVertices().get(0).getX(), 0.0000001);
        assertEquals(100, store.getPolyVertices().get(0).getY(), 0.0000001);
    }

    @Test
    public void mouseClicked_should_create_polygon_when_clicked_on_polygon_vertex_while_closing_vertex() {
        PEditorStore store = spy(new PEditorStore());
        store.getPolyVertices().add(new Vector(0, 0));
        store.getPolyVertices().add(new Vector(10, 0));
        store.getPolyVertices().add(new Vector(10, 10));
        store.getPolyVertices().add(new Vector(0, 10));

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(0).when(mouseEvent).getX();
        doReturn(500).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_POLYGON);

        // Note that this is needed to save the mouseX and mouseY in the handler.
        handler.mouseMoved(mouseEvent);

        handler.mouseClicked(mouseEvent);

        // Verify that only one body is made
        assertEquals(1, store.getCreatedBodies().size());

        PBody createdBody = store.getCreatedBodies().get(0);
        assertTrue(createdBody instanceof PPolygon);

        // Verify the center point of the polygon
        PPolygon createdPolygon = (PPolygon) createdBody;
        assertEquals(new Vector(5, 5), createdPolygon.getCenterPt());

        // Verify the vertices of the polygon
        assertEquals(4, createdPolygon.getVertices().size(), 0.0001);
        assertEquals(new Vector(0, 0), createdPolygon.getVertices().get(0));
        assertEquals(new Vector(10, 0), createdPolygon.getVertices().get(1));
        assertEquals(new Vector(10, 10), createdPolygon.getVertices().get(2));
        assertEquals(new Vector(0, 10), createdPolygon.getVertices().get(3));

        verify(store, times(1)).reset();
    }

    @Test
    public void mousePressed_should_not_do_anything_to_store() {
        PEditorStore store = spy(new PEditorStore());

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(200).when(mouseEvent).getX();
        doReturn(400).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CURSOR);

        handler.mousePressed(mouseEvent);

        verifyZeroInteractions(store);
    }

    @Test
    public void mouseReleased_should_not_do_anything_to_store() {
        PEditorStore store = spy(new PEditorStore());

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(200).when(mouseEvent).getX();
        doReturn(400).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CURSOR);

        handler.mouseReleased(mouseEvent);

        verifyZeroInteractions(store);
    }

    @Test
    public void mouseEntered_should_not_do_anything_to_store() {
        PEditorStore store = spy(new PEditorStore());

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(200).when(mouseEvent).getX();
        doReturn(400).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CURSOR);

        handler.mouseEntered(mouseEvent);

        verifyZeroInteractions(store);
    }

    @Test
    public void mouseExited_should_not_do_anything_to_store() {
        PEditorStore store = spy(new PEditorStore());

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(200).when(mouseEvent).getX();
        doReturn(400).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CURSOR);

        handler.mouseExited(mouseEvent);

        verifyZeroInteractions(store);
    }

    @Test
    public void mouseDragged_should_move_circle_when_selected_object_is_circle() {
        PCircle circle = new PCircle("My circle");
        circle.setCenterPt(new Vector(10, 10));
        circle.setRadius(10);

        PEditorStore store = spy(new PEditorStore());
        store.addBody(circle);
        store.setSelectedBody(circle);

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(200).when(mouseEvent).getX();
        doReturn(400).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CURSOR);

        // Snap the point to the circle
        handler.mouseDragged(mouseEvent);

        assertEquals(200, circle.getCenterPt().getX(), 0.00001);
        assertEquals(100, circle.getCenterPt().getY(), 0.00001);
    }

    @Test
    public void mouseDragged_should_not_move_any_object_when_no_object_is_selected() {
        PCircle circle = new PCircle("My circle");
        circle.setCenterPt(new Vector(10, 10));
        circle.setRadius(10);

        PEditorStore store = spy(new PEditorStore());
        store.addBody(circle);

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(200).when(mouseEvent).getX();
        doReturn(400).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CURSOR);

        // Snap the point to the circle
        handler.mouseDragged(mouseEvent);

        assertEquals(10, circle.getCenterPt().getX(), 0.00001);
        assertEquals(10, circle.getCenterPt().getY(), 0.00001);
    }

    @Test
    public void mouseMoved_should_snap_to_center_of_circle_when_mouse_near_center_point() {
        PCircle circle = new PCircle("My circle");
        circle.setCenterPt(new Vector(200, 200));
        circle.setRadius(10);

        PEditorStore store = spy(new PEditorStore());
        store.addBody(circle);

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(202).when(mouseEvent).getX();
        doReturn(500 - 202).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CURSOR);

        // Snap the point to the circle
        handler.mouseMoved(mouseEvent);

        assertEquals(200, handler.getMouseX());
        assertEquals(500 - 200, handler.getMouseY());
    }

    @Test
    public void mouseMoved_should_snap_to_polygon_vertex_when_mouse_near_wip_polygon_vertex() {
        PEditorStore store = spy(new PEditorStore());
        store.getPolyVertices().add(new Vector(0, 0));
        store.getPolyVertices().add(new Vector(10, 0));
        store.getPolyVertices().add(new Vector(10, 10));
        store.getPolyVertices().add(new Vector(0, 10));

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(11).when(mouseEvent).getX();
        doReturn(500 - 11).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_POLYGON);

        // Snap the point to the circle
        handler.mouseMoved(mouseEvent);

        assertEquals(10, handler.getMouseX());
        assertEquals(500 - 10, handler.getMouseY());
        assertTrue(handler.isMouseSnappedToPoint());
    }

    @Test
    public void mouseMoved_should_not_snap_to_polygon_vertex_when_mouse_is_far_from_wip_polygon_vertex() {
        PEditorStore store = spy(new PEditorStore());
        store.getPolyVertices().add(new Vector(0, 0));
        store.getPolyVertices().add(new Vector(10, 0));
        store.getPolyVertices().add(new Vector(10, 10));
        store.getPolyVertices().add(new Vector(0, 10));

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(20).when(mouseEvent).getX();
        doReturn(500 - 20).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_POLYGON);

        // Snap the point to the circle
        handler.mouseMoved(mouseEvent);

        assertEquals(20, handler.getMouseX());
        assertEquals(500 - 20, handler.getMouseY());
        assertFalse(handler.isMouseSnappedToPoint());
    }

    @Test
    public void mouseMoved_should_not_snap_to_center_of_circle_when_mouse_is_far_from_center_point() {
        PCircle circle = new PCircle("My circle");
        circle.setCenterPt(new Vector(200, 200));
        circle.setRadius(10);

        PEditorStore store = spy(new PEditorStore());
        store.addBody(circle);

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(100).when(mouseEvent).getX();
        doReturn(302).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CURSOR);

        // Snap the point to the circle
        handler.mouseMoved(mouseEvent);

        assertEquals(100, handler.getMouseX());
        assertEquals(302, handler.getMouseY());
        assertFalse(handler.isMouseSnappedToPoint());
    }

    @Test
    public void mouseMoved_should_extend_radius_when_creating_a_circle() {
        PEditorStore store = spy(new PEditorStore());
        store.getCircleCenterPt().setXY(10, 100);

        Component component = mock(Component.class);
        doReturn(500).when(component).getHeight();

        MouseEvent mouseEvent = mock(MouseEvent.class);
        doReturn(component).when(mouseEvent).getComponent();
        doReturn(10).when(mouseEvent).getX();
        doReturn(300).when(mouseEvent).getY();

        PEditorMouseHandler handler = new PEditorMouseHandler(store, EDIT_MODE_CIRCLE);

        // Snap the point to the circle
        handler.mouseMoved(mouseEvent);

        assertEquals(10, handler.getMouseX());
        assertEquals(300, handler.getMouseY());
        assertFalse(handler.isMouseSnappedToPoint());
        assertEquals(100, store.getCircleRadius(), 0.0000001);
    }
}
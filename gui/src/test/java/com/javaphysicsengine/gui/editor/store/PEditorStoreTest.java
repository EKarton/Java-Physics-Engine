package com.javaphysicsengine.gui.editor.store;

import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.api.body.PString;
import com.javaphysicsengine.utils.Vector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PEditorStoreTest {

    private PEditorStore store;

    @Before
    public void setup() {
        store = new PEditorStore();
    }

    @Test
    public void getPolyVertices() {
        assertEquals(0, store.getPolyVertices().size());
    }

    @Test
    public void getCircleCenterPt_should_return_center_point_set_in_store() {
        store.getCircleCenterPt().setXY(10, 20);
        assertEquals(Vector.of(10, 20), store.getCircleCenterPt());
    }

    @Test
    public void getCircleRadius_should_return_radius_set_in_store() {
        store.setCircleRadius(10);
        assertEquals(10, store.getCircleRadius(), 0.00001);
    }

    @Test
    public void getSelectedBody_should_return_object_set_to_body_in_store() {
        PCircle circle = new PCircle("Circle 1");
        circle.setCenterPt(Vector.of(10, 20));
        circle.setRadius(10);

        store.addBody(circle);
        store.setSelectedBody(circle);

        assertSame(store.getSelectedBody(), circle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setSelectedBody_should_throw_exception_when_set_to_body_not_in_store() {
        PCircle circle = new PCircle("Circle 1");
        circle.setCenterPt(Vector.of(10, 20));
        circle.setRadius(10);

        store.setSelectedBody(circle);
    }

    @Test
    public void getAttachedBody1_should_return_object_set_as_attachedBody_to_store() {
        PCircle circle = new PCircle("Circle 1");
        circle.setCenterPt(Vector.of(10, 20));
        circle.setRadius(10);

        store.addBody(circle);
        store.setAttachedBody1(circle);

        assertSame(store.getAttachedBody1(), circle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setAttachedBody1_should_throw_exception_when_set_to_body_not_in_store() {
        PCircle circle = new PCircle("Circle 1");
        circle.setCenterPt(Vector.of(10, 20));
        circle.setRadius(10);

        store.setAttachedBody1(circle);
    }

    @Test
    public void getCreatedBodies_should_return_bodies_added_to_store() {
        // Add created objects to the store
        PCircle createdCircle1 = new PCircle("Circle 1");
        createdCircle1.setCenterPt(Vector.of(10, 20));
        createdCircle1.setRadius(10);

        PCircle createdCircle2 = new PCircle("Circle 2");
        createdCircle2.setCenterPt(Vector.of(30, 40));
        createdCircle2.setRadius(20);

        store.addBody(createdCircle1);
        store.addBody(createdCircle2);

        assertEquals(2, store.getCreatedBodies().size());
        assertSame(store.getCreatedBodies().get(0), createdCircle1);
        assertSame(store.getCreatedBodies().get(1), createdCircle2);
    }

    @Test
    public void getCreatedConstraints() {
        // Add created objects to the store
        PCircle createdCircle1 = new PCircle("Circle 1");
        createdCircle1.setCenterPt(Vector.of(10, 20));
        createdCircle1.setRadius(10);

        PCircle createdCircle2 = new PCircle("Circle 2");
        createdCircle2.setCenterPt(Vector.of(30, 40));
        createdCircle2.setRadius(20);

        PConstraints createdSpring = new PSpring(createdCircle1, createdCircle2);

        store.addBody(createdCircle1);
        store.addBody(createdCircle2);
        store.addConstraint(createdSpring);

        assertEquals(1, store.getCreatedConstraints().size());
        assertSame(store.getCreatedConstraints().get(0), createdSpring);
    }

    @Test
    public void reset_should_remove_all_wip_objects_except_for_created_bodies_and_constraints() {
        // Add created objects to the store
        PCircle createdCircle1 = new PCircle("Circle 1");
        createdCircle1.setCenterPt(Vector.of(10, 20));
        createdCircle1.setRadius(10);

        PCircle createdCircle2 = new PCircle("Circle 2");
        createdCircle2.setCenterPt(Vector.of(30, 40));
        createdCircle2.setRadius(20);

        PConstraints createdSpring = new PSpring(createdCircle1, createdCircle2);

        store.addBody(createdCircle1);
        store.addBody(createdCircle2);
        store.addConstraint(createdSpring);

        // Add temp variables to the store
        store.getPolyVertices().add(Vector.of(10, 20));
        store.getCircleCenterPt().setXY(30, 40);
        store.setCircleRadius(10);
        store.setSelectedBody(createdCircle1);
        store.setAttachedBody1(createdCircle2);

        store.reset();

        // Verify that the wip variables are deleted
        assertEquals(0, store.getPolyVertices().size());
        assertEquals(store.getCircleCenterPt(), Vector.of(-1, -1));
        assertNull(store.getAttachedBody1());
        assertNull(store.getSelectedBody());
        assertEquals(store.getCircleRadius(), -1, 0.0);

        // Verify that the created objects still remain
        assertEquals(2, store.getCreatedBodies().size());
        assertSame(store.getCreatedBodies().get(0), createdCircle1);
        assertSame(store.getCreatedBodies().get(1), createdCircle2);
        assertEquals(1, store.getCreatedConstraints().size());
        assertSame(store.getCreatedConstraints().get(0), createdSpring);
    }

    @Test
    public void addBody_should_add_body_to_store_when_adding_body_with_unused_name() {
        PCircle circle1 = new PCircle("Bob");
        PCircle circle2 = new PCircle("Smith");
        store.addBody(circle1);
        store.addBody(circle2);

        assertTrue(store.getCreatedBodies().contains(circle1));
        assertTrue(store.getCreatedBodies().contains(circle2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBody_should_throw_exception_when_adding_body_with_used_name() {
        PCircle circle1 = new PCircle("Bob");
        PCircle circle2 = new PCircle("Bob");
        store.addBody(circle1);
        store.addBody(circle2);
    }

    @Test
    public void addConstraint_should_allow_adding_constraint_to_bodies_already_in_store() {
        PCircle circle1 = new PCircle("Bob");
        PCircle circle2 = new PCircle("Smith");
        PConstraints constraint = new PSpring(circle1, circle2);
        store.addBody(circle1);
        store.addBody(circle2);
        store.addConstraint(constraint);

        assertTrue(store.getCreatedConstraints().contains(constraint));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addConstraint_should_not_allow_adding_constraint_to_bodies_not_in_store() {
        PCircle circle1 = new PCircle("Bob");
        PCircle circle2 = new PCircle("Smith");
        PConstraints constraint = new PSpring(circle1, circle2);
        store.addBody(circle1);
        store.addConstraint(constraint);
    }

    @Test
    public void deleteBody_should_remove_body_and_its_constraint() {
        PCircle circle1 = new PCircle("Bob");
        PCircle circle2 = new PCircle("Smith");
        PCircle circle3 = new PCircle("Joe");
        PConstraints constraints1 = new PString(circle1, circle2);
        PConstraints constraints2 = new PString(circle1, circle3);

        store.addBody(circle1);
        store.addBody(circle2);
        store.addBody(circle3);
        store.addConstraint(constraints1);
        store.addConstraint(constraints2);

        store.deleteBody("Bob");

        assertFalse(store.getCreatedBodies().contains(circle1));
        assertTrue(store.getCreatedBodies().contains(circle2));
        assertTrue(store.getCreatedBodies().contains(circle3));
        assertFalse(store.getCreatedConstraints().contains(constraints1));
        assertFalse(store.getCreatedConstraints().contains(constraints2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteBody_should_throw_error_when_deleting_body_not_in_store() {
        PCircle circle1 = new PCircle("Bob");
        PCircle circle2 = new PCircle("Smith");

        store.addBody(circle1);
        store.addBody(circle2);

        store.deleteBody("Joe");
    }

    @Test
    public void clearBodies_should_remove_all_bodies_and_constraints() {
        PCircle circle1 = new PCircle("Bob");
        PCircle circle2 = new PCircle("Smith");
        PConstraints constraints = new PString(circle1, circle2);
        store.addBody(circle1);
        store.addBody(circle2);
        store.addConstraint(constraints);

        store.clearBodies();

        assertEquals(0, store.getCreatedBodies().size());
        assertEquals(0, store.getCreatedConstraints().size());
    }

    @Test
    public void clearConstraints_should_remove_all_constraints_but_leave_bodies_alone() {
        PCircle circle1 = new PCircle("Bob");
        PCircle circle2 = new PCircle("Smith");
        PConstraints constraint = new PSpring(circle1, circle2);
        store.addBody(circle1);
        store.addBody(circle2);
        store.addConstraint(constraint);

        store.clearConstraints();

        assertTrue(store.getCreatedBodies().contains(circle1));
        assertTrue(store.getCreatedBodies().contains(circle2));
        assertFalse(store.getCreatedConstraints().contains(constraint));
    }

    @Test
    public void changeBodyName_should_change_name_of_body_when_new_name_does_not_exist_in_store() {
        PCircle circle = new PCircle("Bob");
        store.addBody(circle);
        store.changeBodyName("Sam", circle);

        assertEquals("Sam", circle.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeBodyName_should_throw_exception_when_new_name_already_exists_in_store() {
        PCircle circle1 = new PCircle("Bob");
        PCircle circle2 = new PCircle("Sam");

        store.addBody(circle1);
        store.addBody(circle2);
        store.changeBodyName("Sam", circle1);
    }

    @Test
    public void getBodyFromName_should_retrieve_body_when_name_is_in_store() {
        PCircle circle1 = new PCircle("Bob");
        PCircle circle2 = new PCircle("Sam");

        store.addBody(circle1);
        store.addBody(circle2);
        assertSame(circle1, store.getBodyFromName("Bob"));
    }

    @Test
    public void getBodyFromName_should_return_null_when_name_is_not_in_store() {
        PCircle circle1 = new PCircle("Bob");
        PCircle circle2 = new PCircle("Sam");

        store.addBody(circle1);
        store.addBody(circle2);
        assertNull(store.getBodyFromName("Joe"));
    }
}
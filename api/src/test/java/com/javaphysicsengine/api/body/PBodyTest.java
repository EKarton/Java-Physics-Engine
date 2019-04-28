package com.javaphysicsengine.api.body;

import com.javaphysicsengine.utils.Vector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PBodyTest {

    private PBody pBody;

    @Before
    public void setup() {
        pBody = new PBody("My body") {
            @Override
            public void move(Vector newCenterPt) { }

            @Override
            public void rotate(double newAngle) { }

            @Override
            public void translate(Vector displacement) { }
        };
    }

    @Test
    public void constructor_should_return_default_values() {
        assertEquals(1, pBody.getMass(), 0.000001);
        assertEquals("My body", pBody.getName());
        assertEquals(new Vector(0, 0), pBody.getCenterPt());
        assertEquals(new Vector(0, 0), pBody.getNetForce());
        assertEquals(new Vector(0, 0), pBody.getVelocity());
        assertEquals(0, pBody.getAngle(), 0.000001);
        assertTrue(pBody.isMoving());
    }

    @Test
    public void constructor_should_hard_copy_body_when_given_another_body() {
        PBody anotherBody = new PBody("My other body") {
            @Override
            public void move(Vector newCenterPt) { }

            @Override
            public void rotate(double newAngle) { }

            @Override
            public void translate(Vector displacement) { }
        };
        anotherBody.setMass(1000);
        anotherBody.setCenterPt(new Vector(10, 10));
        anotherBody.setNetForce(new Vector(100, 100));
        anotherBody.setVelocity(new Vector(1, 1));
        anotherBody.setAngle(10);
        anotherBody.setMoveable(false);

        pBody = new PBody(anotherBody) {
            @Override
            public void move(Vector newCenterPt) { }

            @Override
            public void rotate(double newAngle) { }

            @Override
            public void translate(Vector displacement) { }
        };

        assertEquals(1000, pBody.getMass(), 0.000001);
        assertEquals("My other body", pBody.getName());
        assertEquals(new Vector(10, 10), pBody.getCenterPt());
        assertEquals(new Vector(100, 100), pBody.getNetForce());
        assertEquals(new Vector(1, 1), pBody.getVelocity());
        assertEquals(10, pBody.getAngle(), 0.000001);
        assertFalse(pBody.isMoving());
    }

    @Test
    public void getMass_should_return_1000_when_mass_set_to_1000() {
        pBody.setMass(1000);
        assertEquals(1000, pBody.getMass(), 0.000001);
    }

    @Test
    public void getName_should_return_Bob_when_name_set_to_Bob() {
        pBody.setName("Bob");
        assertEquals("Bob", pBody.getName());
    }

    @Test
    public void getCenterPt_should_return_1_1_when_center_pt_set_to_1_1() {
        pBody.setCenterPt(new Vector(1, 1));
        assertEquals(new Vector(1, 1), pBody.getCenterPt());
    }

    @Test
    public void getNetForce_should_return_1_1_when_net_force_set_to_1_1() {
        pBody.setNetForce(new Vector(1, 1));
        assertEquals(new Vector(1, 1), pBody.getNetForce());
    }

    @Test
    public void getVelocity_should_return_1_1_when_velocity_set_to_1_1() {
        pBody.setVelocity(new Vector(1, 1));
        assertEquals(new Vector(1, 1), pBody.getVelocity());
    }

    @Test
    public void getAngle_should_return_45_when_angle_set_to_45() {
        pBody.setAngle(45);
        assertEquals(45, pBody.getAngle(), 0.000001);
    }

    @Test
    public void isMoving_should_return_false_when_set_to_static_object() {
        pBody.setMoveable(false);
        assertFalse(pBody.isMoving());
    }

    @Test
    public void toString1() {
        assertTrue(pBody.toString().length() > 0);
    }
}
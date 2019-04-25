package com.javaphysicsengine.api.body;

import com.javaphysicsengine.utils.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PBodyTest {

    private PBody pBody;

    @BeforeEach
    void setup() {
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
    void constructor_should_return_default_values() {
        assertEquals(1, pBody.getMass());
        assertEquals("My body", pBody.getName());
        assertEquals(new Vector(0, 0), pBody.getCenterPt());
        assertEquals(new Vector(0, 0), pBody.getNetForce());
        assertEquals(new Vector(0, 0), pBody.getVelocity());
        assertEquals(0, pBody.getAngle());
        assertTrue(pBody.isMoving());
    }

    @Test
    void constructor_should_hard_copy_body_when_given_another_body() {
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

        assertEquals(1000, pBody.getMass());
        assertEquals("My other body", pBody.getName());
        assertEquals(new Vector(10, 10), pBody.getCenterPt());
        assertEquals(new Vector(100, 100), pBody.getNetForce());
        assertEquals(new Vector(1, 1), pBody.getVelocity());
        assertEquals(10, pBody.getAngle());
        assertFalse(pBody.isMoving());
    }

    @Test
    void getMass_should_return_1000_when_mass_set_to_1000() {
        pBody.setMass(1000);
        assertEquals(1000, pBody.getMass());
    }

    @Test
    void getName_should_return_Bob_when_name_set_to_Bob() {
        pBody.setName("Bob");
        assertEquals("Bob", pBody.getName());
    }

    @Test
    void getCenterPt_should_return_1_1_when_center_pt_set_to_1_1() {
        pBody.setCenterPt(new Vector(1, 1));
        assertEquals(new Vector(1, 1), pBody.getCenterPt());
    }

    @Test
    void getNetForce_should_return_1_1_when_net_force_set_to_1_1() {
        pBody.setNetForce(new Vector(1, 1));
        assertEquals(new Vector(1, 1), pBody.getNetForce());
    }

    @Test
    void getVelocity_should_return_1_1_when_velocity_set_to_1_1() {
        pBody.setVelocity(new Vector(1, 1));
        assertEquals(new Vector(1, 1), pBody.getVelocity());
    }

    @Test
    void getAngle_should_return_45_when_angle_set_to_45() {
        pBody.setAngle(45);
        assertEquals(45, pBody.getAngle());
    }

    @Test
    void isMoving_should_return_false_when_set_to_static_object() {
        pBody.setMoveable(false);
        assertFalse(pBody.isMoving());
    }

    @Test
    void toString1() {
        assertTrue(pBody.toString().length() > 0);
    }
}
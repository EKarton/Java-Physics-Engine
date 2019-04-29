package com.javaphysicsengine.api.body;

import com.javaphysicsengine.utils.Vector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PSpringTest {

    private PSpring spring;
    private PCircle circle1;
    private PCircle circle2;

    @Before
    public void setUp() {
        circle1 = new PCircle("Bob");
        circle1.setRadius(10);
        circle1.setCenterPt(Vector.of(10, 10));

        circle2 = new PCircle("Bob");
        circle2.setRadius(10);
        circle2.setCenterPt(Vector.of(100, 10));

        spring = new PSpring(circle1, circle2);
        spring.setKValue(100);
    }

    @Test
    public void getKValue_should_return_200_given_kValue_is_set_to_200() {
        spring.setKValue(200);
        assertEquals(200, spring.getKValue(), 0.00001);
    }

    @Test
    public void addTensionForce_should_add_forces_to_the_circles_given_kValue_is_100() {
        spring.addTensionForce();

        assertEquals(Vector.of(45.0, 0.0), circle1.getNetForce());
        assertEquals(Vector.of(-45.0, 0.0), circle2.getNetForce());

        fail("Failing");
    }
}
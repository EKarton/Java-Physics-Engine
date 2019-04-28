package com.javaphysicsengine.api.body;

import com.javaphysicsengine.utils.Vector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PCircleTest {

    private PCircle circle;

    @Before
    public void setup() {
        circle = new PCircle("Bob");
        circle.setCenterPt(Vector.of(10, 10));
        circle.setRadius(20);
    }

    @Test
    public void getRadius_should_return_30_after_setting_radius_to_30() {
        circle.setRadius(30);
        assertEquals(30, circle.getRadius(), 0.00001);
    }

    @Test
    public void rotate_should_do_nothing_except_for_storing_angle_information() {
        circle.rotate(45);

        assertEquals(45, circle.getAngle(), 0.0001);
        assertEquals(Vector.of(10, 10), circle.getCenterPt());
        assertEquals(20, circle.getRadius(), 0.00001);
    }

    @Test
    public void move_should_move_center_point_to_20_20_given_new_center_point_is_20_20() {
        circle.setCenterPt(Vector.of(20, 20));
        assertEquals(Vector.of(20, 20), circle.getCenterPt());
    }

    @Test
    public void translate_should_move_center_point_to_20_20_given_translation_vector_is_10_10() {
        circle.translate(Vector.of(10, 10));
        assertEquals(Vector.of(20, 20), circle.getCenterPt());
    }
}
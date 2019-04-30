package com.javaphysicsengine.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrigonometryTest {

    @Test
    public void sin_should_return_sin_of_any_angle_in_degrees() {
        assertEquals(Math.sin(Math.toRadians(45)), Trigonometry.sin(45), 0.0001);
    }

    @Test
    public void cos_should_return_cos_of_any_angle_in_degrees() {
        assertEquals(Math.cos(Math.toRadians(45)), Trigonometry.cos(45), 0.0001);
    }

    @Test
    public void tan_should_return_tan_of_any_angle_in_degrees() {
        assertEquals(Math.tan(Math.toRadians(45)), Trigonometry.tan(45), 0.0001);
    }

    @Test
    public void inverseOfTan_should_return_angle_in_degrees() {
        assertEquals(Math.toDegrees(Math.atan(0.5)), Trigonometry.inverseOfTan(0.5), 0.0001);
    }

    @Test
    public void inverseOfCos_should_return_angle_in_degrees() {
        assertEquals(Math.toDegrees(Math.acos(0.5)), Trigonometry.inverseOfCos(0.5), 0.0001);
    }

    @Test
    public void inverseOfSin_should_return_angle_in_degrees() {
        assertEquals(Math.toDegrees(Math.asin(0.5)), Trigonometry.inverseOfSin(0.5), 0.0001);
    }
}
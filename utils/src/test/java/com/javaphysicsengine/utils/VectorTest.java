package com.javaphysicsengine.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class VectorTest {

    @Test
    public void of_should_create_new_vector_object_with_correct_x_y_value() {
        Vector vector = Vector.of(1, -20);
        assertEquals(1, vector.getX(), 0.0001);
        assertEquals(-20, vector.getY(), 0.0001);
    }

    @Test
    public void subtract_should_subtract_two_vectors() {
        Vector vector1 = Vector.of(10, 100);
        Vector vector2 = Vector.of(1, -20);
        Vector resultantVector = Vector.minus(vector1, vector2);

        assertEquals(9, resultantVector.getX(), 0.0001);
        assertEquals(120, resultantVector.getY(), 0.0001);
    }

    @Test
    public void dotProduct_should_compute_dot_product_of_two_vector() {
        Vector vector1 = Vector.of(10, 100);
        Vector vector2 = Vector.of(1, -20);
        double result = Vector.dot(vector1, vector2);

        assertEquals(-1990, result, 0.0001);
    }

    @Test
    public void multiply_should_multiply_vector() {
        Vector vector = Vector.of(10, 100);
        Vector resultantVector = Vector.scale(vector, 10);

        assertEquals(100, resultantVector.getX(), 0.0001);
        assertEquals(1000, resultantVector.getY(), 0.0001);
    }

    @Test
    public void multiply_2_should_multiply_vector() {
        Vector vector = Vector.of(10, 100);
        Vector resultantVector = Vector.scale(vector, -10);

        assertEquals(-100, resultantVector.getX(), 0.0001);
        assertEquals(-1000, resultantVector.getY(), 0.0001);
    }

    @Test
    public void add_should_add_two_vectors() {
        Vector vector1 = Vector.of(10, 100);
        Vector vector2 = Vector.of(1, -20);
        Vector resultantVector = Vector.add(vector1, vector2);

        assertEquals(11, resultantVector.getX(), 0.0001);
        assertEquals(80, resultantVector.getY(), 0.0001);
    }

    @Test
    public void getLength_should_return_length_of_vector() {
        Vector vector = Vector.of(10, 12);
        assertEquals(15.6204, vector.norm2(), 0.0001);
    }

    @Test
    public void setLength_should_scale_vector_to_length_of_2() {
        Vector vector = Vector.of(1, 1);
        vector.setLength(2);

        assertEquals(1.4142, vector.getX(), 0.0001);
        assertEquals(1.4142, vector.getY(), 0.0001);
    }

    @Test
    public void setLength_2_should_scale_vector_to_length_of_2() {
        Vector vector = Vector.of(-1, 1);
        vector.setLength(2);

        assertEquals(-1.4142, vector.getX(), 0.0001);
        assertEquals(1.4142, vector.getY(), 0.0001);
    }

    @Test
    public void normalise_should_set_vector_length_to_one() {
        Vector vector = Vector.of(1, 1);
        vector.normalized();

        assertEquals(0.7071, vector.getX(), 0.0001);
        assertEquals(0.7071, vector.getY(), 0.0001);
        assertEquals(1, vector.norm2(), 0.0001);
    }

    @Test
    public void normalise_2_should_set_vector_length_to_one() {
        Vector vector = Vector.of(-1, 1);
        vector.normalized();

        assertEquals(-0.7071, vector.getX(), 0.0001);
        assertEquals(0.7071, vector.getY(), 0.0001);
        assertEquals(1, vector.norm2(), 0.0001);
    }

    @Test
    public void normalise_3_should_set_vector_length_to_one() {
        Vector vector = Vector.of(0, 1);
        vector.normalized();

        assertEquals(0, vector.getX(), 0.0001);
        assertEquals(1, vector.getY(), 0.0001);
        assertEquals(1, vector.norm2(), 0.0001);
    }

    @Test
    public void getX_should_return_x_of_vector() {
        Vector vector = new Vector(10, 12);
        assertEquals(10, vector.getX(), 0.0001);
    }

    @Test
    public void setX_should_set_x_of_vector() {
        Vector vector = new Vector(10, 12);
        vector.setX(20);
        assertEquals(20, vector.getX(), 0.0001);
    }

    @Test
    public void getY_should_return_y_of_vector() {
        Vector vector = new Vector(10, 12.0);
        assertEquals(12, vector.getY(), 0.0001);
    }

    @Test
    public void setY_should_set_y_of_vector() {
        Vector vector = new Vector(10, 12);
        vector.setY(20.0);
        assertEquals(20.0, vector.getY(), 0.0001);
    }

    @Test
    public void setXY_should_set_x_y_of_vector() {
        Vector vector = new Vector(10, 12);
        vector.setXY(20, 100);

        assertEquals(20, vector.getX(), 0.0001);
        assertEquals(100, vector.getY(), 0.0001);
    }

    @Test
    public void equals_should_return_true_if_values_close_within_range() {
        Vector vector1 = new Vector(-10, 12);
        Vector vector2 = new Vector(-10, 12);
        Vector vector3 = new Vector(10, 12);

        assertEquals(vector1, vector2);
        assertNotEquals(vector1, vector3);
        assertNotEquals(vector1, new Object());
    }

    @Test
    public void equals_should_return_false_if_values_not_within_range() {
        Vector vector1 = new Vector(10, 12);
        Vector vector2 = new Vector(11, 12);

        assertNotEquals(vector1, vector2);
    }

    @Test
    public void equals_should_return_false_if_other_value_is_not_a_vector() {
        Vector vector1 = new Vector(10, 12);
        assertNotEquals(vector1, new Object());
    }

    @Test
    public void toString_should_output_vector_in_correct_format() {
        Vector vector = new Vector(10, 12);
        assertNotNull(vector.toString());
    }
}
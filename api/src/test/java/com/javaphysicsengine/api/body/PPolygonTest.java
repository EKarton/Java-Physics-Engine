package com.javaphysicsengine.api.body;

import com.javaphysicsengine.utils.Vector;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PPolygonTest {

    private PPolygon polygon;

    @Before
    public void setup() {

        // These are the minimum requirements needed to create a polygon.
        polygon = new PPolygon("Bob");
        polygon.getVertices().add(Vector.of(0, 0));
        polygon.getVertices().add(Vector.of(10, 0));
        polygon.getVertices().add(Vector.of(10, 10));
        polygon.getVertices().add(Vector.of(0, 10));
        polygon.computeCenterOfMass();
    }

    @Test
    public void constructor_should_make_copy_of_object_given_polygon_instance_is_passed_as_a_parameter() {
        PPolygon copyOfPolygon = new PPolygon(polygon);

        polygon.rotate(45);
        polygon.translate(Vector.of(100, 100));

        // Check that the copy is not affected
        List<Vector> vertices = copyOfPolygon.getVertices();
        assertEquals(Vector.of(0, 0), vertices.get(0));
        assertEquals(Vector.of(10, 0), vertices.get(1));
        assertEquals(Vector.of(10, 10), vertices.get(2));
        assertEquals(Vector.of(0, 10), vertices.get(3));
    }

    @Test
    public void getBoundingBox_should_return_bounding_box_with_correct_min_max_values() {
        PBoundingBox polygonBoundingBox = polygon.getBoundingBox();

        assertEquals(0, polygonBoundingBox.getMinX(), 0.00001);
        assertEquals(0, polygonBoundingBox.getMinY(), 0.00001);
        assertEquals(10, polygonBoundingBox.getMaxX(), 0.00001);
        assertEquals(10, polygonBoundingBox.getMaxY(), 0.00001);
    }

    @Test
    public void computeCenterOfMass_should_return_5_5() {
        assertEquals(5, polygon.getCenterPt().getX(), 0.00001);
        assertEquals(5, polygon.getCenterPt().getY(), 0.00001);
    }

    @Test
    public void translate_should_translate_polygon_by_10_units_to_right_given_translation_vector_is_10_0() {
        polygon.translate(Vector.of(10, 0));

        // Verify that the vertices has been updated
        List<Vector> vertices = polygon.getVertices();
        assertEquals(Vector.of(10, 0), vertices.get(0));
        assertEquals(Vector.of(20, 0), vertices.get(1));
        assertEquals(Vector.of(20, 10), vertices.get(2));
        assertEquals(Vector.of(10, 10), vertices.get(3));

        // Verify the bounding box has been updated.
        PBoundingBox polygonBoundingBox = polygon.getBoundingBox();

        assertEquals(10, polygonBoundingBox.getMinX(), 0.00001);
        assertEquals(0, polygonBoundingBox.getMinY(), 0.00001);
        assertEquals(20, polygonBoundingBox.getMaxX(), 0.00001);
        assertEquals(10, polygonBoundingBox.getMaxY(), 0.00001);

        // Verify that the center point has been updated
        assertEquals(15, polygon.getCenterPt().getX(), 0.00001);
        assertEquals(5, polygon.getCenterPt().getY(), 0.00001);
    }

    @Test
    public void rotate_should_rotate_polygon_by_45_degrees_clockwise_given_rotation_value_is_45_degrees() {
        polygon.rotate(45);

        // Verify that the vertices has been updated
        List<Vector> vertices = polygon.getVertices();
        assertEquals(Vector.of(4.999999999999999, -2.0710678118654755), vertices.get(0));
        assertEquals(Vector.of(12.071067811865476, 4.999999999999998), vertices.get(1));
        assertEquals(Vector.of(5.0, 12.071067811865476), vertices.get(2));
        assertEquals(Vector.of(-2.0710678118654755, 5.000000000000001), vertices.get(3));

        // Verify the bounding box has been updated.
        PBoundingBox polygonBoundingBox = polygon.getBoundingBox();

        assertEquals(-2.0710678118654755, polygonBoundingBox.getMinX(), 0.00001);
        assertEquals(-2.0710678118654755, polygonBoundingBox.getMinY(), 0.00001);
        assertEquals(12.071067811865476, polygonBoundingBox.getMaxX(), 0.00001);
        assertEquals(12.071067811865476, polygonBoundingBox.getMaxY(), 0.00001);

        // Verify that the center point has been updated
        assertEquals(5, polygon.getCenterPt().getX(), 0.00001);
        assertEquals(5, polygon.getCenterPt().getY(), 0.00001);
    }

    @Test
    public void move_should_translate_polygon_by_10_0_given_new_center_point_is_15_5() {
        polygon.move(Vector.of(15, 5));

        // Verify that the vertices has been updated
        List<Vector> vertices = polygon.getVertices();
        assertEquals(Vector.of(10, 0), vertices.get(0));
        assertEquals(Vector.of(20, 0), vertices.get(1));
        assertEquals(Vector.of(20, 10), vertices.get(2));
        assertEquals(Vector.of(10, 10), vertices.get(3));

        // Verify the bounding box has been updated.
        PBoundingBox polygonBoundingBox = polygon.getBoundingBox();

        assertEquals(10, polygonBoundingBox.getMinX(), 0.00001);
        assertEquals(0, polygonBoundingBox.getMinY(), 0.00001);
        assertEquals(20, polygonBoundingBox.getMaxX(), 0.00001);
        assertEquals(10, polygonBoundingBox.getMaxY(), 0.00001);

        // Verify that the center point has been updated
        assertEquals(15, polygon.getCenterPt().getX(), 0.00001);
        assertEquals(5, polygon.getCenterPt().getY(), 0.00001);
    }

    @Test
    public void drawBoundingBox() {
    }

    @Test
    public void drawFill() {
    }

    @Test
    public void drawOutline() {
    }

    @Test
    public void toString1() {
    }
}
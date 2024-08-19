package com.javaphysicsengine.api.body;


import com.javaphysicsengine.utils.Vector;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PBoundingBoxTest {

    private PBoundingBox boundingBox;
    private List<Vector> vertices;

    @Before
    public void setup() {
        vertices = new ArrayList<>();
        vertices.add(Vector.of(0, 0));
        vertices.add(Vector.of(20, 0));
        vertices.add(Vector.of(30, 40));
        vertices.add(Vector.of(0, 50));

        boundingBox = new PBoundingBox(vertices);
    }

    @Test
    public void recomputeBoundaries_should_be_called_when_creating_new_bounding_box() {
        List<Vector> vertices = new ArrayList<>();
        vertices.add(Vector.of(0, 0));
        vertices.add(Vector.of(100, 0));
        vertices.add(Vector.of(100, 20));
        vertices.add(Vector.of(0, 20));

        PBoundingBox boundingBox2 = new PBoundingBox(vertices);

        assertEquals(0, boundingBox2.getMinX(), 0.0001);
        assertEquals(0, boundingBox2.getMinY(), 0.0001);
        assertEquals(100, boundingBox2.getMaxX(), 0.0001);
        assertEquals(20, boundingBox2.getMaxY(), 0.0001);
    }

    @Test
    public void recomputeBoundaries_should_recompute_boundaries_when_given_new_set_of_vertices() {
        vertices.set(0, Vector.of(10, 10));
        vertices.set(3, Vector.of(10, 50));

        boundingBox.recomputeBoundaries(vertices);

        assertEquals(10, boundingBox.getMinX(), 0.0001);
        assertEquals(0, boundingBox.getMinY(), 0.0001);
        assertEquals(30, boundingBox.getMaxX(), 0.0001);
        assertEquals(50, boundingBox.getMaxY(), 0.0001);
    }

    @Test
    public void getMinX_should_return_1000_when_minX_is_set_to_1000() {
        boundingBox.setMinX(1000);
        assertEquals(1000, boundingBox.getMinX(), 0.00001);
    }

    @Test
    public void getMinY_should_return_1000_when_minY_is_set_to_1000() {
        boundingBox.setMinY(1000);
        assertEquals(1000, boundingBox.getMinY(), 0.00001);
    }

    @Test
    public void getMaxX_should_return_1000_when_maxX_is_set_to_1000() {
        boundingBox.setMaxX(1000);
        assertEquals(1000, boundingBox.getMaxX(), 0.00001);
    }

    @Test
    public void getMaxY_should_return_1000_when_maxY_is_set_to_1000() {
        boundingBox.setMaxY(1000);
        assertEquals(1000, boundingBox.getMaxY(), 0.00001);
    }

    @Test
    public void drawBoundingBox() {
        Graphics graphics = spy(Graphics.class);

        boundingBox.drawBoundingBox(graphics, 600);

        verify(graphics, times(1)).drawLine(0, 600, 0, 550);
        verify(graphics, times(1)).drawLine(0, 550, 30, 550);
        verify(graphics, times(1)).drawLine(30, 550, 30, 600);
        verify(graphics, times(1)).drawLine(30, 600, 0, 600);
    }
}
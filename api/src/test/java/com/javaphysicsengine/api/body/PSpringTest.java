package com.javaphysicsengine.api.body;

import com.javaphysicsengine.utils.Vector;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    }

    @Test
    public void drawConstraints_should_draw_line_between_the_center_points_of_two_objects_in_green() {
        Graphics graphics = mock(Graphics.class);
        spring.drawConstraints(graphics, 600);

        verify(graphics, times(1)).setColor(eq(Color.GREEN));
        verify(graphics, times(1)).drawLine(
                (int) circle1.getCenterPt().getX(),
                (int) (600 - circle1.getCenterPt().getY()),
                (int) circle2.getCenterPt().getX(),
                (int) (600 - circle2.getCenterPt().getY()));
    }
}
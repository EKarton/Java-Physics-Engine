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

public class PStringTest {

    private PString string;
    private PCircle circle2;
    private PCircle circle1;

    @Before
    public void setup() {
        circle1 = new PCircle("Bob");
        circle1.setRadius(10);
        circle1.setCenterPt(Vector.of(10, 10));

        circle2 = new PCircle("Bob");
        circle2.setRadius(10);
        circle2.setCenterPt(Vector.of(100, 10));

        string = new PString(circle1, circle2);
    }

    @Test
    public void addTensionForce_should_not_move_circles() {
        string.addTensionForce();

        assertEquals(Vector.of(0, 0), circle1.getNetForce());
        assertEquals(Vector.of(0, 0), circle2.getNetForce());

        assertEquals(Vector.of(10, 10), circle1.getCenterPt());
        assertEquals(Vector.of(100, 10), circle2.getCenterPt());
    }

    @Test
    public void addTensionForce_should_move_right_circle_given_left_circle_has_moved() {
        circle1.move(Vector.of(0, 10));
        circle1.setNetForce(Vector.of(0, 10));
        string.addTensionForce();

        assertEquals(Vector.of(0, 0), circle1.getNetForce());
        assertEquals(Vector.of(0, 0), circle2.getNetForce());

        assertEquals(Vector.of(-5.0, 10.0), circle1.getCenterPt());
        assertEquals(Vector.of(105.0, 10.0), circle2.getCenterPt());
    }

    @Test
    public void drawConstraints_should_draw_line_between_the_center_points_of_two_objects_in_white() {
        Graphics graphics = mock(Graphics.class);
        string.drawConstraints(graphics, 600);

        verify(graphics, times(1)).setColor(eq(Color.WHITE));
        verify(graphics, times(1)).drawLine(
                (int) circle1.getCenterPt().getX(),
                (int) (600 - circle1.getCenterPt().getY()),
                (int) circle2.getCenterPt().getX(),
                (int) (600 - circle2.getCenterPt().getY()));
    }
}
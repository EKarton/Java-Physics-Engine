package com.javaphysicsengine.api;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.utils.Vector;
import org.junit.Before;
import org.junit.Test;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PWorldTest {

    private PWorld world;
    private ArrayList<PBody> bodies;
    private ArrayList<PConstraints> constraints;

    @Before
    public void setup() {
        bodies = new ArrayList<>();
        bodies.add(createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4))));
        bodies.add(createPPolygon(Arrays.asList(Vector.of(1, 0), Vector.of(-2, -2), Vector.of(0, 4))));
        bodies.add(createPCircle(2, Vector.of(6, 6)));
        bodies.add(createPCircle(2, Vector.of(4, 4)));

        constraints = new ArrayList<>();
        constraints.add(createPSpring(bodies.get(0), bodies.get(1)));

        world = new PWorld();
        world.getBodies().addAll(bodies);
        world.getConstraints().addAll(constraints);
    }

    @Test
    public void draw_should_draw_outline_of_all_bodies_and_constraints() {
        Graphics graphics = mock(Graphics.class);
        world.draw(graphics);

        for (PBody body : bodies) {
            verify(body, times(1)).drawOutline(eq(graphics), eq(600));
        }

        for (PConstraints constraint : constraints) {
            verify(constraint, times(1)).drawConstraints(eq(graphics), eq(600));
        }
    }

    @Test
    public void simulate_should_move_bodies() {
        world.simulate(0.006);

        for (PBody body : bodies) {
            verify(body, atLeastOnce()).translate(any(Vector.class));
        }
    }

    private static PPolygon createPPolygon(List<Vector> vertices) {
        PPolygon polygon = spy(new PPolygon(""));
        polygon.getVertices().addAll(vertices);
        polygon.computeCenterOfMass();

        return polygon;
    }

    private static PCircle createPCircle(int radius, Vector centerPt) {
        PCircle newPCircle = spy(new PCircle(""));
        newPCircle.setCenterPt(centerPt);
        newPCircle.setRadius(radius);

        return newPCircle;
    }

    private static PSpring createPSpring(PBody body1, PBody body2) {
        return spy(new PSpring(body1, body2));
    }
}
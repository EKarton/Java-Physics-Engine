package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.utils.Vector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PCirclePolyCollisionTest {

    @Parameterized.Parameters
    public static Collection getTestData() {
        return Arrays.asList(new Object[][]{
//            {
//                createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0)),
//                createPCircle(4, Vector.of(-4, 3), Vector.of(0, 0)),
//                false,
//                Vector.of(0, 0),
//                Vector.of(0, 0),
//                Vector.of(0, 0)
//            },
//            {
//                createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0)),
//                createPCircle(4, Vector.of(4, -2), Vector.of(0, 0)),
//                false,
//                Vector.of(0, 0),
//                Vector.of(0, 0),
//                Vector.of(0, 0)
//            },
//            {
//                createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0)),
//                createPCircle(4, Vector.of(2, 2), Vector.of(0, 0)),
//                true,
//                Vector.of(0, 0),
//                Vector.of(0, 0),
//                Vector.of(2.8284, 2.8284)
//            },
//            {
//                createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(1, 1)),
//                createPCircle(2, Vector.of(3, 1), Vector.of(-1, 1)),
//                true,
//                Vector.of(0.2071, 0.2071),
//                Vector.of(0.2071, 0.2071),
//                Vector.of(0.4142, 0.4142)
//            },
//            {
//                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 1)),
//                    createPCircle(2, Vector.of(2, 5), Vector.of(0, -1)),
//                    true,
//                    Vector.of(0.0, 0.5),
//                    Vector.of(0.0, -0.5),
//                    Vector.of(0.0, 1.0)
//            },
            {
                createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(-1, 0), true),
                createPCircle(2, Vector.of(-1, 5), Vector.of(1, 0), true),
                true,
                Vector.of(0, 0.5),
                Vector.of(0, -0.5),
                Vector.of(0, 1.0)
            },
            {
                createPPolygon(Arrays.asList(Vector.of(600, 600), Vector.of(240, 240), Vector.of(600, 240)), Vector.of(0, 0), false),
                createPCircle(40, Vector.of(420, 240), Vector.of(0, -10), true),
                true,
                Vector.of(0, -40),
                Vector.of(0, 0),
                Vector.of(0, -40)
            }
        });
    }

    @Parameterized.Parameter
    public PPolygon polygon;

    @Parameterized.Parameter(value = 1)
    public PCircle circle;

    @Parameterized.Parameter(value = 2)
    public boolean expectedResult;

    @Parameterized.Parameter(value = 3)
    public Vector expectedTranslationVectorForCircle;

    @Parameterized.Parameter(value = 4)
    public Vector expectedTranslationVectorForPolygon;

    @Parameterized.Parameter(value = 5)
    public Vector expectedMtdVector;

    @Test
    public void doBodiesCollide() {
        PCollisionResult result =  PCirclePolyCollision.doBodiesCollide(circle, polygon);

        assertEquals(expectedResult, result.isHasCollided());
        assertEquals(expectedTranslationVectorForCircle, result.getBody1Mtv());
        assertEquals(expectedTranslationVectorForPolygon, result.getBody2Mtv());
        assertEquals(expectedMtdVector, result.getMtv());
    }

    private static PPolygon createPPolygon(List<Vector> vertices, Vector velocity, boolean isMoving) {
        PPolygon polygon = new PPolygon("");
        polygon.getVertices().addAll(vertices);
        polygon.setVelocity(velocity);
        polygon.setMoveable(isMoving);
        polygon.computeCenterOfMass();

        return polygon;
    }

    private static PCircle createPCircle(int radius, Vector centerPt, Vector velocity, boolean isMoving) {
        PCircle newPCircle = new PCircle("");
        newPCircle.setCenterPt(centerPt);
        newPCircle.setRadius(radius);
        newPCircle.setMoveable(isMoving);
        newPCircle.setVelocity(velocity);

        return newPCircle;
    }
}
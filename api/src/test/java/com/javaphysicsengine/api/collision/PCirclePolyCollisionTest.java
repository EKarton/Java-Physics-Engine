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
            {
                createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0)),
                createPCircle(4, Vector.of(-4, 3), Vector.of(0, 0)),
                false,
                Vector.of(0, 0),
                Vector.of(0, 0),
                Vector.of(0, 0)
            },
            {
                createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0)),
                createPCircle(4, Vector.of(4, -2), Vector.of(0, 0)),
                false,
                Vector.of(0, 0),
                Vector.of(0, 0),
                Vector.of(0, 0)
            },
            {
                createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0)),
                createPCircle(4, Vector.of(2, 2), Vector.of(0, 0)),
                true,
                Vector.of(0, 0),
                Vector.of(0, 0),
                Vector.of(2.8284, 2.8284)
            },
            {
                createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(1, 1)),
                createPCircle(2, Vector.of(3, 1), Vector.of(-1, 1)),
                true,
                Vector.of(0.2071, 0.2071),
                Vector.of(0.2071, 0.2071),
                Vector.of(0.4142, 0.4142)
            },
            {
                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 1)),
                    createPCircle(2, Vector.of(2, 5), Vector.of(0, -1)),
                    true,
                    Vector.of(0.0, 0.5),
                    Vector.of(0.0, -0.5),
                    Vector.of(0.0, 1.0)
            },
            {
                createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(-1, 0)),
                createPCircle(2, Vector.of(-1, 5), Vector.of(1, 0)),
                true,
                Vector.of(0.2071, 0.2071),
                Vector.of(-0.2071, -0.2071),
                Vector.of(0.4142, 0.4142)
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
        Vector circleTranslationVector = Vector.of(0, 0);
        Vector polygonTranslationVector = Vector.of(0, 0);
        Vector mtd = Vector.of(0, 0);

        boolean actualResult =  PCirclePolyCollision.doBodiesCollide(
                circle, polygon, circleTranslationVector, polygonTranslationVector, mtd);

        assertEquals(expectedResult, actualResult);
        assertEquals(expectedTranslationVectorForCircle, circleTranslationVector);
        assertEquals(expectedTranslationVectorForPolygon, polygonTranslationVector);
        assertEquals(expectedMtdVector, mtd);
    }

    private static PPolygon createPPolygon(List<Vector> vertices, Vector velocity) {
        PPolygon polygon = new PPolygon("");
        polygon.getVertices().addAll(vertices);
        polygon.setVelocity(velocity);
        polygon.computeCenterOfMass();

        return polygon;
    }

    private static PCircle createPCircle(int radius, Vector centerPt, Vector velocity) {
        PCircle newPCircle = new PCircle("");
        newPCircle.setCenterPt(centerPt);
        newPCircle.setRadius(radius);
        newPCircle.setVelocity(velocity);

        return newPCircle;
    }
}
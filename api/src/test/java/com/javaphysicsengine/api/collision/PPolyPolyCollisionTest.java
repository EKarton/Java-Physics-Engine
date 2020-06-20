package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.utils.Vector;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class PPolyPolyCollisionTest {

    @RunWith(Parameterized.class)
    public static class DoBodiesCollideTest {

        @Parameterized.Parameters
        public static Collection getTestData() {
            return Arrays.asList(new Object[][]{
//                {
//                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0), true),
//                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(4, 0)), Vector.of(0, 0), true),
//                    false,
//                    null,
//                    null,
//                    null
//                },
//                {
//                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0), true),
//                    createPPolygon(Arrays.asList(Vector.of(-4, 0), Vector.of(0, 4), Vector.of(0, 0)), Vector.of(0, 0), true),
//                    false,
//                    null,
//                    null,
//                    null
//                },
//                {
//                    createPPolygon(Arrays.asList(Vector.of(-4, 0), Vector.of(0, -4), Vector.of(0, 0)), Vector.of(0, 0), true),
//                    createPPolygon(Arrays.asList(Vector.of(-4, 0), Vector.of(0, 4), Vector.of(0, 0)), Vector.of(0, 0), true),
//                    false,
//                    null,
//                    null,
//                    null
//                },
//                {
//                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0), true),
//                    createPPolygon(Arrays.asList(Vector.of(-1, 0), Vector.of(3, 0), Vector.of(3, 4)), Vector.of(1, 0), true),
//                    true,
//                    Vector.of(0, 0),
//                    Vector.of(0.4999999999999999, -0.4999999999999999),
//                    Vector.of(-0.4999999999999999, 0.4999999999999999)
//                },
//                {
//                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0), true),
//                    createPPolygon(Arrays.asList(Vector.of(-3, 0), Vector.of(-1, 0), Vector.of(1, 4)), Vector.of(1, 0), true),
//                    true,
//                    Vector.of(0, 0),
//                    Vector.of(-0.7999999999999999, 0.39999999999999997),
//                    Vector.of(0.7999999999999999, -0.39999999999999997)
//                },
//                {
//                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0), true),
//                    createPPolygon(Arrays.asList(Vector.of(-2, 3), Vector.of(1, 3), Vector.of(1, 6)), Vector.of(1, 0), true),
//                    true,
//                    Vector.of(0, 0),
//                    Vector.of(0, 1),
//                    Vector.of(0, -1)
//                },
//                {
//                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(1, 0), true),
//                    createPPolygon(Arrays.asList(Vector.of(-3, 0), Vector.of(-1, 0), Vector.of(1, 4)), Vector.of(-1, 0), true),
//                    true,
//                    Vector.of(0.39999999999999997, -0.19999999999999998),
//                    Vector.of(-0.39999999999999997, 0.19999999999999998),
//                    Vector.of(0.7999999999999999, -0.39999999999999997)
//                },
                {
                    createPPolygon(Arrays.asList(Vector.of(600, 600), Vector.of(240, 240), Vector.of(600, 240)), Vector.of(0, 0), false),
                    createPPolygon(Arrays.asList(Vector.of(300, 400), Vector.of(500, 400), Vector.of(500, 600), Vector.of(300, 600)), Vector.of(0, -10), true),
                    true,
                    Vector.of(0, 0),
                    Vector.of(-50, 50),
                    Vector.of(-50, 50),
                    Vector.of(450, 450),
                },
                {
                    createPPolygon(Arrays.asList(Vector.of(400, 100), Vector.of(600, 100), Vector.of(600, 300), Vector.of(400, 300)), Vector.of(0, 1), true),
                    createPPolygon(Arrays.asList(Vector.of(450, 210), Vector.of(550, 210), Vector.of(550, 410), Vector.of(450, 410)), Vector.of(0, -1), true),
                    true,
                    Vector.of(0, -45),
                    Vector.of(0, 45),
                    Vector.of(0, -90),
                    Vector.of(500, 255)
                }
            });
        }

        @Parameterized.Parameter
        public PPolygon polygon1;

        @Parameterized.Parameter(value = 1)
        public PPolygon polygon2;

        @Parameterized.Parameter(value = 2)
        public boolean expectedResult;

        @Parameterized.Parameter(value = 3)
        public Vector expectedPoly1Mtv;

        @Parameterized.Parameter(value = 4)
        public Vector expectedPoly2Mtv;

        @Parameterized.Parameter(value = 5)
        public Vector expectedMtv;

        @Parameterized.Parameter(value = 6)
        public Vector expectedContactPt;

        @Test
        public void doBodiesCollide_should_output_correct_result_mtd_and_translation_vectors() {
            PCollisionResult result = PPolyPolyCollision.doBodiesCollide(polygon1, polygon2);

            assertEquals(expectedResult, result.isHasCollided());
            assertEquals(expectedPoly1Mtv, result.getBody1Mtv());
            assertEquals(expectedPoly2Mtv, result.getBody2Mtv());
            assertEquals(expectedMtv, result.getMtv());
            assertEquals(expectedContactPt, result.getContactPt());
        }

        private static PPolygon createPPolygon(List<Vector> vertices, Vector velocity, boolean isMoving) {
            PPolygon polygon = new PPolygon("");
            polygon.getVertices().addAll(vertices);
            polygon.setVelocity(velocity);
            polygon.setMoveable(isMoving);
            polygon.computeCenterOfMass();

            return polygon;
        }
    }
}
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
                {
                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0)),
                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(4, 0)), Vector.of(0, 0)),
                    false,
                    Vector.of(0, 0),
                    Vector.of(0, 0),
                    Vector.of(0, 0)
                },
                {
                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0)),
                    createPPolygon(Arrays.asList(Vector.of(-4, 0), Vector.of(0, 4), Vector.of(0, 0)), Vector.of(0, 0)),
                    false,
                    Vector.of(0, 0),
                    Vector.of(0, 0),
                    Vector.of(0, 0)
                },
                {
                    createPPolygon(Arrays.asList(Vector.of(-4, 0), Vector.of(0, -4), Vector.of(0, 0)), Vector.of(0, 0)),
                    createPPolygon(Arrays.asList(Vector.of(-4, 0), Vector.of(0, 4), Vector.of(0, 0)), Vector.of(0, 0)),
                    false,
                    Vector.of(0, 0),
                    Vector.of(0, 0),
                    Vector.of(0, 0)
                },
                {
                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0)),
                    createPPolygon(Arrays.asList(Vector.of(-1, 0), Vector.of(3, 4), Vector.of(3, 0)), Vector.of(0, 0)),
                    true,
                    Vector.of(0, 0),
                    Vector.of(0, 0),
                    Vector.of(0.5, 0.5)
                },
                {
                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(0, 0)),
                    createPPolygon(Arrays.asList(Vector.of(-3, 0), Vector.of(1, 4), Vector.of(-1, 0)), Vector.of(0, 0)),
                    true,
                    Vector.of(0, 0),
                    Vector.of(0, 0),
                    Vector.of(0.8000, 0.3999)
                },
                {
                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(-1, -1)),
                    createPPolygon(Arrays.asList(Vector.of(-1, 0), Vector.of(3, 4), Vector.of(3, 0)), Vector.of(1, 1)),
                    true,
                    Vector.of(0.25, 0.25),
                    Vector.of(-0.25, -0.25),
                    Vector.of(0.5, 0.5)
                },
                {
                    createPPolygon(Arrays.asList(Vector.of(0, 0), Vector.of(4, 4), Vector.of(0, 4)), Vector.of(1, 0)),
                    createPPolygon(Arrays.asList(Vector.of(-3, 0), Vector.of(1, 4), Vector.of(-1, 0)), Vector.of(-1, 0)),
                    true,
                    Vector.of(0.4000, 0.1999),
                    Vector.of(-0.4000, -0.1999),
                    Vector.of(0.8000, 0.3999)
                },
            });
        }

        @Parameterized.Parameter
        public PPolygon polygon1;

        @Parameterized.Parameter(value = 1)
        public PPolygon polygon2;

        @Parameterized.Parameter(value = 2)
        public boolean expectedResult;

        @Parameterized.Parameter(value = 3)
        public Vector expectedPolygon1TransVector;

        @Parameterized.Parameter(value = 4)
        public Vector expectedPolygon2TransVector;

        @Parameterized.Parameter(value = 5)
        public Vector expectedMtdVector;

//        @Test
//        public void doBodiesCollide_should_output_correct_result_mtd_and_translation_vectors() {
//            Vector polygon1TransVector = Vector.of(0, 0);
//            Vector polygon2TransVector = Vector.of(0, 0);
//            Vector mtd = Vector.of(0, 0);
//
//            boolean actualResult =  PPolyPolyCollision.doBodiesCollide(
//                    polygon1, polygon2, polygon1TransVector, polygon2TransVector, mtd);
//
//            assertEquals(expectedResult, actualResult);
//            assertEquals(expectedPolygon1TransVector, polygon1TransVector);
//            assertEquals(expectedPolygon2TransVector, polygon2TransVector);
//            assertEquals(expectedMtdVector, mtd);
//        }

        private static PPolygon createPPolygon(List<Vector> vertices, Vector velocity) {
            PPolygon polygon = new PPolygon("");
            polygon.getVertices().addAll(vertices);
            polygon.setVelocity(velocity);
            polygon.computeCenterOfMass();

            return polygon;
        }
    }
}
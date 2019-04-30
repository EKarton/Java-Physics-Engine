package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.utils.Vector;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class PCollisionUtilTest {

    @RunWith(Parameterized.class)
    public static class ProjectPointToLineTest {

        @Parameterized.Parameters
        public static Collection getTestData() {
            return Arrays.asList(new Object[][]{
                { 1, -1, Vector.of(0, 5), Vector.of(3, 2) },
                { -1, 4, Vector.of(0, 5), Vector.of(-0.5, 4.5) },
                { 0, 4, Vector.of(-5, 5), Vector.of(-5, 4) }
            });
        }

        @Parameterized.Parameter
        public double slopeOfLine;

        @Parameterized.Parameter(value = 1)
        public double yInterceptOfLine;

        @Parameterized.Parameter(value = 2)
        public Vector point;

        @Parameterized.Parameter(value = 3)
        public Vector expectedProjectedPoint;

        @Test
        public void executeAndVerify() {
            Vector actualProjectedPoint = PCollisionUtil.projectPointToLine(slopeOfLine, yInterceptOfLine, point);
            assertEquals(expectedProjectedPoint, actualProjectedPoint);
        }
    }

    @RunWith(Parameterized.class)
    public static class DoDomainsIntersectTest {

        @Parameterized.Parameters
        public static Collection getTestData() {
            return Arrays.asList(new Object[][]{
                {
                    Vector.of(0, 0),
                    Vector.of(3, 4),
                    Vector.of(3.001, 4.001),
                    Vector.of(10, 11),
                    false,
                    Vector.of(0, 0)
                },
                {
                    Vector.of(0, 0),
                    Vector.of(3, 4),
                    Vector.of(3, 4),
                    Vector.of(10, 11),
                    true,
                    Vector.of(0, 0)
                },
                {
                    Vector.of(0, 0),
                    Vector.of(3, 3),
                    Vector.of(2, 3),
                    Vector.of(10, 3),
                    true,
                    Vector.of(1, 0)
                },
                {
                    Vector.of(0, 0),
                    Vector.of(3, 3),
                    Vector.of(3, 2),
                    Vector.of(3, 10),
                    true,
                    Vector.of(0, 1)
                },
                {
                    Vector.of(0, 0),
                    Vector.of(3, 3),
                    Vector.of(2, 2),
                    Vector.of(3, 10),
                    true,
                    Vector.of(1, 1)
                }
            });
        }

        @Parameterized.Parameter
        public Vector min1Values;

        @Parameterized.Parameter(value = 1)
        public Vector max1Values;

        @Parameterized.Parameter(value = 2)
        public Vector min2Values;

        @Parameterized.Parameter(value = 3)
        public Vector max2Values;

        @Parameterized.Parameter(value = 4)
        public boolean expectedResult;

        @Parameterized.Parameter(value = 5)
        public Vector expectedOverlap;

        @Test
        public void executeAndVerify() {
            Vector actualOverlap = Vector.of(0, 0);
            boolean isOverlap = PCollisionUtil.doDomainsIntersect(min1Values, max1Values, min2Values, max2Values, actualOverlap);

            assertEquals(expectedResult, isOverlap);
            assertEquals(expectedOverlap, actualOverlap);
        }
    }
}
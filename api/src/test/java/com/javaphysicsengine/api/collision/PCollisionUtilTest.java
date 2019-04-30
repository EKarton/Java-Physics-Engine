package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.utils.Vector;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

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

}
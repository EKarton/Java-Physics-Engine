package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.utils.Vector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class PCircleCircleCollisionTest {

    @Parameterized.Parameters
    public static Collection getTestData() {
        return Arrays.asList(new Object[][]{
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(0, 0), true),
                    createPCircle(10, Vector.of(15, 15), Vector.of(0, 0), true),
                    false,
                    null,
                    null,
                    null
                },
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(0, 1), true),
                    createPCircle(20, Vector.of(0, 15), Vector.of(0, -1), true),
                    true,
                    Vector.of(0, -7.5),
                    Vector.of(0, 7.5),
                    Vector.of(0, -15)
                },
                {
                    createPCircle(10, Vector.of(0, 2), Vector.of(12, 0), true),
                    createPCircle(10, Vector.of(0, 9), Vector.of(-12, 0), true),
                    true,
                    Vector.of(0, -6.5),
                    Vector.of(0, 6.5),
                    Vector.of(0, -13)
                },
                {
                    createPCircle(10, Vector.of(0, 8), Vector.of(-2, 0), true),
                    createPCircle(10, Vector.of(0, -8), Vector.of(2, 0), true),
                    true,
                    Vector.of(0, 2),
                    Vector.of(0, -2),
                    Vector.of(0, 4)
                },
                {
                        createPCircle(10, Vector.of(0, 8), Vector.of(-3, 0), true),
                        createPCircle(10, Vector.of(0, -8), Vector.of(2, 0), true),
                        true,
                        Vector.of(0, 2.4),
                        Vector.of(0, -1.6),
                        Vector.of(0, 4)
                },
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(2, 0), true),
                    createPCircle(10, Vector.of(5, 0), Vector.of(-2, 0), true),
                    true,
                    Vector.of(-7.5, 0),
                    Vector.of(7.5, 0),
                    Vector.of(-15, 0)
                },
                {
                        createPCircle(10, Vector.of(1, 1), Vector.of(2, 0), true),
                        createPCircle(10, Vector.of(5, 5), Vector.of(-2, 0), true),
                        true,
                        Vector.of(-5.071, -5.071),
                        Vector.of(5.071, 5.071),
                        Vector.of(-10.1421, -10.1421)
                }
        });
    }

    @Parameterized.Parameter
    public PCircle circle1;

    @Parameterized.Parameter(value = 1)
    public PCircle circle2;

    @Parameterized.Parameter(value = 2)
    public boolean expectedResult;

    @Parameterized.Parameter(value = 3)
    public Vector expectedCircle1Mtv;

    @Parameterized.Parameter(value = 4)
    public Vector expectedCircle2Mtv;

    @Parameterized.Parameter(value = 5)
    public Vector expectedMtv;

    @Test
    public void doBodiesCollide() {

        PCollisionResult result = PCircleCircleCollision.doBodiesCollide(circle1, circle2);

        assertEquals(expectedResult, result.isHasCollided());

        if (!expectedResult) {
            assertNull(expectedCircle1Mtv);
            assertNull(expectedCircle2Mtv);
            assertNull(expectedMtv);

        } else {

            assertEquals(expectedCircle1Mtv.getX(), result.getBody1Mtv().getX(), 0.0001);
            assertEquals(expectedCircle1Mtv.getY(), result.getBody1Mtv().getY(), 0.0001);

            assertEquals(expectedCircle2Mtv.getX(), result.getBody2Mtv().getX(), 0.0001);
            assertEquals(expectedCircle2Mtv.getY(), result.getBody2Mtv().getY(), 0.0001);

            assertEquals(expectedMtv.getX(), result.getMtv().getX(), 0.0001);
            assertEquals(expectedMtv.getY(), result.getMtv().getY(), 0.0001);
        }
    }

    private static PCircle createPCircle(int radius, Vector centerPt, Vector velocity, boolean isMoving) {
        PCircle newPCircle = new PCircle("");
        newPCircle.setCenterPt(centerPt);
        newPCircle.setRadius(radius);
        newPCircle.setVelocity(velocity);
        newPCircle.setMoveable(isMoving);

        return newPCircle;
    }
}
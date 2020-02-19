package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.utils.Vector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

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
                    createPCircle(10, Vector.of(0, 0), Vector.of(0, 0), true),
                    createPCircle(20, Vector.of(15, 15), Vector.of(1, 0), true),
                    true,
                    Vector.of(0, 0),
                    Vector.of(0.7071067811865475, 0.7071067811865475),
                    Vector.of(-0.7071067811865475, -0.7071067811865475)
                },
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(0, 0), true),
                    createPCircle(20, Vector.of(-15, 0), Vector.of(1, 0), true),
                    true,
                    Vector.of(0, 0),
                    Vector.of(-1.0, 0.0),
                    Vector.of(1.0, 0.0)
                },
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(12, 0), true),
                    createPCircle(10, Vector.of(9, 0), Vector.of(-12, 0), true),
                    true,
                    Vector.of(-0.5, 0.0),
                    Vector.of(0.5, 0.0),
                    Vector.of(-1.0, 0.0)
                },
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(12, 0), true),
                    createPCircle(10, Vector.of(9, 0), Vector.of(-10, 0), true),
                    true,
                    Vector.of(-0.5901639344262295, 0.0),
                    Vector.of(0.4098360655737705, 0),
                    Vector.of(-1.0, 0.0)
                },
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(2, 2), true),
                    createPCircle(10, Vector.of(5, 5), Vector.of(-2, -3), true),
                    true,
                    Vector.of(-0.2693740118805895, -0.2693740118805895),
                    Vector.of(0.43773276930595795, 0.43773276930595795),
                    Vector.of(-0.7071067811865475, -0.7071067811865475)
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
        assertEquals(expectedCircle1Mtv, result.getBody1Mtv());
        assertEquals(expectedCircle2Mtv, result.getBody2Mtv());
        assertEquals(expectedMtv, result.getMtv());
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
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
                    createPCircle(10, Vector.of(0, 0), Vector.of(0, 0)),
                    createPCircle(10, Vector.of(15, 15), Vector.of(0, 0)),
                    false,
                    Vector.of(0, 0),
                    Vector.of(0, 0),
                    Vector.of(0, 0)
                },
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(0, 0)),
                    createPCircle(20, Vector.of(15, 15), Vector.of(0, 0)),
                    true,
                    Vector.of(0, 0),
                    Vector.of(0, 0),
                    Vector.of(-6.213203435596425, -6.213203435596425)
                },
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(0, 0)),
                    createPCircle(20, Vector.of(-15, 0), Vector.of(0, 0)),
                    true,
                    Vector.of(0, 0),
                    Vector.of(0, 0),
                    Vector.of(15.0, 0.0)
                },
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(12, 0)),
                    createPCircle(10, Vector.of(9, 0), Vector.of(-12, 0)),
                    true,
                    Vector.of(-5.5, 0.0),
                    Vector.of(5.5, -0.0),
                    Vector.of(-11.0, 0.0)
                },
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(12, 0)),
                    createPCircle(10, Vector.of(9, 0), Vector.of(-10, 0)),
                    true,
                    Vector.of(-6.0, 0.0),
                    Vector.of(5.0, -0.0),
                    Vector.of(-11.0, 0.0)
                },
                {
                    createPCircle(10, Vector.of(0, 0), Vector.of(2, 2)),
                    createPCircle(10, Vector.of(5, 5), Vector.of(-2, -3)),
                    true,
                    Vector.of(-4.0189, -4.0189),
                    Vector.of(5.1231, 5.1231),
                    Vector.of(-9.1421, -9.1421)
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
    public Vector expectedTranslationVectorForCircle1;

    @Parameterized.Parameter(value = 4)
    public Vector expectedTranslationVectorForCircle2;

    @Parameterized.Parameter(value = 5)
    public Vector expectedMtdVector;

    @Test
    public void doBodiesCollide() {

        PCollisionResult result = PCircleCircleCollision.doBodiesCollide(circle1, circle2);

        assertEquals(expectedResult, result.isHasCollided());
        assertEquals(expectedTranslationVectorForCircle1, result.getBody1Mtv());
        assertEquals(expectedTranslationVectorForCircle2, result.getBody2Mtv());
        assertEquals(expectedMtdVector, result.getMtv());
    }

    private static PCircle createPCircle(int radius, Vector centerPt, Vector velocity) {
        PCircle newPCircle = new PCircle("");
        newPCircle.setCenterPt(centerPt);
        newPCircle.setRadius(radius);
        newPCircle.setVelocity(velocity);

        return newPCircle;
    }
}
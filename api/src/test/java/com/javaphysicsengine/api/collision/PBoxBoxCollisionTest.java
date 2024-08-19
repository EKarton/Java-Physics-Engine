package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.api.body.PBoundingBox;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PBoxBoxCollisionTest {

    @Parameterized.Parameters(name = "{0} vs {1} should output {2}")
    public static Collection getTestData() {
        return Arrays.asList(new Object[][]{
                {
                    new PBoundingBox(0, 10, 0, 10),
                    new PBoundingBox(10, 20, 0, 10),
                    true
                },
                {
                    new PBoundingBox(0, 10, 0, 10),
                    new PBoundingBox(0, 10, 10, 20),
                    true
                },
                {
                    new PBoundingBox(0, 10, 0, 10),
                    new PBoundingBox(-10, 0, 0, 10),
                    true
                },
                {
                    new PBoundingBox(0, 10, 0, 10),
                    new PBoundingBox(0, 10, -10, 0),
                    true
                },
                {
                    new PBoundingBox(0, 10, 0, 10),
                    new PBoundingBox(5, 15, 5, 15),
                    true
                },
                {
                    new PBoundingBox(0, 10, 0, 10),
                    new PBoundingBox(11, 21, 0, 10),
                    false
                },
                {
                    new PBoundingBox(0, 10, 0, 10),
                    new PBoundingBox(0, 10, 11, 21),
                    false
                },
                {
                    new PBoundingBox(0, 10, 0, 10),
                    new PBoundingBox(-11, -1, 0, 10),
                    false
                },
                {
                    new PBoundingBox(0, 10, 0, 10),
                    new PBoundingBox(0, 10, -11, -1),
                    false
                },
        });
    }

    @Parameterized.Parameter
    public PBoundingBox box1;

    @Parameterized.Parameter(value = 1)
    public PBoundingBox box2;

    @Parameterized.Parameter(value = 2)
    public boolean expectedResult;

    @Test
    public void doBodiesCollide_should_output_same_as_expected_result() {
        assertEquals(expectedResult, PBoxBoxCollision.doBodiesCollide(box1, box2));
    }
}
/*
 * Purpose: To detect whether two circles collide with each other
 * Original Creation Date: January 1 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */
package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.utils.Vector;

public class PCircleCircleCollision {

    public static PCollisionResult doBodiesCollide(PCircle circle1, PCircle circle2) {

        double centerPtDistance = Vector.minus(circle1.getCenterPt(), circle2.getCenterPt()).norm1();
        double radiusSum = circle1.getRadius() + circle2.getRadius();

        boolean isIntersecting = centerPtDistance < radiusSum * radiusSum;

        // If there is a collision
        if (isIntersecting) {

            // Properly calculate the distance between the center pts
            centerPtDistance = Math.sqrt(centerPtDistance);

            // Compute the mtd
            double mtd = radiusSum - centerPtDistance;

            // Calculate the MTD
            Vector mtv = circle1.getCenterPt().minus(circle2.getCenterPt()).normalize();
            mtv.multiply(mtd);

            // Compute how much MTD each object gets
            double f1 = circle1.isMoving() ? circle1.getVelocity().norm1() / (circle1.getVelocity().norm1() + circle2.getVelocity().norm1()) : 0;
            double f2 = circle2.isMoving() ? circle2.getVelocity().norm1() / (circle1.getVelocity().norm1() + circle2.getVelocity().norm1()) : 0;

            // Get the translation vector
            Vector circle1Trans = mtv.multiply(f1);
            Vector circle2Trans = mtv.multiply(-1).multiply(f2);

            // Calculate the contact point
            Vector contactPt = mtv.normalize().multiply(-1 * circle1.getRadius()).add(circle1.getCenterPt());
            contactPt = contactPt.add(circle1Trans);

            return new PCollisionResult(true, circle1Trans, circle2Trans, mtv, contactPt);
        }
        return new PCollisionResult(false, null, null, null, null);
    }
}
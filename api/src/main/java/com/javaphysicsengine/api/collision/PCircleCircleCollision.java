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

    /**
     * Determines if two circles collide
     * @param circle1 the first circle
     * @param circle2 the second circle
     * @return A PCollisionResult
     */
    public static PCollisionResult doBodiesCollide(PCircle circle1, PCircle circle2) {

        // The distance between the two circles' center pts squared
        double centerPtDistSqed = Vector.minus(circle1.getCenterPt(), circle2.getCenterPt()).norm1();

        double radiusSum = circle1.getRadius() + circle2.getRadius();

        // If there is a collision
        if (centerPtDistSqed < radiusSum * radiusSum) {

            System.out.println(circle1.getCenterPt() + "," + circle2.getCenterPt());

            // Calculate the MTD:
            double mtd = (circle1.getRadius() + circle2.getRadius()) - circle1.getCenterPt().minus(circle2.getCenterPt()).norm2();

            // Calculate the MTV:
            Vector mtv = circle1.getCenterPt().minus(circle2.getCenterPt()).normalize().multiply(mtd);

            // Compute how much MTV each object gets
            double f1 = circle1.isMoving() ? circle1.getVelocity().norm2() / (circle1.getVelocity().norm2() + circle2.getVelocity().norm2()) : 0;
            double f2 = circle2.isMoving() ? circle2.getVelocity().norm2() / (circle1.getVelocity().norm2() + circle2.getVelocity().norm2()) : 0;

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
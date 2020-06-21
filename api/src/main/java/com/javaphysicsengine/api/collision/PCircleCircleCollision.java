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

//        Vector vFrom1to2 = circle2.getCenterPt().minus(circle1.getCenterPt());
//        double rSum = circle1.getRadius() + circle2.getRadius();
//        double dist = vFrom1to2.norm2();
//        if (dist > Math.sqrt(rSum * rSum)) {
//            return new PCollisionResult(false, null, null, null, null);
//        }
//
//
//        Vector mtv;
//        Vector contactPt;
//
//        if (dist != 0) {
//            // overlapping bu not same position
//            Vector normalFrom2to1 = vFrom1to2.scale(-1).normalize();
//            Vector radiusC2 = normalFrom2to1.scale(circle2.getRadius());
//
//            double mtd = rSum - dist;
//            contactPt = circle2.getCenterPt().add(radiusC2);
//            mtv = vFrom1to2.normalize().scale(mtd);
//
//        } else {
//            //same position
//            double mtd = rSum;
//            mtv = Vector.of(0, -1).scale(mtd);
//
//            if (circle1.getRadius() > circle2.getRadius()) {
//                contactPt = circle1.getCenterPt().add(Vector.of(0, circle1.getRadius()));
//
//            } else {
//                contactPt = circle2.getCenterPt().add(Vector.of(0, circle2.getRadius()));
//            }
//        }
//
//        double f1 = circle1.isMoving() ? circle1.getVelocity().norm2() / (circle1.getVelocity().norm2() + circle2.getVelocity().norm2()) : 0;
//        double f2 = circle2.isMoving() ? circle2.getVelocity().norm2() / (circle1.getVelocity().norm2() + circle2.getVelocity().norm2()) : 0;
//
//        // Get the translation vector
//        Vector circle1Trans = mtv.scale(-f1);
//        Vector circle2Trans = mtv.scale(f2);
//
//        return new PCollisionResult(true, circle1Trans, circle2Trans, mtv, contactPt);

        // The distance between the two circles' center pts squared
        double centerPtDistSqed = Vector.minus(circle1.getCenterPt(), circle2.getCenterPt()).norm1();

        double radiusSum = circle1.getRadius() + circle2.getRadius();

        // If there is a collision
        if (centerPtDistSqed < radiusSum * radiusSum) {

            // Calculate the MTD:
            double mtd = radiusSum - Math.sqrt(centerPtDistSqed); //(circle1.getRadius() + circle2.getRadius()) - circle1.getCenterPt().minus(circle2.getCenterPt()).norm2();

            // Calculate the MTV:
            Vector mtv = circle1.getCenterPt().minus(circle2.getCenterPt()).normalize().scale(mtd);

            // Compute how much MTV each object gets
            double f1 = circle1.isMoving() ? circle1.getVelocity().norm2() / (circle1.getVelocity().norm2() + circle2.getVelocity().norm2()) : 0;
            double f2 = circle2.isMoving() ? circle2.getVelocity().norm2() / (circle1.getVelocity().norm2() + circle2.getVelocity().norm2()) : 0;

            // Get the translation vector
            Vector circle1Trans = mtv.scale(f1);
            Vector circle2Trans = mtv.scale(-1).scale(f2);

            // Calculate the contact point
            Vector contactPt = mtv.normalize().scale(-1 * circle1.getRadius()).add(circle1.getCenterPt());
            contactPt = contactPt.add(circle1Trans);

            return new PCollisionResult(true, circle1Trans, circle2Trans, mtv, contactPt);
        }
        return new PCollisionResult(false, null, null, null, null);
    }
}
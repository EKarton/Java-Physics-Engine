/*
 * Purpose: To detect whether two circles collide with each other
 * Original Creation Date: January 1 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */
package com.javaphysicsengine.api;

import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.utils.Vector;

public class PCircleCircleCollision {
    /**
     * Returns the displacement the main circle should move by
     * @param mtd The minimum translation vector from SAT algorithm
     * @param mainPolyCenterPt The center point of the main circle
     * @param otherPolyCenterPt The center point of the other circle
     * @param mainPolyVelocity The velocity of the main circle
     * @param otherPolyVelocity The velocity of the other circle
     * @return Returns the displacement the main circle should move by
     */
    private static Vector getTranslationVectors(Vector mtd, Vector mainPolyCenterPt, Vector otherPolyCenterPt, Vector mainPolyVelocity, Vector otherPolyVelocity) {
        // Checking if the velocity of main polygon is 0
        if (mainPolyVelocity.getX() == 0 && mainPolyVelocity.getY() == 0)
            return new Vector(0, 0);

        // Making sure the push vector is pushing the polygons away
        Vector translationVector = new Vector(mtd.getX(), mtd.getY());

        Vector displacementBetweenPolygons = Vector.subtract(mainPolyCenterPt, otherPolyCenterPt);
        if (Vector.dotProduct(displacementBetweenPolygons, mtd) < 0) {
            // // // System.out.println("I am here!");
            translationVector.setX(translationVector.getX() * -1);
            translationVector.setY(translationVector.getY() * -1);
        }

        // Get the ratio of the translation vector when both objects are moving
        double curLength = translationVector.getLength();
        double lengthOfMainVelocity = mainPolyVelocity.getLength();
        double lengthOfOtherVelocity = otherPolyVelocity.getLength();
        double newLength = curLength * (lengthOfMainVelocity / (lengthOfMainVelocity + lengthOfOtherVelocity));

        translationVector.setLength(newLength);

        // Checking if the new translation vector is a null (happens if the length is a 0)
        if (Double.isNaN(translationVector.getX()) && Double.isNaN(translationVector.getY()))
            translationVector.setXY(0, 0);

        return translationVector;
    }

    /**
     * Determines whether two circles are colliding, and returns the displacements
     * each circle should move by as well as the minimum translation vector
     * @param circle1 The first circle
     * @param circle2 The second circle
     * @param circle1TransVector The displacement circle1 should move by if they are colliding
     * @param circle2TransVector The displacement circle1 should move by if they are colliding
     * @param mtd Returns the minimum translation vector from SAT algorithm
     * @return Returns true if the two circles are colliding; else false. Also returns the MTD which is stored in the "mtd" parameter
     */
    public static boolean doBodiesCollide(PCircle circle1, PCircle circle2, Vector circle1TransVector, Vector circle2TransVector, Vector mtd) {
        // Compute the distance between the two circles as well as the sum of their radiuses
        double centerPtDistance = Math.pow(circle1.getCenterPt().getX() - circle2.getCenterPt().getX(), 2) + Math.pow(circle1.getCenterPt().getY() - circle2.getCenterPt().getY(), 2);
        double radiusSum = circle1.getRadius() + circle2.getRadius();

        // // System.out.println("CD: " + centerPtDistance + " | RS: " + radiusSum);

        // If there is a collision
        if (centerPtDistance < radiusSum * radiusSum) {
            // Properly calculate the distance between the center pts
            centerPtDistance = Math.sqrt(centerPtDistance);

            // Calculate the MTD
            Vector mtdVector = Vector.subtract(circle1.getCenterPt(), circle2.getCenterPt());
            mtdVector.setLength(radiusSum - centerPtDistance);
            // // System.out.println("MTD: " + mtdVector);

            // Get the translation vector
            Vector circle1Trans = getTranslationVectors(mtdVector, circle1.getCenterPt(), circle2.getCenterPt(), circle1.getVelocity(), circle2.getVelocity());
            Vector circle2Trans = getTranslationVectors(mtdVector, circle2.getCenterPt(), circle1.getCenterPt(), circle2.getVelocity(), circle1.getVelocity());

            // Save the trans vectors to the variables in the parameters
            circle1TransVector.setXY(circle1Trans.getX(), circle1Trans.getY());
            circle2TransVector.setXY(circle2Trans.getX(), circle2Trans.getY());
            mtd.setXY(mtdVector.getX(), mtdVector.getY());
            return true;
        }
        return false;
    }
}
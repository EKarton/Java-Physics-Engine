/*
 * Purpose: To detect whether two bodies collide with each other
 * Original Creation Date: January 1 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */
package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.api.body.PBoundingBox;

public class PBoxBoxCollision {

    /**
     * Determines if two bounding boxes collide
     * @param box1 The first bounding box
     * @param box2 The second bounding box
     * @return {@code true} when the two bounding boxes collide; else {@code false}
     */
    public static boolean doBodiesCollide(PBoundingBox box1, PBoundingBox box2) {
        if (box1.getMaxX() < box2.getMinX()) // If box 1 is left of box 2
            return false;

        if (box1.getMinX() > box2.getMaxX()) // If box 1 is right of box 2
            return false;

        if (box1.getMaxY() < box2.getMinY()) // If box 1 is below box 2
            return false;

        // If box 1 is above box 2
        return !(box1.getMinY() >= box2.getMaxY());
    }
}
/*
 * Purpose: To detect whether a circle and a polygon collide with each other (using SAT algorithm)
 * Original Creation Date: January 1 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.utils.Vector;

import java.util.ArrayList;

public class PCirclePolyCollision extends PPolyPolyCollision {
    private static double isRayHitCircle(Vector rayOrigin, Vector rayDir, Vector origin, double radius) {
        Vector shiftedOrigin = rayOrigin.minus(origin);

        // See where it hits the circle
        double a = rayDir.dot(rayDir);
        double b = 2 * shiftedOrigin.dot(rayDir);
        double c = shiftedOrigin.dot(shiftedOrigin) - (radius * radius);
        double delta = b * b - 4 * a * c;

        // If there are no solutions
        if (delta < 0) {
            return -1;
        }

        // Getting the solutions to t
        double t1 = (-b + Math.sqrt(delta)) / (2 * a);
        double t2 = (-b - Math.sqrt(delta)) / (2 * a);

        double best_t = -1;
        if (t1 < t2) {
            if (t1 >= 0) {
                best_t = t1;
            }
            else if (t2 >= 0) {
                best_t = t2;
            }
        }
        else if (t2 <= t1) {
            if (t2 >= 0) {
                best_t = t2;
            }
            else if (t1 >= 0) {
                best_t = t1;
            }
        }

        return best_t;
    }

    /**
     * Pre-condition: "circle1", "circle2", "circle1TransVector", "circle2TransVector", "mtd" must not be null
     * Post-condition: Determines whether a circle and polygon is colliding, and returns the displacements each body should move by as well as the minimum translation vector
     *
     * Note: vertices in poly must be in clockwise order!
     *
     * @param circle The circle
     * @param poly The polygon
     * @return Returns the results of the collision
     */
    public static PCollisionResult doBodiesCollide(PCircle circle, PPolygon poly) {
        // Saving the properties of the bodies to the global variables
        Vector circleCenterPt = circle.getCenterPt();
        double circleRadius = circle.getRadius();
        ArrayList<Vector> polyVertices = poly.getVertices();

        double bestOverlapDistance = Double.MAX_VALUE;
        Vector bestMtv = Vector.of(0, 0);
        Vector bestCircleMtv = Vector.of(0, 0);
        Vector bestPolyMtv = Vector.of(0, 0);

        // Going through all the sides in the polygon
        for (int i = 0; i < polyVertices.size(); i++) {

            // The two points that make up an edge
            Vector sidePt1 = polyVertices.get(i);
            Vector sidePt2 = i + 1 < polyVertices.size() ? polyVertices.get(i + 1) : polyVertices.get(0);

            // Compute the normal of the edge
            Vector normal = Vector.of(sidePt2.getY() - sidePt1.getY(), -1 * (sidePt2.getX() - sidePt1.getX())).normalize();

            // Compute the length of the edge
            double edgeLength = sidePt2.minus(sidePt1).norm2();

            // Compute the two edge directions
            Vector edgeDir1 = sidePt2.minus(sidePt1).normalize();
            Vector edgeDir2 = sidePt1.minus(sidePt2).normalize();

            double edgeDir1_t = isRayHitCircle(sidePt1, edgeDir1, circle.getCenterPt(), circle.getRadius());
            double edgeDir2_t = isRayHitCircle(sidePt2, edgeDir2, circle.getCenterPt(), circle.getRadius());
            boolean isIntersect = (0 <= edgeDir1_t && edgeDir1_t <= edgeLength) ||
                    (0 <= edgeDir2_t && edgeDir2_t <= edgeLength);

            if (isIntersect) {

                // Project the center of the circle to the edgeDir
                double scalarProj = edgeDir1.dot(circleCenterPt.minus(sidePt1)) / edgeDir1.norm();
                Vector vectorProj = edgeDir1.multiply(scalarProj).add(sidePt1);

                // Compute the mtd
                double mtd = circleRadius - vectorProj.minus(circleCenterPt).norm2();
                double f1 = circle.getVelocity().norm() / (circle.getVelocity().norm() + poly.getVelocity().norm());
                double f2 = poly.getVelocity().norm() / (circle.getVelocity().norm() + poly.getVelocity().norm());

                if (0 < mtd && mtd < bestOverlapDistance) {
                    bestOverlapDistance = mtd;
                    bestMtv = normal.multiply(mtd);

                    bestCircleMtv = circle.isMoving() ? normal.multiply(mtd * f1) : Vector.of(0, 0);
                    bestPolyMtv = poly.isMoving() ? normal.multiply(mtd * f2 * -1) : Vector.of(0, 0);
                }
            }
        }

        return new PCollisionResult(bestMtv.norm() > 0, bestCircleMtv, bestPolyMtv, bestMtv);
    }
}
/*
 * Purpose: To detect whether two polygons collide with each other (using SAT algorithm)
 * Original Creation Date: January 1 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.utils.Vector;

import java.util.ArrayList;
import java.util.List;

public class PPolyPolyCollision {

    private Vector getProjectionBounds(List<Vector> vertices, Vector normal) {
        double minT1 = 1000000000;
        double maxT1 = -1000000000;

        for (Vector poly1Vertex : vertices) {
            double scalarProj = normal.dot(poly1Vertex);

            minT1 = Math.min(minT1, scalarProj);
            maxT1 = Math.max(maxT1, scalarProj);
        }

        return Vector.of(minT1, maxT1);
    }

    /**
     * Computes the MTV using the separating axis theorem
     * It returns the MTV for polygon2
     * If there is no intersection, then it returns NULL
     */
    private static Vector getSeparatingAxis(List<Vector> poly1Vertices, List<Vector> poly2Vertices) {

        double bestMtd = 10000000;
        Vector bestMtv = null;

        // Going through each side in poly1 and see if poly2 intersects it
        for (int i = 0; i < poly1Vertices.size(); i++) {

            Vector sidePt1 = poly1Vertices.get(i);
            Vector sidePt2 = i + 1 < poly1Vertices.size() ? poly1Vertices.get(i + 1) : poly1Vertices.get(0);
            Vector edge = sidePt2.minus(sidePt1);

            Vector normal = Vector.of(edge.getY(), -1 * edge.getX()).normalize();

            // Project all poly1's vertices onto the normal and get its bounds
            double minT1 = 1000000000;
            double maxT1 = -1000000000;

            for (Vector poly1Vertex : poly1Vertices) {
                double scalarProj = normal.dot(poly1Vertex);

                minT1 = Math.min(minT1, scalarProj);
                maxT1 = Math.max(maxT1, scalarProj);
            }

            // Project all poly2's vertices onto the normal and get its bounds
            double minT2 = 1000000000;
            double maxT2 = -1000000000;

            for (Vector poly2Vertex : poly2Vertices) {
                double scalarProj = normal.dot(poly2Vertex);

                minT2 = Math.min(minT2, scalarProj);
                maxT2 = Math.max(maxT2, scalarProj);
            }

            boolean isIntersecting = minT1 < maxT2 && maxT1 > minT2; // p1.x < p2.y && p1.y > p2.x; //maxT1 >= minT2 && maxT2 >= minT1;

            if (isIntersecting) {
                double mtd = minT1 < maxT2 ? maxT1 - minT2 : maxT2 - minT1;

                if (mtd < bestMtd) {
                    bestMtd = mtd;
                    bestMtv = normal.multiply(mtd);
                }

            } else {
                return null;
            }
        }

        return bestMtv;
    }

    public static PCollisionResult doBodiesCollide(PPolygon body1, PPolygon body2) {

        ArrayList<Vector> poly1Vertices = body1.getVertices();
        ArrayList<Vector> poly2Vertices = body2.getVertices();

        // The weighted velocities
        double f1 = body1.isMoving() ? body1.getVelocity().norm() / (body1.getVelocity().norm() + body2.getVelocity().norm()) : 0;
        double f2 = body2.isMoving() ? body2.getVelocity().norm() / (body1.getVelocity().norm() + body2.getVelocity().norm()) : 0;

        // Note: with SAT we can terminate early as soon as there is a separating axis
        Vector mtv1 = getSeparatingAxis(poly1Vertices, poly2Vertices);

        if (mtv1 == null) {
            return new PCollisionResult(false, null, null, null);
        }

        Vector mtv2 = getSeparatingAxis(poly2Vertices, poly1Vertices);

        if (mtv2 == null) {
            return new PCollisionResult(false, null, null, null);
        }

        Vector bestMtv;
        Vector body1Mtv;
        Vector body2Mtv;

        if (mtv2.norm() <= mtv1.norm()) {
            bestMtv = mtv2;
            body1Mtv = mtv2.multiply(f1);
            body2Mtv = mtv2.multiply(-1).multiply(f2);

        } else {
            bestMtv = mtv1;
            body1Mtv = mtv1.multiply(-1).multiply(f1);
            body2Mtv = mtv1.multiply(f2);
        }

        return new PCollisionResult(true, body1Mtv, body2Mtv, bestMtv);
    }
}
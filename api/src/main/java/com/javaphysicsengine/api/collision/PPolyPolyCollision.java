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

    private static Vector getProjectionBounds(List<Vector> vertices, Vector proj) {
        double minScalar = 1000000000;
        double maxScalar = -1000000000;

        for (Vector poly1Vertex : vertices) {
            double scalarProj = proj.dot(poly1Vertex);

            minScalar = Math.min(minScalar, scalarProj);
            maxScalar = Math.max(maxScalar, scalarProj);
        }

        return Vector.of(minScalar, maxScalar);
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
            Vector bounds1 = getProjectionBounds(poly1Vertices, normal);

            // Project all poly2's vertices onto the normal and get its bounds
            Vector bounds2 = getProjectionBounds(poly2Vertices, normal);

            boolean isIntersecting = bounds1.getX() < bounds2.getY() && bounds1.getY() > bounds2.getX();

            if (isIntersecting) {
                double mtd;
                if (bounds1.getX() < bounds2.getY()) {
                    mtd = bounds1.getY() - bounds2.getX();

                } else {
                    mtd = bounds2.getY() - bounds1.getX();
                }

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
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

    /**
     * Computes the MTV using the separating axis theorem
     * It returns the MTV of polygon2 w.r.t polygon 1
     * If there is no intersection, then it returns NULL
     */
    private static Vector getSeparatingAxis(List<Vector> poly1Vertices, List<Vector> poly2Vertices) {

        double bestMtd = 10000000;
        Vector bestMtv = null;

        // Going through each side in poly1 and see if poly2 intersects it
        for (int i = 0; i < poly1Vertices.size(); i++) {

            Vector sidePt1 = poly1Vertices.get(i);
            Vector sidePt2 = i + 1 < poly1Vertices.size() ? poly1Vertices.get(i + 1) : poly1Vertices.get(0);

            Vector normal = Vector.of(sidePt2.getY() - sidePt1.getY(), -1 * (sidePt2.getX() - sidePt1.getX())).normalize();

            // Project all poly1's vertices onto the normal and get its bounds
            double minT1 = 1000000000;
            double maxT1 = -1000000000;

            for (Vector poly1Vertex : poly1Vertices) {
                double scalarProj = normal.dot(poly1Vertex.minus(sidePt1));

                minT1 = Math.min(minT1, scalarProj);
                maxT1 = Math.max(maxT1, scalarProj);
            }

            // Project all poly2's vertices onto the normal and get its bounds
            double minT2 = 1000000000;
            double maxT2 = -1000000000;

            for (Vector poly2Vertex : poly2Vertices) {
                double scalarProj = normal.dot(poly2Vertex.minus(sidePt1));

                minT2 = Math.min(minT2, scalarProj);
                maxT2 = Math.max(maxT2, scalarProj);
            }

            // See if the bounds intersect
            double minT = Math.max(minT1, minT2);
            double maxT = Math.min(maxT1, maxT2);

            boolean isNotIntersect = minT >= maxT;
            if (isNotIntersect) {
                return null;

            } else {
                double mtd = maxT - minT;

                if (mtd < bestMtd) {
                    bestMtd = mtd;
                    bestMtv = normal.multiply(mtd);
                }
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

        System.out.println("mtv1: " + mtv1 + " mtv2: " + mtv2);

        Vector bestMtv;
        Vector body1Mtv;
        Vector body2Mtv;

        if (mtv1.norm2() > mtv2.norm2()) {
            bestMtv = mtv2;
            body1Mtv = mtv2.multiply(f1);
            body2Mtv = mtv2.multiply(-1).multiply(f2);

        } else {
            bestMtv = mtv1;
            body1Mtv = mtv1.multiply(f1);
            body2Mtv = mtv1.multiply(-1).multiply(f2);
        }

        System.out.println("bestMtv: " + bestMtv);

        return new PCollisionResult(true, body1Mtv, body2Mtv, bestMtv);
    }
}
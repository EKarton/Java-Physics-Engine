package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.utils.Vector;

import java.util.ArrayList;
import java.util.List;

public class PPolyPolyCollision {

    /**
     * A class used to store the min/max scalar values when projecting
     * points onto a line
     */
    private static class Bounds {

        private final double min;
        private final double max;

        public Bounds(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }
    }

    /**
     * Get the minimum and max. bounds when projecting the vertices onto a line
     * @param vertices the vertices
     * @param proj the line to project the vertices on
     * @return the min/max distances away from the projected line
     */
    private static Bounds getProjectionBounds(List<Vector> vertices, Vector proj) {
        double minScalar = 1000000000;
        double maxScalar = -1000000000;

        for (Vector poly1Vertex : vertices) {
            double scalarProj = proj.dot(poly1Vertex);

            if (scalarProj < minScalar) {
                minScalar = scalarProj;
            }

            if (scalarProj > maxScalar) {
                maxScalar = scalarProj;
            }
        }

        return new Bounds(minScalar, maxScalar);
    }

    /**
     * Computes the MTV for polygon2 using the separating axis theorem
     * It also computes the contact point on polygon2
     *
     * @param poly1Vertices the vertices for polygon1
     * @param poly2Vertices the vertices for polygon2
     * @return the mtv
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
            Bounds bounds1 = getProjectionBounds(poly1Vertices, normal);

            // Project all poly2's vertices onto the normal and get its bounds
            Bounds bounds2 = getProjectionBounds(poly2Vertices, normal);

            boolean isIntersecting = bounds1.getMin() < bounds2.getMax() && bounds1.getMax() > bounds2.getMin();

            if (isIntersecting) {
                double mtd;

                if (bounds1.getMin() < bounds2.getMax()) {
                    mtd = bounds1.getMax() - bounds2.getMin();

                } else {
                    mtd = bounds2.getMax() - bounds1.getMin();
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

    /**
     * Finds the contact point of the collision based on the polygon and its mtv
     *
     * The algorithm:
     *  Construct a ray starting from poly1 and a direction mtv
     *  For each point, project it onto the ray
     *  For each projected point, find the projected point that is the farthest
     *  Add the farthest projected point with body1Mtv
     *
     * @param poly1 the vertices of a moving polygon
     * @param body1Mtv the mtv of that moving polygon
     * @return the contact point
     */
    private static Vector getContactPt(PPolygon poly1, Vector body1Mtv) {
        Vector origin = poly1.getCenterPt();
        Vector rayDir = body1Mtv.normalize().multiply(-1);

        double maxProj = -1000000000;
        Vector maxProjPt = null;

        for (Vector vertex : poly1.getVertices()) {

            double scalarProj = rayDir.dot(vertex.minus(origin));
            Vector projectedPt = origin.add(rayDir.normalize().multiply(scalarProj));

            if (scalarProj > maxProj) {
                maxProj = scalarProj;
                maxProjPt = projectedPt;
            }
        }

        return maxProjPt.add(body1Mtv);
    }

    public static PCollisionResult doBodiesCollide(PPolygon body1, PPolygon body2) {

        ArrayList<Vector> poly1Vertices = body1.getVertices();
        ArrayList<Vector> poly2Vertices = body2.getVertices();

        // The weighted velocities
        double f1 = body1.isMoving() ? body1.getVelocity().norm2() / (body1.getVelocity().norm2() + body2.getVelocity().norm2()) : 0;
        double f2 = body2.isMoving() ? body2.getVelocity().norm2() / (body1.getVelocity().norm2() + body2.getVelocity().norm2()) : 0;

        // Note: with SAT we can terminate early as soon as there is a separating axis
        Vector mtv1 = getSeparatingAxis(poly1Vertices, poly2Vertices);

        if (mtv1 == null) {
            return new PCollisionResult(false, null, null, null, null);
        }

        Vector mtv2 = getSeparatingAxis(poly2Vertices, poly1Vertices);

        if (mtv2 == null) {
            return new PCollisionResult(false, null, null, null, null);
        }

        Vector bestMtv;
        Vector body1Mtv;
        Vector body2Mtv;

        if (mtv2.norm1() <= mtv1.norm1()) {
            bestMtv = mtv2;
            body1Mtv = mtv2.multiply(f1);
            body2Mtv = mtv2.multiply(-1).multiply(f2);

        } else {
            bestMtv = mtv1;
            body1Mtv = mtv1.multiply(-1).multiply(f1);
            body2Mtv = mtv1.multiply(f2);
        }

        Vector contactPt;
        if (body1Mtv.norm1() == 0) {
            contactPt = getContactPt(body2, body2Mtv);

        } else {
            contactPt = getContactPt(body1, body1Mtv);
        }


        return new PCollisionResult(true, body1Mtv, body2Mtv, bestMtv, contactPt);
    }
}
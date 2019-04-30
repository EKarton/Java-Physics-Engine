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
    private static Vector circleCenterPt;
    private static double circleRadius;
    private static ArrayList<Vector> polyVertices;

    /**
     * Returns the points on the circle that are tangents to line only defined by its slope
     * @param normalSlope The slope of the line
     * @return Returns an array of points that are tangents to the line defined only by its slope
     */
    private static Vector[] getTangentPtsOfCircle(double normalSlope) {
        Vector[] intersectionPts = new Vector[2];
        intersectionPts[0] = new Vector(0, 0);
        intersectionPts[1] = new Vector(0, 0);

        // If the normal is vertical:
        if (Double.isInfinite(Math.abs(normalSlope))) {
            intersectionPts[0].setX(circleCenterPt.getX());
            intersectionPts[1].setX(circleCenterPt.getX());
            intersectionPts[0].setY(circleCenterPt.getY() + circleRadius);
            intersectionPts[1].setY(circleCenterPt.getY() - circleRadius);
        }

        // If the normal is horizontal
        else if (Math.abs(normalSlope) == 0) {
            // // // System.out.println("I am here!");
            intersectionPts[0].setX(circleCenterPt.getX() + circleRadius);
            intersectionPts[1].setX(circleCenterPt.getX() - circleRadius);
            intersectionPts[0].setY(circleCenterPt.getY());
            intersectionPts[1].setY(circleCenterPt.getY());
        } else {
            // Getting info of line passing through center of circle
            double yIntercept = circleCenterPt.getY() - (normalSlope * circleCenterPt.getX());

            // Getting the quadratic formula to calculate the tangent
            double a = (1 + (normalSlope * normalSlope));
            double b = -((2 * circleCenterPt.getX()) - (2 * normalSlope * yIntercept) + (2 * normalSlope * circleCenterPt.getY()));
            double c = (circleCenterPt.getX() * circleCenterPt.getX()) + (yIntercept * yIntercept) - (2 * circleCenterPt.getY() * yIntercept) + (circleCenterPt.getY() * circleCenterPt.getY()) - (circleRadius * circleRadius);

            // Using the quadratic formula to isolate 'x's and find the two tangent points
            double sqrtResult = Math.sqrt(b * b - (4 * a * c));
            intersectionPts[0].setX((-b + sqrtResult) / (2 * a));
            intersectionPts[0].setY((normalSlope * intersectionPts[0].getX()) + yIntercept);
            intersectionPts[1].setX((-b - sqrtResult) / (2 * a));
            intersectionPts[1].setY((normalSlope * intersectionPts[1].getX()) + yIntercept);
        }
        return intersectionPts;
    }

    /**
     * Post-condition: Returns true if a separating line exist between a circle and a polygon based on a normal.
     *                  Also returns the MTD from the normal if there is no separating line
     * @param normalSlope The slope of the normal
     * @param bestOverlap The MTD from the normal
     * @return Returns true if there is a separating line between a circle and a polygon based on a normal. Also returns the MTD from the "bestOverlap" parameter
     */
    private static boolean isSeparatingLineExist(double normalSlope, Vector bestOverlap) {
        // Projecting the poly's points to the normal and keeping track of its min/max
        Vector minPolyValues = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
        Vector maxPolyValues = new Vector(-Double.MIN_VALUE, -Double.MIN_VALUE);
        for (Vector vertex : polyVertices) {
            Vector poi = projectPointToLine(normalSlope, 13, vertex);

            // Checking if the current POI is the new min/max x and y coordinate
            if (poi.getX() < minPolyValues.getX()) minPolyValues.setX(poi.getX());
            if (poi.getY() < minPolyValues.getY()) minPolyValues.setY(poi.getY());
            if (poi.getX() > maxPolyValues.getX()) maxPolyValues.setX(poi.getX());
            if (poi.getY() > maxPolyValues.getY()) maxPolyValues.setY(poi.getY());
        }

        // Getting the min/max x and y values of projecting the tangent of the circle
        Vector minCircleValues = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
        Vector maxCircleValues = new Vector(-Double.MIN_VALUE, -Double.MIN_VALUE);
        Vector[] tangentPts = getTangentPtsOfCircle(normalSlope);
        for (Vector tangentPt : tangentPts) {
            Vector poi = projectPointToLine(normalSlope, 13, tangentPt);

            // Checking if the current POI is the new min/max x and y coordinate
            if (poi.getX() < minCircleValues.getX()) minCircleValues.setX(poi.getX());
            if (poi.getY() < minCircleValues.getY()) minCircleValues.setY(poi.getY());
            if (poi.getX() > maxCircleValues.getX()) maxCircleValues.setX(poi.getX());
            if (poi.getY() > maxCircleValues.getY()) maxCircleValues.setY(poi.getY());
        }

        return !isOverlap(minCircleValues, maxCircleValues, minPolyValues, maxPolyValues, bestOverlap);
    }

    /**
     * Post-condition: Returns true if the circle and polygon is intersecting; else false.
     * Also returns the MTD of the circle and the polygon if they are intersecting
     * @param mtd The MTD (minimum translation vector) of the circle and polygon
     * @return Returns whether the circle and the polygon is intersecting; and the MTD stored in the parameter "mtd"
     */
    private static boolean isIntersecting(Vector mtd) {
        mtd.setXY(0, 0);
        Vector bestOverlap = null;
        double bestOverlapDistance = Double.MAX_VALUE;

        // Going through all the sides in the polygon
        for (int i = 0; i < polyVertices.size(); i++) {

            // Getting the two points that make up an edge
            int sidePt1Index = i;
            int sidePt2Index = i + 1;
            if (i == polyVertices.size() - 1)
                sidePt2Index = 0;

            // Getting the normal slope of the current edge
            Vector sidePt1 = polyVertices.get(sidePt1Index);
            Vector sidePt2 = polyVertices.get(sidePt2Index);
            double normalSlope = -1 / ((sidePt2.getY() - sidePt1.getY()) / (sidePt2.getX() - sidePt1.getX()));

            // Getting the current overlap from the current edge
            Vector curBestOverlap = new Vector(0, 0);
            if (isSeparatingLineExist(normalSlope, curBestOverlap)) {  // <- SAT algorithm: If there is a separating line between the polygons, there is no collision
                return false;
            }

            // Calculating the MTD
            double curBestOverlapDistance = (curBestOverlap.getX() * curBestOverlap.getX()) + (curBestOverlap.getY() * curBestOverlap.getY());

            if (curBestOverlapDistance < bestOverlapDistance) {
                bestOverlapDistance = curBestOverlapDistance;
                bestOverlap = curBestOverlap;
            }
        }

        if (bestOverlap == null)
            return false;

        mtd.setXY(bestOverlap.getX(), bestOverlap.getY());
        return true;
    }

    /**
     * Pre-condition: "circle1", "circle2", "circle1TransVector", "circle2TransVector", "mtd" must not be null
     * Post-condition: Determines whether a circle and polygon is colliding, and returns the displacements each body should move by as well as the minimum translation vector
     * @param circle The circle
     * @param poly The polygon
     * @param circleTransVector The displacement the circle should move by if they are colliding
     * @param polyTransVector The displacement the polygon should move by if they are colliding
     * @param mtd Returns the minimum translation vector from SAT algorithm
     * @return Returns true if the two circles are colliding; else false. Also returns the MTD which is stored in the "mtd" parameter
     */
    public static boolean doBodiesCollide(PCircle circle, PPolygon poly, Vector circleTransVector, Vector polyTransVector, Vector mtd) {
        // Saving the properties of the bodies to the global variables
        circleCenterPt = circle.getCenterPt();
        circleRadius = circle.getRadius();
        Vector circleVelocity = circle.getVelocity();
        polyVertices = poly.getVertices();
        Vector polyCenterPt = poly.getCenterPt();
        Vector polyVelocity = poly.getVelocity();

        // The translation vectors for both bodies will be 0 when there is no intersection
        circleTransVector.setXY(0, 0);
        polyTransVector.setXY(0, 0);

        // Determining if the bodies intersect or not
        if (isIntersecting(mtd)) {
            // If the two objects are not touching, they are not colliding!
            if (mtd.getX() == 0 && mtd.getY() == 0) {
                return false;
            }

            Vector circleTrans = getTranslationVectors(mtd, circleCenterPt, polyCenterPt, circleVelocity, polyVelocity);
            Vector polyTrans = getTranslationVectors(mtd, polyCenterPt, circleCenterPt, polyVelocity, circleVelocity);

            circleTransVector.setXY(circleTrans.getX(), circleTrans.getY());
            polyTransVector.setXY(polyTrans.getX(), polyTrans.getY());

            return true;
        }
        return false;
    }
}
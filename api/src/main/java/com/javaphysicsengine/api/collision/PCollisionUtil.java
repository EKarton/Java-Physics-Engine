package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.utils.Vector;

public class PCollisionUtil {
    /**
     * Projects a point to a line defined by its slope and y intercept.
     *
     * @param slopeOfLine The slope of the line
     * @param yInterceptOfLine The y intercept of the line
     * @param point The point to project
     * @return The projected point
     */
    public static Vector projectPointToLine(double slopeOfLine, double yInterceptOfLine, Vector point) {
        double perpendicularSlope = -1 / slopeOfLine;
        double b = point.getY() - (perpendicularSlope * point.getX());

        // Getting point of intersection
        double x = (b - yInterceptOfLine) / (slopeOfLine - perpendicularSlope);
        double y = (slopeOfLine * x) + yInterceptOfLine;

        // Special cases where when the slope is infinity (Line is vertical), it will affect the intersection point
        if (Double.isInfinite(Math.abs(slopeOfLine))) {
            x = 0;
            y = point.getY();
        }

        // Special case where when the line is horizontal, it will affect the intersection point
        else if (Math.abs(slopeOfLine) == 0) {
            x = point.getX();
            y = yInterceptOfLine;
        }

        return new Vector(x, y);
    }

    /**
     * Checks if two domains interect one another. Also calculates the amount of overlap between them
     *
     * @param min1Values The minimum x and y values for the first domain
     * @param max1Values The maximum x and y values for the first domain
     * @param min2Values The minimum x and y values for the second domain
     * @param max2Values The maximum x and y values for the seecond domain
     * @param overlap The overlap between the two domains
     * @return {@code true} if the two domains overlap; else {@code false}.
     *         Also returns the amount of overlap in the "overlap" parameter
     */
    public static boolean doDomainsIntersect(Vector min1Values, Vector max1Values, Vector min2Values, Vector max2Values, Vector overlap) {
        // Making the overlap to 0 when domain and ranges of poly1 and poly2 are not intersecting
        overlap.setXY(0, 0);

        // Checking if the x components overlap
        double overlapX1 = max1Values.getX() - min2Values.getX(); //maxX1 - minX2;
        double overlapX2 = max2Values.getX() - min1Values.getX(); //maxX2 - minX1;
        if (overlapX1 < 0 || overlapX2 < 0)
            return false;

        // Checking if the y components overlap
        double overlapY1 = max1Values.getY() - min2Values.getY(); //maxY1 - minY2;
        double overlapY2 = max2Values.getY() - min1Values.getY(); //maxY2 - minY1;
        if (overlapY1 < 0 || overlapY2 < 0)
            return false;

        // Calculating the best overlap by taking the min overlap for each component
        overlap.setX(Math.min(overlapX1, overlapX2));
        overlap.setY(Math.min(overlapY1, overlapY2));
        return true;
    }
}

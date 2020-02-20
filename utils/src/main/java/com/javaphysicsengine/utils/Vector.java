/*
 * A class that stores a point (x and y coordinate), or a direction and magnitude
 * @author Emilio Kartono
 * @version September 13, 2015
 */

package com.javaphysicsengine.utils;

public class Vector {

    public static final double EQUALITY_ACCURACY = 0.0001;

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector oldVector) {
        this.x = oldVector.x;
        this.y = oldVector.y;
    }

    public static Vector of(double x, double y) {
        return new Vector(x, y);
    }

    public static Vector minus(Vector v2, Vector v1) {
        return new Vector(v2.getX() - v1.getX(), v2.getY() - v1.getY());
    }

    public Vector minus(Vector v1) {
        return Vector.minus(this, v1);
    }

    public static Vector min(Vector... vectors) {
        Vector minVector = Vector.of(Double.MAX_VALUE, Double.MAX_VALUE);
        for (Vector v : vectors) {
            minVector.setXY(Math.min(minVector.getX(), v.getX()), Math.min(minVector.getY(), v.getY()));
        }
        return minVector;
    }

    public static Vector max(Vector... vectors) {
        Vector minVector = Vector.of(Double.MIN_VALUE, Double.MIN_VALUE);
        for (Vector v : vectors) {
            minVector.setXY(Math.max(minVector.getX(), v.getX()), Math.max(minVector.getY(), v.getY()));
        }
        return minVector;
    }

    public static double dot(Vector v1, Vector v2) {
        return (v1.getX() * v2.getX()) + (v1.getY() * v2.getY());
    }

    public double dot(Vector v2) {
        return Vector.dot(this, v2);
    }

    public static Vector multiply(Vector v, double amount) {
        return new Vector(v.x * amount, v.y * amount);
    }

    public Vector multiply(double amount) {
        return Vector.multiply(this, amount);
    }

    public static Vector multiply(Vector v1, Vector v2) {
        return new Vector(v1.x * v2.x, v1.y * v2.y);
    }

    public Vector multiply(Vector v) {
        return Vector.multiply(this, v);
    }

    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    public Vector add(Vector v2) {
        return Vector.add(this, v2);
    }

    public static double cross(Vector v1, Vector v2) {
        return v1.x * v2.y - v1.y * v2.x;
    }

    public static Vector cross(Vector v1, double amount) {
        return Vector.of(v1.y * -amount, v1.x * amount);
    }

    public double cross(Vector v2) {
        return Vector.cross(this, v2);
    }

    public Vector cross(double amount) {
        return Vector.cross(this, amount);
    }

    public double norm1() {
        return (x * x) + (y * y);
    }

    public double norm2() {
        return Math.sqrt((x * x) + (y * y));
    }

    public void setLength(double length) {
        normalized();
        x *= length;
        y *= length;
    }

    public void normalized() {
        double curLengthOfVector = norm2();
        x /= curLengthOfVector;
        y /= curLengthOfVector;
    }

    public Vector normalize() {
        double curLengthOfVector = norm2();
        return Vector.of(x / curLengthOfVector, y / curLengthOfVector);
    }

    public double getX() {
        return this.x;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    public void setXY(double newX, double newY) {
        this.x = newX;
        this.y = newY;
    }

    public void set(Vector v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Determines if two vectors are identical, within the range of {@code Vector#EQUALITY_ACCURACY}
     * @param object the object
     * @return {@code true} if the two vectors are identical; else {@code false}
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Vector) {
            Vector vector = (Vector) object;

            boolean isXEqual = Math.abs(this.x - vector.x) < EQUALITY_ACCURACY;
            boolean isYEqual = Math.abs(this.y - vector.y) < EQUALITY_ACCURACY;
            return isXEqual && isYEqual;
        }
        return false;
    }

    /**
     * Returns the contents of this vector in a string.
     * Note that this should only be used for debugging purposes.
     *
     * @return The contents of this vector.
     */
    public String toString() {
        return x + ", " + y;
    }
}
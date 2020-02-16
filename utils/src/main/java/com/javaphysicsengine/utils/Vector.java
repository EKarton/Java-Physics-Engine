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

    /**
     * Creates a vector object from a x and y value
     * @param x The x coordiante of a vector
     * @param y The y coordinate of a vector
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a vector object from a x and y value
     * @param x The x coordiante of a vector
     * @param y The y coordinate of a vector
     * @return A vector object with the specified x and y value
     */
    public static Vector of(double x, double y) {
        return new Vector(x, y);
    }

    /**
     * Subtracts the two vectors
     * @param v2 The first vector
     * @param v1 The second vector
     * @return The resultant vector
     */
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

    /**
     * Performs the dot product of two vectors
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The dot product of two vectors
     */
    public static double dot(Vector v1, Vector v2) {
        return (v1.getX() * v2.getX()) + (v1.getY() * v2.getY());
    }

    public double dot(Vector v2) {
        return Vector.dot(this, v2);
    }

    /**
     * Performs scalar multiplication on a vector
     * @param v The vector
     * @param amount The amount to multiply each component in the vector by
     * @return The resultant vector
     */
    public static Vector multiply(Vector v, double amount) {
        return new Vector(v.getX() * amount, v.getY() * amount);
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

    /**
     * Adds two vectors
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The resultant vector
     */
    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    public Vector add(Vector v2) {
        return Vector.add(this, v2);
    }

    public double norm() {
        return (x * x) + (y * y);
    }

    public double norm2() {
        return Math.sqrt((x * x) + (y * y));
    }

    /**
     * Scales up/down the vector to a given length
     * @param length The length of the new vector
     * @deprecated Use vector.normalize().multiply(length);
     */
    public void setLength(double length) {
        normalized();
        x *= length;
        y *= length;
    }

    /**
     * Scales the vector up/down so that the length of the vector is 1.
     * Pre-condition: The x and y values must not be 0
     */
    public void normalized() {
        double curLengthOfVector = norm2();
        x /= curLengthOfVector;
        y /= curLengthOfVector;
    }

    public Vector normalize() {
        double curLengthOfVector = norm2();
        return Vector.of(x / curLengthOfVector, y / curLengthOfVector);
    }

    /**
     * Gets the x component of the vector
     * @return The x component of the vector
     */
    public double getX() {
        return this.x;
    }

    /**
     * Set the x component of the vector
     * @param newX the new X value
     */
    public void setX(double newX) {
        this.x = newX;
    }

    /**
     * Gets the y component of the vector
     * @return The y component of the vector
     */
    public double getY() {
        return this.y;
    }

    /**
     * Sets the y component of the vector
     * @param newY The new y vector
     */
    public void setY(double newY) {
        this.y = newY;
    }

    /**
     * Sets the x and y component of the vector
     * @param newX The new X component
     * @param newY The new Y component
     */
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
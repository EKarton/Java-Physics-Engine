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

    // This is the cached vector length.
    private double length = -1;

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
    public static Vector subtract(Vector v2, Vector v1) {
        return new Vector(v2.getX() - v1.getX(), v2.getY() - v1.getY());
    }

    /**
     * Performs the dot product of two vectors
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The dot product of two vectors
     */
    public static double dotProduct(Vector v1, Vector v2) {
        return (v1.getX() * v2.getX()) + (v1.getY() * v2.getY());
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

    /**
     * Adds two vectors
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The resultant vector
     */
    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    /**
     * Calculates and returns the length of the vector to the origin (a.k.a magnitude of the vector)
     * @return the magnitude of the vector
     */
    public double getLength() {
        // If the length was not computed before
        if (length == -1)
            length = Math.sqrt((x * x) + (y * y));

        return length;
    }

    /**
     * Scales up/down the vector to a given length
     * @param length The length of the new vector
     */
    public void setLength(double length) {
        normalise();
        x *= length;
        y *= length;
        this.length = length;
    }

    /**
     * Scales the vector up/down so that the length of the vector is 1.
     * Pre-condition: The x and y values must not be 0
    */
    public void normalise() {
        double curLengthOfVector = getLength();
        x /= curLengthOfVector;
        y /= curLengthOfVector;
        this.length = 1;
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
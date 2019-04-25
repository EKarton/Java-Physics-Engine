/*
  Name: Vector
  A class that stores a point (x and y coordinate), or a direction and magnitude
  @author Emilio Kartono
  @version September 13, 2015
*/

package com.javaphysicsengine.utils;

public class Vector
{
  private double x;
  private double y;
  private double length = -1;
  
  /*
    Post-condition: Creates a vector object from a x and y value
    @param x The x coordiante of a vector
    @param y The y coordinate of a vector
  */
  public Vector(double x, double y)
  {
    this.x = x;
    this.y = y;
  }
  
  /*
    Post-condition: Calculates and returns the length of the vector to the origin (a.k.a magnitude of the vector)
    @return Returns the length of this vector to the origin (0, 0)
  */
  public double getLength()
  {
    // If the length was not computed before
    if (length == -1)
      length = Math.sqrt((x * x) + (y * y));
    
    return length;
  }
  
  /*
    Pre-condition: The length must be greater than 0
    Post-condition: Resizes the vector with the given length
  */
  public void setLength(double length)
  {
    normalise();
    x *= length;
    y *= length;
    this.length = length;
  }
  
  /*
    Pre-condition: The x and y values of the vector must not be 0
    Post-condition: Sets the length of the vector to 1
  */
  public void normalise()
  {
    double curLengthOfVector = getLength();
    x /= curLengthOfVector;
    y /= curLengthOfVector;
    this.length = 1;
  }
  
  /*
    @return Returns the x component of the vector
  */
  public double getX() { return this.x; }
  
  /*
    @return Returns the y component of the vector
  */
  public double getY() { return this.y; }
  
  /*
    Post-condition: Set the x component of the vector
    @param newX The x component of the new vector
  */
  public void setX(double newX) { this.x = newX; }
  
  /*
    Post-condition: Sets the y component of the vector 
    @param newY The y component of the new vector
  */
  public void setY(double newY) { this.y = newY; }
  
  public void setXY(double newX, double newY)
  {
    this.x = newX;
    this.y = newY;
  }
  
  /*
    Pre-condition: Vectors must not be of null values!
    Post-condition: Subtracts the two vectors
    @param v2 The first vector in the equation (v2 - v1)
    @param v1 The second vector in the equation (v2 - v1)
  */
  public static Vector subtract(Vector v2, Vector v1)
  {
    return new Vector(v2.getX() - v1.getX(), v2.getY() - v1.getY());
  }
  
  public static double dotProduct(Vector v1, Vector v2)
  {
    return (v1.getX() * v2.getX()) + (v1.getY() * v2.getY());
  }
  
  public static Vector multiply(Vector v, double amount)
  {
    return new Vector(v.getX() * amount, v.getY() * amount);
  }
  
  public static Vector add(Vector v1, Vector v2)
  {
    return new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
  }
  
  public boolean equals(Vector vector)
  {
    if (this.x == vector.x && this.y == vector.y)
      return true;
    return false;
  }
  
  public String toString()
  {
    return x + ", " + y;
  }
}
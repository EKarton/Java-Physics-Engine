/*
  Name: Trigonometry
  A class that deals with trigonometric-related functions where the angle is in terms of degrees
  @author Emilio Kartono
  @version December 21, 2015
*/

package com.javaphysicsengine.utils;

public class Trigonometry
{
  /*
    Post-condition: Returns the sin of an angle in degrees
    @param The angle, in degrees
    @return The sin of the angle in degrees
  */
  public static double sin(double angleInDegrees)
  {
    double angleInRadians = Math.toRadians(angleInDegrees);
    double answer = Math.sin(angleInRadians);
    return answer;
  }
  
  /*
    Post-condition: Returns the cos of an angle in degrees
    @param The angle, in degrees
    @return The cos of the angle in degrees
  */
  public static double cos(double angleInDegrees)
  {
    double angleInRadians = Math.toRadians(angleInDegrees);
    double answer = Math.cos(angleInRadians);
    return answer;
  }
  
  /*
    Post-condition: Returns the tan of an angle in degrees
    @param The angle, in degrees
    @return The tan of the angle in degrees
  */
  public static double tan(double angleInDegrees)
  {
    double angleInRadians = Math.toRadians(angleInDegrees);
    double answer = Math.tan(angleInRadians);
    return answer;
  }
  
  /*
    Post-condition: Returns the inverse of tan in degrees
    @param value The value you want to perform on the inverse of tan
    @return The angle, in degrees
  */
  public static double inverseOfTan(double value)
  {
    double angleInRadians = Math.atan(value);
    double angleInDegrees = Math.toDegrees(angleInRadians);
    return angleInDegrees;
  }
  
  /*
    Post-condition: Returns the inverse of cos in degrees
    @param value The value you want to perform on the inverse of cos
    @return The angle, in degrees
  */
  public static double inverseOfCos(double value)
  {
    double angleInRadians = Math.acos(value);
    double angleInDegrees = Math.toDegrees(angleInRadians);
    return angleInDegrees;
  }
  
  /*
    Post-condition: Returns the inverse of sin in degrees
    @param value The value you want to perform on the inverse of sin
    @return The angle, in degrees
  */
  public static double inverseOfSin(double value)
  {
    double angleInRadians = Math.asin(value);
    double angleInDegrees = Math.toDegrees(angleInRadians);
    return angleInDegrees;
  }
  
  /*
    Pre-condition: The beta angle must be positive
    Post-condition: Converts a beta angle (angle 0 - 90 degrees) to a theta angle (0 - 360 degrees)

    @param xPt The x coordinate that creates the angle with the origin
    @param yPt The y coordinate that creates the angle with the origin
    @param betaAngle The angle you want to make it into a theta angle
    
    @return Returns the corresponding theta angle (0 - 360 degrees)
  */
  public static double convertBetaToThetaAngle(double xPt, double yPt, double betaAngle)
  {
    if (xPt > 0 && yPt > 0)  // <- First quadrant
      return betaAngle;
    
    else if (xPt < 0 && yPt > 0)  // <- Second quadrant
      return 180 - betaAngle;
    
    else if (xPt < 0 && yPt < 0)  // <- Third quadrant
      return 180 + betaAngle;
    
    else if (xPt > 0 && yPt < 0)  // <- Fourth quadrant
      return 360 - betaAngle;  
    
    else if (xPt > 0 && yPt == 0) // <- If it is at 0 degrees
      return 0;
    
    else if (xPt == 0 && yPt > 0) // <- If it is at 90 degrees
      return 90;
    
    else if (xPt < 0 && yPt == 0)  // <- If it is at 180 degrees
      return 180;
    
    else if (xPt == 0 && yPt < 0)  // <- If it is at 270 degrees
      return 270;
    
    else  // <- If it is at 360 degrees
      return 360;    
  }
}
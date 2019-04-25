/*
  Purpose: To detect whether two bodies collide with each other (why not put it in the PPolygon class? To make it more modular)
  Original Creation Date: January 1 2016
  @author Emilio Kartono
  @version January 15 2016
*/

package com.javaphysicsengine.api;

class PBoxBoxCollision
{
  /*
    Post-condition: Returns true if two bounding boxes collide; else false
    Pre-condition: "box1" and "box2" must not be null
    @param box1 The first bounding box
    @param box2 The second bounding box
    @return Returns true if colliding; else false
  */
  public static boolean doBodiesCollide(PBoundingBox box1, PBoundingBox box2)
  {
    if (box1.getMaxX() < box2.getMinX()) // If box 1 is left of box 2
      return false;
    
    if (box1.getMinX() > box2.getMaxX()) // If box 1 is right of box 2
      return false;
    
    if (box1.getMaxY() < box2.getMinY()) // If box 1 is below box 2
      return false;
    
    if (box1.getMinY() >= box2.getMaxY()) // If box 1 is above box 2
      return false;
    
    return true;
  }
}
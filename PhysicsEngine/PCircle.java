/*
  Purpose: To represent a PBody object with a circle shape
  Original Creation Date: January 1 2016
  @author Emilio Kartono
  @version January 15 2016
*/

package PhysicsEngine;

import ToolBox.Vector;
import java.awt.Graphics;
import java.awt.Color;

public class PCircle extends PBody
{
  // Geometric properties about the circle
  private double radius = 10;
  
  /*
     Pre-condition: "name" must not be null
     Post-condition: Creates a PCircle object with a certain name attached
     @param name The name of the circle
  */  
  public PCircle(String name)
  {
    super(name);
  }
  
  /*
     Pre-condition: "existing" must not be null
     Post-condition: Creates a PCircle object from a pre-existing PCircle object
     @param existingCircle A pre-existing PCircle object
  */  
  public PCircle(PCircle existingCircle)
  {
    super(existingCircle);
    this.radius = existingCircle.radius;
  }
  
  /*
     Pre-condition: "newRadius" must be greater than 0
     Post-condition: Sets the radius of the circle
     @param newCircle The new radius of the circle
  */  
  public void setRadius(double newRadius) { radius = newRadius; }
  
  /*
     Post-condition: Returns the radius of the circle
     @return Returns the radius of the circle
  */  
  public double getRadius() { return radius; }
  
  /*
     Post-condition: Rotates the circle
     @param newAngle The new angle of the circle
  */  
  public void rotate(double newAngle) { super.angle = newAngle; }
  
  /*
     Pre-condition: "move" must not be null
     Post-condition: Moves the circle to its new center point
     @param newCenterPt The new center point of the circle
  */  
  public void move(Vector newCenterPt) { centerPt = newCenterPt; }
  
  /*
     Pre-condition: "displacement" must not be null
     Post-condition: Moves the circle based on a certain displacement
     @param displacement The amount to move the circle by
  */  
  public void translate(Vector displacement) 
  {
    centerPt.setY(centerPt.getY() + displacement.getY());
    centerPt.setX(centerPt.getX() + displacement.getX());
  }
  
  /*
    Pre-condition: The "g" must not be null and the "windowHeight" must be greater than 0
    Post-condition: Draws the fill of the circle
    @param g The Graphics Object
    @param windowHeight The height of the window that is containing the circle being displayed
  */
  @Override
  public void drawFill(Graphics g, int windowHeight)
  {
    // Draws a circle in middle of coordinate
    int topLeftX = (int) (centerPt.getX() - radius);
    int topLeftY = windowHeight - (int) (centerPt.getY() + radius);
    g.setColor(getFillColor());
    g.fillOval(topLeftX, topLeftY, (int) (radius * 2), (int) (radius * 2));
    
    // Draw the center of mass
    super.drawFill(g, windowHeight);
  }
  
  /*
    Pre-condition: The "g" must not be null and the "windowHeight" must be greater than 0
    Post-condition: Draws the outline of the circle
    @param g The Graphics Object
    @param windowHeight The height of the window that is containing the circle being displayed
  */
  @Override
  public void drawOutline(Graphics g, int windowHeight)
  {
    // Draws a circle in middle of coordinate
    int topLeftX = (int) (centerPt.getX() - radius);
    int topLeftY = windowHeight - (int) (centerPt.getY() + radius);
    g.setColor(getOutlineColor());
    g.drawOval(topLeftX, topLeftY, (int) (radius * 2), (int) (radius * 2));
    
    // Draw the center of mass
    super.drawOutline(g, windowHeight);
  }
  
   /*
    Post-condition: Returns the properties of the circle in a string where each property is stored in format { propertyType:propertyValue; } (excluding the curly brackets)
    @return Returns the properties of the circle in a string
  */
  @Override
  public String toString()
  {
    return super.toString() + "Radius:" + radius + ";"; 
  }
}
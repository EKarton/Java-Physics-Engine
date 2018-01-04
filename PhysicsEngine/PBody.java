/*
  Purpose: To represent the basic properties of all physical objects with no specific geometric shape
  Original Creation Date: January 1 2016
  @author Emilio Kartono
  @version January 15 2016
*/

package PhysicsEngine;

import ToolBox.Vector;
import java.awt.Color;
import java.awt.Graphics;

public abstract class PBody
{    
  // The basic properties of a physical object
  protected double mass = 1;  // In kg
  protected Vector centerPt = new Vector(0, 0);  // <- This represents the center of mass
  private String name = "";
  
  // The kinematic properties of the physical object
  protected Vector netForce = new Vector(0, 0);
  protected Vector velocity = new Vector(0, 0);
  protected double angle = 0;
  private boolean isMoving = true;
  
  // The graphic properties of the object
  private Color outlineColor = Color.BLACK;
  private Color fillColor = Color.BLUE;
  
  /*
     Pre-condition: "bodyName" must not be null
     Post-condition: Creates a PBody object with a certain name attached
     @param bodyName The name of the body
  */  
  public PBody(String bodyName)
  {
    this.name = bodyName;
  } 
  
   /*
     Pre-condition: "existingBody" must not be null
     Post-condition: Creates a PBody object from an existing PBody object
     @param existingBody An already existing body
  */ 
  public PBody(PBody existingBody)
  {
    this.mass = existingBody.mass;
    this.centerPt = new Vector(existingBody.centerPt.getX(), existingBody.centerPt.getY());
    this.name = new String(existingBody.name);
    this.netForce = new Vector(existingBody.netForce.getX(), existingBody.netForce.getY());
    this.velocity = new Vector(existingBody.velocity.getX(), existingBody.velocity.getY());
    this.angle = existingBody.angle;
    this.isMoving = existingBody.isMoving;
    this.outlineColor = new Color(existingBody.outlineColor.getRed(), existingBody.outlineColor.getGreen(), existingBody.outlineColor.getBlue(), existingBody.outlineColor.getAlpha());
    this.fillColor = new Color(existingBody.fillColor.getRed(), existingBody.fillColor.getGreen(), existingBody.fillColor.getBlue(), existingBody.fillColor.getAlpha());
  }
  
  /*
    Post-condition: Returns the mass of the body
    @return The mass of the body
  */ 
  public double getMass() { return mass; }
  
   /*
    Post-condition: Returns the name of the body
    @return The name of the body
  */ 
  public String getName() { return name; }
  
  /*
    Post-condition: Returns the center point of the body
    @return The center point of the body
  */ 
  public Vector getCenterPt() { return centerPt; }
  
  /*
    Post-condition: Returns the net force of the body
    @return The net force of the body
  */ 
  public Vector getNetForce() { return netForce; }
  
  /*
    Post-condition: Returns the velocity of the body
    @return The velocity of the body
  */ 
  public Vector getVelocity() { return velocity; }
  
  /*
    Post-condition: Returns the angle of the body
    @return The angle of the body
  */ 
  public double getAngle() { return angle; }
  
  /*
    Post-condition: Returns the fill color of the body
    @return The fill color of the body
  */ 
  public Color getFillColor() { return fillColor; }
  
  /*
    Post-condition: Returns the outline color of the body
    @return The outline color of the body
  */ 
  public Color getOutlineColor() { return outlineColor; }
  
  /*
    Post-condition: Returns true if the body is moveable; else false
    @return Returns true if the body is moveable; else false
  */ 
  public boolean isMoving() { return isMoving; }
  
  /*
    Pre-condition: The mass must be greater than 0
    Post-condition: Sets the mass of the body
    @param newMass The new mass of the body
  */
  public void setMass(double newMass) { this.mass = newMass; }
  
  /*
    Pre-condition: The name must not be null
    Post-condition: Sets the name of the body
    @param newName The new name of the body
  */
  public void setName(String newName) { this.name = newName; }
  
  /*
    Pre-condition: The new velocity must not be null
    Post-condition: Sets the velocity of the body
    @param newVelocity The new velocity of the body
  */
  public void setVelocity(Vector newVelocity) { this.velocity = newVelocity; }
  
  /*
    Pre-condition: The new net force must not be null
    Post-condition: Sets the net force of the body
    @param newNetForce The new net force of the body
  */
  public void setNetForce(Vector newNetForce) { this.netForce = newNetForce; }
  
  /*
    Pre-condition: The new center point must not be null
    Post-condition: Sets the new center point of the body
    @param newCenterPt The new center point of the body
  */
  public void setCenterPt(Vector newCenterPt) { this.centerPt = newCenterPt; }
  
  /*
    Pre-condition: The new fill color must not be null
    Post-condition: Sets the fill color of the body
    @param color The new fill color of the body
  */
  public void setFillColor(Color color) { this.fillColor = color; }
  
  /*
    Pre-condition: The new outline color must not be null
    Post-condition: Sets the outline color of the body
    @param color The new outline color of the body
  */
  public void setOutlineColor(Color color) { this.outlineColor = color; }
  
  /*
    Post-condition: Sets whether the body can be moveable or not
    @param isMoving True if the body will be moving; else false
  */
  public void setMoveable(boolean isMoving) { this.isMoving = isMoving; }
  
  /*
    Pre-condition: The new center point must not be null
    Post-condition: Move the body to a new point such that the point is its new center point
    @param newCenterPt The new center point
  */
  public abstract void move(Vector newCenterPt);
  
  /*
    Post-condition: Rotate the body to a certain angle
    @param newAngle The angle of the body
  */
  public abstract void rotate(double newAngle);
  
  /*
    Pre-condition: The displacement must not be null
    Post-condition: Move the body based on a displacement
    @param displacement The displacement to move the body by a certain amount
  */
  public abstract void translate(Vector displacement);
  
  /*
    Pre-condition: The "g" must not be null and the "windowHeight" must be greater than 0
    Post-condition: Draws the outline of the center point
    @param g The Graphics Object
    @param windowHeight The height of the window containing the body being displayed
  */
  public void drawOutline(Graphics g, int windowHeight)
  {
    g.drawOval((int) centerPt.getX() - 2, windowHeight - (int) centerPt.getY() - 2, 4, 4);
  }
  
   /*
    Pre-condition: The "g" must not be null and the "windowHeight" must be greater than 0
    Post-condition: Draws the fill of the center point
    @param g The Graphics Object
    @param windowHeight The height of the window containing the body being displayed
  */
  public void drawFill(Graphics g, int windowHeight)
  {
    g.fillOval((int) centerPt.getX() - 2, windowHeight - (int) centerPt.getY()-  2, 4, 4);
  }
  
  /*
    Pre-condition: The "g" must not be null and the "windowHeight" must be greater than 0
    Post-condition: Draws the bounding box outline of the center point
    @param g The Graphics Object
    @param windowHeight The height of the window that is containing the body being displayed
  */
  public void drawBoundingBox(Graphics g, int windowHeight)
  {
    // Draw the bounding box of the center of mass
    g.drawRect((int) centerPt.getX() - 2, windowHeight - (int) centerPt.getY() - 2, 4, 4);
  }
  
  /*
    Post-condition: Returns the properties of the body in a string where each property is stored in format { propertyType:propertyValue; } (excluding the curly brackets)
    @return Returns the properties of the body in a string
  */
  @Override
  public String toString()
  {
    return "Name:" + name + ";Mass:" + mass + ";CenterPoint:" + centerPt.getX() + " " + centerPt.getY() + 
      ";Velocity:" + velocity.getX() + " " + velocity.getY() + ";Angle:" + angle + ";Is Moveable:" + isMoving + ";";
  }
}
/*
  Purpose: A class that simulates a list of bodies and constraints based on physics
  Original Creation Date: January 1 2016
  @author Emilio Kartono
  @version January 15 2016
*/

package com.javaphysicsengine.api;

import com.javaphysicsengine.utils.Vector;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class PWorld
{
  // List containing the physical bodies and joints
  private ArrayList<PBody> bodies = new ArrayList<PBody>();
  private ArrayList<PConstraints> constraints = new ArrayList<PConstraints>();
  
  // Physic properties about this world
  private final double GRAVITY_ACCELERATION = -9.81;
  private double scale = 100;  // <- The number of pixels that represent 1 meter
  private int heightOfWindow = 600;
  
  /*
     Post-condition: Creates a PWorld object
  */  
  public PWorld()
  {
    this.heightOfWindow = 600;
  }
  
  /*
     Post-condition: Returns the list of bodies added to the world
     @return Returns the list of bodies added to the world
  */  
  public ArrayList<PBody> getBodies() { return bodies; }  
  
  /*
     Post-condition: Returns the list of constraints added to the world
     @return Returns the list of constraints added to the world
  */  
  public ArrayList<PConstraints> getConstraints() { return constraints; }
  
  /*
     Post-condition: Adds the forces to all the bodies
  */
  private void addForces()
  {
    for (PBody body : bodies)
    {
      if (body.isMoving() == false)
        continue;
      
      // Adding gravitational force
      body.getNetForce().setY(body.getNetForce().getY() + GRAVITY_ACCELERATION * body.getMass());
    }
    
    // Adding forces from constraints
    for (PConstraints constraint : constraints)
      constraint.addTensionForce();
  }
  
  /*
     Post-condition: Translates all the bodies based on a certain time frame
     Pre-condition: "timeEllapsed" should be greater than 0
     @param timeEllapsed The time that has ellapsed
  */
  private void translateBodies(double timeEllapsed)
  {
    for (PBody body : bodies)
    {
      if (body.isMoving() == false)
        continue;
      
      // Getting the acceleration from force ( Force = mass * acceleration ) 
      double accelerationX = body.netForce.getX() / body.getMass();
      double accelerationY = body.netForce.getY() / body.getMass();
      
      // Calculating the new velocity ( V2 = V1 + at)
      body.velocity.setX(body.velocity.getX() + accelerationX * timeEllapsed);
      body.velocity.setY(body.velocity.getY() + accelerationY * timeEllapsed);
      
      // Getting the amount to translate by (Velocity = displacement / time)
      double dx = body.velocity.getX() * timeEllapsed * scale;
      double dy = body.velocity.getY() * timeEllapsed * scale;
      
      // Translate the body
      body.translate(new Vector(dx, dy));
    }
  }
  
  /*
    Post-condition: Calculates the impulse and applies them to two bodies
    Pre-condition: "body1", "body2", "mtd" should not be null
    @param body1 The first body involved in the collision
    @param body2 The second body involved in the collision
    @param mtd The MTD of the two bodies
  */
  private void calculateImpulse(PBody body1, PBody body2, Vector mtd)
  {
    double body1InversedMass = 1 / body1.getMass();
    double body2InversedMass = 1 / body2.getMass();
    
    if (body1.isMoving() == false)
      body1InversedMass = 0;
    
    if (body2.isMoving() == false)
      body2InversedMass = 0;
    
    Vector rv = Vector.subtract(body2.getVelocity(), body1.getVelocity());
    Vector normal = new Vector(mtd.getX(), mtd.getY());
    normal.normalise();
    double velAlongNormal = Vector.dotProduct(normal, rv);
    
    // Getting the total impulse of the two bodies as a system
    double coefficientOfResitution = 0.8;
    double totalImpulse = -(1.0f + coefficientOfResitution) * velAlongNormal;
    totalImpulse /= (body1InversedMass) + (body2InversedMass);            
    
    // Apply impulse to each object
    Vector impulse = Vector.multiply(normal, totalImpulse);
    body1.setVelocity(Vector.subtract(body1.getVelocity(), Vector.multiply(impulse, body1InversedMass))); 
    body2.setVelocity(Vector.add(body2.getVelocity(), Vector.multiply(impulse, body2InversedMass)));
    
    //// System.out.println("Body 1 Velocity: " + body1.getVelocity() + " Body 2 Velocity: " + body2.getVelocity());
  }
  
  /*
    Post-condition: Moves the two bodies by a slight bit after a collision occured (to prevent gittering)
    Pre-condition: "body1", "body2", "mtd" should not be null
    @param body1 The first body involved in the collision
    @param body2 The second body involved in the collision
    @param mtd The MTD of the two bodies
  */
  private void positionalCorrection(PBody body1, PBody body2, Vector mtd)
  {
    double body1InversedMass = 1 / body1.getMass();
    double body2InversedMass = 1 / body2.getMass();
    
    if (body1.isMoving() == false)
      body1InversedMass = 0;
    
    if (body2.isMoving() == false)
      body2InversedMass = 0;
    
    double penetrationDepth = mtd.getLength();
    Vector normal = new Vector(mtd.getX(), mtd.getY());
    normal.normalise();
    
    final double percent = 0.01f; // usually 20% to 80%
    final double slop = 0.1f; // usually 0.01 to 0.1
    Vector correction =  Vector.multiply(Vector.multiply(normal, percent), Math.max(penetrationDepth - slop, 0.0f) / (body1InversedMass + body2InversedMass));
    
    // Move the first body by a certain amount
    Vector body1Trans = new Vector(0, 0);
    body1Trans.setX(-correction.getX() * body1InversedMass);
    body1Trans.setY(-correction.getY() * body1InversedMass);
    body1.translate(body1Trans);
    
    // Move the second body by a certain amount
    Vector body2Trans = new Vector(0, 0);
    body2Trans.setX(correction.getX() * body2InversedMass);
    body2Trans.setY(correction.getY() * body2InversedMass);
    body2.translate(body2Trans);
  }
  
  /*
   *Post-condition: Simulates the bodies for a certain time
    Pre-condition: "timeEllapsed" should be greater than 0
    @param timeEllapsed The time that has ellapsed
  */
  public void simulate(double timeEllapsed)
  {
    // Clear all the forces from all the bodies
    for (PBody body : bodies)
    {
      body.getNetForce().setX(0);
      body.getNetForce().setY(0);
    }
    
    // Add the nessessary forces to all the bodies
    addForces();
    
    // Translate the bodies based on the forces
    translateBodies(timeEllapsed);
    
    // Check for collisions
    for (int i = 0; i < bodies.size(); i++)
    {
      for (int j = i + 1; j < bodies.size(); j++)
      {
        // If a circle and polygon collided
        if (bodies.get(i) instanceof PCircle && bodies.get(j) instanceof PPolygon)
        {
          PCircle circle = (PCircle) bodies.get(i);
          PPolygon poly = (PPolygon) bodies.get(j);
          
          Vector circleTrans = new Vector(0, 0);
          Vector polyTrans = new Vector(0, 0);
          Vector mtd = new Vector(0, 0);          
          
          if (PCirclePolyCollision.doBodiesCollide(circle, poly, circleTrans, polyTrans, mtd))
          {
            circle.translate(circleTrans);
            poly.translate(polyTrans);
            calculateImpulse(circle, poly, mtd);
            positionalCorrection(circle, poly, mtd);
          }
        }       
        
        // If a polygon and a circle collided
        else if (bodies.get(i) instanceof PPolygon && bodies.get(j) instanceof PCircle)
        {
          PCircle circle = (PCircle) bodies.get(j);
          PPolygon poly = (PPolygon) bodies.get(i);
          
          Vector circleTrans = new Vector(0, 0);
          Vector polyTrans = new Vector(0, 0);
          Vector mtd = new Vector(0, 0);
          
          if (PCirclePolyCollision.doBodiesCollide(circle, poly, circleTrans, polyTrans, mtd))
          {
            circle.translate(circleTrans);
            poly.translate(polyTrans);
            calculateImpulse(circle, poly, mtd);
            positionalCorrection(circle, poly, mtd);
          }
        }
        
        // If a circle and a circle collided
        else if (bodies.get(i) instanceof PCircle && bodies.get(j) instanceof PCircle)
        {
          PCircle circle1 = (PCircle) bodies.get(i);
          PCircle circle2 = (PCircle) bodies.get(j);
          Vector mtd = new Vector(0, 0);
          Vector circle1TransVector = new Vector(0, 0);
          Vector circle2TransVector = new Vector(0, 0);
          
          if (PCircleCircleCollision.doBodiesCollide(circle1, circle2, circle1TransVector, circle2TransVector, mtd))
          {
            // System.out.println("Circles collided!");
            circle1.translate(circle1TransVector);
            circle2.translate(circle2TransVector);
            calculateImpulse(circle1, circle2, mtd);
            positionalCorrection(circle1, circle2, mtd);
          }
        }
        
        // If a polygon and a polygon collided
        else if (bodies.get(i) instanceof PPolygon && bodies.get(j) instanceof PPolygon)
        {
          PPolygon body1 = (PPolygon) bodies.get(i);
          PPolygon body2 = (PPolygon) bodies.get(j);
          
          if (PBoxBoxCollision.doBodiesCollide(body1.getBoundingBox(), body2.getBoundingBox()))
          {
            Vector poly1Trans = new Vector(0, 0);
            Vector poly2Trans = new Vector(0, 0);
            Vector mtd = new Vector(0, 0);
            
            if (PPolyPolyCollision.doBodiesCollide(body1, body2, poly1Trans, poly2Trans, mtd))
            {
              //// System.out.println("Collision!! " + mtd);
              body1.translate(poly1Trans);
              body2.translate(poly2Trans);
              calculateImpulse(body1, body2, mtd);
              positionalCorrection(body1, body2, mtd);
            }
          }
        }
      }
    }
  }
  
  /*
    Pre-condition: The "g" must not be null
    Post-condition: Draws the bodies and constraints to the screen
    @param g The Graphics Object
  */
  public void draw(Graphics g)
  {
    g.setColor(Color.BLACK);
    for (PBody body : bodies)
      body.drawOutline(g, 600);
    
    // Drawing the constraints
    for (PConstraints constraint : constraints)
      constraint.drawConstraints(g, 600);
  }
}
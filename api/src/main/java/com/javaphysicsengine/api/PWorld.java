/*
 * Purpose: A class that simulates a list of bodies and constraints based on physics
 * Original Creation Date: January 1 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.api;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCollidable;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.collision.PCollisionResult;
import com.javaphysicsengine.utils.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PWorld {
    // Physic properties about this world
    private static final Vector GRAVITY = Vector.of(0, -0.1); //Vector.of(0, -9.81);
    private static final double SCALE = 100;

    // List containing the physical bodies and joints
    private ArrayList<PBody> bodies = new ArrayList<>();
    private ArrayList<PConstraints> constraints = new ArrayList<>();

    public ConcurrentLinkedQueue<Vector> pointsToDraw = new ConcurrentLinkedQueue<>();

    /**
     * Post-condition: Returns the list of bodies added to the world
     * @return Returns the list of bodies added to the world
     */
    public ArrayList<PBody> getBodies() {
        return bodies;
    }

    /**
     * Post-condition: Returns the list of constraints added to the world
     * @return Returns the list of constraints added to the world
     */
    public ArrayList<PConstraints> getConstraints() {
        return constraints;
    }

    /**
     * Pre-condition: The "g" must not be null
     * Post-condition: Draws the bodies and constraints to the screen
     * @param g The Graphics Object
     */
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        for (PBody body : bodies)
            body.drawOutline(g, 600);

        // Drawing the constraints
        for (PConstraints constraint : constraints) {
            constraint.drawConstraints(g, 600);
        }

        // Draw all of the points
        for (Vector pt : pointsToDraw) {
            int topLeftX = (int) (pt.getX() - 2);
            int topLeftY = 600 - (int) (pt.getY() + 2);
            g.setColor(Color.GREEN);
            g.fillOval(topLeftX, topLeftY, 2 * 2, 2 * 2);
        }
    }

    /**
     * Post-condition: Simulates the bodies for a certain time
     * Pre-condition: "timeEllapsed" should be greater than 0
     * @param timeEllapsed The time that has ellapsed in seconds
     */
    public void simulate(double timeEllapsed) {
        // Clear all the forces from all the bodies
        for (PBody body : bodies) {
            body.getNetForce().setX(0);
            body.getNetForce().setY(0);
        }

        // Add the nessessary forces to all the bodies
        addForces();

        // Translate the bodies based on the forces
        translateBodies(timeEllapsed);

        // Check for collisions
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                PBody firstBody = bodies.get(i);
                PBody secondBody = bodies.get(j);

                boolean isCollidable = (firstBody instanceof PCollidable && secondBody instanceof PCollidable) &&
                    (firstBody.isMoving() || secondBody.isMoving());

                if (isCollidable) {
                    PCollidable collidable1 = (PCollidable) firstBody;
                    PCollidable collidable2 = (PCollidable) secondBody;

                    PCollisionResult result = collidable1.hasCollidedWith(collidable2);

                    if (result.isHasCollided()) {
                        pointsToDraw.add(result.getContactPt());

                        firstBody.translate(result.getBody1Mtv());
                        secondBody.translate(result.getBody2Mtv());
                        calculateImpulse(firstBody, secondBody, result.getMtv(), result.getContactPt());
                        positionalCorrection(firstBody, secondBody, result.getMtv());
                    }
                }
            }
        }
    }

    /**
     * Post-condition: Adds the forces to all the bodies
     */
    private void addForces() {
        for (PBody body : bodies) {
            if (!body.isMoving()) {
                continue;
            }

            // Adding gravitational force
            Vector gravitationalForce = GRAVITY.multiply(body.getMass());
            Vector newNetForce = body.getNetForce().add(gravitationalForce);
            body.setNetForce(newNetForce);
        }

        // Adding forces from constraints
        for (PConstraints constraint : constraints) {
            constraint.addTensionForce();
        }
    }

    /**
     * Post-condition: Translates all the bodies based on a certain time frame
     * Pre-condition: "timeEllapsed" should be greater than 0
     * @param timeEllapsed The time that has ellapsed
     */
    private void translateBodies(double timeEllapsed) {
        for (PBody body : bodies) {
            if (!body.isMoving()) {
                continue;
            }

            // Getting the acceleration from force ( Force = mass * acceleration )
            Vector acceleration = body.getNetForce().multiply(1 / body.getMass());

            // Calculating the new velocity ( V' = V + at)
            Vector velocity = body.getVelocity().add(acceleration.multiply(timeEllapsed));
            body.setVelocity(velocity);

            // Getting the amount to translate by (Velocity = displacement / time)
            Vector translation = velocity.multiply(timeEllapsed).multiply(SCALE);
            body.translate(translation);

            // Calculating the new angular velocity (AngularVelocity' = AngularVelocity + torque * (1 / inertia) * time)
            double angularVelocity = body.getAngularVelocity() + body.getTorque() * (1 / body.getInertia()) * timeEllapsed;
            body.setAngularVelocity(angularVelocity);

            // Rotate the body (angle += AngularVelocity' * time)
            double newAngle = body.getAngle() + (body.getAngularVelocity() * timeEllapsed * SCALE);
//            double newAngle = (body.getAngularVelocity() * timeEllapsed * SCALE);
            body.rotate(newAngle);
        }
    }

    /**
     * Calculates the impulse and applies them to two bodies
     * @param body1 The first body involved in the collision
     * @param body2 The second body involved in the collision
     * @param mtv The MTV of the two bodies
     */
    private void calculateImpulse(PBody body1, PBody body2, Vector mtv, Vector contactPt) {
        double body1InversedMass = body1.isMoving() ? 1 / body1.getMass() : 0;
        double body2InversedMass = body2.isMoving() ? 1 / body2.getMass() : 0;

        double body1InverseInertia = body1.isMoving() ? 1 / body1.getInertia() : 0;
        double body2InverseInertia = body1.isMoving() ? 1 / body2.getInertia() : 0;

        Vector r1 = contactPt.minus(body1.getCenterPt());
        Vector r2 = contactPt.minus(body2.getCenterPt());

        Vector rv = body2.getVelocity().add(r2.cross(-1 * body2.getAngularVelocity()));
        rv = rv.minus(body1.getVelocity()).minus(r1.cross(-1 * body1.getAngularVelocity()));

        Vector normal = mtv.normalize();
        double velAlongNormal = rv.dot(normal);

        double r1crossN = r1.cross(normal);
        double r2crossN = r2.cross(normal);

        double inverseMassSum = body1InversedMass + body2InversedMass;
        inverseMassSum += (r1crossN * r1crossN * body1InverseInertia);
        inverseMassSum += (r2crossN * r2crossN * body2InverseInertia);

        // Getting the total impulse of the two bodies as a system
        double coefficientOfResitution = 0.1;
        double totalImpulse = -(1.0f + coefficientOfResitution) * velAlongNormal / inverseMassSum;

        // Compute impulse of each object
        Vector impulse = normal.multiply(totalImpulse);

        // Apply impulse
        applyImpulse(body1, impulse.multiply(-1), r1);
        applyImpulse(body2, impulse, r2);

        // Re-compute the velocity vectors
        rv = body2.getVelocity().add(r2.cross(-1 * body2.getAngularVelocity()));
        rv = rv.minus(body1.getVelocity()).minus(r1.cross(-1 * body1.getAngularVelocity()));

        System.out.println("rv:" + rv);

        // Compute the vector tangent to the normal
        Vector tangent = rv.add(normal.multiply(-1 * rv.dot(normal))).normalize();

        System.out.println(tangent);

        // Compute the impulse tangent to the normal
        double totalTangentImpulse = (-1 * rv.dot(tangent)) / inverseMassSum;

        double sf = 0.8;
        double df = 0.8;

//        Vector tangentImpulse = tangent.multiply(totalTangentImpulse);

        // Coulumb's law
        Vector tangentImpulse;
        if (StrictMath.abs(totalTangentImpulse) < totalImpulse * sf) {
            tangentImpulse = tangent.multiply(totalTangentImpulse);
            System.out.println("I AM HERE");

        } else {
//            tangentImpulse = tangent.multiply(totalImpulse).multiply(-df);
            tangentImpulse = tangent.multiply(totalImpulse);
            System.out.println("HA");
        }

        // Apply friction impulse
        applyImpulse(body1, tangentImpulse.multiply(-1), r1);
        applyImpulse(body2, tangentImpulse, r2);
    }

    private void applyImpulse(PBody body, Vector impulse, Vector contactPt) {
        double bodyInversedMass = body.isMoving() ? 1 / body.getMass() : 0;
        double bodyInverseInertia = body.isMoving() ? 1 / body.getInertia() : 0;

        body.setVelocity(body.getVelocity().add(impulse.multiply(bodyInversedMass)));
        body.setAngularVelocity(body.getAngularVelocity() + (bodyInverseInertia) * contactPt.cross(impulse));
    }

    /**
     * Post-condition: Moves the two bodies by a slight bit after a collision occured (to prevent gittering)
     * Pre-condition: "body1", "body2", "mtv" should not be null
     * @param body1 The first body involved in the collision
     * @param body2 The second body involved in the collision
     * @param mtv The MTD of the two bodies
     */
    private void positionalCorrection(PBody body1, PBody body2, Vector mtv) {
        final double PERCENT = 0.01f; // usually 20% to 80%
        final double SLOP = 0.1f; // usually 0.01 to 0.1

        double body1InversedMass = body1.isMoving() ? 1 / body1.getMass() : 0;
        double body2InversedMass = body2.isMoving() ? 1 / body2.getMass() : 0;

        double penetrationDepth = mtv.norm2();
        Vector normal = mtv.normalize();

        Vector correction = normal.multiply(PERCENT)
                .multiply(Math.max(penetrationDepth - SLOP, 0.0f))
                .multiply(body1InversedMass + body2InversedMass);

        // Move the first body by a certain amount
        body1.translate(correction.multiply(-1 * body1InversedMass));

        // Move the second body by a certain amount
        body2.translate(correction.multiply(body2InversedMass));
    }
}
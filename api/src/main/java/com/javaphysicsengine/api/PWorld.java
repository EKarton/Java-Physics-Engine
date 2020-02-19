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
    private static final Vector GRAVITY = Vector.of(0, -9.81);
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
            System.out.println("Drawing " + pt);
            int topLeftX = (int) (pt.getX() - 10);
            int topLeftY = 600 - (int) (pt.getY() + 10);
            g.setColor(Color.GREEN);
            g.fillOval(topLeftX, topLeftY, 10 * 2, 10 * 2);
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

            // Calculating the new angular velocity (AngularVelocity' = AngularVelocity + torque * (1 / inertia) * time)
            double angularVelocity = body.getAngularVelocity() + (body.getTorque() * (1 / body.getInertia()) * timeEllapsed);
            body.setAngularVelocity(angularVelocity);

            // Getting the amount to translate by (Velocity = displacement / time)
            Vector translation = velocity.multiply(timeEllapsed).multiply(SCALE);
            body.translate(translation);

            // Rotate the body (angle += AngularVelocity' * time)
            double newAngle = body.getAngle() + (body.getAngularVelocity() * timeEllapsed);
            body.rotate(newAngle);
        }
    }

    /**
     * Post-condition: Calculates the impulse and applies them to two bodies
     * Pre-condition: "body1", "body2", "mtv" should not be null
     * @param body1 The first body involved in the collision
     * @param body2 The second body involved in the collision
     * @param mtv The MTD of the two bodies
     */
    private void calculateImpulse(PBody body1, PBody body2, Vector mtv, Vector contactPt) {
        double body1InversedMass = body1.isMoving() ? 1 / body1.getMass() : 0;
        double body2InversedMass = body2.isMoving() ? 1 / body2.getMass() : 0;

        double body1InverseInertia = body1.getInertia() != 0 ? 1.0f / body1.getInertia() : 0;
        double body2InverseInertia = body2.getInertia() != 0 ? 1.0f / body2.getInertia() : 0;

        Vector rv = body2.getVelocity().minus(body1.getVelocity());
        Vector normal = mtv.normalize();
        double velAlongNormal = normal.dot(rv);

        Vector r1 = contactPt.minus(body1.getCenterPt());
        Vector r2 = contactPt.minus(body2.getCenterPt());

        double r1crossN = r1.cross(normal);
        double r2crossN = r2.cross(normal);

        // Getting the total impulse of the two bodies as a system
        double coefficientOfResitution = 0.8;
        double totalImpulse = -(1.0f + coefficientOfResitution) * velAlongNormal;
        totalImpulse /= (body1InversedMass + body2InversedMass +
                (r1crossN * r1crossN * body1InverseInertia) +
                (r2crossN * r2crossN * body2InverseInertia));

        // Compute impulse of each object
        Vector body2Impulse = Vector.multiply(normal, totalImpulse);
        Vector body1Impulse = body2Impulse.multiply(-1);

        // Apply the impulse to the translation velocities
        body1.setVelocity(body1.getVelocity().add(body1Impulse.multiply(body1InversedMass)));
        body2.setVelocity(body2.getVelocity().add(body2Impulse.multiply(body2InversedMass)));

        // Apply the impulse to the angular velocity
        body1.setAngularVelocity(body1.getAngularVelocity() + (body1InverseInertia * r1.cross(body1Impulse)));
        body2.setAngularVelocity(body2.getAngularVelocity() + (body2InverseInertia * r2.cross(body2Impulse)));

        System.out.println(body1.getAngularVelocity() + " | " + body2.getAngularVelocity());
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
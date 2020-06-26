package com.javaphysicsengine.api;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCollidable;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.collision.PCollisionResult;
import com.javaphysicsengine.utils.Vector;
import org.javatuples.Pair;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PWorld {
    // Physic properties about this world
    private static final Vector GRAVITY = Vector.of(0, -9.81);
    private static final double SCALE = 1;

    // List containing the physical bodies and joints
    private ArrayList<PBody> bodies = new ArrayList<>();
    private ArrayList<PConstraints> constraints = new ArrayList<>();

    public ConcurrentLinkedQueue<Vector> pointsToDraw = new ConcurrentLinkedQueue<>();

    /**
     * Returns the list of bodies added to the world
     * @return Returns the list of bodies added to the world
     */
    public ArrayList<PBody> getBodies() {
        return bodies;
    }

    /**
     * Returns the list of constraints added to the world
     * @return Returns the list of constraints added to the world
     */
    public ArrayList<PConstraints> getConstraints() {
        return constraints;
    }

    /**
     * Draws the bodies and constraints to the screen
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
     * Simulates the bodies for a certain time
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

        PQuadTree tree = new PQuadTree(bodies);
        for (Pair<PBody, PBody> pair : tree.getPotentialIntersectingBodies()) {
            PBody body1 = pair.getValue0();
            PBody body2 = pair.getValue1();

            boolean isCollidable = (body1 instanceof PCollidable && body2 instanceof PCollidable) &&
                    (body1.isMoving() || body2.isMoving());

            if (isCollidable) {
                PCollidable collidable1 = (PCollidable) body1;
                PCollidable collidable2 = (PCollidable) body2;

                PCollisionResult result = collidable1.hasCollidedWith(collidable2);

                if (result.isHasCollided()) {
                    pointsToDraw.add(result.getContactPt());

                    if (body1.isMoving()) {
                        body1.translate(result.getBody1Mtv());
                    }
                    if (body2.isMoving()) {
                        body2.translate(result.getBody2Mtv());
                    }

                    if (result.getMtv().dot(body2.getCenterPt().minus(body1.getCenterPt())) < 0) {
                        throw new IllegalArgumentException("MTV's direction should be from body1 to body2!");
                    }

                    positionalCorrection(body1, body2, result.getMtv());
                    applyImpulse(body1, body2, result.getMtv(), result.getContactPt());
                }
            }
        }
    }

    /**
     * Adds the forces to all the bodies
     */
    private void addForces() {
        for (PBody body : bodies) {
            if (!body.isMoving()) {
                continue;
            }

            // Adding gravitational force
            Vector gravitationalForce = GRAVITY.scale(body.getMass());
            Vector newNetForce = body.getNetForce().add(gravitationalForce);
            body.setNetForce(newNetForce);
        }

        // Adding forces from constraints
        for (PConstraints constraint : constraints) {
            constraint.addTensionForce();
        }
    }

    /**
     * Translates all the bodies based on a certain time frame
     * @param timeEllapsed The time that has ellapsed
     */
    private void translateBodies(double timeEllapsed) {
        for (PBody body : bodies) {
            if (!body.isMoving()) {
                continue;
            }

            // Getting the acceleration from force ( Force = mass * acceleration )
            Vector acceleration = body.getNetForce().scale(1 / body.getMass());

            // Calculating the new velocity ( V' = V + at)
            Vector velocity = body.getVelocity().add(acceleration.scale(timeEllapsed));
            body.setVelocity(velocity);

            // Getting the amount to translate by (Velocity = displacement / time)
            Vector translation = velocity.scale(timeEllapsed).scale(SCALE);
            body.translate(translation);

            // Calculating the new angular velocity (AngularVelocity' = AngularVelocity + torque * (1 / inertia) * time)
            double angularVelocity = body.getAngularVelocity() + body.getTorque() * (1 / body.getInertia()) * timeEllapsed;
            body.setAngularVelocity(angularVelocity);

            // Rotate the body (angle += AngularVelocity' * time)
            double newAngle = body.getAngle() + (body.getAngularVelocity() * timeEllapsed * SCALE);
            body.rotate(newAngle);
        }
    }

    /**
     * Calculates and applies the impulse to the two bodies
     * @param body1 The first body involved in the collision
     * @param body2 The second body involved in the collision
     * @param mtv The MTV of the two bodies
     * @param
     */
    private void applyImpulse(PBody body1, PBody body2, Vector mtv, Vector contactPt) {

        double body1InversedMass = body1.isMoving() ? 1 / body1.getMass() : 0;
        double body2InversedMass = body2.isMoving() ? 1 / body2.getMass() : 0;

        double body1InverseInertia = body1.isMoving() ? body1.getInertia() : 0;
        double body2InverseInertia = body2.isMoving() ? body2.getInertia() : 0;

        Vector r1 = contactPt.minus(body1.getCenterPt());
        Vector r2 = contactPt.minus(body2.getCenterPt());

        Vector newV1 = body1.getVelocity().add(Vector.of(-1 * body1.getAngularVelocity() * r1.getY(), body1.getAngularVelocity() * r1.getX()));
        Vector newV2 = body2.getVelocity().add(Vector.of(-1 * body2.getAngularVelocity() * r2.getY(), body2.getAngularVelocity() * r2.getX()));
        Vector relativeVelocity = newV2.minus(newV1);

        Vector normal = mtv.normalize();
        double velAlongNormal = relativeVelocity.dot(normal);

        if(velAlongNormal > 0) {
            return;
        }

        double r1CrossN = r1.cross(normal);
        double r2CrossN = r2.cross(normal);

        // Getting the total impulse of the two bodies as a system
        double coefficientOfResitution = 0.2;
        double totalImpulse = -(1 + coefficientOfResitution) * velAlongNormal;
        totalImpulse /= (body1InversedMass + body2InversedMass +
                r1CrossN * r1CrossN * body1InverseInertia +
                r2CrossN * r2CrossN * body2InverseInertia);

        Vector impulse = normal.scale(totalImpulse);

        // Add impulse of each object
        if (body1.isMoving()) {
            body1.setVelocity(body1.getVelocity().minus(impulse.scale(body1InversedMass)));
            body1.setAngularVelocity(body1.getAngularVelocity() - r1CrossN * totalImpulse * body1InverseInertia);
        }

        if (body2.isMoving()) {
            body2.setVelocity(body2.getVelocity().add(impulse.scale(body2InversedMass)));
            body2.setAngularVelocity(body2.getAngularVelocity() + r2CrossN * totalImpulse * body2InverseInertia);
        }

        Vector tangent = relativeVelocity.minus(normal.scale(relativeVelocity.dot(normal)));
        tangent = tangent.normalize().scale(-1);

        double r1CrossT = r1.cross(tangent);
        double r2CrossT = r2.cross(tangent);

        double newFriction = 0.1;
        double tangentImpulse = -(1 + coefficientOfResitution) * relativeVelocity.dot(tangent) * newFriction;
        tangentImpulse /= (body1InversedMass + body2InversedMass +
                r1CrossT * r1CrossT * body1InverseInertia +
                r2CrossT * r2CrossT * body2InverseInertia);

        if (tangentImpulse > totalImpulse) {
            tangentImpulse = totalImpulse;
        }

        impulse = tangent.scale(tangentImpulse);

        if (body1.isMoving()) {
            body1.setVelocity(body1.getVelocity().minus(impulse.scale(body1InversedMass)));
            body1.setAngularVelocity(body1.getAngularVelocity() - r1CrossT * tangentImpulse * body1InverseInertia);
        }

        if (body2.isMoving()) {
            body2.setVelocity(body2.getVelocity().add(impulse.scale(body2InversedMass)));
            body2.setAngularVelocity(body2.getAngularVelocity() + r2CrossT * tangentImpulse * body2InverseInertia);
        }
    }

    /**
     * Moves the two bodies by a slight bit after a collision occured (to prevent gittering)
     *
     * @param body1 The first body involved in the collision
     * @param body2 The second body involved in the collision
     * @param mtv The MTD of the two bodies
     */
    private void positionalCorrection(PBody body1, PBody body2, Vector mtv) {
        final double PERCENT = 0.2; // usually 20% to 80%
        final double SLOP = 0.01; // usually 0.01 to 0.1

        double body1InversedMass = body1.isMoving() ? 1 / body1.getMass() : 0;
        double body2InversedMass = body2.isMoving() ? 1 / body2.getMass() : 0;

        double penetrationDepth = mtv.norm2();
        Vector normal = mtv.normalize();

        double correctionDepth = Math.max(penetrationDepth - SLOP, 0) / (body1InversedMass + body2InversedMass) * PERCENT;
        Vector correction = normal.scale(correctionDepth);

        if (body1.isMoving()) {
            body1.translate(correction.scale(-1 * body1InversedMass));
        }

        if (body2.isMoving()) {
            body2.translate(correction.scale(body2InversedMass));
        }
    }
}
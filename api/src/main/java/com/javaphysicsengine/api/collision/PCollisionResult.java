package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.utils.Vector;

/**
 * Stores the result of a collision
 * Note that the mtv must go in the direction of body1Mtv
 */
public class PCollisionResult {
    public void setHasCollided(boolean hasCollided) {
        this.hasCollided = hasCollided;
    }

    public void setBody1Mtv(Vector body1Mtv) {
        this.body1Mtv = body1Mtv;
    }

    public void setBody2Mtv(Vector body2Mtv) {
        this.body2Mtv = body2Mtv;
    }

    public void setMtv(Vector mtv) {
        this.mtv = mtv;
    }

    public void setContactPt(Vector contactPt) {
        this.contactPt = contactPt;
    }

    private boolean hasCollided;
    private Vector body1Mtv;
    private Vector body2Mtv;
    private Vector mtv;
    private Vector contactPt;

    /**
     * Constructs a PCollisionResult from the collision of body1 and body2
     *
     * @param hasCollided {@code True} if collided; else {@code False}
     * @param body1Mtv the amount to translate body1 by so that it is not touching body2
     * @param body2Mtv the amount to translate body2 by so that it is not touching body1
     * @param mtv the amount and direction to move body2 away from body1
     *            (regardless of whether they can move or not) so that they don't touch
     * @param contactPt the point of contact body1 and body2 touched each other
     */
    public PCollisionResult(boolean hasCollided, Vector body1Mtv, Vector body2Mtv, Vector mtv, Vector contactPt) {
        this.hasCollided = hasCollided;
        this.body1Mtv = body1Mtv;
        this.body2Mtv = body2Mtv;
        this.mtv = mtv;
        this.contactPt = contactPt;
    }

    public boolean isHasCollided() {
        return hasCollided;
    }

    public Vector getBody1Mtv() {
        return body1Mtv;
    }

    public Vector getBody2Mtv() {
        return body2Mtv;
    }

    public Vector getMtv() {
        return mtv;
    }

    public Vector getContactPt() {
        return contactPt;
    }
}

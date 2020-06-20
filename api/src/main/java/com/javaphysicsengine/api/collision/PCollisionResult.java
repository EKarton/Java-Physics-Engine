package com.javaphysicsengine.api.collision;

import com.javaphysicsengine.utils.Vector;

/**
 * Stores the result of a collision
 * Note that the mtv must go in the direction of body1Mtv
 */
public class PCollisionResult {
    private final boolean hasCollided;
    private final Vector body1Mtv;
    private final Vector body2Mtv;
    private final Vector mtv;
    private final Vector contactPt;

    @Deprecated
    public PCollisionResult(boolean hasCollided, Vector body1Mtv, Vector body2Mtv, Vector mtv) {
        this.hasCollided = hasCollided;
        this.body1Mtv = body1Mtv;
        this.body2Mtv = body2Mtv;
        this.mtv = mtv;
        this.contactPt = Vector.of(0, 0);
    }

    /**
     * Constructs a PCollisionResult from the collision of body1 and body2
     * @param hasCollided {@code True} if collided; else {@code False}
     * @param body1Mtv the amount to translate body1 by so that it is not touching body2
     * @param body2Mtv the amount to translate body2 by so that it is not touching body1
     * @param mtv the amount to translate body1 by without moving body2
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

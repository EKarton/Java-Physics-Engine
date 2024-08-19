package com.javaphysicsengine.api.body;

import com.javaphysicsengine.api.collision.PCollisionResult;

public interface PCollidable {
    PCollisionResult hasCollidedWith(PCollidable body);
}

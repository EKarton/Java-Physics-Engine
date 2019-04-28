package com.javaphysicsengine.api.body;

public class PBodyFactory {

    public PBody createCopy(PBody body) {
        PBody copiedBody;
        if (body instanceof PPolygon) {
            copiedBody = new PPolygon((PPolygon) body);
        } else if (body instanceof PCircle) {
            copiedBody = new PCircle((PCircle) body);
        } else {
            throw new IllegalArgumentException("The class type " + body.getClass() + " is not supported!");
        }
        return copiedBody;
    }

    public PBody createCopy(PPolygon polygon) {
        return new PPolygon(polygon);
    }

    public PBody createCopy(PCircle circle) {
        return new PCircle(circle);
    }
}

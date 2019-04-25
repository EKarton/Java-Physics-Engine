package com.javaphysicsengine.graphics;

import com.javaphysicsengine.api.body.PBody;

import java.awt.Color;

public class PBodyGraphicProperties {

    private PBody body;

    private Color outlineColor = Color.BLACK;
    private Color fillColor = Color.BLUE;

    public PBodyGraphicProperties(PBody body) {
        this.body = body;
    }

    /**
     * Returns the fill color of the body
     * @return The fill color of the body
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * Sets the fill color of the body
     * @param color The new fill color of the body
     */
    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    /**
     * Returns the outline color of the body
     * @return The outline color of the body
     */
    public Color getOutlineColor() {
        return outlineColor;
    }

    /**
     * Sets the outline color of the body
     * @param color The new outline color of the body
     */
    public void setOutlineColor(Color color) {
        this.outlineColor = color;
    }
}

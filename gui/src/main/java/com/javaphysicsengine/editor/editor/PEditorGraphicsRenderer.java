package com.javaphysicsengine.editor.editor;

import java.awt.*;

public class PEditorGraphicsRenderer {
    public static final Color BACKGROUND_COLOR = new Color(60, 60, 60);
    public static final Color CURSOR_COLOR = Color.WHITE;
    public static final Color OBJECT_TEXT_COLOR = Color.WHITE;
    public static final Color POBJECT_NAME_TEXT_COLOR = OBJECT_TEXT_COLOR;
    public static final Color SNAP_POINT_RANGE_COLOR = new Color(207, 176, 41);
    public static final Color POLYGON_INPROGRESS_EDGE_COLOR = Color.WHITE;
    public static final Color POLYGON_FINISHED_EDGE_COLOR = Color.BLACK;
    public static final Color CIRCLE_INPROGRESS_EDGE_COLOR = Color.WHITE;

    // Graphic properties showing which parts are visible
    private boolean isBoundingBoxDisplayed = true;
    private boolean isShapeOutlineDisplayed = true;
    private boolean isShapeFillDisplayed = false;
    private boolean isVelocityVectorsDisplayed = false;
    private boolean isAntiAliasingToggled = false;

    private final PEditorStore store;

    public PEditorGraphicsRenderer(PEditorStore store) {
        this.store = store;
    }

    public boolean isBoundingBoxDisplayed() {
        return isBoundingBoxDisplayed;
    }

    public void setBoundingBoxDisplayed(boolean boundingBoxDisplayed) {
        isBoundingBoxDisplayed = boundingBoxDisplayed;
    }

    public boolean isShapeOutlineDisplayed() {
        return isShapeOutlineDisplayed;
    }

    public void setShapeOutlineDisplayed(boolean shapeOutlineDisplayed) {
        isShapeOutlineDisplayed = shapeOutlineDisplayed;
    }

    public boolean isShapeFillDisplayed() {
        return isShapeFillDisplayed;
    }

    public void setShapeFillDisplayed(boolean shapeFillDisplayed) {
        isShapeFillDisplayed = shapeFillDisplayed;
    }

    public boolean isVelocityVectorsDisplayed() {
        return isVelocityVectorsDisplayed;
    }

    public void setVelocityVectorsDisplayed(boolean velocityVectorsDisplayed) {
        isVelocityVectorsDisplayed = velocityVectorsDisplayed;
    }

    public boolean isAntiAliasingToggled() {
        return isAntiAliasingToggled;
    }

    public void setAntiAliasingToggled(boolean antiAliasingToggled) {
        isAntiAliasingToggled = antiAliasingToggled;
    }

    public PEditorStore getStore() {
        return store;
    }

    public void renderGraphics(Graphics g, int width, int height, int mouseX, int mouseY) {
    }
}

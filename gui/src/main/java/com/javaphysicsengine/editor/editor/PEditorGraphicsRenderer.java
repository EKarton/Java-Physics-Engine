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
    public boolean isBoundingBoxDisplayed = true;
    public boolean isShapeOutlineDisplayed = true;
    public boolean isShapeFillDisplayed = false;
    public boolean isVelocityVectorsDisplayed = false;
    public boolean isAntiAliasingToggled = false;

    private final PEditorStore store;

    public PEditorGraphicsRenderer(PEditorStore store) {
        this.store = store;
    }

    public void renderGraphics(Graphics g, int width, int height) {
    }
}

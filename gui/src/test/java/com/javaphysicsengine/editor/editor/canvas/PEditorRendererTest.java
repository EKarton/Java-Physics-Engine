package com.javaphysicsengine.editor.editor.canvas;

import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.editor.editor.store.PEditorStore;
import com.javaphysicsengine.utils.Vector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_CIRCLE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class PEditorRendererTest {

    @Spy
    private PEditorStore store;

    private PEditorRenderer renderer;

    @Before
    public void setup() {
        renderer = new PEditorRenderer(store);
    }

    @Test
    public void isBoundingBoxDisplayed_should_return_true_when_set_bounding_box_display_to_true() {
        renderer.setBoundingBoxDisplayed(true);
        assertTrue(renderer.isBoundingBoxDisplayed());
    }

    @Test
    public void isShapeOutlineDisplayed_should_return_false_when_set_bounding_box_display_to_false() {
        renderer.setBoundingBoxDisplayed(false);
        assertFalse(renderer.isBoundingBoxDisplayed());
    }

    @Test
    public void isShapeOutlineDisplayed_should_return_true_when_set_shape_outline_display_to_true() {
        renderer.setShapeOutlineDisplayed(true);
        assertTrue(renderer.isShapeOutlineDisplayed());
    }

    @Test
    public void isShapeOutlineDisplayed_should_return_false_when_set_shape_outline_display_to_false() {
        renderer.setShapeOutlineDisplayed(false);
        assertFalse(renderer.isShapeOutlineDisplayed());
    }

    @Test
    public void isShapeFillDisplayed_should_return_true_when_set_shape_fill_displayed_to_true() {
        renderer.setShapeFillDisplayed(true);
        assertTrue(renderer.isShapeFillDisplayed());
    }

    @Test
    public void isShapeFillDisplayed_should_return_false_when_set_shape_fill_displayed_to_false() {
        renderer.setShapeFillDisplayed(false);
        assertFalse(renderer.isShapeFillDisplayed());
    }

    @Test
    public void isAntiAliasingToggled_should_return_true_when_set_anti_aliasing_to_true() {
        renderer.setAntiAliasingToggled(true);
        assertTrue(renderer.isAntiAliasingToggled());
    }

    @Test
    public void isAntiAliasingToggled_should_return_false_when_set_anti_aliasing_to_false() {
        renderer.setAntiAliasingToggled(false);
        assertFalse(renderer.isAntiAliasingToggled());
    }

    @Test
    public void renderGraphics_should_render_everything_in_store() {
        PPolygon polygon = new PPolygon("My polygon");
        polygon.getVertices().add(new Vector(0, 0));
        polygon.getVertices().add(new Vector(10, 0));
        polygon.getVertices().add(new Vector(10, 10));
        polygon.getVertices().add(new Vector(0, 10));
        polygon.computeCenterOfMass();

        PCircle circle = new PCircle("My circle");
        circle.getCenterPt().setXY(100, 100);
        circle.setRadius(100);

        PConstraints constraints = new PSpring(polygon, circle);

        store.addBody(polygon);
        store.addBody(circle);
        store.addConstraint(constraints);

        BufferedImage bi = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bi.createGraphics();
        renderer.renderGraphics(graphics, 1000, 1000, 100, 100, false, EDIT_MODE_CIRCLE);

        graphics.dispose();
    }
}
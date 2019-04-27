package com.javaphysicsengine.editor.editor.canvas;

import com.javaphysicsengine.editor.editor.store.PEditorStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_CIRCLE;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_CURSOR;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_POLYGON;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_SPRING;
import static com.javaphysicsengine.editor.editor.canvas.PEditorPanel.EDIT_MODE_STRING;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PEditorPanelTest {

    @Mock
    private PEditorStore store;

    @Mock
    private PEditorRenderer render;

    @Mock
    private PEditorMouseHandler mouseHandler;

    private PEditorPanel panel;
    private Graphics2D graphics;

    @Before
    public void setup() {
        panel = new PEditorPanel(store, mouseHandler, render);
        panel.setSize(1000, 1000);

        // Set up the graphics object
        BufferedImage bi = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        graphics = bi.createGraphics();
    }

    @After
    public void cleanup() {
        graphics.dispose();
    }

    @Test
    public void paintComponent_should_call_renderer() {
        doReturn(10).when(mouseHandler).getMouseX();
        doReturn(20).when(mouseHandler).getMouseY();
        doReturn(false).when(mouseHandler).isMouseSnappedToPoint();

        panel.paintComponent(graphics);

        verify(render, times(1)).renderGraphics(
                eq(graphics), eq(1000), eq(1000),
                eq(10), eq(20), eq(false), anyString());
    }

    @Test
    public void actionPerformed_should_set_edit_mode_to_cursor_when_button_clicked_is_0() {
        JToggleButton button = new JToggleButton();
        button.setName("0");

        ActionEvent event = mock(ActionEvent.class);
        doReturn(button).when(event).getSource();

        panel.actionPerformed(event);

        assertEquals(EDIT_MODE_CURSOR, panel.getEditMode());
        verify(mouseHandler, times(1)).setEditMode(eq(EDIT_MODE_CURSOR));
    }

    @Test
    public void actionPerformed_should_set_edit_mode_to_polygon_when_button_clicked_is_1() {
        JToggleButton button = new JToggleButton();
        button.setName("1");

        ActionEvent event = mock(ActionEvent.class);
        doReturn(button).when(event).getSource();

        panel.actionPerformed(event);

        assertEquals(EDIT_MODE_POLYGON, panel.getEditMode());
        verify(mouseHandler, times(1)).setEditMode(eq(EDIT_MODE_POLYGON));
    }

    @Test
    public void actionPerformed_should_set_edit_mode_to_circle_when_button_clicked_is_2() {
        JToggleButton button = new JToggleButton();
        button.setName("2");

        ActionEvent event = mock(ActionEvent.class);
        doReturn(button).when(event).getSource();

        panel.actionPerformed(event);

        assertEquals(EDIT_MODE_CIRCLE, panel.getEditMode());
        verify(mouseHandler, times(1)).setEditMode(eq(EDIT_MODE_CIRCLE));
    }

    @Test
    public void actionPerformed_should_set_edit_mode_to_spring_when_button_clicked_is_3() {
        JToggleButton button = new JToggleButton();
        button.setName("3");

        ActionEvent event = mock(ActionEvent.class);
        doReturn(button).when(event).getSource();

        panel.actionPerformed(event);

        assertEquals(EDIT_MODE_SPRING, panel.getEditMode());
        verify(mouseHandler, times(1)).setEditMode(eq(EDIT_MODE_SPRING));
    }

    @Test
    public void actionPerformed_should_set_edit_mode_to_string_when_button_clicked_is_4() {
        JToggleButton button = new JToggleButton();
        button.setName("4");

        ActionEvent event = mock(ActionEvent.class);
        doReturn(button).when(event).getSource();

        panel.actionPerformed(event);

        assertEquals(EDIT_MODE_STRING, panel.getEditMode());
        verify(mouseHandler, times(1)).setEditMode(eq(EDIT_MODE_STRING));
    }
}
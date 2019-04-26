package com.javaphysicsengine.editor.editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class PEditorMouseHandler implements MouseListener, MouseMotionListener {

    private final PEditorStore store;
    public int mouseX = 0;
    public int mouseY = 0;
    public boolean isMouseSnappedToPoint = false;

    public PEditorMouseHandler(PEditorStore store) {
        this.store = store;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}

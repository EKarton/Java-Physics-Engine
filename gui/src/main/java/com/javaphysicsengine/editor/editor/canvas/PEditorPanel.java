/*
 * Purpose: To create the PhysicsAPI Editor Window that is able to define objects by the user, simulate them, and save them.
 * Original Creation Date: January 11 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.editor.editor.canvas;

import com.javaphysicsengine.editor.editor.store.PEditorStore;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class PEditorPanel extends JPanel implements ActionListener {
    private static final String CIRCLE_SELECTED_IMAGE_PATH = "icons/circle-selected.png";
    private static final String CIRCLE_UNSELECTED_IMAGE_PATH = "icons/circle-unselected.png";

    private static final String CURSOR_SELECTED_IMAGE_PATH = "icons/cursor-selected.png";
    private static final String CURSOR_UNSELECTED_IMAGE_PATH = "icons/cursor-unselected.png";

    private static final String POLYGON_SELECTED_IMAGE_PATH = "icons/polygon-selected.png";
    private static final String POLYGON_UNSELECTED_IMAGE_PATH = "icons/polygon-unselected.png";

    private static final String SPRINGS_SELECTED_IMAGE_PATH = "icons/springs-selected.png";
    private static final String SPRINGS_UNSELECTED_IMAGE_PATH = "icons/springs-unselected.png";

    private static final String STRINGS_SELECTED_IMAGE_PATH = "icons/strings-selected.png";
    private static final String STRINGS_UNSELECTED_IMAGE_PATH = "icons/strings-unselected.png";

    public static final String EDIT_MODE_CURSOR = "CURSOR";
    public static final String EDIT_MODE_POLYGON = "POLYGON";
    public static final String EDIT_MODE_CIRCLE = "CIRCLE";
    public static final String EDIT_MODE_SPRING = "SPRING";
    public static final String EDIT_MODE_STRING = "STRING";

    private final PEditorStore store;
    private final PEditorRenderer renderer;
    private final PEditorMouseHandler mouseHandler;

    private String editMode = EDIT_MODE_CURSOR;  // <- Either "POLYGON", "CIRCLE", "SPRING", "STRING", "CURSOR"

    // The drawing buttons
    private JToggleButton[] drawingBttns;// <- Mouse will be on point if it is within a certain pixels away from actual point

    /**
     * Creates the PEditorPanel. It uses the {@code mouseHandler} as a handler for this panel's mouse events,
     * and {@code renderer} to render the bodies and constraints on this panel.
     *
     * @param store the store
     * @param mouseHandler the mouse handler
     * @param renderer the graphics renderer
     */
    public PEditorPanel(PEditorStore store, PEditorMouseHandler mouseHandler, PEditorRenderer renderer) {
        super();

        this.store = store;
        this.mouseHandler = mouseHandler;
        this.renderer = renderer;

        this.addMouseListener(this.mouseHandler);
        this.addMouseMotionListener(this.mouseHandler);

        this.setupEditButtons();
        this.startGameLoop();
    }

    /**
     * Draws the bodies and mouse cursor on the screen
     * @param g The Graphics Object used to display the objects on the screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.renderGraphics(g, getWidth(), getHeight(), mouseHandler.getMouseX(), mouseHandler.getMouseY(), mouseHandler.isMouseSnappedToPoint(), editMode);
    }

    /**
     * Handles events made from the timer or from the edit buttons
     * @param e The ActionEvent object
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            repaint();

        } else if (e.getSource() instanceof JToggleButton) {
            // Deselect all the other buttons that are not the current button
            for (JToggleButton bttn : drawingBttns)
                if (!bttn.equals(e.getSource()))
                    bttn.setSelected(false);

            // Remove all pre-existing drawable objects
            store.reset();

            // Grab which one was called
            JToggleButton curBttn = (JToggleButton) e.getSource();
            switch (curBttn.getName()) {
                case "0":
                    editMode = EDIT_MODE_CURSOR;
                    break;
                case "1":
                    editMode = EDIT_MODE_POLYGON;
                    break;
                case "2":
                    editMode = EDIT_MODE_CIRCLE;
                    break;
                case "3":
                    editMode = EDIT_MODE_SPRING;
                    break;
                case "4":
                    editMode = EDIT_MODE_STRING;
                    break;
            }
            this.mouseHandler.setEditMode(editMode);
        }
    }

    /**
     * Adds the edit buttons to this panel
     */
    private void setupEditButtons() {
        drawingBttns = new JToggleButton[5];
        drawingBttns[0] = createCursorDrawingButton();
        drawingBttns[1] = createPolygonDrawingButton();
        drawingBttns[2] = createCircleDrawingButton();
        drawingBttns[3] = createSpringDrawingButton();
        drawingBttns[4] = createStringDrawingButton();

        for (JToggleButton toggleButton : drawingBttns) {
            this.add(toggleButton);
        }
    }

    /**
     * Creates the cursor drawing toggle button
     * @return a toggle button
     */
    private JToggleButton createCursorDrawingButton() {
        JToggleButton jToggleButton = new JToggleButton("");
        jToggleButton.setName("" + 0);
        jToggleButton.addActionListener(this);

        ClassLoader classLoader = this.getClass().getClassLoader();
        URL selectedIconPath = classLoader.getResource(CURSOR_SELECTED_IMAGE_PATH);
        URL unselectedIconPath = classLoader.getResource(CURSOR_UNSELECTED_IMAGE_PATH);

        jToggleButton.setIcon(new ImageIcon(unselectedIconPath));
        jToggleButton.setSelectedIcon(new ImageIcon(selectedIconPath));

        return jToggleButton;
    }

    /**
     * Creates the polygon drawing toggle button
     * @return a toggle button
     */
    private JToggleButton createPolygonDrawingButton() {
        JToggleButton jToggleButton = new JToggleButton("");
        jToggleButton.setName("" + 1);
        jToggleButton.addActionListener(this);

        ClassLoader classLoader = this.getClass().getClassLoader();
        URL selectedIconPath = classLoader.getResource(POLYGON_SELECTED_IMAGE_PATH);
        URL unselectedIconPath = classLoader.getResource(POLYGON_UNSELECTED_IMAGE_PATH);

        jToggleButton.setIcon(new ImageIcon(unselectedIconPath));
        jToggleButton.setSelectedIcon(new ImageIcon(selectedIconPath));

        return jToggleButton;
    }

    /**
     * Creates the circle drawing toggle button
     * @return a toggle button
     */
    private JToggleButton createCircleDrawingButton() {
        JToggleButton jToggleButton = new JToggleButton("");
        jToggleButton.setName("" + 2);
        jToggleButton.addActionListener(this);

        ClassLoader classLoader = this.getClass().getClassLoader();
        URL selectedIconPath = classLoader.getResource(CIRCLE_SELECTED_IMAGE_PATH);
        URL unselectedIconPath = classLoader.getResource(CIRCLE_UNSELECTED_IMAGE_PATH);

        jToggleButton.setIcon(new ImageIcon(unselectedIconPath));
        jToggleButton.setSelectedIcon(new ImageIcon(selectedIconPath));

        return jToggleButton;
    }

    /**
     * Creates the spring drawing toggle button
     * @return a toggle button
     */
    private JToggleButton createSpringDrawingButton() {
        JToggleButton jToggleButton = new JToggleButton("");
        jToggleButton.setName("" + 3);
        jToggleButton.addActionListener(this);

        ClassLoader classLoader = this.getClass().getClassLoader();
        URL selectedIconPath = classLoader.getResource(SPRINGS_SELECTED_IMAGE_PATH);
        URL unselectedIconPath = classLoader.getResource(SPRINGS_UNSELECTED_IMAGE_PATH);

        jToggleButton.setIcon(new ImageIcon(unselectedIconPath));
        jToggleButton.setSelectedIcon(new ImageIcon(selectedIconPath));

        return jToggleButton;
    }

    /**
     * Creates the string drawing toggle button
     * @return a toggle button
     */
    private JToggleButton createStringDrawingButton() {
        JToggleButton jToggleButton = new JToggleButton("");
        jToggleButton.setName("" + 3);
        jToggleButton.addActionListener(this);

        ClassLoader classLoader = this.getClass().getClassLoader();
        URL selectedIconPath = classLoader.getResource(STRINGS_SELECTED_IMAGE_PATH);
        URL unselectedIconPath = classLoader.getResource(STRINGS_UNSELECTED_IMAGE_PATH);

        jToggleButton.setIcon(new ImageIcon(unselectedIconPath));
        jToggleButton.setSelectedIcon(new ImageIcon(selectedIconPath));

        return jToggleButton;
    }

    /**
     * Starts the game loop for rendering the bodies
     */
    private void startGameLoop() {
        Timer gameTimer = new Timer(1000 / 60, this);
        gameTimer.start();
    }
}
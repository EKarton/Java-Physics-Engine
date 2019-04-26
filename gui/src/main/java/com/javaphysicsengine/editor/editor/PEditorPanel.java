/*
  Purpose: To create the PhysicsAPI Editor Window that is able to define objects by the user, simulate them, and save them.
  Original Creation Date: January 11 2016
  @author Emilio Kartono
  @version January 15 2016
*/

package com.javaphysicsengine.editor.editor;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PConstraints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.List;

public class PEditorPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
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

    public static final int SNAP_TOOL_POINT_RANGE = 4;
    public static final String EDIT_MODE_CURSOR = "CURSOR";
    public static final String EDIT_MODE_POLYGON = "POLYGON";
    public static final String EDIT_MODE_CIRCLE = "CIRCLE";
    public static final String EDIT_MODE_SPRING = "SPRING";
    public static final String EDIT_MODE_STRING = "STRING";

    private final PEditorObservableStore store;
    private final PEditorRenderer renderer;
    private final PEditorMouseHandler mouseHandler;

    // Storing the tabbed panel to add objects, as well as body coordinates for future creation of objects
    private JTabbedPane propertiesPane;

    private String editMode = EDIT_MODE_CURSOR;  // <- Either "POLYGON", "CIRCLE", "SPRING", "STRING", "CURSOR"

    // The drawing buttons
    private JToggleButton[] drawingBttns;// <- Mouse will be on point if it is within a certain pixels away from actual point

    /*
      Post-condition: Creates a PEditorPane object
      Pre-condition: "propertiesPane" must not be null
      @param propertiesPane The JTabbedPane that will display the properties of the objects created in this PEditorPanel object
    */
    public PEditorPanel(JTabbedPane propertiesPane) {
        // Initialise the fields
        super();
        this.propertiesPane = propertiesPane;

        // Set up the buttons
        drawingBttns =  new JToggleButton[5];
        drawingBttns[0] = createCursorDrawingButton();
        drawingBttns[1] = createPolygonDrawingButton();
        drawingBttns[2] = createCircleDrawingButton();
        drawingBttns[3] = createSpringDrawingButton();
        drawingBttns[4] = createStringDrawingButton();

        for (JToggleButton toggleButton : drawingBttns) {
            this.add(toggleButton);
        }

        // Initialise the event handlers
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        PEditorPanel panel = this;

        this.store = new PEditorObservableStore();
        this.store.getEventListeners().add(body -> propertiesPane.add(body.getName(), new JScrollPane(new PBodyPropertiesPanel(body, propertiesPane, panel))));

        this.mouseHandler = new PEditorMouseHandler(store, editMode);
        this.renderer = new PEditorRenderer(store);

        // Initialise the game loop
        Timer gameTimer = new Timer(1000 / 60, this);
        gameTimer.start();
    }

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

    public void displayBoundingBox(boolean isBoundingBoxDisplayed) {
        renderer.setBoundingBoxDisplayed(isBoundingBoxDisplayed);
    }

    public void displayShapeOutline(boolean isShapeOutlineDisplayed) {
        renderer.setShapeOutlineDisplayed(isShapeOutlineDisplayed);
    }

    public void displayShapeFill(boolean isShapeFillDisplayed) {
        renderer.setShapeFillDisplayed(isShapeFillDisplayed);
    }

    public void setAntiAliasing(boolean isToggled) {
        renderer.setAntiAliasingToggled(isToggled);
    }

    public boolean isShapeOutlineDisplayed() {
        return renderer.isShapeOutlineDisplayed();
    }

    public boolean isShapeFillDisplayed() {
        return renderer.isShapeFillDisplayed();
    }

    public boolean isAntiAliasingToggled() {
        return renderer.isAntiAliasingToggled();
    }

    /*
      Post-condition: Returns a copy of all the PBody objects created in this panel
      @return List of all copied PBody objects created in this panel
    */
    public List<PBody> getBodies() {
        return store.getCopiesOfBodies();
    }

    /*
      Post-condition: Returns a copy of all the PConstraints objects created in this panel
      @return List of all copied PConstraints objects created in this panel
    */
    public List<PConstraints> getConstraints() {
        return store.getCopiesOfConstraints();
    }

    /*
      Post-condition: Add a body to the EditorPanel
      @param body The PBody object
    */
    public void addBody(PBody body) {
        store.addBody(body);
        propertiesPane.add(body.getName(), new JScrollPane(new PBodyPropertiesPanel(body, propertiesPane, this)));
    }

    /*
      Post-condition: Add a constraint to the EditorPanel
      @param constraint The PContraints object
    */
    public void addConstraint(PConstraints constraint) {
        store.addConstraint(constraint);
    }

    /*
      Post-condition: Deletes a PBody object based on its name.
      @param objectName The name of the object
    */
    public void deleteObject(String objectName) {
        if (store.canDeleteBody(objectName)) {
            store.deleteBody(objectName);

            // Close the properties tab that shows the properties of the delete object
            for (int i = 0; i < propertiesPane.getTabCount(); i++) {
                String label = propertiesPane.getTitleAt(i);
                if (label.equals(objectName))
                    propertiesPane.remove(i);
            }
        }
    }

    /*
      Post-condition: Clear all bodies made
    */
    public void clearBodies() {
        store.clearBodies();
        propertiesPane.removeAll();
    }

    /*
      Post-condition: Clear all constraints made
    */
    public void clearConstraints() {
        store.clearConstraints();
    }

    /*
      Post-condition: Changes the name of a known PBody object
      Pre-condition: The "body" must not be null
      @param newName The new name of the body
      @param body The PBody that is wished to have its name changed
      @returns Returns true if the name was changed successfully; else false.
    */
    public boolean changeBodyName(String newName, PBody body) {
        if (store.canChangeBodyName(newName, body)) {
            store.changeBodyName(newName, body);

            // Change the name of the body as well as the title of the body's properties pane
            for (int i = 0; i < propertiesPane.getTabCount(); i++) {
                if (propertiesPane.getTitleAt(i).equals(body.getName()))
                    propertiesPane.setTitleAt(i, newName);
            }

            return true;
        }
        return false;
    }

    /*
      Post-condition: Returns true if the mouse is within a range of 4 pixels of a specified point
      @param mouseX The x mouse coodinate
      @param mouseY The y mouse coordinate
      @param posX The x coordinate of a specified point
      @param posY The y coordinate of a specified point
      @return Returns true if the mouse is close to a specified point; else false
    */
    private boolean isMouseNearPoint(int mouseX, int mouseY, int posX, int posY) {
        double distToPoint = Math.pow(mouseX - posX, 2) + Math.pow(mouseY - posY, 2);
        return distToPoint < SNAP_TOOL_POINT_RANGE * SNAP_TOOL_POINT_RANGE;
    }

    /*
      Post-condition: Draws the bodies and mouse cursor on the screen
      Pre-condition: The "g" must not be a null value
      @param g The Graphics Object used to display the objects on the screen
    */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.renderGraphics(g, getWidth(), getHeight(), mouseHandler.getMouseX(), mouseHandler.getMouseY(), mouseHandler.isMouseSnappedToPoint(), editMode);
    }

    /*
      Post-condition: Handles which cursor button the mouse clicked.
      Pre-condition: The "e" must not be null
      @param e The ActionEvent object
    */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer)
            repaint();

        else if (e.getSource() instanceof JToggleButton) {
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

    /*
      Post-condition: Called when the mouse is clicked on the panel
      Pre-condition: The "e" must not be a null value
      @param e The MouseEvent object
    */
    public void mouseClicked(MouseEvent e) {
        mouseHandler.mouseClicked(e, getHeight());

        // --------------------------------------------------------
        // THING TO DO WITH THE PROPERTIES PANEL:
        // --------------------------------------------------------
        // If selected a body
        if (editMode.equals(EDIT_MODE_CURSOR) && store.getSelectedBody() != null) {
            // Search for the body in the properties pane. If there is not, show the properties on the screen
            for (int i = 0; i < propertiesPane.getTabCount(); i++) {
                String label = propertiesPane.getTitleAt(i);
                if (label.equals(store.getSelectedBody().getName()))
                    return;
            }

            // Create a properties tab for that body
            propertiesPane.add(store.getSelectedBody().getName(), new JScrollPane(new PBodyPropertiesPanel(store.getSelectedBody(), propertiesPane, this)));
        }
    }

    /*
      Post-condition: Called when a mouse button is pressed and the mouse moves in the panel
      Pre-condition: The "e" must not be a null value
      @param e The MouseEvent object
    */
    public void mouseDragged(MouseEvent e) {
        mouseHandler.mouseDragged(e, this.getHeight());
    }

    /*
      Post-condition: Called when the mouse moves in the panel
      Pre-condition: The "e" must not be a null value
      @param e The MouseEvent object
    */
    public void mouseMoved(MouseEvent e) {
        mouseHandler.mouseMoved(e, getHeight());
    }

    /*
      Post-condition: Called when a mouse button is pressed in the panel
      Pre-condition: The "e" must not be a null value
      @param e The MouseEvent object
    */
    public void mousePressed(MouseEvent e) {
    }

    /*
      Post-condition: Called when the mouse enters the panel
      Pre-condition: The "e" must not be a null value
      @param e The MouseEvent object
    */
    public void mouseEntered(MouseEvent e) {
    }

    /*
      Post-condition: Called when the mouse exits the panel
      Pre-condition: The "e" must not be a null value
      @param e The MouseEvent object
    */
    public void mouseExited(MouseEvent e) {
    }

    /*
      Post-condition: Called when a mouse button is released in the panel
      Pre-condition: The "e" must not be a null value
      @param e The MouseEvent object
    */
    public void mouseReleased(MouseEvent e) {
    }
}
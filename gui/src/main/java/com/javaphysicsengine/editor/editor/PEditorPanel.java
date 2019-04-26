/*
  Purpose: To create the PhysicsAPI Editor Window that is able to define objects by the user, simulate them, and save them.
  Original Creation Date: January 11 2016
  @author Emilio Kartono
  @version January 15 2016
*/

package com.javaphysicsengine.editor.editor;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.api.body.PString;
import com.javaphysicsengine.utils.Vector;

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

    private static final int SNAP_TOOL_POINT_RANGE = 4;
    public static final String MOUSE_STATE_CURSOR = "CURSOR";
    public static final String MOUSE_STATE_POLYGON = "POLYGON";
    public static final String MOUSE_STATE_CIRCLE = "CIRCLE";
    public static final String MOUSE_STATE_SPRING = "SPRING";
    public static final String MOUSE_STATE_STRING = "STRING";

    private final PEditorStore store;
    private final PEditorRenderer renderer;

    // Storing the tabbed panel to add objects, as well as body coordinates for future creation of objects
    private JTabbedPane propertiesPane;

    // Stores the mouse coordinates and what the mouse is doing
    private int mouseX = 0;
    private int mouseY = 0;
    private boolean isMouseSnappedToPoint = false;
    private String mouseState = MOUSE_STATE_CURSOR;  // <- Either "POLYGON", "CIRCLE", "SPRING", "STRING", "CURSOR"

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

        this.store = new PEditorStore();
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

    public boolean isBoundingBoxDisplayed() {
        return renderer.isBoundingBoxDisplayed();
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
      Post-condition: Returns the index in a list of bodies when found a body with the name name
      @param name The name of the body to search for
      @param bodies The list of bodies
      @return Returns the index of the body with the same name
    */
    private int getBodyIndexByName(String name, List<PBody> bodies) {
        return store.getBodyIndexByName(name, bodies);
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
        renderer.renderGraphics(g, getWidth(), getHeight(), mouseX, mouseY, isMouseSnappedToPoint, mouseState);
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
                    mouseState = MOUSE_STATE_CURSOR;
                    break;
                case "1":
                    mouseState = MOUSE_STATE_POLYGON;
                    break;
                case "2":
                    mouseState = MOUSE_STATE_CIRCLE;
                    break;
                case "3":
                    mouseState = MOUSE_STATE_SPRING;
                    break;
                case "4":
                    mouseState = MOUSE_STATE_STRING;
                    break;
            }
        }
    }

    /*
      Post-condition: Called when the mouse is clicked on the panel
      Pre-condition: The "e" must not be a null value
      @param e The MouseEvent object
    */
    public void mouseClicked(MouseEvent e) {
        // If it selected an object
        if (mouseState.equals(MOUSE_STATE_CURSOR) || mouseState.equals(MOUSE_STATE_SPRING) || mouseState.equals(MOUSE_STATE_STRING)) {
            store.setSelectedBody(null);
            if (isMouseSnappedToPoint)
                for (PBody body : store.getCreatedBodies())
                    if ((int) body.getCenterPt().getX() == mouseX && (int) (getHeight() - body.getCenterPt().getY()) == mouseY) {
                        store.setSelectedBody(body);
                        break;
                    }
        }

        // If selected a body
        if (mouseState.equals(MOUSE_STATE_CURSOR) && store.getSelectedBody() != null) {
            // Search for the body in the properties pane. If there is not, show the properties on the screen
            for (int i = 0; i < propertiesPane.getTabCount(); i++) {
                String label = propertiesPane.getTitleAt(i);
                if (label.equals(store.getSelectedBody().getName()))
                    return;
            }

            // Create a properties tab for that body
            propertiesPane.add(store.getSelectedBody().getName(), new JScrollPane(new PBodyPropertiesPanel(store.getSelectedBody(), propertiesPane, this)));
        }

        // If selected a body to connect to spring
        if (mouseState.equals(MOUSE_STATE_SPRING) || mouseState.equals(MOUSE_STATE_STRING) && store.getSelectedBody() != null) {
            if (store.getAttachedBody1() == null)
                store.setAttachedBody1(store.getSelectedBody());
            else if (store.getSelectedBody() != null) {
                // Create the object
                PConstraints constraint = null;
                if (mouseState.equals(MOUSE_STATE_SPRING)) {
                    constraint = new PSpring(store.getAttachedBody1(), store.getSelectedBody());
                }
                else {
                    constraint = new PString(store.getAttachedBody1(), store.getSelectedBody());
                }

                store.getCreatedConstraints().add(constraint);
                store.setAttachedBody1(null);
                store.setSelectedBody(null);
            }
        } else if (mouseState.equals(MOUSE_STATE_CIRCLE)) {
            // If the center point is not defined yet, define it
            if (store.getCircleCenterPt().getX() == -1)
                store.getCircleCenterPt().setXY(mouseX, mouseY);

                // If the user selects the radius, create the circle object
            else {
                // Generate the circle name (the name must be unique from the other bodies)
                String circleName = "";
                do
                    circleName = "Circle " + (int) (Math.random() * (10000));
                while (getBodyIndexByName(circleName, store.getCreatedBodies()) != -1);

                PCircle circle = new PCircle(circleName);
                circle.setRadius(store.getCircleRadius());
                circle.setCenterPt(new Vector(store.getCircleCenterPt().getX(), this.getHeight() - store.getCircleCenterPt().getY()));
                addBody(circle);
                store.reset();
            }
        } else if (mouseState.equals(MOUSE_STATE_POLYGON)) {
            store.getPolyVertices().add(new Vector(mouseX, mouseY));

            // Check if it closed the polygon
            if (store.getPolyVertices().size() > 2)
                if (store.getPolyVertices().get(0).equals(store.getPolyVertices().get(store.getPolyVertices().size() - 1))) {
                    // Generate the body name (the body name must be unique from all the rest)
                    String polyName = "";
                    do
                        polyName = "Polygon " + (int) (Math.random() * (10000));
                    while (getBodyIndexByName(polyName, store.getCreatedBodies()) != -1);

                    // Create the body
                    PPolygon polygon = new PPolygon(polyName);
                    for (int i = 0; i < store.getPolyVertices().size() - 1; i++) // -1 since the last vertex is a copy of the first vertex
                    {
                        store.getPolyVertices().get(i).setY(this.getHeight() - store.getPolyVertices().get(i).getY());  // Translate the point since the origin for polygon is bottom left
                        polygon.getVertices().add(store.getPolyVertices().get(i));
                    }
                    polygon.computeCenterOfMass();
                    addBody(polygon);
                    store.reset();
                }
        }
    }

    /*
      Post-condition: Called when a mouse button is pressed and the mouse moves in the panel
      Pre-condition: The "e" must not be a null value
      @param e The MouseEvent object
    */
    public void mouseDragged(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
        isMouseSnappedToPoint = false;

        if (store.getSelectedBody() != null && mouseState.equals(MOUSE_STATE_CURSOR))
            store.getSelectedBody().move(new Vector(mouseX, this.getHeight() - mouseY));
    }

    /*
      Post-condition: Called when the mouse moves in the panel
      Pre-condition: The "e" must not be a null value
      @param e The MouseEvent object
    */
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
        isMouseSnappedToPoint = false;

        // If the mouse is on a certain point on the polygon not made yet
        if (mouseState.equals(MOUSE_STATE_POLYGON))
            for (Vector pt : store.getPolyVertices())
                if (isMouseNearPoint(mouseX, mouseY, (int) pt.getX(), (int) pt.getY())) {
                    // Save the point it is snapped to
                    isMouseSnappedToPoint = true;
                    mouseX = (int) pt.getX();
                    mouseY = (int) pt.getY();
                    break;
                }

        // Check if it snapped to any of the made bodies
        if (!isMouseSnappedToPoint)
            for (PBody body : store.getCreatedBodies()) {
                // Check if it is on its center pt
                Vector bodyCenterPt = body.getCenterPt();
                if (isMouseNearPoint(mouseX, mouseY, (int) bodyCenterPt.getX(), (int) (getHeight() - bodyCenterPt.getY()))) {
                    // Save the point it is snapped to
                    isMouseSnappedToPoint = true;
                    mouseX = (int) bodyCenterPt.getX();
                    mouseY = (int) (getHeight() - bodyCenterPt.getY());
                    break;
                }
            }

        // If the mouse is adjusting the radius of the circle
        if (mouseState.equals(MOUSE_STATE_CIRCLE) && store.getCircleCenterPt().getX() != -1) {
            double xMinus = mouseX - store.getCircleCenterPt().getX();
            double yMinus = mouseY - store.getCircleCenterPt().getY();
            store.setCircleRadius(Math.sqrt((xMinus * xMinus) + (yMinus * yMinus)));
        }
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
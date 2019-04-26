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
import java.util.ArrayList;
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
    public static final Color BACKGROUND_COLOR = new Color(60, 60, 60);
    public static final Color CURSOR_COLOR = Color.WHITE;
    public static final Color OBJECT_TEXT_COLOR = Color.WHITE;
    public static final Color POBJECT_NAME_TEXT_COLOR = OBJECT_TEXT_COLOR;
    public static final Color SNAP_POINT_RANGE_COLOR = new Color(207, 176, 41);
    public static final Color POLYGON_INPROGRESS_EDGE_COLOR = Color.WHITE;
    public static final Color POLYGON_FINISHED_EDGE_COLOR = Color.BLACK;
    public static final Color CIRCLE_INPROGRESS_EDGE_COLOR = Color.WHITE;
    private final PEditorStore store;

    // Storing the tabbed panel to add objects, as well as body coordinates for future creation of objects
    private JTabbedPane propertiesPane;

    // Stores the mouse coordinates and what the mouse is doing
    private int mouseX = 0;
    private int mouseY = 0;
    private boolean isMouseSnappedToPoint = false;
    private String mouseState = MOUSE_STATE_CURSOR;  // <- Either "POLYGON", "CIRCLE", "SPRING", "STRING", "CURSOR"

    // Graphic properties showing which parts are visible
    private boolean isBoundingBoxDisplayed = true;
    private boolean isShapeOutlineDisplayed = true;
    private boolean isShapeFillDisplayed = false;
    private boolean isVelocityVectorsDisplayed = false;
    private boolean isAntiAliasingToggled = false;

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
        this.isBoundingBoxDisplayed = isBoundingBoxDisplayed;
    }

    public void displayShapeOutline(boolean isShapeOutlineDisplayed) {
        this.isShapeOutlineDisplayed = isShapeOutlineDisplayed;
    }

    public void displayShapeFill(boolean isShapeFillDisplayed) {
        this.isShapeFillDisplayed = isShapeFillDisplayed;
    }

    public void setAntiAliasing(boolean isToggled) {
        this.isAntiAliasingToggled = isToggled;
    }

    public boolean isBoundingBoxDisplayed() {
        return isBoundingBoxDisplayed;
    }

    public boolean isShapeOutlineDisplayed() {
        return isShapeOutlineDisplayed;
    }

    public boolean isShapeFillDisplayed() {
        return isShapeFillDisplayed;
    }

    public boolean isAntiAliasingToggled() {
        return isAntiAliasingToggled;
    }

    /*
      Post-condition: Returns a copy of all the PBody objects created in this panel
      @return List of all copied PBody objects created in this panel
    */
    public List<PBody> getBodies() {
        List<PBody> copyOfBodies = new ArrayList<>();
        for (PBody body : store.getCreatedBodies()) {
            PBody copiedBody = null;
            if (body instanceof PPolygon)
                copiedBody = new PPolygon((PPolygon) body);
            else if (body instanceof PCircle)
                copiedBody = new PCircle((PCircle) body);

            if (copiedBody != null)
                copyOfBodies.add(copiedBody);
        }
        return copyOfBodies;
    }

    /*
      Post-condition: Returns a copy of all the PConstraints objects created in this panel
      @return List of all copied PConstraints objects created in this panel
    */
    public List<PConstraints> getConstraints() {
        List<PConstraints> copyOfConstraints = new ArrayList<>();
        for (PConstraints constraint : store.getCreatedConstraints()) {
            PBody[] bodiesAttached_Copy = new PBody[2];

            // Making a copy of the bodies attached
            for (int i = 0; i < bodiesAttached_Copy.length; i++) {
                PBody bodyAttached = constraint.getAttachedBodies()[i];
                if (bodyAttached instanceof PPolygon)
                    bodiesAttached_Copy[i] = new PPolygon((PPolygon) bodyAttached);
                else if (bodyAttached instanceof PCircle)
                    bodiesAttached_Copy[i] = new PCircle((PCircle) bodyAttached);
            }

            // Making a copy of the constraints
            if (constraint instanceof PSpring) {
                PSpring springCopy = new PSpring(bodiesAttached_Copy[0], bodiesAttached_Copy[1]);
                springCopy.setKValue(((PSpring) constraint).getKValue());
                springCopy.setLength(constraint.getLength());
                copyOfConstraints.add(springCopy);
            } else if (constraint instanceof PString) {
                PString stringCopy = new PString(bodiesAttached_Copy[0], bodiesAttached_Copy[1]);
                stringCopy.setLength(constraint.getLength());
                copyOfConstraints.add(stringCopy);
            }
        }
        return copyOfConstraints;
    }

    /*
      Post-condition: Add a body to the EditorPanel
      @param body The PBody object
    */
    public void addBody(PBody body) {
        // Add the body to the end of the list and show its properties pane
        store.getCreatedBodies().add(body);
        propertiesPane.add(body.getName(), new JScrollPane(new PBodyPropertiesPanel(body, propertiesPane, this)));

        sortBodyByName();
    }

    /*
      Post-condition: Add a constraint to the EditorPanel
      @param constraint The PContraints object
    */
    public void addConstraint(PConstraints constraint) {
        store.getCreatedConstraints().add(constraint);
    }

    /*
      Post-condition: Deletes a PBody object based on its name.
      @param objectName The name of the object
    */
    public void deleteObject(String objectName) {
        // Search for the index of the object with the object name
        int bodyIndex = getBodyIndexByName(objectName, store.getCreatedBodies());

        // If there was a -1, then there is an error
        if (bodyIndex == -1)
            return;

        // Search for any constraints attached to the body to be deleted. If there is, delete it
        for (int i = 0; i < store.getCreatedConstraints().size(); i++) {
            PConstraints currentConstraint = store.getCreatedConstraints().get(i);
            System.out.println("Has constraint");
            for (int j = 0; j < currentConstraint.getAttachedBodies().length; j++) {
                String nameOfAttachedBody = currentConstraint.getAttachedBodies()[j].getName();
                System.out.println("  AB: " + nameOfAttachedBody);
                if (nameOfAttachedBody.equals(objectName)) {
                    System.out.println("Found a constraint!");
                    store.getCreatedConstraints().remove(i);
                    i--;
                    break;
                }
            }
        }

        // Remove the body from the arraylist of bodies
        store.getCreatedBodies().remove(bodyIndex);

        // Close the properties tab that shows the properties of the delete object
        for (int i = 0; i < propertiesPane.getTabCount(); i++) {
            String label = propertiesPane.getTitleAt(i);
            if (label.equals(objectName))
                propertiesPane.remove(i);
        }
    }

    /*
      Post-condition: Clear all bodies made
    */
    public void clearBodies() {
        // Remove all the bodies in the list
        store.getCreatedBodies().clear();

        // Remove all the propertiy panes of all bodies
        propertiesPane.removeAll();
    }

    /*
      Post-condition: Clear all constraints made
    */
    public void clearConstraints() {
        store.getCreatedConstraints().clear();
    }

    /*
      Post-condition: Changes the name of a known PBody object
      Pre-condition: The "body" must not be null
      @param newName The new name of the body
      @param body The PBody that is wished to have its name changed
      @returns Returns true if the name was changed successfully; else false.
    */
    public boolean changeBodyName(String newName, PBody body) {
        // Check if the body name already exists in the list of bodies
        int bodyFoundIndex = getBodyIndexByName(newName, store.getCreatedBodies());

        // If the body was found
        if (bodyFoundIndex != -1 && !store.getCreatedBodies().get(bodyFoundIndex).equals(body))
            return false;

        // Change the name of the body as well as the title of the body's properties pane
        for (int i = 0; i < propertiesPane.getTabCount(); i++)
            if (propertiesPane.getTitleAt(i).equals(body.getName()))
                propertiesPane.setTitleAt(i, newName);

        body.setName(newName);
        sortBodyByName();
        return true;
    }

    /*
      Post-condition: Sorts the createdBodies[] list in alphabethica order according to body name
    */
    private void sortBodyByName() {
        // Sort the bodies list by name in ascending order using insertion sort
        for (int i = 1; i < store.getCreatedBodies().size(); i++) {
            PBody curBody = store.getCreatedBodies().get(i);
            int j = i;
            while (j > 0 && store.getCreatedBodies().get(j - 1).getName().compareTo(curBody.getName()) > 0) {
                store.getCreatedBodies().set(j, store.getCreatedBodies().get(j - 1));
                j--;
            }
            store.getCreatedBodies().set(j, curBody);
        }
    }

    /*
      Post-condition: Returns the index in a list of bodies when found a body with the name name
      @param name The name of the body to search for
      @param bodies The list of bodies
      @return Returns the index of the body with the same name
    */
    private int getBodyIndexByName(String name, ArrayList<PBody> bodies) {
        // Do a binary search (already sorted by name in ascending order)
        int left = 0;
        int right = bodies.size() - 1;

        while (right >= left) {
            int mid = (left + right) / 2;
            PBody curBody = bodies.get(mid);

            if (curBody.getName().compareTo(name) < 0)
                left = mid + 1;
            else if (curBody.getName().compareTo(name) > 0)
                right = mid - 1;
            else
                return mid;
        }
        return -1;
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
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        // If antialiasing toggled
        if (isAntiAliasingToggled) {
            // Set antialiasing (for smoother but slower graphics)
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        // Draw all the user-created bodies
        for (PBody body : store.getCreatedBodies()) {
            if (isBoundingBoxDisplayed)
                body.drawBoundingBox(g, this.getHeight());

            if (isShapeOutlineDisplayed)
                body.drawOutline(g, this.getHeight());

            if (isShapeFillDisplayed)
                body.drawFill(g, this.getHeight());

            // Draw the name of the object
            g.setColor(POBJECT_NAME_TEXT_COLOR);
            g.drawString(body.getName(), (int) body.getCenterPt().getX(), (int) (this.getHeight() - body.getCenterPt().getY()));
        }

        // Draw the constraints
        for (PConstraints constraint : store.getCreatedConstraints())
            constraint.drawConstraints(g, getHeight());

        // Draw the cursor
        g.setColor(CURSOR_COLOR);
        g.drawLine(mouseX, mouseY - 20, mouseX, mouseY + 20);
        g.drawLine(mouseX - 20, mouseY, mouseX + 20, mouseY);

        handleBodyDrawing(g);
    }

    /*
      Post-condition: Draws the bodies and constraints that are in the process of being created by the user
      Pre-condition: The "g" must not be a null value
      @param g The Graphics Object used to display the objects on the screen
    */
    private void handleBodyDrawing(Graphics g) {
        // Draw the point the cursor is snapped to (if it is snapped to)
        if (isMouseSnappedToPoint) {
            // Draw a circle around the point
            g.setColor(SNAP_POINT_RANGE_COLOR);
            g.drawOval(mouseX - 5, mouseY - 5, 10, 10);
        }

        // Drawing the line that will connect the mouse pos to the last vertex of the polygon
        if (mouseState.equals(MOUSE_STATE_POLYGON) && store.getPolyVertices().size() > 0) {
            Vector lastAddedVertex = store.getPolyVertices().get(store.getPolyVertices().size() - 1);
            g.setColor(POLYGON_INPROGRESS_EDGE_COLOR);
            g.drawLine((int) lastAddedVertex.getX(), (int) lastAddedVertex.getY(), mouseX, mouseY);

            // Drawing the going-to-be-drawn polygons
            g.setColor(POLYGON_FINISHED_EDGE_COLOR);
            for (int i = 0; i < store.getPolyVertices().size() - 1; i++) {
                int x1 = (int) store.getPolyVertices().get(i).getX();
                int y1 = (int) store.getPolyVertices().get(i).getY();
                int x2 = (int) store.getPolyVertices().get(i + 1).getX();
                int y2 = (int) store.getPolyVertices().get(i + 1).getY();
                g.drawLine(x1, y1, x2, y2);
            }
        }

        // Drawing the circle that will be drawn
        else if (mouseState.equals(MOUSE_STATE_CIRCLE) && store.getCircleRadius() > 0) {
            int topLeftX = (int) (store.getCircleCenterPt().getX() - store.getCircleRadius());
            int topLeftY = (int) (store.getCircleCenterPt().getY() - store.getCircleRadius());
            g.setColor(CIRCLE_INPROGRESS_EDGE_COLOR);
            g.drawOval(topLeftX, topLeftY, (int) (store.getCircleRadius() * 2), (int) (store.getCircleRadius() * 2));
            g.fillOval((int) store.getCircleCenterPt().getX() - 2, (int) store.getCircleCenterPt().getY() - 2, 4, 4);
        }

        // Drawing the constraint that will be drawn
        else if (mouseState.equals(MOUSE_STATE_SPRING) || mouseState.equals(MOUSE_STATE_STRING))
            if (store.getAttachedBody1() != null) {
                // Draw a line from the centerpt of attachedBody1 to the mouse
                g.setColor(Color.YELLOW);
                g.drawLine((int) store.getAttachedBody1().getCenterPt().getX(), getHeight() - (int) store.getAttachedBody1().getCenterPt().getY(), mouseX, mouseY);
            }

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
                circle.getCenterPt().setXY(store.getCircleCenterPt().getX(), this.getHeight() - store.getCircleCenterPt().getY());
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
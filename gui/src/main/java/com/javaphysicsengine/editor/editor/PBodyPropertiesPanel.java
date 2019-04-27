/*
 * Purpose: Represents a body's properties pane where users can modify the body's properties through GUI components
 * Original Creation Date: January 11 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.editor.editor;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class PBodyPropertiesPanel extends JPanel implements ActionListener {

    private JPanel colorPanel = new JPanel();  // A panel used to host the color picker
    private JTabbedPane tabbedPane;
    private PEditorPanel editorPanel;
    private ArrayList<PBodyPropertyItem> propertyItems = new ArrayList<PBodyPropertyItem>();
    private PBody body;

    /**
     * Post-condition: Creates a PBodyPropertiesPanel object tied to a specific object
     * Pre-condition: The "body", "currentPane", and "editorPanel" must not be null values
     *
     * @param body        The PBody object that will have its properties modified throug GUI components
     * @param currentPane The JTabbedPane that will contain this object
     * @param editorPanel The PEditorPanel that created this object
     */
    public PBodyPropertiesPanel(PBody body, JTabbedPane currentPane, PEditorPanel editorPanel) {
        super();
        this.body = body;
        this.tabbedPane = currentPane;
        this.editorPanel = editorPanel;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add the "Close Tab" button
        JButton closeTabBttn = new JButton("Close Tab");
        closeTabBttn.setName("Close Tab");
        closeTabBttn.setForeground(new Color(237, 182, 52));
        closeTabBttn.addActionListener(this);
        propertyItems.add(new PBodyPropertyItem("", closeTabBttn));

        // Add the "Delete Object" tab button
        JButton deleteBttn = new JButton("Delete Object");
        deleteBttn.setName("Delete Object");
        deleteBttn.setForeground(new Color(237, 182, 52));
        deleteBttn.addActionListener(this);
        propertyItems.add(new PBodyPropertyItem("", deleteBttn));

        // Add the properties common to all PBodies:
        addPropertiesOfPBody();

        // Add the properties common to all PCircles
        if (body instanceof PCircle) {
            // Convert body to PCircle type
            PCircle circle = (PCircle) body;

            // Add the properties
            JTextField radiusTxtBox = new JTextField(10);
            radiusTxtBox.addActionListener(this);
            radiusTxtBox.setText("" + circle.getRadius());
            propertyItems.add(new PBodyPropertyItem("Radius", radiusTxtBox));
        }

        // Add the property items to the JPanel
        for (PBodyPropertyItem item : propertyItems)
            this.add(item);
    }

    /**
     * Adds the GUI components for changing the properties of all PBody objects
     */
    private void addPropertiesOfPBody() {
        // Adding name txtbox
        JTextField nameTxtBox = new JTextField(10);
        nameTxtBox.setText(body.getName());
        nameTxtBox.addActionListener(this);
        propertyItems.add(new PBodyPropertyItem("Name", nameTxtBox));

        // Adding name txtbox
        JTextField massTxtBox = new JTextField(10);
        massTxtBox.setText("" + body.getMass());
        massTxtBox.addActionListener(this);
        propertyItems.add(new PBodyPropertyItem("Mass", massTxtBox));

        // Adding fill button
        JButton fillBttn = new JButton("  Set   Fill   Color  ");
        fillBttn.addActionListener(this);
        propertyItems.add(new PBodyPropertyItem("Fill Color", fillBttn));

        // Adding outline button
        JButton outlineBttn = new JButton("Set Stroke Color");
        outlineBttn.addActionListener(this);
        propertyItems.add(new PBodyPropertyItem("Stroke Color", outlineBttn));

        // Adding velocity txtbox
        JTextField velTxtBox = new JTextField(10);
        velTxtBox.setText("" + body.getVelocity());
        velTxtBox.addActionListener(this);
        propertyItems.add(new PBodyPropertyItem("Velocity", velTxtBox));

        // Adding angle txtbox
        JTextField angleTxtBox = new JTextField(10);
        angleTxtBox.setText("" + body.getAngle());
        angleTxtBox.addActionListener(this);
        propertyItems.add(new PBodyPropertyItem("Angle", angleTxtBox));

        // Adding moveable checkbox
        JCheckBox moveableChckBox = new JCheckBox("Is Moveable");
        moveableChckBox.setForeground(new Color(186, 186, 186));
        moveableChckBox.setSelected(body.isMoving());
        moveableChckBox.addActionListener(this);
        propertyItems.add(new PBodyPropertyItem("", moveableChckBox));
    }

    /**
     * Handles modifying the properties of the body each time a GUI component triggers an event
     *
     * @param e The event
     */
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() instanceof JTextField) {
                JTextField txtbox = (JTextField) e.getSource();
                switch (txtbox.getName()) {
                    case "Name":

                        try {
                            editorPanel.getStore().changeBodyName(txtbox.getText(), body);
                        } catch (IllegalArgumentException exception) {
                            JOptionPane.showMessageDialog(null, "Name already set to another body!");
                            txtbox.setText(body.getName());
                        }

                        break;
                    case "Mass":
                        body.setMass(Double.parseDouble(txtbox.getText()));
                        break;
                    case "Radius":
                        PCircle circle = (PCircle) body;
                        circle.setRadius(Integer.parseInt(txtbox.getText()));
                        break;
                    case "Velocity":
                        StringTokenizer tokenizer = new StringTokenizer(txtbox.getText(), ",");
                        if (tokenizer.countTokens() == 2) {
                            double xComponent = Double.parseDouble(tokenizer.nextToken());
                            double yComponent = Double.parseDouble(tokenizer.nextToken());
                            body.getVelocity().setXY(xComponent, yComponent);
                            txtbox.setText(body.getVelocity().toString());
                        }
                        break;
                    case "Angle":
                        body.rotate(Double.parseDouble(txtbox.getText()));
                        break;
                }
            } else if (e.getSource() instanceof JButton) {
                JButton bttn = (JButton) e.getSource();
                switch (bttn.getName()) {
                    case "Fill Color":
                        body.setFillColor(JColorChooser.showDialog(colorPanel, "Pick a Fill Color", body.getFillColor()));
                        break;
                    case "Stroke Color":
                        body.setOutlineColor(JColorChooser.showDialog(colorPanel, "Pick a Stroke Color", body.getOutlineColor()));
                        break;
                    case "Close Tab":
                        int indexOfTab = tabbedPane.getSelectedIndex();
                        if (indexOfTab != -1)
                            tabbedPane.removeTabAt(indexOfTab);
                        else
                            System.out.println("Tab Removal Error");
                        break;
                    case "Delete Object":
                        editorPanel.deleteObject(body.getName());

                        break;
                }
            } else if (e.getSource() instanceof JCheckBox) {
                JCheckBox chckbox = (JCheckBox) e.getSource();
                if (chckbox.getText().equals("Is Moveable"))
                    body.setMoveable(chckbox.isSelected());
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Input Invalid");
        }
    }
}
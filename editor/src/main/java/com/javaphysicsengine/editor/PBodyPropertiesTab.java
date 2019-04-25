/*
 Purpose: Represents a body's properties pane where users can modify the body's properties through GUI components
 Original Creation Date: January 11 2016
 @author Emilio Kartono
 @version January 15 2016
 */

package com.javaphysicsengine.editor;

import com.javaphysicsengine.api.PBody;
import com.javaphysicsengine.api.PCircle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class PBodyPropertiesTab extends JPanel implements ActionListener
{
  private JPanel colorPanel = new JPanel();  // A panel used to host the color picker
  private JTabbedPane tabbedPane;
  private PEditorPanel editorPanel;
  private ArrayList<PPropertyItem> propertyItems = new ArrayList<PPropertyItem>();
  private PBody body;
  
  /*
   Post-condition: Creates a PBodyPropertiesTab object tied to a specific object
   Pre-condition: The "body", "currentPane", and "editorPanel" must not be null values
   @param body The PBody object that will have its properties modified throug GUI components
   @param currentPane The JTabbedPane that will contain this object
   @param editorPanel The PEditorPanel that created this object
   */
  public PBodyPropertiesTab(PBody body, JTabbedPane currentPane, PEditorPanel editorPanel)
  {
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
    propertyItems.add(new PPropertyItem("", closeTabBttn));
    
    // Add the "Delete Object" tab button
    JButton deleteBttn = new JButton("Delete Object");
    deleteBttn.setName("Delete Object");
    deleteBttn.setForeground(new Color(237, 182, 52));
    deleteBttn.addActionListener(this);
    propertyItems.add(new PPropertyItem("", deleteBttn));
    
    // Add the properties common to all PBodies:
    addPropertiesOfPBody();
    
    // Add the properties common to all PCircles
    if (body instanceof PCircle)
    {
      // Convert body to PCircle type
      PCircle circle = (PCircle) body;
      
      // Add the properties
      JTextField radiusTxtBox = new JTextField(10);
      radiusTxtBox.addActionListener(this);
      radiusTxtBox.setText("" + circle.getRadius());
      propertyItems.add(new PPropertyItem("Radius", radiusTxtBox));
    }
    
    // Add the property items to the JPanel
    for (PPropertyItem item : propertyItems)
      this.add(item);
  }
  
  /*
   Post-condition: Returns the object attached to this properties pane
   @return The body attached to this properties pane
   */
  public PBody getBodyShown() { return body; }
  
  /*
   Post-condition: Adds the GUI components for changing the properties of all PBody objects
   */  
  private void addPropertiesOfPBody()
  {
    // Adding name txtbox
    JTextField nameTxtBox = new JTextField(10);
    nameTxtBox.setText(body.getName());
    nameTxtBox.addActionListener(this);
    propertyItems.add(new PPropertyItem("Name", nameTxtBox));
    
    // Adding name txtbox
    JTextField massTxtBox = new JTextField(10);
    massTxtBox.setText("" + body.getMass());
    massTxtBox.addActionListener(this);
    propertyItems.add(new PPropertyItem("Mass", massTxtBox));
    
    // Adding fill button
    JButton fillBttn = new JButton("  Set   Fill   Color  ");
    fillBttn.addActionListener(this);
    propertyItems.add(new PPropertyItem("Fill Color", fillBttn));
    
    // Adding outline button
    JButton outlineBttn = new JButton("Set Stroke Color");
    outlineBttn.addActionListener(this);
    propertyItems.add(new PPropertyItem("Stroke Color", outlineBttn));
    
    // Adding velocity txtbox
    JTextField velTxtBox = new JTextField(10);
    velTxtBox.setText("" + body.getVelocity());
    velTxtBox.addActionListener(this);
    propertyItems.add(new PPropertyItem("Velocity", velTxtBox));
    
    // Adding angle txtbox
    JTextField angleTxtBox = new JTextField(10);
    angleTxtBox.setText("" + body.getAngle());
    angleTxtBox.addActionListener(this);
    propertyItems.add(new PPropertyItem("Angle", angleTxtBox));
    
    // Adding moveable checkbox
    JCheckBox moveableChckBox = new JCheckBox("Is Moveable");
    moveableChckBox.setForeground(new Color(186, 186, 186));
    moveableChckBox.setSelected(body.isMoving());
    moveableChckBox.addActionListener(this);
    propertyItems.add(new PPropertyItem("", moveableChckBox));
  }
  
  /*
   Post-condition: Handles modifying the properties of the body each time a GUI component triggers an event
   Pre-condition: The ActionEvent e must not be a null value
   */
  public void actionPerformed(ActionEvent e)
  {
    try
    {
      if (e.getSource() instanceof JTextField)
      {
        JTextField txtbox = (JTextField) e.getSource();      
        switch (txtbox.getName())
        {
          case "Name":
            boolean nameChangeSuccess = editorPanel.changeBodyName(txtbox.getText(), body);
            if (nameChangeSuccess == false)
            {
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
            if (tokenizer.countTokens() == 2)
            {
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
      }
      
      else if (e.getSource() instanceof JButton)
      {
        JButton bttn = (JButton) e.getSource();
        switch (bttn.getName())
        {
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
      }
      
      else if (e.getSource() instanceof JCheckBox)
      {
        JCheckBox chckbox = (JCheckBox) e.getSource();
        if (chckbox.getText().equals("Is Moveable"))
          body.setMoveable(chckbox.isSelected());
      }
    }
    catch (Exception exception)
    {
      JOptionPane.showMessageDialog(null, "Input Invalid");
    }
  }
  
  /*
   Purpose: Represents a property of the PBody. 
   This displays the property to the screen with two GUI components: 
   a label (to display what property it is) and a GUI component to handle user input
   Original Creation Date: January 11 2016
   @author Emilio Kartono
   @version January 15 2016
   */
  private class PPropertyItem extends JPanel
  {
    // Constants
    private final Color LABEL_TEXT_COLOR = new Color(186, 186, 186);
    private final Font LABEL_FONT = new Font("Calibri", Font.PLAIN, 14);
    
    // The GUI components of the PropertyItem
    private String propertyName;
    private JComponent sideComponent;
    
    /*
     Post-condition: Creates a PPropertyItem that will have a label on the left of a GUI component
     Pre-condition: The "sideComponent" and the "propertyName" must not be a null value
     @param propertyName The name of the property (this will be the text of the label)
     @param sideComponent The GUI component that will be on the right of the label
     */
    public PPropertyItem(String propertyName, JComponent sideComponent)
    {
      // Initialising the object
      this.propertyName = propertyName;
      this.sideComponent = sideComponent;
      this.setLayout(new GridBagLayout());
      this.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));  // top, left, bottom, right
      
      // Create the layout for inserting the GUI components
      GridBagConstraints c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.weightx = 0.4;
      c.gridx = 0;
      c.gridy = 0;
      
      // If the user wants to add text to the label
      if (propertyName.equals("") == false)
      {                  
        // Create the label and insert it to the first column
        JLabel label = new JLabel(propertyName);
        label.setForeground(LABEL_TEXT_COLOR);
        label.setFont(LABEL_FONT);
        label.setPreferredSize(new Dimension(70,40));// Width, Height
        
        this.add(label, c);  
        
        // Set the name of the component to the label name
        sideComponent.setName(propertyName);
      }
      
      // Add the component to the next column
      c.gridx = 1;      
      this.add(sideComponent, c);
    }
  }
}
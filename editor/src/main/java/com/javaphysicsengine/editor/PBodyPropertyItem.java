/*
 * Purpose: Represents a property of the PBody.
 * This displays the property to the screen with two GUI components:
 * a label (to display what property it is) and a GUI component to handle user input
 * Original Creation Date: January 11 2016
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.editor;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;

public class PBodyPropertyItem extends JPanel {
    // Constants
    private static final Color LABEL_TEXT_COLOR = new Color(186, 186, 186);
    private static final Font LABEL_FONT = new Font("Calibri", Font.PLAIN, 14);

    /**
     * Post-condition: Creates a PBodyPropertyItem that will have a label on the left of a GUI component
     *
     * @param propertyName  The name of the property (this will be the text of the label)
     * @param sideComponent The GUI component that will be on the right of the label
     */
    public PBodyPropertyItem(String propertyName, JComponent sideComponent) {
        // Initialising the object
        this.setLayout(new java.awt.GridBagLayout());
        this.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));  // top, left, bottom, right

        // Create the layout for inserting the GUI components
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.4;
        c.gridx = 0;
        c.gridy = 0;

        // If the user wants to add text to the label
        if (!"".equals(propertyName)) {
            // Create the label and insert it to the first column
            JLabel label = new JLabel(propertyName);
            label.setForeground(LABEL_TEXT_COLOR);
            label.setFont(LABEL_FONT);
            label.setPreferredSize(new Dimension(70, 40));// Width, Height

            this.add(label, c);

            // Set the name of the component to the label name
            sideComponent.setName(propertyName);
        }

        // Add the component to the next column
        c.gridx = 1;
        this.add(sideComponent, c);
    }
}

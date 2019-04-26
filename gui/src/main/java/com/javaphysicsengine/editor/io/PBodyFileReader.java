/*
 * Reads in the bodies from a file.
 * @author Emilio Kartono
 * @version January 15 2016
 */
package com.javaphysicsengine.editor.io;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.api.body.PString;
import com.javaphysicsengine.utils.File;
import com.javaphysicsengine.utils.Vector;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PBodyFileReader {

    /**
     * Loads the bodies and constraints from a file
     * @param filePath the file path
     * @return the bodies and the constraints
     */
    public Pair<List<PBody>, List<PConstraints>> loadBodiesFromFile(String filePath) {
        PBodyFileReader fileReader = new PBodyFileReader();

        // Start storing the bodies and constraints coming from the text file
        List<PBody> bodies = new ArrayList<>();
        List<PConstraints> constraints = new ArrayList<>();

        // Get the string of lines
        String[] lines = File.readAllLines(filePath);
        System.out.println(lines.length);

        for (String line : lines) {
            StringTokenizer outerBracketTokenizer = new StringTokenizer(line, "{}");
            String bodyType = outerBracketTokenizer.nextToken();
            String bodyProperties = outerBracketTokenizer.nextToken();
            StringTokenizer propertiesTokenizer = new StringTokenizer(bodyProperties, ";");

            if (bodyType.equals("PPolygon")) {
                System.out.println("Loading Polygon:");
                bodies.add(fileReader.createPolygonBody(propertiesTokenizer));
            } else if (bodyType.equals("PCircle")) {
                System.out.println("Loading Circle:");
                bodies.add(fileReader.createCircleBody(propertiesTokenizer));
            } else if (bodyType.equals("PSpring")) {
                System.out.println("Loading Spring");
                constraints.add(fileReader.createSpringConstraint(propertiesTokenizer, bodies));
            } else if (bodyType.equals("PString")) {
                System.out.println("Loading String");
                constraints.add(fileReader.createStringConstraint(propertiesTokenizer, bodies));
            }
        }
        return Pair.of(bodies, constraints);
    }

    /*
       Pre-condition: "properitesTokenizer" and "bodies" must not be null
       Post-condition: Creates a PSpring object based on the properties listed in the StringTokenizer
       @param bodies A list of bodies already created
       @param propertiesTokenizer The StringTokenizer containing the properties of the PPolygon
       @return PSpring The generated PSpring
    */
    private PSpring createSpringConstraint(StringTokenizer propertiesTokenizer, List<PBody> bodies) {
        PSpring createdSpring = null;
        while (propertiesTokenizer.hasMoreTokens()) {
            // Grab the properties and its values
            StringTokenizer propertyTokenizer = new StringTokenizer(propertiesTokenizer.nextToken(), ":");
            String propertyType = propertyTokenizer.nextToken();
            String propertyValue = propertyTokenizer.nextToken();

            // Set the appropriate properties
            switch (propertyType) {
                case "KValue":
                    createdSpring.setKValue(Double.parseDouble(propertyValue));
                    break;
                case "Length":
                    createdSpring.setLength(Double.parseDouble(propertyValue));
                    break;
                case "BodiesAttached":
                    StringTokenizer bodiesTokenizer = new StringTokenizer(propertyValue, "[]");
                    PBody[] bodiesAttached = new PBody[2];
                    int curBodyAttached = 0;
                    while (bodiesTokenizer.hasMoreTokens()) {
                        // Search for the body that has that name (note! bodies[] is not in order!)
                        String targetName = bodiesTokenizer.nextToken();
                        for (PBody body : bodies)
                            if (body.getName().equals(targetName))
                                bodiesAttached[curBodyAttached] = body;

                        curBodyAttached++;
                    }

                    // Add the bodies to the spring
                    createdSpring = new PSpring(bodiesAttached[0], bodiesAttached[1]);
                    break;
            }
        }

        return createdSpring;
    }

    /*
      Pre-condition: "properitesTokenizer" and "bodies" must not be null
      Post-condition: Creates a PString object based on the properties listed in the StringTokenizer
      @param bodies A list of bodies already created
      @param propertiesTokenizer The StringTokenizer containing the properties of the PPolygon
      @return PString The generated PString
   */
    private PString createStringConstraint(StringTokenizer propertiesTokenizer, List<PBody> bodies) {
        PString createdString = null;
        while (propertiesTokenizer.hasMoreTokens()) {
            // Grab the properties and its values
            StringTokenizer propertyTokenizer = new StringTokenizer(propertiesTokenizer.nextToken(), ":");
            String propertyType = propertyTokenizer.nextToken();
            String propertyValue = propertyTokenizer.nextToken();

            // Set the appropriate properties
            switch (propertyType) {
                case "Length":
                    createdString.setLength(Double.parseDouble(propertyValue));
                    break;
                case "BodiesAttached":
                    StringTokenizer bodiesTokenizer = new StringTokenizer(propertyValue, "[]");
                    PBody[] bodiesAttached = new PBody[2];
                    int curBodyAttached = 0;
                    while (bodiesTokenizer.hasMoreTokens()) {
                        // Search for the body that has that name (note! bodies[] is not in order!)
                        String targetName = bodiesTokenizer.nextToken();
                        for (PBody body : bodies)
                            if (body.getName().equals(targetName))
                                bodiesAttached[curBodyAttached] = body;

                        curBodyAttached++;
                    }

                    // Add the bodies to the spring
                    createdString = new PString(bodiesAttached[0], bodiesAttached[1]);
                    break;
            }
        }

        return createdString;
    }

    /*
       Pre-condition: "properitesTokenizer" must not be null; it must have valid property syntaxes (i.e. propertyName:propertyValue)
       Post-condition: Creates a PPolygon object based on the properties listed in the StringTokenizer
       @param propertiesTokenizer The StringTokenizer containing the properties of the PPolygon
       @return PPolygon The generated PPolygon
    */
    private PPolygon createPolygonBody(StringTokenizer propertiesTokenizer) {
        PPolygon createdPoly = new PPolygon("");

        while (propertiesTokenizer.hasMoreTokens()) {
            // Grab the properties and its values
            StringTokenizer propertyTokenizer = new StringTokenizer(propertiesTokenizer.nextToken(), ":");
            String propertyType = propertyTokenizer.nextToken();
            String propertyValue = propertyTokenizer.nextToken();

            // Set the appropriate properties
            switch (propertyType) {
                case "Name":
                    createdPoly.setName(propertyValue);
                    break;
                case "Velocity":
                    StringTokenizer velocityTokenizer = new StringTokenizer(propertyValue, " ");
                    double xVelocity = Double.parseDouble(velocityTokenizer.nextToken());
                    double yVelocity = Double.parseDouble(velocityTokenizer.nextToken());
                    createdPoly.getVelocity().setXY(xVelocity, yVelocity);
                    break;
                case "Is Moveable":
                    createdPoly.setMoveable(Boolean.parseBoolean(propertyValue));
                    break;
                case "Mass":
                    createdPoly.setMass(Double.parseDouble(propertyValue));
                    break;
                case "Angle":
                    createdPoly.rotate(Double.parseDouble(propertyValue));
                    break;
                case "Vertices":
                    StringTokenizer verticesTokenizer = new StringTokenizer(propertyValue, ",");
                    while (verticesTokenizer.hasMoreTokens()) {
                        StringTokenizer vertexTokenizer = new StringTokenizer(verticesTokenizer.nextToken(), " ");
                        double xVertexComponent = Double.parseDouble(vertexTokenizer.nextToken());
                        double yVertexComponent = Double.parseDouble(vertexTokenizer.nextToken());
                        createdPoly.getVertices().add(new Vector(xVertexComponent, yVertexComponent));
                    }
                    break;
            }
        }

        // Initialise the center of mass
        if (createdPoly != null)
            createdPoly.computeCenterOfMass();

        System.out.println("    Successfully Created PPolygon !!!");
        return createdPoly;
    }

    /*
       Pre-condition: "properitesTokenizer" must not be null; it must have valid property syntaxes (i.e. propertyName:propertyValue)
       Post-condition: Creates a PCircle object based on the properties listed in the StringTokenizer
       @param propertiesTokenizer The StringTokenizer containing the properties of the PCircle
       @return The generated PCircle
    */
    private PCircle createCircleBody(StringTokenizer propertiesTokenizer) {
        PCircle createdCircle = new PCircle("");

        while (propertiesTokenizer.hasMoreTokens()) {
            StringTokenizer propertyTokenizer = new StringTokenizer(propertiesTokenizer.nextToken(), ":");
            String propertyType = propertyTokenizer.nextToken();
            String propertyValue = propertyTokenizer.nextToken();

            switch (propertyType) {
                case "Name":
                    createdCircle.setName(propertyValue);
                    break;
                case "Velocity":
                    StringTokenizer velocityTokenizer = new StringTokenizer(propertyValue, " ");
                    double xVelocity = Double.parseDouble(velocityTokenizer.nextToken());
                    double yVelocity = Double.parseDouble(velocityTokenizer.nextToken());
                    createdCircle.getVelocity().setXY(xVelocity, yVelocity);
                    break;
                case "Is Moveable":
                    createdCircle.setMoveable(Boolean.parseBoolean(propertyValue));
                    break;
                case "Mass":
                    createdCircle.setMass(Double.parseDouble(propertyValue));
                    break;
                case "Angle":
                    createdCircle.rotate(Double.parseDouble(propertyValue));
                    break;
                case "Radius":
                    createdCircle.setRadius(Double.parseDouble(propertyValue));
                    break;
                case "CenterPoint":
                    StringTokenizer centerPtTokenizer = new StringTokenizer(propertyValue, " ");
                    double xCenterPt = Double.parseDouble(centerPtTokenizer.nextToken());
                    double yCenterPt = Double.parseDouble(centerPtTokenizer.nextToken());
                    createdCircle.getCenterPt().setXY(xCenterPt, yCenterPt);
                    break;
            }
        }

        System.out.println("   Successfully Loaded PCircle !!!!!!!");
        return createdCircle;
    }
}

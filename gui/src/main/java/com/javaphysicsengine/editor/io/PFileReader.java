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
import com.javaphysicsengine.utils.Vector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

public class PFileReader {

    private InputStream inputStream;
    private Optional<List<PBody>> bodies = Optional.empty();
    private Optional<List<PConstraints>> constraints = Optional.empty();

    /**
     * Creates the PFileReader from an input stream
     * @param inputStream the input stream
     */
    public PFileReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Gets the body from the input stream
     * @return a list of bodies
     */
    public List<PBody> getBodies() {
        if (!bodies.isPresent()) {
            loadBodiesAndConstraints();
        }
        return bodies.get();
    }

    /**
     * Gets the constraints from the input stream
     * @return a list of constraints
     */
    public List<PConstraints> getConstraints() {
        if (!constraints.isPresent()) {
            loadBodiesAndConstraints();
        }
        return constraints.get();
    }

    /**
     * Loads the {@code this.bodies} and {@code this.constraints} from the input stream
     */
    private void loadBodiesAndConstraints() {
        // Start storing the bodies and constraints coming from the text file
        List<PBody> bodies = new ArrayList<>();
        List<PConstraints> constraints = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                StringTokenizer outerBracketTokenizer = new StringTokenizer(line, "{}");
                String bodyType = outerBracketTokenizer.nextToken();
                String bodyProperties = outerBracketTokenizer.nextToken();
                StringTokenizer propertiesTokenizer = new StringTokenizer(bodyProperties, ";");

                switch (bodyType) {
                    case "PPolygon":
                        System.out.println("Loading Polygon:");
                        bodies.add(createPolygonBody(propertiesTokenizer));
                        break;
                    case "PCircle":
                        System.out.println("Loading Circle:");
                        bodies.add(createCircleBody(propertiesTokenizer));
                        break;
                    case "PSpring":
                        System.out.println("Loading Spring");
                        constraints.add(createSpringConstraint(propertiesTokenizer, bodies));
                        break;
                    case "PString":
                        System.out.println("Loading String");
                        constraints.add(createStringConstraint(propertiesTokenizer, bodies));
                        break;
                }
            }

        } catch (IOException ioe) {
            System.out.println("Exception while reading input " + ioe);
        }

        this.bodies = Optional.of(bodies);
        this.constraints = Optional.of(constraints);
    }

    /**
     * Creates a PSpring object based on the properties listed in the StringTokenizer
     * Note: "properitesTokenizer" and "bodies" must not be null
     * @param bodies A list of bodies already created
     * @param propertiesTokenizer The StringTokenizer containing the properties of the PPolygon
     * @return PSpring The generated PSpring
     */
    private PSpring createSpringConstraint(StringTokenizer propertiesTokenizer, List<PBody> bodies) {
        PSpring createdSpring = new PSpring(null, null);
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

    /**
     * Creates a PString object based on the properties listed in the StringTokenizer
     * Note: "properitesTokenizer" and "bodies" must not be null
     * @param bodies A list of bodies already created
     * @param propertiesTokenizer The StringTokenizer containing the properties of the PPolygon
     * @return PString The generated PString
     */
    private PString createStringConstraint(StringTokenizer propertiesTokenizer, List<PBody> bodies) {
        PString createdString = new PString(null, null);
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

    /**
     * Creates a PPolygon object based on the properties listed in the StringTokenizer
     * Note: "properitesTokenizer" must not be null; it must have valid property syntaxes (i.e. propertyName:propertyValue)
     * @param propertiesTokenizer The StringTokenizer containing the properties of the PPolygon
     * @return PPolygon The generated PPolygon
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
        createdPoly.computeCenterOfMass();

        System.out.println("    Successfully Created PPolygon !!!");
        return createdPoly;
    }

    /**
     * Creates a PCircle object based on the properties listed in the StringTokenizer
     * Note: "properitesTokenizer" must not be null; it must have valid property syntaxes (i.e. propertyName:propertyValue)
     * @param propertiesTokenizer The StringTokenizer containing the properties of the PCircle
     * @return The generated PCircle
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

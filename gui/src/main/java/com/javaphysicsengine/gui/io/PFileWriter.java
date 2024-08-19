/*
 * Reads in the bodies from an output stream.
 * @author Emilio Kartono
 * @version January 15 2016
 */

package com.javaphysicsengine.gui.io;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.api.body.PString;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

public class PFileWriter {

    private BufferedOutputStream bufferedOutputStream;

    /**
     * Constructs the PFileWriter
     * @param outputStream the output stream
     */
    public PFileWriter(OutputStream outputStream) {
        this.bufferedOutputStream = new BufferedOutputStream(outputStream);
    }

    /**
     * Saves a body to the specified output stream in UTF-8 form.
     * @param body a body
     * @throws IOException thrown when it cannot save the body.
     */
    public void saveBody(PBody body) throws IOException {
        String line = "";
        if (body instanceof PPolygon)
            line += "PPolygon";
        else if (body instanceof PCircle)
            line += "PCircle";

        // Adding the properties of the body
        line += "{";
        line += body.toString();
        line += "}";

        bufferedOutputStream.write(line.getBytes(Charset.forName("UTF-8")));
    }

    /**
     * Saves the bodies to the specified output stream in UTF-8 form.
     * @param bodies a list of bodies
     * @throws IOException thrown when it cannot save the bodies.
     */
    public void saveBodies(List<PBody> bodies) throws IOException {
        for (PBody body : bodies) {
            saveBody(body);
        }
    }

    /**
     * Saves a constraint to the specified output stream in UTF-8 form.
     * @param constraint a list of constraints
     * @throws IOException thrown when it cannot save the constraint.
     */
    public void saveConstraint(PConstraints constraint) throws IOException {
        String line = "";
        if (constraint instanceof PSpring)
            line += "PSpring";
        else if (constraint instanceof PString)
            line += "PString";

        // Adding the properties of the constraint
        line += "{";
        line += constraint.toString();
        line += "}";

        bufferedOutputStream.write(line.getBytes(Charset.forName("UTF-8")));
    }

    /**
     * Saves the constraints to the specified output stream in UTF-8 form.
     * @param constraints a list of constraints
     * @throws IOException thrown when it cannot save the constraints.
     */
    public void saveConstraints(List<PConstraints> constraints) throws IOException {
        for (PConstraints constraint : constraints) {
            saveConstraint(constraint);
        }
    }
}

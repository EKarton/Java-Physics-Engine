package com.javaphysicsengine.editor.io;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.api.body.PConstraints;
import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.api.body.PSpring;
import com.javaphysicsengine.api.body.PString;
import com.javaphysicsengine.utils.File;

import java.util.ArrayList;
import java.util.List;

public class PBodyFileWriter {

    /**
     * Saves the bodies and constraints to a file.
     * @param bodies the bodies
     * @param constraints the constraints
     * @param filePath the file path
     */
    public void saveBodiesToFile(List<PBody> bodies, List<PConstraints> constraints, String filePath){
        ArrayList<String> fileLines = new ArrayList<>();

        // Adding all the bodies to an array of strings
        for (PBody body : bodies) {
            String line = "";
            if (body instanceof PPolygon)
                line += "PPolygon";
            else if (body instanceof PCircle)
                line += "PCircle";

            // Adding the properties of the body
            line += "{";
            line += body.toString();
            line += "}";
            fileLines.add(line);
        }

        // Adding the constraints
        for (PConstraints constraint : constraints) {
            String line = "";
            if (constraint instanceof PSpring)
                line += "PSpring";
            else if (constraint instanceof PString)
                line += "PString";

            // Adding the properties of the constraint
            line += "{";
            line += constraint.toString();
            line += "}";
            fileLines.add(line);
        }

        // Save them to a text file
        String[] fileLines_Array = new String[fileLines.size()];
        fileLines_Array = fileLines.toArray(fileLines_Array);
        File.write(fileLines_Array, filePath);
    }
}

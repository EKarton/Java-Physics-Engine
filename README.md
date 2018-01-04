# Java Physics Engine

### Description
The Java Physics Engine aims to incorporate physics capabilities to any Java projects easily. Equipped with advanced physics and object collision algorithms, the Java Physics Engine can simulate and handle object collisions realistically in 2D space. The sky is the limit when it comes to creating games with the Java Physics Engine!

The Java Physics Engine is a project made during my final year in high school. The project is comprised of two components: the Java Physics Engine API and the Java Physics Engine GUI Editor. 

### Table of Contents
- Walkthrough
- Installation
- Usage
- Credits
- License

### Walkthrough of this project
This project consists of several components, each responsible for performing a certain task. The image below illustrates the system architecture of the project.
<div width="100%">
    <p align="center">
<img src="https://raw.githubusercontent.com/EKarton/Java-Physics-Engine/master/Documents/Images/SystemArchitecture.PNG" width="600px"/>
    </p>
</div>

Using the Object Creation Toolbar, users are able to create various objects.
<div width="100%">
    <p align="center">
    <img src="https://raw.githubusercontent.com/EKarton/Java-Physics-Engine/master/Documents/Images/Toolbar.PNG" width="600px"/>
    </p>
</div>

To help users create their objects, a snapping tool is used to snap the cursor to the nearest critical point (ex: points to line edges, center of mass, etc). This only occurs during object creation, and when the cursor is close to a critical point.
<div width="100%">
    <p align="center">
    <img src="https://raw.githubusercontent.com/EKarton/Java-Physics-Engine/master/Documents/Images/SnappingTool.PNG" width="600px"/>
    </p>
</div>

After creating an object, users are able to edit their object properties. The tabs correspond to the names of their object, and selecting different tabs will display the properties for each corresponding object. After editing an object property, users must type the Enter key to save the changes. Note: every object name must be unique and it is strictly enforced by the app.
<div width="100%">
    <p align="center">
    <img src="https://raw.githubusercontent.com/EKarton/Java-Physics-Engine/master/Documents/Images/PropertiesTab2.PNG" width="600px"/>
    </p>
</div>

Users will be using the Java Physics Engine GUI editor to generate API code from the objects made in the editor. 
<div width="100%">
    <p align="center">
<img src="https://raw.githubusercontent.com/EKarton/Java-Physics-Engine/master/Documents/Images/GeneratedAPICode.PNG" width="600px"/>
    </p>
</div>

In addition, users can generate the API code neede to implement their objects into their own Java objects. They can do this by clicking on View -> View Generated API Code. A new window will appear, and users can copy the code from the window.
<div width="100%">
    <p align="center">
<img src="https://raw.githubusercontent.com/EKarton/Java-Physics-Engine/master/Documents/Images/SimulationWindow.PNG" width="600px"/>
    </p>
</div>

Furthermore, Users are able to render the objects by navigating to Run -> Run Simulation. A new window will appear, simulating the objects in real time.
<div width="100%">
    <p align="center">
        <img src="https://raw.githubusercontent.com/EKarton/Java-Physics-Engine/master/Documents/Images/GeneratedAPICode.PNG" width="600px"/>
    </p>
</div>

### Installation
##### Required Programs and Tools:
- Java SDK

##### If you have not already...
- Add the Java SDK to your OS' Environment Variables / Path.

##### Running the program through the command line
- Launch the Command Line and navigate to the appropriate Java Physics Engine directory.
- Compile all of the .java files in the PhysicsAPIEditor, PhysicsEngine, and ToolBox folder through the **javac {file name}.java**
- Run the GUI editor by navigating to the PhysicsEngine folder (which holds the source code for the GUI editor), and runing the command: **java PEditorFrame**

### Usage
Please note that this project is used for educational purposes and is not intended to be used commercially. We are not liable for any damages/changes done by this project.

### Credits
EKarton

### License
This project is protected under the GNU licence. Please refer to the Licence.txt for more information.

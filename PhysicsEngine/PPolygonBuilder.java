package PhysicsEngine;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import ToolBox.Vector;
import ToolBox.Trigonometry;

public class PPolygonBuilder
{
  // Fields representing the geometric and graphic structure of a polygon
  private ArrayList<Vector> vertices = new ArrayList<Vector>();
  private String name;
  
  public PPolygonBuilder() {

  }

  public PPolygonBuilder addVertex(Vector vector) {
    vertices.add(vector);
    return this;
  }

  public PPolygonBuilder addName(String name) {
    this.name = name;
    return this;
  }

  public PPolygon build() {
    PPolygon polygon = new PPolygon(this.name);

    for (Vector vertex : vertices) {
      polygon.getVertices().add(vertex);
    }

    return polygon;
  }
}
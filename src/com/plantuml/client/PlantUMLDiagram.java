package com.plantuml.client;

import java.io.File;
import java.net.URI;

/**
 *  Holds the diagram data, such as:
 *  <ul>
 *      <li>local image file location</li>
 *      <li>remote image URI on the server</li>
 *      <li>original UML text</li>
 *  </ul>
 */
public class PlantUMLDiagram {

    private File file;
    private URI uri;
    private String uml;

    PlantUMLDiagram(File file, URI uri, String uml) {
        this.file = file;
        this.uri = uri;
        this.uml = uml;
    }

    public File getImageFile() {
        return this.file;
    }
    
    public URI getImageURI() {
        return this.uri;
    }
    
    public String getUMLText() {
        return this.uml;
    }
    
}

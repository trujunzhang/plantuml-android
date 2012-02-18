package com.plantuml.client;

import android.test.AndroidTestCase;

import java.io.File;
import java.io.IOException;

public class PlantUMLClientTest extends AndroidTestCase {

    PlantUMLClient client;
    
    public void setUp() throws Exception {
        File dir = new File("/sdcard/tmp/plantuml");
        dir.mkdirs();
        client = new PlantUMLClient(dir);
    } 

    public void testGetDiagramFile() throws IOException {
        client.getDiagramFile("Alice -> Bob");
    }

}

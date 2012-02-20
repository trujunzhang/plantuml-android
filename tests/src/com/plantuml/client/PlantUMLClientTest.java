package com.plantuml.client;

import android.test.AndroidTestCase;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PlantUMLClientTest extends AndroidTestCase {

    PlantUMLClient client;
    
    public void setUp() throws Exception {
        File dir = new File("/sdcard/tmp/plantuml");
        dir.mkdirs();
        client = new PlantUMLClient(dir);
    } 

    public void testGetDiagramFile() throws IOException {
        long startTime = System.currentTimeMillis();
        File image = client.getDiagramFile("Alice -> Bob");
        assertTrue(image.isFile());
        assertTrue(image.lastModified() > startTime);
        assertTrue(image.length() > 0);
    }

    public void testGetDiagramFileTwoLines() throws IOException {
        long startTime = System.currentTimeMillis();
        File image = client.getDiagramFile("Alice -> Bob\nBob -> Carol");
        assertTrue(image.isFile());
        assertTrue(image.lastModified() > startTime);
        assertTrue(image.length() > 0);
    }

    public void testGetEmptyDiagramFile() throws IOException {
        long startTime = System.currentTimeMillis();
        try {
            File image = client.getDiagramFile("");
        } catch (IllegalArgumentException e) {
            return; //pass
        }
        fail();
    }
    
    public void testGetImageFile() throws URISyntaxException {
        assertEquals(new File("/sdcard/tmp/plantuml/file.png"),
                client.getImageFile(new URI("http://plantuml.com:80/img/file")));
    }

}

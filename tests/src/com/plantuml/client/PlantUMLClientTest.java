package com.plantuml.client;

import android.test.AndroidTestCase;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.CharBuffer;

public class PlantUMLClientTest extends AndroidTestCase {

    PlantUMLClient client;
    
    public void setUp() throws Exception {
        File dir = new File("/sdcard/tmp/plantuml");
        dir.mkdirs();
        client = new PlantUMLClient(dir);
    } 

    public void testGetDiagramFile() throws IOException, PlantUMLClientException {
        long startTime = System.currentTimeMillis();
        File image = client.getDiagramFile("Alice -> Bob");
        assertTrue(image.isFile());
        assertTrue(image.lastModified() > startTime);
        assertTrue(image.length() > 0);
    }

    public void testGetDiagramFileTwoLines() throws IOException, PlantUMLClientException {
        long startTime = System.currentTimeMillis();
        File image = client.getDiagramFile("Alice -> Bob\nBob -> Carol");
        assertTrue(image.isFile());
        assertTrue(image.lastModified() > startTime);
        assertTrue(image.length() > 0);
    }

    public void testGetEmptyDiagramFile() throws IOException, PlantUMLClientException {
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

    public void testGetDiagramTagInUML() throws IOException, PlantUMLClientException {
        InputStreamReader res = new InputStreamReader(this.getClass().getResourceAsStream("curve.puml"));
        StringBuilder buf = new StringBuilder();
        int c = -1;
        while ((c = res.read()) >= 0) {
            buf.append((char)c);
        }
        long startTime = System.currentTimeMillis();
        File image = client.getDiagramFile(buf.toString());
        assertTrue(image.isFile());
        assertTrue(image.lastModified() > startTime);
        assertTrue(image.length() > 0);
    }

    public void testGetDiagramLongFileName() throws IOException, PlantUMLClientException {
        InputStreamReader res = new InputStreamReader(this.getClass().getResourceAsStream("curve2.puml"));
        StringBuilder buf = new StringBuilder();
        int c = -1;
        while ((c = res.read()) >= 0) {
            buf.append((char)c);
        }
        long startTime = System.currentTimeMillis();
        File image = client.getDiagramFile(buf.toString());
        assertTrue(image.isFile());
        assertTrue(image.lastModified() > startTime);
        assertTrue(image.length() > 0);
    }

}

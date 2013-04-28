package com.plantuml.client;

import android.test.AndroidTestCase;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PlantUMLClientTest extends AndroidTestCase {

    PlantUMLClient client;
    
    public void setUp() throws Exception {
        File dir = new File("/sdcard/tmp/plantuml");
        dir.mkdirs();
        client = new PlantUMLClient(dir);
    }

    void checkPNGFile(File image, long newerThan) throws IOException {
        assertTrue("not a file", image.isFile());
        assertTrue("not a new file", image.lastModified() > newerThan);
        assertTrue("empty file", image.length() > 0);
        InputStream in = new FileInputStream(image);
        byte[] sign = new byte[8];
        in.read(sign);
        in.close();
        //http://www.libpng.org/pub/png/spec/1.2/PNG-Structure.html
        assertEquals("not a png image", 137-256, sign[0]);
        assertEquals("not a png image", 80, sign[1]);
        assertEquals("not a png image", 78, sign[2]);
        assertEquals("not a png image", 71, sign[3]);
        assertEquals("not a png image", 13, sign[4]);
        assertEquals("not a png image", 10, sign[5]);
        assertEquals("not a png image", 26, sign[6]);
        assertEquals("not a png image", 10, sign[7]);
    }

    public void testGetDiagramFile() throws IOException, PlantUMLClientException {
        long startTime = System.currentTimeMillis();
        File image = client.getDiagramFile("Alice -> Bob");
        checkPNGFile(image, startTime);
    }

    public void testGetDiagramFileTwoLines() throws IOException, PlantUMLClientException {
        long startTime = System.currentTimeMillis();
        File image = client.getDiagramFile("Alice -> Bob\nBob -> Carol");
        checkPNGFile(image, startTime);
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss");
        assertEquals(new File("/sdcard/tmp/plantuml/plantuml_" +
                format.format(new Date()) + ".png"),
                client.getImageFile());
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
        checkPNGFile(image, startTime);
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
        checkPNGFile(image, startTime);
    }

    public void testSetServerURI1() throws URISyntaxException {
        URI uri = new URI("http://plantuml.example.net/plantuml/img/");
        client.setServerURI(uri);
        assertEquals(new URI("http://plantuml.example.net/plantuml/img/"), client.getImageURI);
    }

    public void testSetServerURI2() throws URISyntaxException {
        URI uri = new URI("http://plantuml.example.net/plantuml/img");  //no final slash
        client.setServerURI(uri);
        assertEquals(new URI("http://plantuml.example.net/plantuml/img/"), client.getImageURI);
    }

}

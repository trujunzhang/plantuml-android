package com.plantuml.client;

import android.test.AndroidTestCase;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
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

    public void testGetDiagram() throws IOException, PlantUMLClientException, URISyntaxException {
        long startTime = System.currentTimeMillis();
        PlantUMLDiagram diagram = client.getDiagram("Alice -> Bob");
        checkPNGFile(diagram.getImageFile(), startTime);
        assertEquals(new URI("http://plantuml.com/plantuml/img/Syp9J4vLqBLJSCfF0W00"), diagram.getImageURI());
        assertEquals("Alice -> Bob", diagram.getUMLText());
    }

    public void testGetDiagramFileTwoLines() throws IOException, PlantUMLClientException, URISyntaxException {
        long startTime = System.currentTimeMillis();
        PlantUMLDiagram diagram = client.getDiagram("Alice -> Bob\r\nBob -> Carol");
        checkPNGFile(diagram.getImageFile(), startTime);
        assertEquals(new URI("http://plantuml.com/plantuml/img/Syp9J4vLqBLJSCfFukK24Y2sSs9HVWu0"), diagram.getImageURI());
        assertEquals("Alice -> Bob\r\nBob -> Carol", diagram.getUMLText());
    }

    public void testGetEmptyDiagramFile() throws IOException, PlantUMLClientException {
        try {
            client.getDiagram("");
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

    public void testGetDiagramTagInUML() throws IOException, PlantUMLClientException, URISyntaxException {
        InputStreamReader res = new InputStreamReader(this.getClass().getResourceAsStream("curve.puml"));
        StringBuilder buf = new StringBuilder();
        int c = -1;
        while ((c = res.read()) >= 0) {
            buf.append((char)c);
        }
        long startTime = System.currentTimeMillis();
        PlantUMLDiagram diagram = client.getDiagram(buf.toString());
        checkPNGFile(diagram.getImageFile(), startTime);
        assertEquals(new URI("http://plantuml.com/plantuml/img/" +
                "fLRRSfim47tFJFw1ntQcvMeSTwRjn74dxKCcpTJv0La8h4QMN4caaA_lIlW29DocC1B6EdlExcebSQqqaletnazKlvlx_eV8zzz" +
                "VNYH88m_dyfaevujZVmJL8wmUKHqJU9fgHev89sWE5Z3Wko6q2DeCMjm4b1PW1WFdY6n0mfP0s1my2jGLeCsXNK5RDD6LMdX4p0" +
                "xFkBAkOwEMRDGSh6HZnn9OBIDckazDOAKO6uLuvWJkt6gPxWeezsXFf4707HYkouiDma-P53bFpFz5QYz0Cg4zogccYFsWNDyAU" +
                "KyAlJ8g7cP8gNkqC-vMy-aQlFiW51W8RWm6AFO7QEMC6npWM0YPK8uqMPTAavqd9CseHRb-t0SibWLL7eBk3cq80ulFnTT-S2Qf" +
                "2LhBd0m3brfBkiarKHwZdAm44pQJnS8tzu1RkHOxf2b-f7Y56CuPEWIV8gO6r7c-sn3vCmLRiqBc_MWGiGce-PiAhjVqXLHPZ4P" +
                "ci9CWdawWpRoVCePG5qFvQX9JXBMG8_4FjQ9o5xtbKlvliEN8O0cPpl1C94mZOakEjvP6Ytq_YvbrmZEz1V17SDkV3-DVPonm_7" +
                "5cqukhtrwNTklR29PC9e0CqHSWgQMBaHJsduIaA6Vw6qdj9YIufyeTIDpQaYexTx7rdnn9aArXXydKAGL1F8n6P_VVy2AJQB-bs" +
                "Br1YkBlrSivu4diZQrgikKZ23znebGLcH--nM20or7nhBTm_FG7qY8phm_W59678o2mViGZJTl-ZxKxhrvysghCgNLCKtAoDipm" +
                "5fWkD5tKWG-y2jaIxqIFAzKR94yLEef8lLwf0nQraaWfO-VKf2b6lj_9yfVgxHtaidt6jO3XfBo48_EoeXApzX8UnTWPxiBl33O" +
                "SjVuwZAFU132I4Pvu-Hw-stxHR0i7Y-JL9voxi1-VioPo8ufbLmZ4NasySV1qSfYoW3Lo3GDLDly1"),
                diagram.getImageURI());
        assertEquals(buf.toString(), diagram.getUMLText());
    }

    public void testGetDiagramLongFileName() throws IOException, PlantUMLClientException, URISyntaxException {
        InputStreamReader res = new InputStreamReader(this.getClass().getResourceAsStream("curve2.puml"));
        StringBuilder buf = new StringBuilder();
        int c = -1;
        while ((c = res.read()) >= 0) {
            buf.append((char)c);
        }
        long startTime = System.currentTimeMillis();
        PlantUMLDiagram diagram = client.getDiagram(buf.toString());
        checkPNGFile(diagram.getImageFile(), startTime);
        assertEquals(new URI("http://plantuml.com/plantuml/img/" +
                "fLRRSfim47tFJFw1ntQcvMeSzw4pSTn9-v19FNL-G1O2gv6HAuaKylLTIRu02RSfZ8Inpfwpkrf9t4g5XFgDyJDL7-Q-_odo_O_" +
                "NLmbIoCE5U27I-UgO7u5zXFOHrJ61fwXYv8HqWkPW2GE-6qABeCsWnKr0PG1wC71Eo0OaR0c4pS6JG5q1sXpQ3RH54stLmXDYTd" +
                "p6rhgEZLgoKNFGoiQE8J1QMantNvf0gnXhXNZc1ExSQEdk1YWFw424Hi0TQAxhgot2pvdWHPxe_ylKN81aN7aqfueYzeFcwfwBH" +
                "rAgbLRnC4DIFgAzTjVEfwlnxffBCE2vDXYWE1oXrXatEC8nvoAXEL9aKqb5zXuNDAC6vVfn7x1SbbHwcQ0w3YKACWnSAYNejb14" +
                "UepcPCKPDyaf5xw--mbGeVWUAOgVA5uXXWk6Zl6aYCa1zRpORuducOAjBlfvFnf47j2Mi9g_AS_LXhuIcvXem5Ne-WdYwGHAJ-K" +
                "vOmXrCLHD1fCVhBWOYL_NwiHTz5P0zR_1LYE39MGwmmiHC8s8BNEyCpIA7_fPzAoJF5Cx07y6j_tvCFvjnWB77sSslRZwxLLbTh" +
                "CjPyba0iWGVGMIMheOIM5BIKYA2gQ-aTJiApptPBK7YNjJKbNdnhJvKo01iZNiC9ayfo28Xz7euluxNcG27NOKktT8KlpTlbm2d" +
                "iJUsAecMpw1y7DEfBIH-U5x31gu71NFPWSdIdyWBJBzkWQdY3Zkwe7n8nvfslP_hDrbzUBpLgKFebEQahErPeQtG7UXxg8EVE1P" +
                "P4ky4pskLE-GE5VeA2BvTgKEMDHA8gMCNLAJfXZvVYVBNwfs3v3BzXdN0eQJyXAEpCkA2ipQItWKOsUu2xyps73K-kimZdeJm4X" +
                "6yiGh3l0fzejcEpXOH6w_ojo5-PwSDP5RNYwxGY3cQkArW-UJGvS5h95R6B3P_0S0"),
                diagram.getImageURI());
        assertEquals(buf.toString(), diagram.getUMLText());
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

package com.plantuml.client;

import android.test.AndroidTestCase;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class HTMLParserTest extends AndroidTestCase {

    InputStream html;
    
    public void setUp() throws Exception {
        html = getContext().getResources().getAssets().open("response.html");
    }

    public void testParseImageURI() throws IOException, XmlPullParserException, URISyntaxException {
        assertEquals(new URI("http://plantuml.com:80/plantuml/img/Syp9J4vLqBLJSCfF0W00"),
                HTMLParser.parseImageURI(html));
    }

}

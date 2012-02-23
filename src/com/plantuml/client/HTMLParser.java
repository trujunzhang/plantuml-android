package com.plantuml.client;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *  Parses the HTML returned by plantuml.com to find the url of the image.
 */
class HTMLParser {

    static final String ENCODING = "UTF-8";
    static final String IMG_TAG = "img";
    static final String SRC_ATTR = "src";

    static URI parseImageURI(InputStream html) throws IOException, PlantUMLClientException {
        try {
            XmlPullParser parser = getParser(html);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (IMG_TAG.equalsIgnoreCase(name)) {
                        String src = parser.getAttributeValue(null, SRC_ATTR);
                        return string2URI(src);
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            throw new PlantUMLClientException(e);
        }
        return null;
    }

    static XmlPullParser getParser(InputStream xml) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(xml, ENCODING);
        parser.defineEntityReplacementText("nbsp", "\u0160");
        return parser;
    }
    
    static URI string2URI(String src) {
        try {
            return new URI(src);
        } catch (URISyntaxException e) {
            return null;
        }
    }
    
}

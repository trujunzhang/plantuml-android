package com.plantuml.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Simple client to PlantUMLServer on plantuml.com.
 */
public class PlantUMLClient {

    static final URI DEFAULT_URI;
    static {
        try {
            DEFAULT_URI = new URI("http://plantuml.com/plantuml/form");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);  //should never happen
        }
    }
    
    static final String ENCODING = "UTF-8";
    static final String TEXT_PARAM = "text";

    URI submitURI = DEFAULT_URI;

    File tempPath;

    /**
     *  Creates the client.
     *  @param tempPath  path to store image files
     */
    public PlantUMLClient(File tempPath) {
        if (tempPath == null) {
            throw new NullPointerException("tempPath cannot be null");
        }
        if (!tempPath.isDirectory() || !tempPath.canWrite()) {
            throw new IllegalArgumentException("tempPath must exist and be writable");
        }
        this.tempPath = tempPath;
    }

    /**
     *  Converts the input text into the UML diagram.
     *  Sends the text into plantuml.com server,
     *  saves the resulting image as a file in temp folder,
     *  Returns the new file location.
     *  @param  uml UML as a text in PlantUML format
     *  @return the location of the image file in the temp folder
     */
    public File getDiagramFile(String uml) throws IOException {
        HttpPost request = new HttpPost(this.submitURI);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(TEXT_PARAM, uml));
        try {
            request.setEntity(new UrlEncodedFormEntity(parameters, ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);  //should never happen
        }
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        HttpEntity responseEntity = response.getEntity();
        File file = new File(this.tempPath, "response.html");
        responseEntity.writeTo(new FileOutputStream(file));
        return file;
    }
    
}

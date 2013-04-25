package com.plantuml.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    static final SimpleDateFormat FILE_PATTERN = new SimpleDateFormat("'plantuml_'yyyy-MM-dd'T'HH-mm-ss'.png'");

    URI submitURI = DEFAULT_URI;
    File tempPath;
    HttpClient client;

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
        //if (!tempPath.isDirectory()) {
        //    throw new IllegalArgumentException("tempPath must exist");
        //}
        this.tempPath = tempPath;
        this.client = new DefaultHttpClient();
    }

    /**
     *  Converts the input text into the UML diagram.
     *  Sends the text into plantuml.com server,
     *  saves the resulting image as a file in temp folder,
     *  Returns the new file location.
     *  @param  uml UML as a text in PlantUML format
     *  @return the location of the image file in the temp folder
     *  @throws PlantUMLClientException if conversion cannot be done
     */
    public File getDiagramFile(String uml) throws PlantUMLClientException {
        if (uml == null) {
            throw new NullPointerException("uml cannot be null");
        }
        if ("".equals(uml)) {
            throw new IllegalArgumentException("uml cannot be empty string");
        }
        try {
            InputStream html = getHTML(uml);
            URI imageURI = HTMLParser.parseImageURI(html);
            if (imageURI == null) {
                throw new PlantUMLClientException("no image uri");
            }
            File imageFile = getImageFile();
            loadImage(imageURI, imageFile);
            return imageFile;
        } catch (IOException e) {
            throw new PlantUMLClientException(e);
        }

    }
    
    InputStream getHTML(String uml) throws IOException {
        HttpPost request = new HttpPost(this.submitURI);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(TEXT_PARAM, uml));
        try {
            request.setEntity(new UrlEncodedFormEntity(parameters, ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);  //should never happen
        }
        HttpResponse response = this.client.execute(request);
        HttpEntity responseEntity = response.getEntity();
        return responseEntity.getContent();
    }
    
    File getImageFile() {
        return new File(this.tempPath, FILE_PATTERN.format(new Date()));
    }

    void loadImage(URI uri, File file) throws IOException {
        HttpGet request = new HttpGet(uri);
        HttpResponse response = this.client.execute(request);
        HttpEntity responseEntity = response.getEntity();
        responseEntity.writeTo(new FileOutputStream(file));
    }
    
}

package ru.gelin.android.plantuml.intent;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 *  Intent text which reads the text from
 *  the stream from the provided content URI.
 */
public class StreamText extends IntentText {

    /** Content resolver to read Uri */
    ContentResolver contentResolver;
    
    /** Uri of the stream */
    Uri uri;
    /** Flag indicating that the Uri was queried for some additional information */
    protected volatile boolean queried = false;
    
    StreamText(Context context, Intent intent) throws IntentException {
        this.contentResolver = context.getContentResolver();
        this.uri = getStreamUri(intent);
    }
    
    /**
     *  Reads the text from the stream.
     */
    @Override
    public String getText() throws IOException {
        Reader in = new InputStreamReader(getStream());
        StringBuilder out = new StringBuilder();
        char[] buf = new char[1024];
        int read;
        while ((read = in.read(buf)) > 0) {
            out.append(buf, 0, read);
        }
        in.close();
        return out.toString();
    }
    
    /**
     *  Returns the file as stream.
     */
    InputStream getStream() throws FileNotFoundException {
        return this.contentResolver.openInputStream(this.uri);
    }
    
    @Override
    public String toString() {
        return "stream: " + this.uri;
    }

}

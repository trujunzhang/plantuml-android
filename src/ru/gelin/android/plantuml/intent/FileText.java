package ru.gelin.android.plantuml.intent;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.util.List;

/**
 *  Reads text from file:// URI.
 */
public class FileText extends AbstractFileText {

    public FileText(Context context, Intent intent) throws IntentTextException {
        super(context, intent);
        this.file = getFile();
    }

    /**
     *  Returns the file as File for file:/// URIs
     *  @throws IntentTextException if the URI cannot be converted to file
     */
    File getFile() throws IntentTextException {
        try {
            List<String> pathSegments = this.uri.getPathSegments();
            File result = new File("/");
            for (String segment : pathSegments) {
                result = new File(result, segment);
            }
            return result;
        } catch (Exception e) {
            throw new IntentTextException("cannot convert URI to file", e);
        }
    }
    
    @Override
    public String toString() {
    	return "file: " + this.uri + " -> " + this.file;
    }

}

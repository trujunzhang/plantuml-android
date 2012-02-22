package ru.gelin.android.plantuml.intent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.IOException;

/**
 *  A text provided with the intent to be processed as UML.
 */
public abstract class IntentText {
    
    /**
     *  Creates the concrete instance of the IntentText from Intent.
     *  @throws IntentTextException if it's not possible to create the text from intent
     */
    public static IntentText getInstance(Context context, Intent intent) throws IntentException {
        if (isText(intent)) {
            return new TextText(intent);
        }
        Uri uri = getStreamUri(intent);
        //Log.i(TAG, "file uri: " + uri);
        if (uri == null) {
            throw new IntentException("null file uri");
        }
        String scheme = uri.getScheme();
        if ("file".equals(scheme)) {
            return new FileText(context, intent);
        } else if ("content".equals(scheme)) {
            return new ContentText(context, intent);
        }
        return new StreamText(context, intent);
    }

    /**
     *  Returns the text from the Intent;
     */
    abstract public String getText() throws IOException;
    
    /**
     *  Returns true if the file is plain/text.
     */
    static boolean isText(Intent intent) {
        //return "text/plain".equals(intent.getType());
        return intent.hasExtra(Intent.EXTRA_TEXT) && 
                !intent.hasExtra(Intent.EXTRA_STREAM);  //stream is more preferable
    }
    
    /**
     *  Returns the Uri of the stream of the intent.
     */
    static Uri getStreamUri(Intent intent) throws IntentException {
        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            return (Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            return intent.getData();
        } else {
            throw new IntentException("unknown intent action: " + intent.getAction());
        }
    }

}

package ru.gelin.android.plantuml.intent;

import android.content.Context;
import android.content.Intent;

import java.io.File;

/**
 *  Reads text from real file located on filesystem.
 */
public abstract class AbstractFileText extends StreamText {

    File file;
    
    public AbstractFileText(Context context, Intent intent) throws IntentTextException {
        super(context, intent);
    }

}

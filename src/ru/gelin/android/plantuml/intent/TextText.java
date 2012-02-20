package ru.gelin.android.plantuml.intent;

import android.content.Intent;

/**
 *  Reads text from text/plain content.
 *  The text is selected from the EXTRA_TEXT.
 */
public class TextText extends IntentText {

    private static final String TEXT_MIME_TYPE = "text/plain";
    
    /** The text content of the file */
    String text = "";
    
    TextText(Intent intent) throws IntentTextException {
        this.text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (this.text == null) {
            throw new IntentTextException("null text");
        }
    }
    
    @Override
    public String toString() {
        return "text: " + this.text;
    }

}

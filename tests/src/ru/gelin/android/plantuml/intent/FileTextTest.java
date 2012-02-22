package ru.gelin.android.plantuml.intent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileTextTest extends AndroidTestCase {

    public void setUp() throws IOException {
        OutputStream file = getContext().openFileOutput("alice.iml", Context.MODE_PRIVATE);
        Writer out = new OutputStreamWriter(file);
        out.write("Alice -> Bob");
        out.close();
    }
    
    public void testGetText() throws IOException, IntentTextException {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri file = Uri.fromFile(getContext().getFileStreamPath("alice.iml"));
        intent.putExtra(Intent.EXTRA_STREAM, file);
        assertEquals("Alice -> Bob", IntentText.getInstance(getContext(), intent).getText());
    }

    public void testGetEmptyText() throws IOException, IntentTextException {
        Intent intent = new Intent(Intent.ACTION_SEND);
        try {
            IntentText.getInstance(getContext(), intent).getText();
            fail();
        } catch (IntentTextException e) {
            //pass
        }
    }

    public void testGetTextViewAction() throws IOException, IntentTextException {
        Uri file = Uri.fromFile(getContext().getFileStreamPath("alice.iml"));
        Intent intent = new Intent(Intent.ACTION_VIEW, file);
        assertEquals("Alice -> Bob", IntentText.getInstance(getContext(), intent).getText());
    }

}

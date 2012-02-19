package ru.gelin.android.plantuml;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 *  Receives the UML text, converts it to the image with plantuml.com site, sends the image to another recipients.
 */
public class ConvertActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);

        Intent intent = getIntent();
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
    }

}
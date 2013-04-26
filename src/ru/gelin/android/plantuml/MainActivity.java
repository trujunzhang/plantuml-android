package ru.gelin.android.plantuml;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 *  Main activity which displays how-to-use instructions and allows to set the PlantUML server URL.
 */
public class MainActivity extends PreferenceActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preferences);
    }



}
package ru.gelin.android.plantuml;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.SherlockPreferenceActivity;

/**
 *  Main activity which displays how-to-use instructions and allows to set the PlantUML server URL.
 */
public class MainActivity extends SherlockPreferenceActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preferences);
    }



}
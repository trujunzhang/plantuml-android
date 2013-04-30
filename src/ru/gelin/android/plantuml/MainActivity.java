package ru.gelin.android.plantuml;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockPreferenceActivity;

/**
 *  Main activity which displays how-to-use instructions and allows to set the PlantUML server URL.
 */
public class MainActivity extends SherlockPreferenceActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preferences);
    }



}
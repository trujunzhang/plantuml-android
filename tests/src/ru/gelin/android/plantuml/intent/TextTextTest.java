package ru.gelin.android.plantuml.intent;

import android.content.Intent;
import android.test.AndroidTestCase;

import java.io.IOException;

public class TextTextTest extends AndroidTestCase {

    public void testGetText() throws IOException, IntentTextException {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Alice -> Bob");
        assertEquals("Alice -> Bob", IntentText.getInstance(getContext(), intent).getText());
    }

}

package ru.gelin.android.plantuml;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

/**
 *  Displays the diagram images saved locally.
 *  Allows to share/open the image in another programs.
 */
public class ImageViewActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view);

        Uri image = getIntent().getData();
        if (image == null) {
            finish();
            Toast.makeText(this, R.string.invalid_image, Toast.LENGTH_LONG).show();
            return;
        }

        WebView web = (WebView)findViewById(R.id.web_view);
        WebSettings settings = web.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        web.loadUrl(image.toString());
    }
}
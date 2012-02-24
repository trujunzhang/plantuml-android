package ru.gelin.android.plantuml;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.MenuInflater;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent origIntent = getIntent();
        Uri image = origIntent.getData();
        String type = origIntent.getType();
        if (image == null) {
            return super.onOptionsItemSelected(item);
        }
        Intent intent;
        switch (item.getItemId()) {
            case R.id.send_menu:
                intent = new Intent(Intent.ACTION_SEND, image);
                intent.setType(type);
                intent.putExtra(Intent.EXTRA_STREAM, image);
                startActivity(Intent.createChooser(intent, getString(R.string.send_to)));
                return true;
            case R.id.open_menu:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(image, type);
                startActivity(Intent.createChooser(intent, getString(R.string.open_in)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
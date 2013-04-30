package ru.gelin.android.plantuml;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 *  Displays the diagram images saved locally.
 *  Allows to share/open the image in another programs.
 */
public class ImageViewActivity extends SherlockActivity {

    public static final String EXTRA_IMAGE_URI = ImageViewActivity.class.getPackage().getName() + ".EXTRA_IMAGE_URI";
    public static final String EXTRA_UML_TEXT = ImageViewActivity.class.getPackage().getName() + ".EXTRA_UML_TEXT";

    public static final String TEXT_PLAIN = "text/plain";


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
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.image_view_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Intent origIntent = getIntent();
        menu.findItem(R.id.share_url_menu).setEnabled(origIntent.hasExtra(EXTRA_IMAGE_URI));
        menu.findItem(R.id.share_uml_menu).setEnabled(origIntent.hasExtra(EXTRA_UML_TEXT));
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
            case R.id.share_menu:
                intent = new Intent(Intent.ACTION_SEND, image);
                intent.setType(type);
                intent.putExtra(Intent.EXTRA_STREAM, image);
                startActivity(Intent.createChooser(intent, getString(R.string.share_to)));
                return true;
            case R.id.open_menu:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(image, type);
                startActivity(Intent.createChooser(intent, getString(R.string.open_in)));
                return true;
            case R.id.share_url_menu:
                if (!origIntent.hasExtra(EXTRA_IMAGE_URI)) {
                    return super.onOptionsItemSelected(item);
                }
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType(TEXT_PLAIN);
                intent.putExtra(Intent.EXTRA_TEXT, origIntent.getStringExtra(EXTRA_IMAGE_URI));
                startActivity(Intent.createChooser(intent, getString(R.string.share_url_to)));
                return true;
            case R.id.share_uml_menu:
                if (!origIntent.hasExtra(EXTRA_UML_TEXT)) {
                    return super.onOptionsItemSelected(item);
                }
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType(TEXT_PLAIN);
                intent.putExtra(Intent.EXTRA_TEXT, origIntent.getStringExtra(EXTRA_UML_TEXT));
                startActivity(Intent.createChooser(intent, getString(R.string.share_uml_to)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
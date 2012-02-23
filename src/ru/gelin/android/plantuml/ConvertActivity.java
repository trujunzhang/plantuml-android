package ru.gelin.android.plantuml;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.plantuml.client.PlantUMLClient;
import com.plantuml.client.PlantUMLClientException;
import ru.gelin.android.plantuml.intent.IntentText;

import java.io.File;

/**
 *  Receives the UML text, converts it to the image with plantuml.com site, sends the image to another recipients.
 */
public class ConvertActivity extends Activity {

    static final String TMP_DIR = "plantuml";
    static final String PNG_TYPE = "image/png";
    
    File tmpDir;
    MediaScanner mediaScanner;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);

        this.mediaScanner = new MediaScanner(this);

        String text = null;
        try {
            text = IntentText.getInstance(this, getIntent()).getText();
        } catch (Exception e) {
            finish();
            Toast.makeText(this, R.string.invalid_intent, Toast.LENGTH_LONG).show();
            return;
        }

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            finish();
            Toast.makeText(this, R.string.no_sd_card, Toast.LENGTH_LONG).show();
            return;
        }
        this.tmpDir = new File(Environment.getExternalStorageDirectory(), TMP_DIR);
        this.tmpDir.mkdirs();

        if (!isNetworkAvailable()) {
            finish();
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }

        new DownloadImageTask().execute(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mediaScanner.disconnect();
    }

    class DownloadImageTask extends AsyncTask<String, Void, File> {

        @Override
        protected File doInBackground(String... strings) {
            PlantUMLClient client = new PlantUMLClient(ConvertActivity.this.tmpDir);
            try {
                return client.getDiagramFile(strings[0]);
            } catch (PlantUMLClientException e) {
                Log.e(Tag.TAG, "cannot convert diagram", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(File file) {
            finish();
            if (file == null) {
                Toast.makeText(ConvertActivity.this, R.string.cannot_convert, Toast.LENGTH_LONG).show();
                return;
            }
            ConvertActivity.this.mediaScanner.scanFile(file, PNG_TYPE);
            Uri fileUri = Uri.fromFile(file);
            //Intent intent = new Intent(Intent.ACTION_SEND, fileUri);
            //intent.setType(PNG_TYPE);
            //intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClass(ConvertActivity.this, ImageViewActivity.class);
            intent.setDataAndType(fileUri, PNG_TYPE);
            //Intent chooser = Intent.createChooser(intent, getString(R.string.open_in));
            //startActivity(chooser);
            startActivity(intent);
        }

    }

    /**
     *  Check availability of network connections.
     *  Returns true if any network connection is available.
     */
    boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isAvailable();
    }


}
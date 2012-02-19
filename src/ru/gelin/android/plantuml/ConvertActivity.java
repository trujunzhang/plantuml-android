package ru.gelin.android.plantuml;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import com.plantuml.client.PlantUMLClient;

import java.io.File;
import java.io.IOException;

/**
 *  Receives the UML text, converts it to the image with plantuml.com site, sends the image to another recipients.
 */
public class ConvertActivity extends Activity {

    static final String TMP_DIR = "plantuml";
    static final String PNG_TYPE = "image/png";
    
    File tmpDir;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);

        //TODO: check intent correctness
        Intent intent = getIntent();

        //TODO: check SD card availability
        this.tmpDir = new File(Environment.getExternalStorageDirectory(), TMP_DIR);
        this.tmpDir.mkdirs();

        //TODO: check network availability

        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        new DownloadImageTask().execute(text);
    }

    class DownloadImageTask extends AsyncTask<String, Void, File> {

        @Override
        protected File doInBackground(String... strings) {
            PlantUMLClient client = new PlantUMLClient(ConvertActivity.this.tmpDir);
            try {
                return client.getDiagramFile(strings[0]);
            } catch (IOException e) {
                return null;    //TODO: provide error details
            }
        }

        @Override
        protected void onPostExecute(File file) {
            finish();
            //TODO: check nullness of file
            Uri fileUri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_SEND, fileUri);
            intent.setType(PNG_TYPE);
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            Intent chooser = Intent.createChooser(intent, getString(R.string.save_to));
            startActivity(chooser);
        }

    }

}
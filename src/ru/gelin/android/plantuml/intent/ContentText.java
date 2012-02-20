package ru.gelin.android.plantuml.intent;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import ru.gelin.android.plantuml.Tag;

import java.io.File;

/**
 *  Extracts text from content:// URI.
 */
public class ContentText extends AbstractFileText {

    /** Projection to select some useful data */
    static final String[] PROJECTION = {
            MediaStore.MediaColumns.DATA,
            //MediaStore.MediaColumns.MIME_TYPE,
            //MediaStore.MediaColumns.SIZE,
            //MediaStore.MediaColumns.DISPLAY_NAME,
            //MediaStore.MediaColumns.TITLE,
    };
    
    ContentText(Context context, Intent intent) throws IntentTextException {
        super(context, intent);
        queryContent();     //called from SEND, can init here
    }
    
    /**
     *  Queries the content for file name, mime type and size.
     *  If the query was done before the new attempt is skipped.
     */
    @Override
    void queryContent() {
        if (this.queried) {
            return;
        }
        try {
            Cursor cursor = contentResolver.query(this.uri, PROJECTION, null, null, null);
            int dataIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            //int typeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE);
            //int sizeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE);
            if (cursor.moveToFirst()) {
                String data = cursor.getString(dataIndex);
                this.file = new File(data);
                //this.type = cursor.getString(typeIndex);
                //this.size = cursor.getLong(sizeIndex);
            }
            cursor.close();
        } catch (Exception e) {
            //nothing to do, we have default behaviour
            Log.w(Tag.TAG, "cannot query content", e);
        }
        this.queried = true;
    }
    
    @Override
    public String toString() {
        return "content: " + this.uri + " -> " + this.file;
    }

}

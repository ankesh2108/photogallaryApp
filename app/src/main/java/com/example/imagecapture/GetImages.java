package com.example.imagecapture;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.AdapterView;

import java.util.ArrayList;

public class GetImages {


    public static ArrayList<String> getListImages(MainActivity context) {

        ArrayList<String> imagesList = new ArrayList<>();
        Uri uri;
        String absoluteImagePath;
        Cursor cursor;
        int column_index_data, column_index_folder_name;


        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        cursor = context.getContentResolver().query(uri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%/storage/emulated/0/Pictures/mypic/%"}, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);


        while (cursor.moveToNext()) {
            absoluteImagePath = cursor.getString((column_index_data));
            imagesList.add(absoluteImagePath);
        }


        return imagesList;
    }
}

package com.example.imagecapture;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //Rohits change
    private static final int MY_READ_PERMISSION_CODE = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    FloatingActionButton capture_floatingBtn;
    GridView gridView;


    private ContentValues values;
    private Uri imageUri;
    //  BitmapDrawable drawable;
    //  Bitmap bitmap;
    //  private File sdcard;
    //  private File directory;

    private ArrayList<String> listOfAllImages = new ArrayList<>();
    private ImageGridViewAdapter imageGridViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        capture_floatingBtn = findViewById(R.id.capture_floatingBtn);
        gridView = findViewById(R.id.gridView);

        //  sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // directory = new File(sdcard.getAbsolutePath() + "/mypic");
        // directory.mkdirs();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ClickItemHandler.class);
                intent.putExtra("id", position);
                startActivity(intent);
            }
        });


        if (!hasCamera()) {
            capture_floatingBtn.setEnabled(false);
            Toast.makeText(this, "Sorry!! Your device doesn't have camera, could not capture images", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_READ_PERMISSION_CODE);
        } else {
            loadImages();
        }


    }//onCreate Ends here


    /*public static class GetListOfImgInBackground extends AsyncTaskLoader {

        MainActivity context;
        public GetListOfImgInBackground(@NonNull MainActivity context) {
            super(context);
            this.context = context;
        }

        @Nullable
        @Override
        public Object loadInBackground() {
            context.listOfAllImages = GetImages.getListImages(context);
            context.imageGridViewAdapter = new ImageGridViewAdapter(context, context.listOfAllImages);
            context.gridView.setAdapter(context.imageGridViewAdapter);
            return null;
        }
    }*/



    public void loadImages() {

       listOfAllImages = GetImages.getListImages(this);
       imageGridViewAdapter = new ImageGridViewAdapter(this, listOfAllImages);
        gridView.setAdapter(imageGridViewAdapter);
    }


    public boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }


    public void captureImg(View view) {

        /////////////////////////  This code is helping to get full sized image
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ////////////////////////////////////////////////////////////////////////////////


        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);


    }

   /* @Override
    protected void onResume() {
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        listOfAllImages = GetImages.getListImages(this);
        imageGridViewAdapter.setImageList(listOfAllImages);
        imageGridViewAdapter.notifyDataSetChanged();
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        super.onResume();

    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            /////////////////////////  This code is helping to get full sized image
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /////////////////////////////////////////////////////////////////////////////

            //   Bundle extras = data.getExtras();
            //  Bitmap bitmap = (Bitmap) extras.get("data");
            FileOutputStream fileOutputStream = null;
            File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File directory = new File(sdcard.getAbsolutePath() + "/mypic");
            directory.mkdirs();
            String filename = String.format("%d.jpg", System.currentTimeMillis());
            File outFile = new File(directory, filename);


            try {
                // outFile.createNewFile();
                fileOutputStream = new FileOutputStream(outFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                fileOutputStream.flush();
                fileOutputStream.close();

                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(outFile));
                sendBroadcast(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }//onActivityResults Ends here


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_READ_PERMISSION_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                loadImages();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }
    }//onRequestPermissionResult Ends here
}

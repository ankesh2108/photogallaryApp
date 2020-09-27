package com.example.imagecapture;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.CaptureRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


   static final int REQUEST_IMAGE_CAPTURE=1;
    ImageView imageView;
    Button capture,btn_save;

    BitmapDrawable drawable;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=(ImageView)findViewById(R.id.imageView);
        capture=(Button)findViewById(R.id.capture);
        btn_save=(Button)findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {

                drawable=(BitmapDrawable)imageView.getDrawable();
                bitmap=drawable.getBitmap();

                FileOutputStream fileOutputStream=null;
                File sdcard= Environment.getExternalStorageDirectory();
                File directory=new File(sdcard.getAbsolutePath()+ "/mypic" );
                directory.mkdir();
                String filename=String.format("%d.jpg",System.currentTimeMillis());
                File outFile=new File(directory,filename);

                try {
                    fileOutputStream=new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();


                    Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));
                    sendBroadcast(intent);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });




        if(! hascamera()){
            capture.setEnabled(false);
        }



    }
    public boolean hascamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) ;
    }

    public void captureimg(View view){

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
     if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK)
     {
         Bundle extras=data.getExtras();
         Bitmap photo=(Bitmap)extras.get("data");
         imageView.setImageBitmap(photo);

     }
    }
}

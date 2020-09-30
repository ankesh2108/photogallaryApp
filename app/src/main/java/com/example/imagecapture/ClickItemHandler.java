package com.example.imagecapture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ClickItemHandler extends AppCompatActivity {
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_item);

        img = findViewById(R.id.imageView);

        Intent intent = getIntent();
        int position = intent.getExtras().getInt("id");


        Bitmap bitmap = BitmapFactory.decodeFile(ImageGridViewAdapter.getItemAtPosition(position));
        //  img.setImageBitmap(bitmap);

        Uri uri = Uri.parse(ImageGridViewAdapter.getItemAtPosition(position));
        img.setImageURI(uri);


        //   Glide.with(this).load(uri).into(img);



    }
}
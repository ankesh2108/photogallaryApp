package com.example.imagecapture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class click_item extends AppCompatActivity {
ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_item);

        img=(ImageView)findViewById(R.id.img);

        Intent intent=getIntent();
        int position=intent.getExtras().getInt("id");
        ImageGridViewAdapter imageGridViewAdapter=new ImageGridViewAdapter(this);
        Bitmap bitmap = BitmapFactory.decodeFile(imageGridViewAdapter.listOfImages.get(position));
        img.setImageBitmap(bitmap);

    }
}
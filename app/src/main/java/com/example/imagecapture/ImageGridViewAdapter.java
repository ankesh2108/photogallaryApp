package com.example.imagecapture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageGridViewAdapter extends BaseAdapter {


    private static ArrayList<String> listOfImages;
    private Context mContext;

    public ImageGridViewAdapter(Context mContext, ArrayList<String> listOfImages) {
        this.listOfImages = listOfImages;
        this.mContext = mContext;
    }

    public static String getItemAtPosition(int position) {
        return listOfImages.get(position);
    }


    public ImageGridViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setImageList(ArrayList<String> listOfImages) {
        this.listOfImages = listOfImages;
    }

    @Override
    public int getCount() {
        return listOfImages.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        Bitmap bitmap = BitmapFactory.decodeFile(listOfImages.get(position));
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(340, 340));

        return imageView;
    }
}

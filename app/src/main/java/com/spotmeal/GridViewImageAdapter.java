package com.spotmeal;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by parneet on 11/29/17.
 */

public class GridViewImageAdapter extends BaseAdapter {
    private static final String TAG = GridViewImageAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<String> mSelectedPictures;

    GridViewImageAdapter(Context context){
        Log.i(TAG, "GridViewImageAdapter created");
        mContext = context;
        mSelectedPictures = CreateEvent.selectedImages;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void clear(){
        mSelectedPictures.clear();
    }

    public void addImages(ArrayList<String> selectedPictures){
        mSelectedPictures = selectedPictures;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "Updating Picture Preview grid");
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);

        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
        imageView.setImageURI(Uri.fromFile(new File(mSelectedPictures.get(position))));
        return imageView;
    }
}

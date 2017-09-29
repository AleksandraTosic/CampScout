package com.lmb_europa.campscout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

public class GaleryActivity extends AppCompatActivity {

    Gallery simpleGallery;
    CustomGalleryAdapter customGalleryAdapter;
    ImageView selectedImageView;
    // array of images
    int[] images = {R.drawable.camp1, R.drawable.camp2, R.drawable.camp3, R.drawable.camp4, R.drawable.camp5,
            R.drawable.camp6, R.drawable.camp7, R.drawable.camp8, R.drawable.camp9, R.drawable.camp10, R.drawable.camp11,
            R.drawable.camp12, R.drawable.camp13, R.drawable.camp14, R.drawable.camp15, R.drawable.camp16, R.drawable.camp17,
            R.drawable.camp18, R.drawable.camp19, R.drawable.camp20, R.drawable.camp21, R.drawable.camp22, R.drawable.camp23};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galery);
        simpleGallery = (Gallery) findViewById(R.id.simpleGallery); // get the reference of Gallery
        selectedImageView = (ImageView) findViewById(R.id.selectedImageView); // get the reference of ImageView
        customGalleryAdapter = new CustomGalleryAdapter(getApplicationContext(), images); // initialize the adapter
        simpleGallery.setAdapter(customGalleryAdapter); // set the adapter
        simpleGallery.setSpacing(10);
        // perform setOnItemClickListener event on the Gallery
        simpleGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set the selected image in the ImageView
                selectedImageView.setImageResource(images[position]);

            }
        });
    }
}

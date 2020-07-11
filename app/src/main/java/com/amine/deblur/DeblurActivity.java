package com.amine.deblur;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class DeblurActivity extends AppCompatActivity {

    private ImageView  photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deblur);

        photo = (ImageView) findViewById(R.id.img_photo);


        Intent intent = getIntent();

        Bundle args = intent.getBundleExtra("images");
        ArrayList<Uri> myPictures = (ArrayList<Uri>) args.getSerializable("ARRAYLIST");

        int lenght_of_array = myPictures.size();

        if (lenght_of_array>0){
            photo.setImageURI(myPictures.get(0));
        }
    }
}

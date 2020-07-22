package com.amine.deblur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DeblurActivity extends AppCompatActivity {

    private ImageView  photo;
    private Button save;
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deblur);

        photo = (ImageView) findViewById(R.id.img_photo);
        save = findViewById(R.id.btn_save);


        Intent intent = getIntent();

        Bundle args = intent.getBundleExtra("images");
        ArrayList<Uri> myPictures = (ArrayList<Uri>) args.getSerializable("ARRAYLIST");

        int lenght_of_array = myPictures.size();

        if (lenght_of_array>0){
            photo.setImageURI(myPictures.get(0));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Uri imageUri = myPictures.get(0);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);

                    File filepath = Environment.getExternalStorageDirectory();
                    File dir = new File(filepath.getAbsolutePath()+"/Demo");
                    dir.mkdir();
                    File file = new File(dir, System.currentTimeMillis()+".png");
                    try {
                        outputStream = new FileOutputStream(file);

                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    Toast.makeText(getApplicationContext(), "Image Saved In Internal Storage", Toast.LENGTH_SHORT).show();
                    outputStream.flush();
                    outputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}

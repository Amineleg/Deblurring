package com.amine.deblur;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class MainActivity extends AppCompatActivity {


    private Button btnSelect;
    private Button btnDeblur;
    private RecyclerView rclPhoto;
    private PhotoAdapter photoAdapter;
    private ArrayList<Uri> myPictures;
    private TextView txt;

    private Button btnTest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        btnSelect = findViewById(R.id.btn_select);
        btnDeblur = findViewById(R.id.btn_deblur);
        btnTest = findViewById(R.id.btn_deb);
        rclPhoto = findViewById(R.id.rcl_images);
        txt = findViewById(R.id.textView);


        photoAdapter = new PhotoAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL,false );
        rclPhoto.setLayoutManager(gridLayoutManager);
        rclPhoto.setFocusable(false);
        rclPhoto.setAdapter(photoAdapter);



        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });

        btnDeblur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deblur();

            }
        });

        btnTest.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Object[] textToShow = getPythonMethod();
                String text1 = textToShow[0].toString();
                Uri uri = Uri.fromFile(new File(text1));
                myPictures.add(0,uri);
                txt.setText(text1);

                Toast.makeText(getApplicationContext(), "Finished Deblurring the image!", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private Object[] getPythonMethod(){
        Python python = Python.getInstance();


        Context context = this;

        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;

        if (totalMemory> 5000000000.0){
            PyObject pythonFile = python.getModule("model");
            List<String> list = new ArrayList<>();
            for (int i = 0; i<myPictures.size(); i++){
                list.add(myPictures.get(i).getPath());
            }
            String[] paths = list.toArray(new String[list.size()]);
            PyObject r = pythonFile.callAttr("calculate", paths);
            return r.asSet().toArray();
        }
        else{
            PyObject pythonFile = python.getModule("small_model");
            List<String> list = new ArrayList<>();
            for (int i = 0; i<myPictures.size(); i++){
                list.add(myPictures.get(i).getPath());
            }
            String[] paths = list.toArray(new String[list.size()]);
            PyObject r = pythonFile.callAttr("calculate", paths);
            return r.asSet().toArray();
        }


    }

    private void requestPermissions(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                openBottomPicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void openBottomPicker(){
        TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
            @Override
            public void onImageSelected(Uri uri) {
                ArrayList<Uri> uriList = new ArrayList<Uri>();
                uriList.add(uri);
                myPictures = uriList;
                imageSetter();

            }
        };

        TedBottomPicker tedBottomPicker =  new TedBottomPicker.Builder(MainActivity.this)
                .setOnImageSelectedListener(listener)
                .setCompleteButtonText("DONE")
                .setEmptySelectionText("NO IMAGE SELECTED!")
                .create();

        tedBottomPicker.show(getSupportFragmentManager());


    }

    public void imageSetter(){

        photoAdapter.setImages(myPictures);

        txt.setText(myPictures.get(0).getPath());

    }


    public void deblur(){

        Intent intent = new Intent(this, DeblurActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)myPictures);
        intent.putExtra("images",args);
        startActivity(intent);

    }
}

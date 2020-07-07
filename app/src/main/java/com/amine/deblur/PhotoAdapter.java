package com.amine.deblur;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{

    private Context myContext;

    private ArrayList<Uri> myImageList;

    public PhotoAdapter(Context myContext) {
        this.myContext = myContext;
    }

    public void setImages(ArrayList<Uri> list){

        this.myImageList = list;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_show, parent, false);

        return new PhotoViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {

        Uri uri = myImageList.get(position);
        try{
            Bitmap bit = MediaStore.Images.Media.getBitmap(myContext.getContentResolver(), uri);
            holder.photo.setImageBitmap(bit);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if ( myImageList == null){
            return 0;
        }
        else {
            return myImageList.size();
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.img_photo);
        }
    }

}

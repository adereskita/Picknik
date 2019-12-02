package com.example.picknik.Adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.picknik.Models.ImageData;
import com.example.picknik.R;

import java.util.ArrayList;

public class PreviewAdaptor extends RecyclerView.Adapter<PreviewAdaptor.ViewHolder> {
    private static final String TAG = "PreviewAdaptor";
    
    private ArrayList<ImageData> data;
    private Context mContext;

    public PreviewAdaptor(ArrayList<ImageData> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.preview_list,parent,false);
        return new PreviewAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final ImageData preview = data.get(position);

        Glide.with(mContext)
                .asBitmap()
                .load(preview.getImagesrc())
                .into(viewHolder.previewImage);

        viewHolder.previewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked on image");
                Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView previewImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            previewImage = itemView.findViewById(R.id.preview_image);
        }
    }
}

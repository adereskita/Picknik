package com.example.picknik.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picknik.Details.DetailActivity;
import com.example.picknik.Details.DetailsFragment;
import com.example.picknik.R;
import com.example.picknik.Utils.PostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.picknik.Details.DetailActivity.EXTRA_LAT;
import static com.example.picknik.Details.DetailActivity.EXTRA_LNG;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private static final String TAG = "RecycleViewAdapter";

    //vars
    private ArrayList<PostModel> data;
    private Context mContext;

    public RecycleViewAdapter(ArrayList<PostModel> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        final PostModel trend = data.get(position);

        holder.tv_tempat.setText(trend.getPicknikName());
        holder.tv_lokasi.setText(trend.getLocation());


        holder.id_user = trend.getId_user();
        holder.id_post = trend.getId_post();
        holder.phone = trend.getPhone();
        holder.type = trend.getType();
        holder.lat = trend.getLat();
        holder.lng = trend.getLng();


        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageRef.child("Images").child(trend.getImageSrc()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(mContext)
                                .load(uri)
                                .into(holder.imageView);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Opsss.... Something is wrong "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView tv_tempat;
        private TextView tv_lokasi;
        private String id_post,id_user, phone, type;
        private double lat, lng;

        public ViewHolder( View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.circleImage);
            tv_tempat = itemView.findViewById(R.id.nTempat);
            tv_lokasi = itemView.findViewById(R.id.nLokasi);


            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra(DetailsFragment.EXTRA_IDPOST, id_post);//put whatever data you need to send
                    intent.putExtra(DetailsFragment.EXTRA_IDUSER, id_user);
                    //Toast.makeText(mContext, lat + " " +lng, Toast.LENGTH_SHORT).show();
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

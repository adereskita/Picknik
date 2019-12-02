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
import com.example.picknik.MapsActivity;
import com.example.picknik.R;
import com.example.picknik.Utils.PostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;
import static com.example.picknik.Details.DetailActivity.EXTRA_LAT;
import static com.example.picknik.Details.DetailActivity.EXTRA_LNG;

public class RecyclerAdapterProfile extends RecyclerView.Adapter
        <RecyclerAdapterProfile.ViewHolder> {

    Context mContext;
    ArrayList<PostModel> mData;
    OnItemClickListener callback;

    private StorageReference mStorageRef;

    public RecyclerAdapterProfile(Context mContext, ArrayList<PostModel> mData, OnItemClickListener callback) {
        this.mContext = mContext;
        this.mData = mData;
        this.callback = callback;
    }

    public RecyclerAdapterProfile(ArrayList<PostModel> mData, OnItemClickListener callback) {
        this.mData = mData;
        this.callback = callback;
    }

    public RecyclerAdapterProfile(Context mContext, ArrayList<PostModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.itemlist_profile,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final PostModel post = mData.get(position);

        holder.tv_nama.setText(post.getPicknikName());
        holder.tv_category.setText(post.getCategory());
        holder.tv_alamat.setText(post.getLocation());
        //holder.rating.setText(post.getRating());

        holder.id_user = post.getId_user();
        holder.id_post = post.getId_post();
        holder.phone = post.getPhone();
        holder.type = post.getType();
        holder.lat = post.getLat();
        holder.lng = post.getLng();


        Log.d(TAG, "onBindViewHolder: "+ post.getImageSrc());

        if (holder.type.equals("Place")){

            mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.child("Images").child("P"+post.getImageSrc()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(mContext)
                                    .load(uri)
                                    .into(holder.foto);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, "Opsss.... Something is wrong "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else if (holder.type.equals("Event")){

            mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.child("Images").child(post.getImageSrc()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(mContext)
                                    .load(uri)
                                    .into(holder.foto);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, "Opsss.... Something is wrong "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return (null != mData ? mData.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_nama,tv_category, tv_alamat, rating;
        private ImageView foto;
        private String id_post,id_user, phone, type, category;
        private double lat, lng;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_nama =  itemView.findViewById(R.id.tv_pTitle);
            tv_category = itemView.findViewById(R.id.tv_pCategory);
            tv_alamat = itemView.findViewById(R.id.tv_pAlamat);
            foto = itemView.findViewById(R.id.pList_cardImg);
            rating = itemView.findViewById(R.id.tv_rating);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra(DetailsFragment.EXTRA_IDPOST, id_post);//put whatever data you need to send
                    intent.putExtra(DetailsFragment.EXTRA_IDUSER, id_user);
                    intent.putExtra(DetailsFragment.EXTRA_PHONE, phone);
                    intent.putExtra(DetailsFragment.EXTRA_CATEGORY, category);
                    intent.putExtra(EXTRA_LAT, lat);
                    intent.putExtra(EXTRA_LNG, lng);
                    //Toast.makeText(mContext, lat + " " +lng, Toast.LENGTH_SHORT).show();
                    mContext.startActivity(intent);

                }
            });


        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }
}

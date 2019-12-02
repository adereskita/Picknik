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

import static com.example.picknik.Details.DetailActivity.EXTRA_LAT;
import static com.example.picknik.Details.DetailActivity.EXTRA_LNG;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    private Context mContext;
    private ArrayList<PostModel> listdata;
    private RecyclerAdapterProfile.OnItemClickListener callback;

    private StorageReference mStorageRef;

    public SearchListAdapter(Context mContext, ArrayList<PostModel> mData, RecyclerAdapterProfile.OnItemClickListener callback) {
        this.mContext = mContext;
        this.listdata = mData;
        this.callback = callback;
    }

    public SearchListAdapter(Context mContext, ArrayList<PostModel> listdata) {
        this.mContext = mContext;
        this.listdata = listdata;
    }

    public SearchListAdapter(ArrayList<PostModel> mData, RecyclerAdapterProfile.OnItemClickListener callback) {
        this.listdata = mData;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cardview_list_search,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final PostModel data = listdata.get(position);

        holder.tv_title.setText(data.getPicknikName());
        holder.tv_desc.setText(data.getCategory());
        holder.tv_location.setText(data.getLocation());
//        holder.tv_rating.setText(data.getRating());

        holder.id_user = data.getId_user();
        holder.id_post = data.getId_post();
        holder.phone = data.getPhone();
        holder.lat = data.getLat();
        holder.lng = data.getLng();
        holder.category = data.getCategory();



        Log.d(TAG, "onBindViewHolder: "+ data.getImageSrc());

        //mengambil foto dari url address Internet
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageRef.child("Images").child("P"+data.getImageSrc()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(mContext)
                                .load(uri)
                                .into(holder.imageCollection);
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
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_desc,tv_rating, tv_location;
        ImageView imageCollection;
        private String id_post,id_user,phone, category;
        private double lat, lng;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_rating= itemView.findViewById(R.id.tv_rating);
            tv_location= itemView.findViewById(R.id.tv_lokasi);
            imageCollection= itemView.findViewById(R.id.image_collcetion);

            itemView.setFocusable(true);
            itemView.setClickable(true);

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
                    mContext.startActivity(intent);
                }
            });

        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }
}

package com.example.picknik.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.picknik.Details.DetailsFragment;
import com.example.picknik.Models.CollectionModel;
import com.example.picknik.QueryListActivity;
import com.example.picknik.R;

import java.util.ArrayList;

public class CollectionRecyclerAdapter extends RecyclerView.Adapter<CollectionRecyclerAdapter.viewHolder> {

    private static final String TAG = "CollectionRecyclerAdapt";

    private Context mcontext;
    private ArrayList<CollectionModel> mdata;


    public CollectionRecyclerAdapter(Context mcontext, ArrayList<CollectionModel> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.cardview_item_collection,parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int position) {

        final CollectionModel data = mdata.get(position);

        Glide.with(mcontext)
                .asBitmap()
                .load(data.getThumbnail())
                .into(viewHolder.cardimageView);

        viewHolder.tv_category_title.setText(data.getCollection());
        viewHolder.tv_category_count.setText(mdata.get(position).getCollection_place());
        viewHolder.tv_desc.setText(data.getDesc());

        viewHolder.category = data.getCategory();

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView tv_category_title;
        private TextView tv_category_count;
        private TextView tv_desc;
        private ImageView cardimageView;
        private String category;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_category_title = itemView.findViewById(R.id.tv_title);
            tv_category_count = itemView.findViewById(R.id.tv_place);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            cardimageView = itemView.findViewById(R.id.image_collcetion);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, QueryListActivity.class);
                    //put whatever data you need to send
                    intent.putExtra(DetailsFragment.EXTRA_CATEGORY, category);
                    //Toast.makeText(mContext, lat + " " +lng, Toast.LENGTH_SHORT).show();
                    mcontext.startActivity(intent);
                }
            });
        }
    }
}

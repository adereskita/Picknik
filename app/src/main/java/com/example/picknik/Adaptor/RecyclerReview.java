package com.example.picknik.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.picknik.R;
import com.example.picknik.Review.ReviewActivity;
import com.example.picknik.Utils.PostModel;

import java.util.ArrayList;

import static com.example.picknik.Details.DetailActivity.EXTRA_IDPOST;

public class RecyclerReview extends RecyclerView.Adapter<RecyclerReview.ViewHolder> {

    private ArrayList<PostModel> listData;
    private Context mContext;

    public RecyclerReview(ArrayList<PostModel> listData, Context mContext) {
        this.listData = listData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerReview.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_review, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerReview.ViewHolder holder, int position) {
        PostModel nModel = listData.get(position);

        holder.tv_title.setText(nModel.getTitle_rating());
        holder.tv_username.setText(nModel.getUsername());
        holder.tv_ratingText.setText(nModel.getTxt_rating());
        holder.ratingBar.setRating(Float.parseFloat(String.valueOf(nModel.getRating())));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_ratingText, tv_title, tv_username;
        private RatingBar ratingBar;
        private String id_post;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_username = itemView.findViewById(R.id.tv_username);
            tv_ratingText = itemView.findViewById(R.id.review_text);
            tv_title = itemView.findViewById(R.id.review_title);
            ratingBar = itemView.findViewById(R.id.rb_rating);
        }
    }
}

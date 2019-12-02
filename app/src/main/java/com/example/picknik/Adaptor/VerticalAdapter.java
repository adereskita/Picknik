package com.example.picknik.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picknik.Details.DetailsFragment;
import com.example.picknik.Models.VerticalModel;
import com.example.picknik.QueryListActivity;
import com.example.picknik.R;
import com.example.picknik.Utils.PostModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.VerticalViewHolder> {

    private static final String TAG = "VerticalRecyclerViewAda";


    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;

    private Context mContext;
    private ArrayList<VerticalModel> data;

    public VerticalAdapter(Context mContext, ArrayList<VerticalModel> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.vertical_list_item,parent,false);
        return new VerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder verticalViewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        VerticalModel vModel = data.get(position);
        PostModel post = new PostModel();

        verticalViewHolder.tv_title.setText(vModel.getTitle());

        ArrayList<PostModel> singleSectionItems = data.get(position).getVerticalList();

        RecycleViewAdapter horizontalList = new RecycleViewAdapter(singleSectionItems, mContext);

        verticalViewHolder.recyclerView_list.setHasFixedSize(true);
        verticalViewHolder.recyclerView_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        verticalViewHolder.recyclerView_list.setAdapter(horizontalList);

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        Button btn_more;
        private String category = "Place";
        private RecyclerView recyclerView_list;

        private VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.title);
            btn_more = itemView.findViewById(R.id.btn_more);
            recyclerView_list = itemView.findViewById(R.id.recyclerview_horizontal);

            itemView.setClickable(true);
            itemView.setFocusable(true);

            btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, QueryListActivity.class);
                    //put whatever data you need to send
                    intent.putExtra(DetailsFragment.EXTRA_CATEGORY, category);
                    //Toast.makeText(mContext, lat + " " +lng, Toast.LENGTH_SHORT).show();
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

package com.example.picknik.Adaptor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.picknik.R;
import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    private List<String> fileNameList;
    private List<String> fileDoneList;


    public UploadListAdapter(List<String> fileNameList, List<String> fileDoneList) {
        this.fileNameList = fileNameList;
        this.fileDoneList = fileDoneList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.upload_itemlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String mFileName = fileNameList.get(position);
        holder.tv_mFileName.setText(mFileName);

        String fileDone = fileDoneList.get(position);

        if (fileDone.equals("uploading")){

            holder.tv_fileDone.setImageResource(R.drawable.ic_checkbox_outline);
        }else {
            holder.tv_fileDone.setImageResource(R.drawable.ic_checkbox);
        }


    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_mFileName;
        ImageView tv_fileDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_mFileName = itemView.findViewById(R.id.tv_fileName);
            tv_fileDone = itemView.findViewById(R.id.btn_checkbox);

            itemView.setClickable(true);
            itemView.setFocusable(true);

        }
    }
}

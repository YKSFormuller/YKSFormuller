package com.yksformuller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.yksformuller.Interface.ItemClickListener;
import com.yksformuller.R;

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadAdapterHolder> {

    private ItemClickListener clickListener;
    private List<String> tableList;
    private Context context;
    private LayoutInflater layoutInflater;
    public DownloadAdapter(Context context, List<String> tableName){
        this.context=context;
        this.tableList=tableName;
        this.layoutInflater=LayoutInflater.from(context);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @NonNull
    @Override
    public DownloadAdapter.DownloadAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DownloadAdapterHolder holder;
        View view = layoutInflater.inflate(R.layout.formula_item, null);
        holder = new DownloadAdapterHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.DownloadAdapterHolder holder, int position) {
        holder.title.setText(tableList.get(position));
    }
    @Override
    public int getItemCount() {
        return tableList.size();
    }
    public class DownloadAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        public DownloadAdapterHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }

    }
}

package com.yksformuller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yksformuller.Interface.ItemClickListener;
import com.yksformuller.R;

import java.util.ArrayList;
import java.util.List;

import model.Formula;

public class FormulaAdapter extends RecyclerView.Adapter<FormulaAdapter.FormulaAdapterHolder> {

    //private List<model.Formula> mFormulaList;
    private List<String> mFormulaList;
    Context mContext;
    private LayoutInflater layoutInflater;
    private ItemClickListener clickListener;

    public FormulaAdapter(Context context, List<String> formulaList) {

        mFormulaList = formulaList;
        mContext = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public FormulaAdapter.FormulaAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FormulaAdapterHolder holder;
        View view = layoutInflater.inflate(R.layout.formula_item, null);
        holder = new FormulaAdapterHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FormulaAdapterHolder holder, int position) {

        //model.Formula formula = mFormulaList.get(position);
        holder.title.setText(mFormulaList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFormulaList.size();
    }

    public class FormulaAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;

        public FormulaAdapterHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}

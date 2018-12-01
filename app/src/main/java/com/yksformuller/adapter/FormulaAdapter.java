package com.yksformuller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.yksformuller.Interface.ItemClickListener;
import com.yksformuller.R;

import java.util.ArrayList;
import java.util.List;

public class FormulaAdapter extends RecyclerView.Adapter<FormulaAdapter.FormulaAdapterHolder> {

    private List<String> mFormulaList;
    Context mContext;
    private LayoutInflater layoutInflater;
    private ItemClickListener clickListener;

    int AD_TYPE = 0;
    int CONTENT_TYPE = 1;
    public static String BANNER_AD_UNIT_ID="ca-app-pub-4047984159420834/8770457029";

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
        AdView adview;

        if (viewType == CONTENT_TYPE) {
            View view = layoutInflater.inflate(R.layout.formula_item, null);
            holder = new FormulaAdapterHolder(view);
        }
        else{

            adview = new AdView(mContext);
            adview.setAdSize(AdSize.LARGE_BANNER);

            adview.setAdUnitId(BANNER_AD_UNIT_ID);

            float density = mContext.getResources().getDisplayMetrics().density;
            int height = Math.round(AdSize.LARGE_BANNER.getHeight() * density);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
            adview.setLayoutParams(params);

            AdRequest request = new AdRequest.Builder().build();
            adview.loadAd(request);
            holder = new FormulaAdapterHolder(adview);
        }





        return holder;



    }

    @Override
    public void onBindViewHolder(FormulaAdapterHolder holder, int position) {
        if(position % 5 != 4) {

            holder.title.setText(mFormulaList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mFormulaList.size();
    }

    public class FormulaAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;

        public FormulaAdapterHolder(View view) {
            super(view);
            if (!(itemView instanceof AdView)) {
                title = view.findViewById(R.id.title);
                view.setOnClickListener(this);
            }
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

    public void setFilter(ArrayList<String> newList) {
        mFormulaList = new ArrayList<>();
        mFormulaList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 5 == 4)
            return AD_TYPE;
        return CONTENT_TYPE;
    }
}

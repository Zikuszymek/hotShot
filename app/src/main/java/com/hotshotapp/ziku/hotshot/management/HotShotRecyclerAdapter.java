package com.hotshotapp.ziku.hotshot.management;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.tables.ActiveHotShots;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ziku on 2017-02-23.
 */

public class HotShotRecyclerAdapter extends RecyclerView.Adapter<HotShotRecyclerAdapter.ViewHolder> {

    private static final String OTHERTEES = "othertees";
    private static final String PATTERN = "wzór koszulki:";

    private static final int ITEM_TYPE_FULL = 1;
    private static final int ITEM_TYPE_PART = 2;

    private List<ActiveHotShots> activeHotShotsList;
    private Context context;

    public HotShotRecyclerAdapter(List<ActiveHotShots> activeHotShotsList, Context context) {
        this.activeHotShotsList = activeHotShotsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType==ITEM_TYPE_FULL){
            viewHolder = layoutInflater.inflate(R.layout.list_element,parent,false);
        } else {
            viewHolder = layoutInflater.inflate(R.layout.list_element_simple,parent,false);
        }
        return new HotShotRecyclerAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ActiveHotShots activeHotShots = activeHotShotsList.get(position);
        if(activeHotShots.webSites.webSiteName.equals(OTHERTEES)){
            holder.productName.setText(String.format("%s \n %s",PATTERN,activeHotShots.productName));
        } else {
            holder.productName.setText(activeHotShots.productName);
        }

        holder.newPrie.setText(String.format("%d zl",activeHotShots.newPrice));

        if(activeHotShots.oldPrice != 0) {
            String oldPriceString = String.valueOf(activeHotShots.oldPrice) + " zł";
            if (activeHotShots.oldPrice != 0 && activeHotShots.newPrice != 0) {
                int percentage = ((activeHotShots.oldPrice - activeHotShots.newPrice) * 100) / activeHotShots.oldPrice;
                String percentageString = "-" + String.valueOf(percentage) + "%";
                holder.percentageDifference.setText(percentageString);
            } else {
                holder.percentageDifference.setText("");
            }
            holder.oldprice.setText(oldPriceString);
        }

        holder.webSiteName.setText(activeHotShots.webSites.webSiteName);

        final String fileName = activeHotShots.webSites.webSiteName + String.valueOf(activeHotShots.getId()) + ".png";
        File file = context.getFileStreamPath(fileName);
        if(file.exists()){
            if(file.length() > 0){
                Uri uri = Uri.fromFile(file);
                holder.imageView.setImageURI(uri);
            } else {
                holder.imageView.setImageResource(R.drawable.hot_shot_icon);
            }
        }

        holder.cardView.requestLayout();
    }

    @Override
    public int getItemCount() {
        return activeHotShotsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(activeHotShotsList.get(position).oldPrice != 0){
            return ITEM_TYPE_FULL;
        }
        return ITEM_TYPE_PART;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Nullable
        @BindView(R.id.old_price)
        TextView oldprice;

        @Nullable
        @BindView(R.id.percentage_difference)
        TextView percentageDifference;

        @BindView(R.id.product_name)
        TextView productName;

        @Nullable
        @BindView(R.id.new_price)
        TextView newPrie;

        @BindView(R.id.web_site_name)
        TextView webSiteName;

        @BindView(R.id.product_image)
        ImageView imageView;

        @BindView(R.id.card_view_layout)
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}

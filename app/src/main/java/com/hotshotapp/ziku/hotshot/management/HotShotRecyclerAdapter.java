package com.hotshotapp.ziku.hotshot.management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.ShowImageActivity;
import com.hotshotapp.ziku.hotshot.tables.ActiveHotShots;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HotShotRecyclerAdapter extends RecyclerView.Adapter<HotShotRecyclerAdapter.ViewHolder> {

    private static final String OTHERTEES = "othertees";
    private static final String PATTERN = "wzór koszulki:";

    private static final int ITEM_TYPE_FULL = 1;
    private static final int ITEM_TYPE_PART = 2;
    private int itemTYpe;

    private List<ActiveHotShots> activeHotShotsList;
    private Context context;
    private Activity activity;
    private View emptyElement;
//    private ViewGroup viewGroup;
    private ActivityReactionOnAdapter activityReaction;

    public HotShotRecyclerAdapter(Activity activity, int itemType, View emptyElement) {
        this.context = activity.getApplicationContext();
        this.itemTYpe = itemType;
        this.activity = activity;
        this.emptyElement = emptyElement;
        this.activityReaction = (ActivityReactionOnAdapter)activity;
//        this.viewGroup = viewGroup;
        refrehstDataSet();
    }

    public void refrehstDataSet() {
        activeHotShotsList = ActiveHotShots.ReturnAllActiveHotShotsActive(itemTYpe);
        if (activeHotShotsList.size() == 0 || activeHotShotsList == null) {
            setEmptyElementVisible(true);
        } else {
            setEmptyElementVisible(false);
        }
        notifyDataSetChanged();
    }

    private void setEmptyElementVisible(boolean isVisible) {
        if (isVisible) {
            emptyElement.setVisibility(View.VISIBLE);
        } else {
            emptyElement.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_TYPE_FULL) {
            viewHolder = layoutInflater.inflate(R.layout.list_element, parent, false);
        } else {
            viewHolder = layoutInflater.inflate(R.layout.list_element_simple, parent, false);
        }
        ((ViewGroup)viewHolder).setClipChildren(false);
        ((ViewGroup)viewHolder).setClipToPadding(false);
        return new HotShotRecyclerAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ActiveHotShots activeHotShots = activeHotShotsList.get(position);
        if (activeHotShots.webSites.webSiteName.equals(OTHERTEES)) {
            holder.productName.setText(String.format("%s \n %s", PATTERN, activeHotShots.productName));
        } else {
            holder.productName.setText(activeHotShots.productName);
        }

        holder.newPrie.setText(String.format("%d zł", activeHotShots.newPrice));

        if (activeHotShots.oldPrice != 0) {
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
        if (file.exists()) {
            if (file.length() > 0) {
                Uri uri = Uri.fromFile(file);
                Glide.with(context).load(uri).into(holder.imageView);
            } else {
                Glide.with(context).load(R.drawable.hot_shot_icon).into(holder.imageView);
            }
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityReaction.doActivityAction(activeHotShots,fileName);
            }
        });
        holder.cardView.requestLayout();
    }

    @Override
    public int getItemCount() {
        return activeHotShotsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (activeHotShotsList.get(position).oldPrice != 0) {
            return ITEM_TYPE_FULL;
        }
        return ITEM_TYPE_PART;
    }


    public interface ActivityReactionOnAdapter{
        void doActivityAction(ActiveHotShots activeHotShots,String fileName);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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

        @Nullable
        @BindView(R.id.separate_line)
        View sideLineSeparator;

        @Nullable
        @BindView(R.id.contener_with_image)
        LinearLayout imageContent;

        @Nullable
        @BindView(R.id.content_layout)
        LinearLayout contentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

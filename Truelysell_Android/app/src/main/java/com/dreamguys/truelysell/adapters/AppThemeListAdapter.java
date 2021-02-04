package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.DAOAppTheme;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AppThemeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    public List<DAOAppTheme> mApptheme;
    public String apptheme = "",secTheme = "";
    String type;

    private int SELF = 1, LOADING = 2;

    public AppThemeListAdapter(Context mContext, List<DAOAppTheme> mApptheme, String type) {
        this.mContext = mContext;
        this.mApptheme = mApptheme;
        this.type = type;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_apptheme, parent, false);
        return new ThemeViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        ThemeViewHolder categoryViewHolder = (ThemeViewHolder) viewHolder;
        categoryViewHolder.ivAppColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(mApptheme.get(position).getAppColor())));

        if (mApptheme.get(position).isSelected()) {
            categoryViewHolder.ivSelected.setVisibility(View.VISIBLE);
        } else {
            categoryViewHolder.ivSelected.setVisibility(View.GONE);
        }

        categoryViewHolder.ivAppColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mApptheme.size(); i++) {
                    mApptheme.get(i).setSelected(false);
                }
                mApptheme.get(position).setSelected(true);
                if (type.equalsIgnoreCase("0")) {
                    apptheme = mApptheme.get(position).getAppColor();
                } else {
                    secTheme = mApptheme.get(position).getAppColor();
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mApptheme.size();
    }


    public class ThemeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_apptheme)
        ImageView ivAppColor;
        @BindView(R.id.iv_selected)
        ImageView ivSelected;


        public ThemeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

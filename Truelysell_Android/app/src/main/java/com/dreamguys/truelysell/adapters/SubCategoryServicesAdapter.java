package com.dreamguys.truelysell.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.dreamguys.truelysell.ActivityServiceDetail;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.SubCategoryServicesActivity;
import com.dreamguys.truelysell.datamodel.Phase3.DAOSubCategoryServices;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;

public class SubCategoryServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context mContext;
    public List<DAOSubCategoryServices.Datum> itemsData = new ArrayList<>();
    SubCategoryServicesActivity mActivity;


    public SubCategoryServicesAdapter(Context mContext, List<DAOSubCategoryServices.Datum> serviceList, SubCategoryServicesActivity mActivity) {
        this.mContext = mContext;
        this.itemsData = serviceList;
        this.mActivity = mActivity;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_service_viewall, parent, false);
        return new SearchServiceViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        SearchServiceViewHolder searchViewHolder = (SearchServiceViewHolder) viewHolder;

        if (AppUtils.isThemeChanged(mContext)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                searchViewHolder.tvViewonmap.setTextColor(AppUtils.getPrimaryAppTheme(mContext));
                searchViewHolder.ivUserimage.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                searchViewHolder.ivUserimage.setBorderColor(AppUtils.getPrimaryAppTheme(mContext));
                searchViewHolder.tvCategory.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                searchViewHolder.tvViewonmap.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
            }
        }

        Glide.with(mContext)
                .load(AppConstants.BASE_URL + itemsData.get(position).getServiceImage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_service_placeholder).transforms(new CenterCrop(), new RoundedCorners(35)))
                .into(searchViewHolder.ivServiceImage);

        Glide.with(mContext)
                .load(AppConstants.BASE_URL + itemsData.get(position).getProfileImg())
                .apply(new RequestOptions().placeholder(R.drawable.ic_user_placeholder).transforms(new CenterCrop(), new RoundedCorners(40)))
                .into(searchViewHolder.ivUserimage);

        searchViewHolder.tvCategory.setText(itemsData.get(position).getCategoryName());
        searchViewHolder.tvCost.setText(Html.fromHtml(itemsData.get(position).getCurrency()) + itemsData.get(position).getServiceAmount());
        searchViewHolder.tvServiceName.setText(itemsData.get(position).getServiceTitle());

        if (!itemsData.get(position).getRating().isEmpty()) {
            searchViewHolder.rbRating.setRating(Float.parseFloat(itemsData.get(position).getRating()));
            searchViewHolder.tvRating.setText("(" + itemsData.get(position).getRatingCount() + ")");
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callServiceDetailAct = new Intent(mContext, ActivityServiceDetail.class);
                callServiceDetailAct.putExtra(AppConstants.FROMPAGE, AppConstants.VIEWALL);
                callServiceDetailAct.putExtra(AppConstants.SERVICEID, itemsData.get(position).getId());
                callServiceDetailAct.putExtra(AppConstants.SERVICETITLE, itemsData.get(position).getServiceTitle());
                mContext.startActivity(callServiceDetailAct);
            }
        });

        ((SearchServiceViewHolder) viewHolder).tvBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.checkUserLogin(itemsData.get(position).getId(),
                        itemsData.get(position).getServiceAmount(),
                        itemsData.get(position).getServiceTitle(),
                        itemsData.get(position).getCurrency());
            }
        });

    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }


    public class SearchServiceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_service_image)
        ImageView ivServiceImage;
        @BindView(R.id.tv_service_name)
        TextView tvServiceName;
        @BindView(R.id.rb_rating)
        RatingBar rbRating;
        @BindView(R.id.tv_rating)
        TextView tvRating;
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.iv_userimage)
        CircleImageView ivUserimage;
        @BindView(R.id.tv_book)
        TextView tvBook;
        @BindView(R.id.tv_viewonmap)
        TextView tvViewonmap;
        @BindView(R.id.tv_cost)
        TextView tvCost;

        public SearchServiceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}

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
import com.dreamguys.truelysell.ActivityServiceDetail;
import com.dreamguys.truelysell.MapsGetDirections;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.ViewAllServicesActivity;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOViewAllServices;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAllPopularAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context mContext;
    public List<DAOViewAllServices.ServiceList> itemsData = new ArrayList<>();
    ViewAllServicesActivity mActivity;
    LanguageResponse.Data.Language.ViewAllServices viewAllStringsList;


    public ViewAllPopularAdapter(Context mContext, List<DAOViewAllServices.ServiceList> serviceList,
                                 ViewAllServicesActivity mActivity, LanguageResponse.Data.Language.ViewAllServices viewAllStringsList) {
        this.mContext = mContext;
        this.itemsData = serviceList;
        this.mActivity = mActivity;
        this.viewAllStringsList = viewAllStringsList;

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
                searchViewHolder.ivUserimage.setBorderColor(AppUtils.getPrimaryAppTheme(mContext));
                searchViewHolder.ivUserimage.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                searchViewHolder.tvCategory.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                searchViewHolder.tvViewonmap.setTextColor(AppUtils.getPrimaryAppTheme(mContext));
                searchViewHolder.tvViewonmap.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
            }
        }

        searchViewHolder.tvViewonmap.setText(AppUtils.cleanLangStr(mContext,
                viewAllStringsList.getLblViewOnMap().getName(), R.string.txt_view_on_map));
        searchViewHolder.tvBook.setText(AppUtils.cleanLangStr(mContext,
                viewAllStringsList.getLblBook().getName(), R.string.txt_book));

        Glide.with(mContext)
                .load(AppConstants.BASE_URL + itemsData.get(position).getServiceImage())
                .apply(new RequestOptions().error(R.drawable.ic_service_placeholder).placeholder(R.drawable.ic_service_placeholder).transforms(new CenterCrop(), new RoundedCorners(35)))
                .into(searchViewHolder.ivServiceImage);

        Glide.with(mContext)
                .load(AppConstants.BASE_URL + itemsData.get(position).getUserImage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_user_placeholder).transforms(new CenterCrop(), new RoundedCorners(40)))
                .into(searchViewHolder.ivUserimage);

        searchViewHolder.tvCategory.setText(itemsData.get(position).getCategoryName());
        searchViewHolder.tvCost.setText(Html.fromHtml(itemsData.get(position).getCurrency()) + itemsData.get(position).getServiceAmount());
        searchViewHolder.tvServiceName.setText(itemsData.get(position).getServiceTitle());

        if (!itemsData.get(position).getRatings().isEmpty()) {
            searchViewHolder.rbRating.setRating(Float.parseFloat(itemsData.get(position).getRatings()));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callServiceDetailAct = new Intent(mContext, ActivityServiceDetail.class);
                callServiceDetailAct.putExtra(AppConstants.FROMPAGE, AppConstants.VIEWALL);
                callServiceDetailAct.putExtra(AppConstants.SERVICEID, itemsData.get(position).getServiceId());
                callServiceDetailAct.putExtra(AppConstants.SERVICETITLE, itemsData.get(position).getServiceTitle());
                mContext.startActivity(callServiceDetailAct);
            }
        });

        ((SearchServiceViewHolder) viewHolder).tvViewonmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callDirectionAct = new Intent(mContext, MapsGetDirections.class);
                callDirectionAct.putExtra(AppConstants.LATITUDE, itemsData.get(position).getService_latitude());
                callDirectionAct.putExtra(AppConstants.LONGITUDE, itemsData.get(position).getService_longitude());
                mContext.startActivity(callDirectionAct);
            }
        });

//        ((SearchServiceViewHolder) viewHolder).tvBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mActivity.checkUserLogin(itemsData.get(position).getServiceId(),
//                        itemsData.get(position).getServiceAmount(),
//                        itemsData.get(position).getServiceTitle(),
//                        itemsData.get(position).getCurrency());
//            }
//        });

    }

    public void updateRecyclerView(Context mContext, ArrayList<DAOViewAllServices.ServiceList> itemsData) {
        this.mContext = mContext;
        this.itemsData.addAll(itemsData);
        notifyDataSetChanged();
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

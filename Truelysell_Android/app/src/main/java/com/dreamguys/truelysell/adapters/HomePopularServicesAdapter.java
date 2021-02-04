package com.dreamguys.truelysell.adapters;

import android.app.Activity;
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
import com.dreamguys.truelysell.datamodel.CategoryList;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.Phase3.GETHomeList;
import com.dreamguys.truelysell.datamodel.ProviderListData;
import com.dreamguys.truelysell.interfaces.OnLoadMoreListener;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;
import com.dreamguys.truelysell.viewwidgets.ViewBinderHelper;

public class HomePopularServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    public ArrayList<ProviderListData.ProviderList> itemsData = new ArrayList<>();
    int viewType;
    LanguageModel.Request_and_provider_list langReqProvData = new LanguageModel().new Request_and_provider_list();
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    public OnLoadMoreListener mOnLoadMoreListener;
    List<CategoryList.Category_list> category_list;
    public String cat_id = "";
    int appColor;
    List<GETHomeList.ServiceList> serviceList = new ArrayList<>();


    public HomePopularServicesAdapter(Context mContext, List<GETHomeList.ServiceList> serviceList) {
        this.mContext = mContext;
        this.serviceList = serviceList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_popular_services, parent, false);
        return new PopularServicesViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        PopularServicesViewHolder popularViewHolder = (PopularServicesViewHolder) viewHolder;

        if (AppUtils.isThemeChanged(mContext)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popularViewHolder.ivProfileImage.setImageTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                popularViewHolder.ivPopularServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                popularViewHolder.tvCategory.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                popularViewHolder.ivProfileImage.setBorderColor(AppUtils.getPrimaryAppTheme(mContext));
            }
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callServiceDetailAct = new Intent(mContext, ActivityServiceDetail.class);
                callServiceDetailAct.putExtra(AppConstants.FROMPAGE, AppConstants.HOME);
                callServiceDetailAct.putExtra(AppConstants.SERVICEID, serviceList.get(position).getServiceId());
                callServiceDetailAct.putExtra(AppConstants.SERVICETITLE, serviceList.get(position).getServiceTitle());
                mContext.startActivity(callServiceDetailAct);
            }
        });

        popularViewHolder.rbRating.setRating(Float.parseFloat(serviceList.get(position).getRatings()));
        popularViewHolder.tvRatingCount.setText("(" + serviceList.get(position).getRatingCount() + ")");
        popularViewHolder.tvCategory.setText(serviceList.get(position).getCategoryName());
        popularViewHolder.tvServiceName.setText(serviceList.get(position).getServiceTitle());
        popularViewHolder.tvServicePrice.setText(Html.fromHtml(serviceList.get(position).getCurrency()) + serviceList.get(position).getServiceAmount());

        Glide.with(mContext)
                .load(AppConstants.BASE_URL + serviceList.get(position).getServiceImage())
                .apply(new RequestOptions().error(R.drawable.ic_service_placeholder).placeholder(R.drawable.ic_service_placeholder).transforms(new CenterCrop(), new RoundedCorners(40)))
                .into(popularViewHolder.ivServiceImage);

        Glide.with(mContext)
                .load(R.drawable.bg_gradient_color_black)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(40)))
                .into(popularViewHolder.ivGradient);

        Glide.with(mContext)
                .load(AppConstants.BASE_URL + serviceList.get(position).getUserImage())
                .apply(new RequestOptions().error(R.drawable.ic_user_placeholder).placeholder(R.drawable.ic_user_placeholder).transforms(new CenterCrop(), new RoundedCorners(40)))
                .into(popularViewHolder.ivProfileImage);
    }

    public void updateRecyclerView(Context mContext, ArrayList<ProviderListData.ProviderList> itemsData) {
        this.mContext = mContext;
        this.itemsData.addAll(itemsData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }


    public class PopularServicesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_profile_image)
        CircleImageView ivProfileImage;
        @BindView(R.id.iv_popular_services)
        ImageView ivPopularServices;
        @BindView(R.id.tv_service_name)
        TextView tvServiceName;
        @BindView(R.id.rb_rating)
        RatingBar rbRating;
        @BindView(R.id.tv_total_rating)
        TextView tvRatingCount;
        @BindView(R.id.tv_service_price)
        TextView tvServicePrice;
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.iv_service_image)
        ImageView ivServiceImage;
        @BindView(R.id.iv_gradient)
        ImageView ivGradient;


        public PopularServicesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dreamguys.truelysell.ActivityServiceDetail;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.Phase3.DAOSearchServices;
import com.dreamguys.truelysell.interfaces.OnLoadMoreListener;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;
import com.dreamguys.truelysell.viewwidgets.ViewBinderHelper;

public class SearchServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    public List<DAOSearchServices.Datum> itemsData = new ArrayList<>();
    int viewType;
    LanguageModel.Request_and_provider_list langReqProvData = new LanguageModel().new Request_and_provider_list();
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    public OnLoadMoreListener mOnLoadMoreListener;
    public String cat_id = "";
    AlertDialog dialog;
    int appColor;
    LanguageResponse.Data.Language.HomeScreen homeStringsList;

    public SearchServicesAdapter(Context mContext, List<DAOSearchServices.Datum> data,
                                 LanguageResponse.Data.Language.HomeScreen homeStringsList) {
        this.mContext = mContext;
        this.itemsData = data;
        this.homeStringsList = homeStringsList;
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
                searchViewHolder.tvViewonmap.setTextColor(AppUtils.getPrimaryAppTheme(mContext));
                searchViewHolder.tvViewonmap.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
            }
        }

        Picasso.get()
                .load(AppConstants.BASE_URL + itemsData.get(position).getServiceImage())
                .placeholder(R.drawable.ic_service_placeholder)
                .error(R.drawable.ic_service_placeholder)
                .into(searchViewHolder.ivServiceImage);

        searchViewHolder.tvCategory.setText(itemsData.get(position).getCategoryName());
        searchViewHolder.tvCost.setText(Html.fromHtml(itemsData.get(position).getCurrency()) + itemsData.get(position).getServiceAmount());
        searchViewHolder.tvServiceName.setText(itemsData.get(position).getServiceTitle());

        if (!itemsData.get(position).getRating().isEmpty()) {
            searchViewHolder.rbRating.setRating(Float.parseFloat(itemsData.get(position).getRating()));
        }

        Picasso.get()
                .load(AppConstants.BASE_URL + itemsData.get(position).getProfileImg())
                .placeholder(R.drawable.ic_service_placeholder)
                .error(R.drawable.ic_service_placeholder)
                .into(searchViewHolder.ivUserimage);

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
    }


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
            tvViewonmap.setText(AppUtils.cleanLangStr(mContext,
                    homeStringsList.getLblViewMap().getName(), R.string.txt_view_on_map));

        }
    }
}

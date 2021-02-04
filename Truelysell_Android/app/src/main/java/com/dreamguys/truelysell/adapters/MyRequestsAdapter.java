package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.dreamguys.truelysell.ActivityBookingRequestDetail;
import com.dreamguys.truelysell.MyBookingRequestsActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.Phase3.DAOMyRequests;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;

public class MyRequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    public List<DAOMyRequests.Datum> itemsData = new ArrayList<>();
    MyBookingRequestsActivity myBookingRequestsActivity;

    public MyRequestsAdapter(Context mContext, MyBookingRequestsActivity myBookingRequestsActivity, List<DAOMyRequests.Datum> data) {
        this.mContext = mContext;
        this.itemsData = data;
        this.myBookingRequestsActivity = myBookingRequestsActivity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_requests, parent, false);
        return new RequestViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        RequestViewHolder requestViewHolder = (RequestViewHolder) viewHolder;

        requestViewHolder.tvServiceName.setText(itemsData.get(position).getServiceTitle());
        requestViewHolder.tvCategory.setText(itemsData.get(position).getCategoryName());
        requestViewHolder.tvServiceCost.setText(Html.fromHtml(itemsData.get(position).getCurrencyCode()) + itemsData.get(position).getServiceAmount());
        requestViewHolder.tvServiceName.setText(itemsData.get(position).getServiceTitle());
        if (!itemsData.get(position).getRating().isEmpty()) {
            requestViewHolder.rbRating.setRating(Float.parseFloat(itemsData.get(position).getRating()));
        }
        requestViewHolder.tvRating.setText("(" + itemsData.get(position).getRatingCount() + ")");
        Glide.with(mContext)
                .load(AppConstants.BASE_URL + itemsData.get(position).getServiceImage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_service_placeholder).transforms(new CenterCrop(), new RoundedCorners(35)))
                .into(requestViewHolder.ivServiceImage);

        Glide.with(mContext)
                .load(AppConstants.BASE_URL + itemsData.get(position).getProfileImg())
                .apply(new RequestOptions().placeholder(R.drawable.ic_user_placeholder).transforms(new CenterCrop(), new RoundedCorners(40)))
                .into(requestViewHolder.ivUserimage);


//        requestViewHolder.ivAccept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//
//        requestViewHolder.ivReject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        requestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callBookingDetailAct = new Intent(mContext, ActivityBookingRequestDetail.class);
                callBookingDetailAct.putExtra(AppConstants.BOOKINGID, itemsData.get(position).getId());
                callBookingDetailAct.putExtra(AppConstants.SERVICETITLE, itemsData.get(position).getServiceTitle());
                mContext.startActivity(callBookingDetailAct);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }


    public class RequestViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.iv_accept)
        ImageView ivAccept;
        @BindView(R.id.iv_reject)
        ImageView ivReject;
        @BindView(R.id.tv_service_cost)
        TextView tvServiceCost;
        @BindView(R.id.tv_viewonmap)
        TextView tvViewOnMap;
        @BindView(R.id.tv_progress)
        TextView tvProgress;

        public RequestViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }
}

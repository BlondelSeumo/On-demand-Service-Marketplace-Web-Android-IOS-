package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
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
import com.dreamguys.truelysell.ActivityBookingRequestDetail;
import com.dreamguys.truelysell.ChatDetailActivity;
import com.dreamguys.truelysell.MapsGetDirections;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOBookingList;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    public List<DAOBookingList.Datum> itemsData = new ArrayList<>();
    LanguageResponse.Data.Language.BookingService name;


    public BookingServicesAdapter(Context mContext, List<DAOBookingList.Datum> data, LanguageResponse.Data.Language.BookingService name) {
        this.mContext = mContext;
        this.itemsData = data;
        this.name = name;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_bookings, parent, false);
        return new BookingViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        BookingViewHolder bookingViewHolder = (BookingViewHolder) viewHolder;

        if (AppUtils.isThemeChanged(mContext)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bookingViewHolder.ivUserimage.setBorderColor(AppUtils.getPrimaryAppTheme(mContext));
                bookingViewHolder.tvCategory.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(mContext)));
                bookingViewHolder.tvProgress.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                bookingViewHolder.tvViewonmap.setTextColor(AppUtils.getSecondaryAppTheme(mContext));
                bookingViewHolder.tvViewonmap.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(mContext)));
                bookingViewHolder.ivCall.setImageTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                bookingViewHolder.ivChat.setImageTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
            }
        }

        bookingViewHolder.tvServiceName.setText(itemsData.get(position).getServiceTitle());
        bookingViewHolder.tvCategory.setText(itemsData.get(position).getCategoryName());
        bookingViewHolder.tvServiceCost.setText(Html.fromHtml(itemsData.get(position).getCurrency()) + itemsData.get(position).getServiceAmount());
        bookingViewHolder.tvServiceName.setText(itemsData.get(position).getServiceTitle());
        if (!itemsData.get(position).getRating().isEmpty()) {
            bookingViewHolder.rbRating.setRating(Float.parseFloat(itemsData.get(position).getRating()));
            bookingViewHolder.tvRating.setText("(" + itemsData.get(position).getRating_count() + ")");
        }


        if (itemsData.get(position).getStatus().equalsIgnoreCase("1")) {
            bookingViewHolder.tvProgress.setText(AppUtils.cleanLangStr(mContext,
                    name.getLblPending().getName(), R.string.txt_pending));
        } else if (itemsData.get(position).getStatus().equalsIgnoreCase("2") || itemsData.get(position).getStatus().equalsIgnoreCase("3")) {
            bookingViewHolder.tvProgress.setText(AppUtils.cleanLangStr(mContext,
                    name.getLblInprogress().getName(), R.string.txt_inprogress));
        }
        if (itemsData.get(position).getStatus().equalsIgnoreCase("5")) {
            bookingViewHolder.tvProgress.setText(AppUtils.cleanLangStr(mContext,
                    name.getLbl_rejected().getName(), R.string.txt_rejected));
        }
        if (itemsData.get(position).getStatus().equalsIgnoreCase("6")) {
            bookingViewHolder.tvProgress.setText(AppUtils.cleanLangStr(mContext,
                    name.getLblCompleted().getName(), R.string.txt_completed));
        }
        if (itemsData.get(position).getStatus().equalsIgnoreCase("7")) {
            bookingViewHolder.tvProgress.setText(AppUtils.cleanLangStr(mContext,
                    name.getLblCancelled().getName(), R.string.txt_cancelled));
        }


        bookingViewHolder.tvViewonmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callDirectionAct = new Intent(mContext, MapsGetDirections.class);
                callDirectionAct.putExtra(AppConstants.LATITUDE, itemsData.get(position).getLatitude());
                callDirectionAct.putExtra(AppConstants.LONGITUDE, itemsData.get(position).getLongitude());
                mContext.startActivity(callDirectionAct);
            }
        });

        Glide.with(mContext)
                .load(AppConstants.BASE_URL + itemsData.get(position).getServiceImage())
                .apply(new RequestOptions().error(R.drawable.ic_service_placeholder).placeholder(R.drawable.ic_service_placeholder).transforms(new CenterCrop(), new RoundedCorners(35)))
                .into(bookingViewHolder.ivServiceImage);

        Glide.with(mContext)
                .load(AppConstants.BASE_URL + itemsData.get(position).getProfileImg())
                .apply(new RequestOptions().placeholder(R.drawable.ic_user_placeholder).transforms(new CenterCrop(), new RoundedCorners(40)))
                .into(bookingViewHolder.ivUserimage);


        bookingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callBookingDetailAct = new Intent(mContext, ActivityBookingRequestDetail.class);
                callBookingDetailAct.putExtra(AppConstants.BOOKINGID, itemsData.get(position).getId());
                callBookingDetailAct.putExtra(AppConstants.SERVICETITLE, itemsData.get(position).getServiceTitle());
                mContext.startActivity(callBookingDetailAct);
            }
        });

        bookingViewHolder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemsData.get(position).getMobileno(), null));
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    if (e instanceof ActivityNotFoundException) {
                        AppUtils.showToast(mContext, "Dialing not supported");//TODO:
                    }
                }
            }
        });


        bookingViewHolder.ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callChatDetailAct = new Intent(mContext, ChatDetailActivity.class);
                callChatDetailAct.putExtra(AppConstants.chatFrom, itemsData.get(position).getToken());
                callChatDetailAct.putExtra(AppConstants.chatUsername, itemsData.get(position).getName());
                callChatDetailAct.putExtra(AppConstants.chatImg, itemsData.get(position).getProfileImg());
                AppUtils.appStartIntent(mContext, callChatDetailAct);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }


    public class BookingViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.iv_call)
        ImageView ivCall;
        @BindView(R.id.iv_chat)
        ImageView ivChat;
        @BindView(R.id.tv_viewonmap)
        TextView tvViewonmap;
        @BindView(R.id.tv_service_cost)
        TextView tvServiceCost;
        @BindView(R.id.tv_progress)
        TextView tvProgress;

        public BookingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvViewonmap.setText(AppUtils.cleanLangStr(mContext,
                    name.getLbl_map().getName(), R.string.txt_view_on_map));
        }
    }
}

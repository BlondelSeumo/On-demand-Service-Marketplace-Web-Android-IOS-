package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dreamguys.truelysell.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.fragments.phase3.GETServiceDetails;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;

public class ServiceReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    List<GETServiceDetails.Reviews> mReviews = new ArrayList<>();


//    public SellerOtherServicesAdapter(Context mContext, List<CategoryList.Category_list> category_list, AlertDialog dialog, TextView etCategory, String cat_ID, int appColor) {
//        this.mContext = mContext;
//        this.dialog = dialog;
//        this.tvCategory = etCategory;
//        this.category_list = category_list;
//        this.cat_id = cat_ID;
//        this.appColor = appColor;
//    }

    public ServiceReviewsAdapter(Context mContext, List<GETServiceDetails.Reviews> mReviews) {
        this.mContext = mContext;
        this.mReviews = mReviews;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_review_list, parent, false);
        return new ReviewsViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        ReviewsViewHolder categoryViewHolder = (ReviewsViewHolder) viewHolder;

        if (AppUtils.isThemeChanged(mContext)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                categoryViewHolder.ivUserImg.setBorderColor(AppUtils.getPrimaryAppTheme(mContext));
                categoryViewHolder.ivUserImg.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
            }
        }

        categoryViewHolder.tvComments.setText(mReviews.get(position).getReview());
        categoryViewHolder.rbReviews.setRating(Float.parseFloat(mReviews.get(position).getRating()));
        categoryViewHolder.tvUsername.setText(mReviews.get(position).getName());


        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
            Date dates = null;
            dates = simpleDateFormat.parse(mReviews.get(position).getCreated());
            String formattedDate = simpleDateFormat2.format(dates);
            categoryViewHolder.tvTime.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Picasso.get()
                .load(AppConstants.BASE_URL + mReviews.get(position).getProfileImg())
                .placeholder(R.drawable.ic_user_placeholder)
                .error(R.drawable.ic_user_placeholder)
                .into(categoryViewHolder.ivUserImg);

    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mReviews.size();
//        return category_list.size();
    }


    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        //        @BindView(R.id.tv_cat_name)
//        TextView tvCatName;
        @BindView(R.id.iv_user_img)
        CircleImageView ivUserImg;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.rb_reviews)
        RatingBar rbReviews;
        @BindView(R.id.tv_comments)
        TextView tvComments;
        @BindView(R.id.tv_show_more_replies)
        TextView tvShowMoreReplies;
        @BindView(R.id.tv_time)
        TextView tvTime;


        public ReviewsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }
}

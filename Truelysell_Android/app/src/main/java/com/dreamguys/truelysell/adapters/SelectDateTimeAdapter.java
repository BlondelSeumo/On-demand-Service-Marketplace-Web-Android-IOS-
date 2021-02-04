package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dreamguys.truelysell.ActivityBookService;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.Phase3.DAOAvailableTimeSlots;
import com.dreamguys.truelysell.interfaces.OnLoadMoreListener;
import com.dreamguys.truelysell.utils.AppUtils;

public class SelectDateTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    public List<DAOAvailableTimeSlots.Availability> itemsData = new ArrayList<>();
    LanguageModel.Request_and_provider_list langReqProvData = new LanguageModel().new Request_and_provider_list();
    public OnLoadMoreListener mOnLoadMoreListener;
    ActivityBookService.BookService bookServiceDetails;


    public SelectDateTimeAdapter(Context mContext, List<DAOAvailableTimeSlots.Availability> availability, ActivityBookService.BookService bookServiceDetails) {
        this.mContext = mContext;
        this.itemsData = availability;
        this.bookServiceDetails = bookServiceDetails;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_datetime_slots, parent, false);
        return new AvailabilityViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        AvailabilityViewHolder categoryViewHolder = (AvailabilityViewHolder) viewHolder;

        if (AppUtils.isThemeChanged(mContext)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                categoryViewHolder.tvFrom.setTextColor(AppUtils.getPrimaryAppTheme(mContext));
                categoryViewHolder.tvTo.setTextColor(AppUtils.getPrimaryAppTheme(mContext));
            }
        }

        categoryViewHolder.tvFromtime.setText(itemsData.get(position).getStartTime());
        categoryViewHolder.tvTotime.setText(itemsData.get(position).getEndTime());

        if (itemsData.get(position).getIsSelected().equalsIgnoreCase("1")) {
            categoryViewHolder.llTimeslots.setBackground(mContext.getResources().getDrawable(R.drawable.shape_timeslots_bg_pink));
        } else {
            categoryViewHolder.llTimeslots.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rect_round_corner_white));
        }
//

        categoryViewHolder.ivOtherServices.setBackground(position % 2 == 0 ? mContext.getResources().getDrawable(R.drawable.shape_rect_round_corner_yellow)
                : mContext.getResources().getDrawable(R.drawable.shape_rect_round_corner_pink));

        categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < itemsData.size(); i++) {
                    itemsData.get(i).setIsSelected("0");
                }
                itemsData.get(position).setIsSelected("1");
                bookServiceDetails.setFromTime(itemsData.get(position).getStartTime());
                bookServiceDetails.setToTime(itemsData.get(position).getEndTime());
                notifyDataSetChanged();
            }
        });

    }

    public void updateRecyclerView(Context mContext, ArrayList<DAOAvailableTimeSlots.Availability> itemsData) {
        this.mContext = mContext;
        this.itemsData.addAll(itemsData);
        notifyDataSetChanged();
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
//        return category_list.size();
    }


    public class AvailabilityViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_fromtime)
        TextView tvFromtime;
        @BindView(R.id.tv_totime)
        TextView tvTotime;
        @BindView(R.id.iv_other_services)
        ImageView ivOtherServices;
        @BindView(R.id.tv_timings)
        TextView tvTimings;
        @BindView(R.id.tv_from)
        TextView tvFrom;
        @BindView(R.id.tv_to)
        TextView tvTo;
        @BindView(R.id.ll_timeslots)
        LinearLayout llTimeslots;

        public AvailabilityViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }
}

package com.dreamguys.truelysell.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.Phase3.NotificationResponse;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private List<NotificationResponse.Data.NotificationList> notificationList;

    public NotificationAdapter(Context context, List<NotificationResponse.Data.NotificationList> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_notification_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (AppUtils.isThemeChanged(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.userImg.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(context)));
                viewHolder.userImg.setBorderColor(AppUtils.getPrimaryAppTheme(context));
            }
        }

        Picasso.get()
                .load(AppConstants.BASE_URL + notificationList.get(i).getProfileImg())
                .placeholder(R.drawable.ic_service_placeholder)
                .into(viewHolder.userImg);

        viewHolder.message.setText(notificationList.get(i).getMessage());
        calTimeDifferentitaion(notificationList.get(i).getUtcDateTime(), viewHolder.time);

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userImg;
        private TextView message, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImg = itemView.findViewById(R.id.iv_user_img);
            message = itemView.findViewById(R.id.tv_username);
            time = itemView.findViewById(R.id.tv_time);
        }
    }

    public void calTimeDifferentitaion(String responseTime, TextView tvMessagerLastTime) {

        ParsePosition pos = new ParsePosition(0);


        Date date1 = null;
        Date date2 = null;
        try {


            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat4.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date dates = simpleDateFormat4.parse(responseTime);
                simpleDateFormat4.setTimeZone(TimeZone.getDefault());
                String formattedDate = simpleDateFormat2.format(dates);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dates = simpleDateFormat.parse(responseTime);

            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            String formattedDate = simpleDateFormat2.format(dates);
            date1 = simpleDateFormat2.parse(formattedDate);
            date2 = new Date();
            long then = date1.getTime();
            long now = date2.getTime();
            long diff = now - then;
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            String time = null, createdAt = null;
            if (seconds < 0) {
                tvMessagerLastTime.setText(" just now");
            } else if (seconds < 60) {
                tvMessagerLastTime.setText(seconds + " seconds ago");
            } else if (minutes < 60) {

                tvMessagerLastTime.setText(minutes + " minutes ago");

            } else if (hours < 24) {
                tvMessagerLastTime.setText(hours + " hours ago");

            } else {
                tvMessagerLastTime.setText(days + " days ago");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

}

package com.dreamguys.truelysell.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dreamguys.truelysell.ChatDetailActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.Phase3.DAOCHATLIST;
import com.dreamguys.truelysell.interfaces.OnLoadMoreListener;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hari on 12-05-2018.
 */

public class ChatHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    public List<DAOCHATLIST.Datum> itemsData = new ArrayList<>();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public OnLoadMoreListener mOnLoadMoreListener;


    public ChatHistoryAdapter(Context mContext, List<DAOCHATLIST.Datum> itemsData) {

        this.mContext = mContext;
        this.itemsData = itemsData;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_chat_history, parent, false);
            return new ChatHistoryViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;

        /*// create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_chat_history, parent, false);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;*/
    }

    @Override
    public int getItemViewType(int position) {
        return itemsData.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ChatHistoryViewHolder) {
            ChatHistoryViewHolder chatHistoryViewHolder = (ChatHistoryViewHolder) holder;
            chatHistoryViewHolder.tvMessagerName.setText(itemsData.get(position).getName().substring(0, 1).toUpperCase() + itemsData.get(position).getName().substring(1));
            chatHistoryViewHolder.tvMessagerLastMessage.setText(itemsData.get(position).getMessage());
            chatHistoryViewHolder.tvMessagerLastTime.setText(itemsData.get(position).getDate());
//            if (!itemsData.get(position).getChat_count().equalsIgnoreCase("0")) {
//                chatHistoryViewHolder.tvUnreadCount.setText(itemsData.get(position).getChat_count());
//            } else {
//                chatHistoryViewHolder.tvUnreadCount.setVisibility(View.GONE);
//            }


//            calTimeDifferentitaion(itemsData.get(position).getChatUtcTime(), chatHistoryViewHolder.tvMessagerLastTime);


            if (itemsData.get(position).getProfileImg() != null && !itemsData.get(position).getProfileImg().isEmpty()) {
                Picasso.get()
                        .load(AppConstants.BASE_URL + itemsData.get(position).getProfileImg())
                        .placeholder(R.drawable.ic_user_placeholder)
                        .error(R.drawable.ic_user_placeholder)
                        .into(chatHistoryViewHolder.ivUserimg);
            }

            chatHistoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callChatDetailAct = new Intent(mContext, ChatDetailActivity.class);
                    callChatDetailAct.putExtra(AppConstants.chatFrom, itemsData.get(position).getToken());
                    callChatDetailAct.putExtra(AppConstants.chatUsername, itemsData.get(position).getName());
                    callChatDetailAct.putExtra(AppConstants.chatImg, itemsData.get(position).getProfileImg());
                    AppUtils.appStartIntent(mContext, callChatDetailAct);
                }
            });


        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return itemsData == null ? 0 : itemsData.size();
    }


    public void updateRecyclerView(Context mContext, List<DAOCHATLIST.Datum> itemsData) {
        this.mContext = mContext;
        this.itemsData.addAll(itemsData);
        notifyDataSetChanged();
    }

    /*public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_userimg)
        CircleImageView ivUserimg;
        @BindView(R.id.tv_messager_name)
        TextView tvMessagerName;
        @BindView(R.id.tv_messager_last_message)
        TextView tvMessagerLastMessage;
        @BindView(R.id.tv_messager_last_time)
        TextView tvMessagerLastTime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }*/


    static class ChatHistoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_userimg)
        CircleImageView ivUserimg;
        @BindView(R.id.tv_messager_name)
        TextView tvMessagerName;
        @BindView(R.id.tv_messager_last_message)
        TextView tvMessagerLastMessage;
        @BindView(R.id.tv_messager_last_time)
        TextView tvMessagerLastTime;
        @BindView(R.id.ll_chat_list)
        LinearLayout llChatList;
        @BindView(R.id.tv_unread_count)
        TextView tvUnreadCount;

        public ChatHistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }


    public void calTimeDifferentitaion(String responseTime, TextView tvMessagerLastTime) {

        ParsePosition pos = new ParsePosition(0);


        Date date1 = null;
        Date date2 = null;
        try {
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



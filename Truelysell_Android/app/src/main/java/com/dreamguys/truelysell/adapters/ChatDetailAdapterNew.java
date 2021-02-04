package com.dreamguys.truelysell.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.ChatDetailListData;
import com.dreamguys.truelysell.datamodel.DateItem;
import com.dreamguys.truelysell.datamodel.GeneralItem;
import com.dreamguys.truelysell.datamodel.ListItem;
import com.dreamguys.truelysell.interfaces.OnLoadMoreListener;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hari on 14-05-2018.
 */

public class ChatDetailAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    public ArrayList<ListItem> itemsData = new ArrayList<>();
    public OnLoadMoreListener mOnLoadMoreListener;
    HashMap<String, ArrayList<ChatDetailListData.ChatList>> hashMap = new HashMap<String, ArrayList<ChatDetailListData.ChatList>>();
    ArrayList<ChatDetailListData.ChatList> tempData;
    HashMap<String, ArrayList<ChatDetailListData.ChatList>> groupedHashMap;

    //private List<ListItem> consolidatedList = new ArrayList<>();
    String key = "";

    private int SELF = 1, OTHER = 2, LOADING = 3, ITEM_DATE = 4;
    private int lastPosition = -1;
    private int lastPosition1 = -1;


    public ChatDetailAdapterNew(Context mContext, ArrayList<ListItem> itemsData) {
        this.mContext = mContext;
        this.itemsData = itemsData;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == LOADING) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(itemView);
        } else if (viewType == ListItem.TYPE_GENERAL) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_chat_general, parent, false);
            return new ChatDetailViewHolder(itemView);
        } else if (viewType == ListItem.TYPE_DATE) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.list_chat_date_item, parent, false);
            return new DateViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (itemsData != null && itemsData.size() > 0) {
            switch (holder.getItemViewType()) {
                case ListItem.TYPE_GENERAL:
                    GeneralItem generalItem = (GeneralItem) itemsData.get(position);
                    ChatDetailViewHolder chatDetailViewHolder = (ChatDetailViewHolder) holder;
                    chatDetailViewHolder.cvChatFrom.setVisibility(View.GONE);
                    chatDetailViewHolder.cvChatTo.setVisibility(View.GONE);
                    if (AppUtils.isThemeChanged(mContext)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            chatDetailViewHolder.cvChatTo.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                        }
                    }
                    if (generalItem.getChatList().getSenderToken().equalsIgnoreCase(PreferenceStorage.getKey(AppConstants.USER_TOKEN))) {
                        if (!generalItem.getChatList().getMessage().isEmpty()) {
                            chatDetailViewHolder.tvFromMsg.setText(generalItem.getChatList().getMessage());
                            chatDetailViewHolder.cvChatFrom.setVisibility(View.VISIBLE);
                        }
                        String aDate = generalItem.getChatList().getUtc_date_time();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;
                        try {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm aa");
                            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date dates = simpleDateFormat.parse(aDate);
                            simpleDateFormat.setTimeZone(TimeZone.getDefault());
                            String formattedDate = simpleDateFormat2.format(dates);
                            chatDetailViewHolder.tvFromMsgTime.setText(formattedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
//                        setAnimationRight(chatDetailViewHolder.itemView, position);
                    } else {
                        if (!generalItem.getChatList().getMessage().isEmpty()) {
                            chatDetailViewHolder.tvToMsg.setText(generalItem.getChatList().getMessage());
                            chatDetailViewHolder.cvChatTo.setVisibility(View.VISIBLE);
                        }
                        if (AppUtils.isThemeChanged(mContext)) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                chatDetailViewHolder.cvChatTo.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                            }
                        }
                        if (generalItem.getChatList().getUtc_date_time() != null &&
                                !generalItem.getChatList().getUtc_date_time().isEmpty()) {
                            String aDate = generalItem.getChatList().getUtc_date_time();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;
                            try {
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm aa");
                                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                Date dates = simpleDateFormat.parse(aDate);
                                simpleDateFormat.setTimeZone(TimeZone.getDefault());
                                String formattedDate = simpleDateFormat2.format(dates);
                                chatDetailViewHolder.tvToMsgTime.setText(formattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
//                        setAnimationLeft(chatDetailViewHolder.itemView, position);
                        }
                    }
                    // Populate general item data here
                    break;

                case ListItem.TYPE_DATE:
                    DateItem dateItem = (DateItem) itemsData.get(position);
                    DateViewHolder dateViewHolder = (DateViewHolder) holder;

                    // Populate date item data here
                    if (dateItem.getDate() != null && !dateItem.getDate().isEmpty()) {
                        dateViewHolder.tvChatDate.setText(dateItem.getDate());
//                        String aDate = dateItem.getDate();
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        Date date = null;
//                        try {
//                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
//                            Date dates = simpleDateFormat.parse(aDate);
//                            String formattedDate = simpleDateFormat2.format(dates);
//                            dateViewHolder.tvChatDate.setText(formattedDate);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
                    }
//                    setAnimationDate(dateViewHolder.itemView, position);
                    break;
            }
        }
    }

    private void setAnimationDate(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? android.R.anim.fade_out
                        : android.R.anim.fade_out);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

    private void setAnimationLeft(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.slide_in_left
                        : R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

    private void setAnimationRight(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition1) ? R.anim.slide_in_right
                        : R.anim.slide_in_right);
        viewToAnimate.startAnimation(animation);
        lastPosition1 = position;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemsData.get(position) == null) {
            return LOADING;
        } else {
            return itemsData.get(position).getType();
        }

    }


    @Override
    public int getItemCount() {
        return itemsData == null ? 0 : itemsData.size();
    }

    public void updateRecyclerView(Context mContext, ArrayList<ListItem> itemsData) {
        this.mContext = mContext;
        this.itemsData = itemsData;
        notifyDataSetChanged();
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    static class ChatDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_from_msg)
        TextView tvFromMsg;
        @BindView(R.id.tv_from_msg_time)
        TextView tvFromMsgTime;
        @BindView(R.id.tv_to_msg)
        TextView tvToMsg;
        @BindView(R.id.tv_to_msg_time)
        TextView tvToMsgTime;
        @BindView(R.id.cv_chat_from)
        CardView cvChatFrom;
        @BindView(R.id.cv_chat_to)
        CardView cvChatTo;


        public ChatDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // ViewHolder for date row item
    class DateViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_chat_date)
        TextView tvChatDate;

        public DateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

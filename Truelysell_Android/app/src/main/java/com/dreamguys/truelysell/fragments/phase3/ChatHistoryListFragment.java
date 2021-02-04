package com.dreamguys.truelysell.fragments.phase3;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamguys.truelysell.HomeActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.adapters.ChatHistoryAdapter;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOCHATLIST;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by Hari on 12-05-2018.
 */

public class ChatHistoryListFragment extends Fragment implements RetrofitHandler.RetrofitResHandler {


    @BindView(R.id.rv_message_chat_list)
    RecyclerView rvMessageChatList;
    @BindView(R.id.tv_no_chat_available)
    TextView tvNoChatAvailable;
    int currentPageNo = 1, nextPageNo = -1;
    DAOCHATLIST chatHistoryListData;
    ChatHistoryAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    @BindView(R.id.txt_chat_lists)
    TextView txtChatLists;
    @BindView(R.id.iv_userlogin)
    ImageView ivUserlogin;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.txt_coming_soon)
    TextView txtComingSoon;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    //private OnLoadMoreListener mOnLoadMoreListener;
    //public LanguageModel.Common_used_texts common_used_texts = new LanguageModel().new Common_used_texts();
    Unbinder unbinder;
    TextView textView;
    EditText editText;
    JSONObject jsonObject;
    String fromID = "", toID = "";
    HomeActivity homeActivity;
    LanguageResponse.Data.Language.ChatScreen chatScreenList;
    LanguageResponse.Data.Language.CommonStrings commonStrings;

    public ChatHistoryListFragment(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.activity_chat, container, false);
        unbinder = ButterKnife.bind(this, mView);
        editText = (EditText) mView.findViewById(R.id.message);
//        getLocaleData();

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvMessageChatList.setLayoutManager(mLayoutManager);

        if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
            getChatHistoryList();
        } else {
//            homeActivity.showUserLoginDialog();
           homeActivity.getLoginType();
        }


//        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                Log.e("haint", "Load More");
//
//                isLoading = true;
//                mAdapter.itemsData.add(null);
//                //mUsers.add(null);
//                mAdapter.notifyItemInserted(mAdapter.itemsData.size() - 1);
//                //Load more data for reyclerview
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("haint", "Load More 2");
//                        getChatHistoryList(true);
//                    }
//                }, 1000);
//
//            }
//        });
//
//        rvMessageChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                totalItemCount = mLayoutManager.getItemCount();
//                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
//                if (!isLoading && (nextPageNo > currentPageNo) && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                    isLoading = true;
//                    if (mAdapter.mOnLoadMoreListener != null) {
//                        mAdapter.mOnLoadMoreListener.onLoadMore();
//                    }
//                }
//            }
//        });

        getLocaleData();
        return mView;
    }

    private void getLocaleData() {
        try {
            String chatDataStr = PreferenceStorage.getKey(CommonLangModel.ChatScreen);
            chatScreenList = new Gson().fromJson(chatDataStr, LanguageResponse.Data.Language.ChatScreen.class);
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.CommonString);
            commonStrings = new Gson().fromJson(commonDataStr, LanguageResponse.Data.Language.CommonStrings.class);
            txtChatLists.setText(AppUtils.cleanLangStr(getActivity(),
                    chatScreenList.getLblChatListTitle().getName(), R.string.txt_chat_lists));
        } catch (Exception e) {
            chatScreenList = new LanguageResponse().new Data().new Language().new ChatScreen();
            commonStrings = new LanguageResponse().new Data().new Language().new CommonStrings();
        }
    }

    public void getChatHistoryList() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.clearDialog();
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<DAOCHATLIST> chatHistoryListDataCall = apiService.postChatList(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
            RetrofitHandler.executeRetrofit(getActivity(), chatHistoryListDataCall, AppConstants.CHAT_HISTORYLIST_DATA, ChatHistoryListFragment.this, false);
        } else {
            AppUtils.showToast(getActivity(), AppUtils.cleanLangStr(getActivity(), commonStrings.getLblInternet().getName(), R.string.txt_enable_internet));
        }
    }


    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseModel) {
//        if (isLoadMore) {
//            //Remove loading item
//            mAdapter.itemsData.remove(mAdapter.itemsData.size() - 1);
//            mAdapter.notifyItemRemoved(mAdapter.itemsData.size());
//        }
        if (myRes instanceof DAOCHATLIST) {
            chatHistoryListData = (DAOCHATLIST) myRes;
            if (chatHistoryListData != null && chatHistoryListData.getData() != null && chatHistoryListData.getData().size() > 0) {
                rvMessageChatList.setVisibility(View.VISIBLE);
                tvNoChatAvailable.setVisibility(View.GONE);
                mAdapter = new ChatHistoryAdapter(getActivity(), chatHistoryListData.getData());
                rvMessageChatList.setAdapter(mAdapter);
            } else {
                rvMessageChatList.setVisibility(View.GONE);
                tvNoChatAvailable.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseModel) {
        //Remove loading item
//        failureHandling(isLoadMore, responseModel);
    }

    private void failureHandling(boolean isLoadMore, String responseModel) {
        isLoading = false;
        switch (responseModel) {
            case AppConstants.CHAT_HISTORYLIST_DATA:
                if (isLoadMore && mAdapter.itemsData != null && mAdapter.itemsData.size() > 1) {
                    mAdapter.itemsData.remove(mAdapter.itemsData.size() - 1);
                    mAdapter.notifyItemRemoved(mAdapter.itemsData.size() - 1);
                    mAdapter.notifyDataSetChanged();
                } else {
                    rvMessageChatList.setVisibility(View.GONE);
                    tvNoChatAvailable.setVisibility(View.VISIBLE);
                    tvNoChatAvailable.setText(AppUtils.cleanLangStr(getActivity(),  chatScreenList.getLblNoChat().getName(), R.string.no_chats_available));
                }
                break;
        }
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseModel) {
//        failureHandling(isLoadMore, responseModel);
    }

    @Override
    public void onResume() {
        super.onResume();
//        setToolBarTitle(AppUtils.cleanLangStr(getActivity(), navData.getLg3_provider_chat_h(), R.string.txt_prochat_history));
//        getChatHistoryList(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

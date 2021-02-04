package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Hari on 12-05-2018.
 */

public class ChatHistoryListData extends BaseResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class ChatList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("chat_from")
        @Expose
        private String chatFrom;
        @SerializedName("chat_to")
        @Expose
        private String chatTo;
        @SerializedName("content")
        @Expose
        private String content;
        @SerializedName("chat_utc_time")
        @Expose
        private String chatUtcTime;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("chat_count")
        @Expose
        private String chat_count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getChatFrom() {
            return chatFrom;
        }

        public void setChatFrom(String chatFrom) {
            this.chatFrom = chatFrom;
        }

        public String getChatTo() {
            return chatTo;
        }

        public void setChatTo(String chatTo) {
            this.chatTo = chatTo;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getChatUtcTime() {
            return chatUtcTime;
        }

        public void setChatUtcTime(String chatUtcTime) {
            this.chatUtcTime = chatUtcTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

        public String getChat_count() {
            return chat_count;
        }

        public void setChat_count(String chat_count) {
            this.chat_count = chat_count;
        }
    }

    public class Data {

        @SerializedName("next_page")
        @Expose
        private Integer nextPage;
        @SerializedName("current_page")
        @Expose
        private String currentPage;
        @SerializedName("total_pages")
        @Expose
        private Integer totalPages;
        @SerializedName("chat_list")
        @Expose
        private ArrayList<ChatList> chatList = null;

        public Integer getNextPage() {
            return nextPage;
        }

        public void setNextPage(Integer nextPage) {
            this.nextPage = nextPage;
        }

        public String getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public ArrayList<ChatList> getChatList() {
            return chatList;
        }

        public void setChatList(ArrayList<ChatList> chatList) {
            this.chatList = chatList;
        }

    }
}

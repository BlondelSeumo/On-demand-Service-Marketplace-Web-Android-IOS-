package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Hari on 14-05-2018.
 */

public class ChatDetailListData extends BaseResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class ChatList {

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
        @SerializedName("fromname")
        @Expose
        private String fromname;
        @SerializedName("toname")
        @Expose
        private String toname;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("date")
        @Expose
        private String date;

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

        public String getFromname() {
            return fromname;
        }

        public void setFromname(String fromname) {
            this.fromname = fromname;
        }

        public String getToname() {
            return toname;
        }

        public void setToname(String toname) {
            this.toname = toname;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }


    }

    public class Data {
        @SerializedName("next_page")
        @Expose
        private Integer nextPage;
        @SerializedName("current_page")
        @Expose
        private Integer currentPage;
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

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
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

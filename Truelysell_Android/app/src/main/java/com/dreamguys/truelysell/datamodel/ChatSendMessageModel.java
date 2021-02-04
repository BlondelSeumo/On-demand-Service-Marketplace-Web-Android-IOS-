package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hari on 14-05-2018.
 */

public class ChatSendMessageModel extends BaseResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("chat_to")
        @Expose
        private String chatTo;
        @SerializedName("chat_from")
        @Expose
        private String chatFrom;
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
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("time")
        @Expose
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getChatTo() {
            return chatTo;
        }

        public void setChatTo(String chatTo) {
            this.chatTo = chatTo;
        }

        public String getChatFrom() {
            return chatFrom;
        }

        public void setChatFrom(String chatFrom) {
            this.chatFrom = chatFrom;
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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

    }
}

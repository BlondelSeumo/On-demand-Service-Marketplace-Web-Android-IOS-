package com.dreamguys.truelysell.datamodel.Phase3;

import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DAOChatSentResponse extends BaseResponse {

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

        @SerializedName("chat_id")
        @Expose
        private String chatId;
        @SerializedName("sender_token")
        @Expose
        private String senderToken;
        @SerializedName("receiver_token")
        @Expose
        private String receiverToken;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("read_status")
        @Expose
        private String readStatus;
        @SerializedName("utc_date_time")
        @Expose
        private String utcDateTime;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("time")
        @Expose
        private String time;

        public String getChatId() {
            return chatId;
        }

        public void setChatId(String chatId) {
            this.chatId = chatId;
        }

        public String getSenderToken() {
            return senderToken;
        }

        public void setSenderToken(String senderToken) {
            this.senderToken = senderToken;
        }

        public String getReceiverToken() {
            return receiverToken;
        }

        public void setReceiverToken(String receiverToken) {
            this.receiverToken = receiverToken;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(String readStatus) {
            this.readStatus = readStatus;
        }

        public String getUtcDateTime() {
            return utcDateTime;
        }

        public void setUtcDateTime(String utcDateTime) {
            this.utcDateTime = utcDateTime;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
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

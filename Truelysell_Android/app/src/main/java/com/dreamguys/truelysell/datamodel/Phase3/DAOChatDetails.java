package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.dreamguys.truelysell.datamodel.BaseResponse;

public class DAOChatDetails extends BaseResponse {

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

        @SerializedName("chat_history")
        @Expose
        private List<ChatHistory> chatHistory = null;

        public List<ChatHistory> getChatHistory() {
            return chatHistory;
        }

        public void setChatHistory(List<ChatHistory> chatHistory) {
            this.chatHistory = chatHistory;
        }

    }
    public static class ChatHistory {

        @SerializedName("chat_id")
        @Expose
        private String chatId="";
        @SerializedName("sender_token")
        @Expose
        private String senderToken="";
        @SerializedName("receiver_token")
        @Expose
        private String receiverToken="";
        @SerializedName("message")
        @Expose
        private String message="";
        @SerializedName("status")
        @Expose
        private String status="";
        @SerializedName("read_status")
        @Expose
        private String readStatus="";
        @SerializedName("created_at")
        @Expose
        private String createdAt="";
        @SerializedName("utc_date_time")
        @Expose
        private String utc_date_time="";

        public String getUtc_date_time() {
            return utc_date_time;
        }

        public void setUtc_date_time(String utc_date_time) {
            this.utc_date_time = utc_date_time;
        }

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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }

}

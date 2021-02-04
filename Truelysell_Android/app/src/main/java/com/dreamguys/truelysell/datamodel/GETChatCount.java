package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GETChatCount{

    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("data")
    @Expose
    private Data data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Response {

        @SerializedName("response_code")
        @Expose
        private String response_code;
        @SerializedName("response_message")
        @Expose
        private String response_message;

        public String getResponse_code() {
            return response_code;
        }

        public void setResponse_code(String response_code) {
            this.response_code = response_code;
        }

        public String getResponse_message() {
            return response_message;
        }

        public void setResponse_message(String response_message) {
            this.response_message = response_message;
        }

    }

    public class Data {

        @SerializedName("provider_chat_count")
        @Expose
        private String provider_chat_count;
        @SerializedName("requester_chat_count")
        @Expose
        private String requester_chat_count;
        @SerializedName("requester_count")
        @Expose
        private String requester_count;

        @SerializedName("provider_count")
        @Expose
        private String provider_count;

        public String getProvider_count() {
            return provider_count;
        }

        public void setProvider_count(String provider_count) {
            this.provider_count = provider_count;
        }

        public String getRequester_count() {
            return requester_count;
        }

        public void setRequester_count(String requester_count) {
            this.requester_count = requester_count;
        }

        public String getProvider_chat_count() {
            return provider_chat_count;
        }

        public void setProvider_chat_count(String provider_chat_count) {
            this.provider_chat_count = provider_chat_count;
        }

        public String getRequester_chat_count() {
            return requester_chat_count;
        }

        public void setRequester_chat_count(String requester_chat_count) {
            this.requester_chat_count = requester_chat_count;
        }

    }

}

package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class POSTShowMoreReplies {

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

    public static class Replies_list {

        @SerializedName("replies")
        @Expose
        private String replies;
        @SerializedName("days_ago")
        @Expose
        private String days_ago;
        @SerializedName("replies_id")
        @Expose
        private String replies_id;
        @SerializedName("provider_id")
        @Expose
        private String provider_id;
        @SerializedName("user_id")
        @Expose
        private String user_id;
        @SerializedName("comment_id")
        @Expose
        private String comment_id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("profile_image")
        @Expose
        private String profile_image;

        public String getReplies() {
            return replies;
        }

        public void setReplies(String replies) {
            this.replies = replies;
        }

        public String getDays_ago() {
            return days_ago;
        }

        public void setDays_ago(String days_ago) {
            this.days_ago = days_ago;
        }

        public String getReplies_id() {
            return replies_id;
        }

        public void setReplies_id(String replies_id) {
            this.replies_id = replies_id;
        }

        public String getProvider_id() {
            return provider_id;
        }

        public void setProvider_id(String provider_id) {
            this.provider_id = provider_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

    }

    public class Data {

        @SerializedName("next_page")
        @Expose
        private Integer next_page;
        @SerializedName("current_page")
        @Expose
        private Integer current_page;
        @SerializedName("total_pages")
        @Expose
        private Integer total_pages;
        @SerializedName("replies_list")
        @Expose
        private List<Replies_list> replies_list = null;

        public Integer getNext_page() {
            return next_page;
        }

        public void setNext_page(Integer next_page) {
            this.next_page = next_page;
        }

        public Integer getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(Integer current_page) {
            this.current_page = current_page;
        }

        public Integer getTotal_pages() {
            return total_pages;
        }

        public void setTotal_pages(Integer total_pages) {
            this.total_pages = total_pages;
        }

        public List<Replies_list> getReplies_list() {
            return replies_list;
        }

        public void setReplies_list(List<Replies_list> replies_list) {
            this.replies_list = replies_list;
        }

    }
}

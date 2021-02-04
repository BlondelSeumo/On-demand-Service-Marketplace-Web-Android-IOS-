package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class POSTQuestionsCommentsList {

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
        private String responseCode;
        @SerializedName("response_message")
        @Expose
        private String responseMessage;

        public String getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }

        public String getResponseMessage() {
            return responseMessage;
        }

        public void setResponseMessage(String responseMessage) {
            this.responseMessage = responseMessage;
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
        @SerializedName("comments_list")
        @Expose
        private List<CommentsList> commentsList = null;

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

        public List<CommentsList> getCommentsList() {
            return commentsList;
        }

        public void setCommentsList(List<CommentsList> commentsList) {
            this.commentsList = commentsList;
        }

    }

    public static class CommentsList implements Serializable {

        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("days_ago")
        @Expose
        private String daysAgo;
        @SerializedName("comment_id")
        @Expose
        private String commentId;
        @SerializedName("provider_id")
        @Expose
        private String providerId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("replies_count")
        @Expose
        private String repliesCount;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getDaysAgo() {
            return daysAgo;
        }

        public void setDaysAgo(String daysAgo) {
            this.daysAgo = daysAgo;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getRepliesCount() {
            return repliesCount;
        }

        public void setRepliesCount(String repliesCount) {
            this.repliesCount = repliesCount;
        }

    }

}

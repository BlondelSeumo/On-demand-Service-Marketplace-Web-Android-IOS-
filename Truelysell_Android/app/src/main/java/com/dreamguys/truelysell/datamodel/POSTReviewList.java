package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class POSTReviewList extends BaseResponse {


    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Review_list {

        @SerializedName("rate_id")
        @Expose
        private String rate_id;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("review")
        @Expose
        private String review;
        @SerializedName("reviewer")
        @Expose
        private String reviewer;
        @SerializedName("profile_img")
        @Expose
        private String profile_img;

        public String getRate_id() {
            return rate_id;
        }

        public void setRate_id(String rate_id) {
            this.rate_id = rate_id;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public String getReviewer() {
            return reviewer;
        }

        public void setReviewer(String reviewer) {
            this.reviewer = reviewer;
        }

        public String getProfile_img() {
            return profile_img;
        }

        public void setProfile_img(String profile_img) {
            this.profile_img = profile_img;
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
        @SerializedName("review_list")
        @Expose
        private List<Review_list> review_list = null;

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

        public List<Review_list> getReview_list() {
            return review_list;
        }

        public void setReview_list(List<Review_list> review_list) {
            this.review_list = review_list;
        }

    }

}

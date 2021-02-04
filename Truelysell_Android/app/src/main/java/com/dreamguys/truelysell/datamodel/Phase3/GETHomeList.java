package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.dreamguys.truelysell.datamodel.BaseResponse;

public class GETHomeList extends BaseResponse {

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

        @SerializedName("category_list")
        @Expose
        private List<CategoryList> categoryList = null;
        @SerializedName("popular_services")
        @Expose
        private List<ServiceList> serviceList = null;
        @SerializedName("new_services")
        @Expose
        private List<NewService> new_services = null;

        public List<CategoryList> getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(List<CategoryList> categoryList) {
            this.categoryList = categoryList;
        }

        public List<ServiceList> getServiceList() {
            return serviceList;
        }

        public void setServiceList(List<ServiceList> serviceList) {
            this.serviceList = serviceList;
        }

        public List<NewService> getNew_services() {
            return new_services;
        }

        public void setNew_services(List<NewService> new_services) {
            this.new_services = new_services;
        }

    }

    public class CategoryList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("category_image")
        @Expose
        private String categoryImage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryImage() {
            return categoryImage;
        }

        public void setCategoryImage(String categoryImage) {
            this.categoryImage = categoryImage;
        }

    }

    public class ServiceList {

        @SerializedName("service_id")
        @Expose
        private String serviceId;
        @SerializedName("service_title")
        @Expose
        private String serviceTitle;
        @SerializedName("service_amount")
        @Expose
        private String serviceAmount;
        @SerializedName("service_image")
        @Expose
        private String serviceImage;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("ratings")
        @Expose
        private String ratings;
        @SerializedName("rating_count")
        @Expose
        private String ratingCount;
        @SerializedName("user_image")
        @Expose
        private String userImage;
        @SerializedName("currency")
        @Expose
        private String currency;

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceTitle() {
            return serviceTitle;
        }

        public void setServiceTitle(String serviceTitle) {
            this.serviceTitle = serviceTitle;
        }

        public String getServiceAmount() {
            return serviceAmount;
        }

        public void setServiceAmount(String serviceAmount) {
            this.serviceAmount = serviceAmount;
        }

        public String getServiceImage() {
            return serviceImage;
        }

        public void setServiceImage(String serviceImage) {
            this.serviceImage = serviceImage;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getRatings() {
            return ratings;
        }

        public void setRatings(String ratings) {
            this.ratings = ratings;
        }

        public String getRatingCount() {
            return ratingCount;
        }

        public void setRatingCount(String ratingCount) {
            this.ratingCount = ratingCount;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

    }

    public class NewService {

        @SerializedName("service_id")
        @Expose
        private String serviceId;
        @SerializedName("service_title")
        @Expose
        private String serviceTitle;
        @SerializedName("service_amount")
        @Expose
        private String serviceAmount;
        @SerializedName("service_image")
        @Expose
        private String serviceImage;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("ratings")
        @Expose
        private String ratings;
        @SerializedName("rating_count")
        @Expose
        private String ratingCount;
        @SerializedName("user_image")
        @Expose
        private String userImage;
        @SerializedName("currency")
        @Expose
        private String currency;

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceTitle() {
            return serviceTitle;
        }

        public void setServiceTitle(String serviceTitle) {
            this.serviceTitle = serviceTitle;
        }

        public String getServiceAmount() {
            return serviceAmount;
        }

        public void setServiceAmount(String serviceAmount) {
            this.serviceAmount = serviceAmount;
        }

        public String getServiceImage() {
            return serviceImage;
        }

        public void setServiceImage(String serviceImage) {
            this.serviceImage = serviceImage;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getRatings() {
            return ratings;
        }

        public void setRatings(String ratings) {
            this.ratings = ratings;
        }

        public String getRatingCount() {
            return ratingCount;
        }

        public void setRatingCount(String ratingCount) {
            this.ratingCount = ratingCount;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

    }

}

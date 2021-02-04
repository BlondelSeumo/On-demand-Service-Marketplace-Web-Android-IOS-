package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.dreamguys.truelysell.datamodel.BaseResponse;

public class DAOViewAllServices extends BaseResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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
        @SerializedName("service_latitude")
        @Expose
        private String service_latitude;
        @SerializedName("service_longitude")
        @Expose
        private String service_longitude;

        public String getService_latitude() {
            return service_latitude;
        }

        public void setService_latitude(String service_latitude) {
            this.service_latitude = service_latitude;
        }

        public String getService_longitude() {
            return service_longitude;
        }

        public void setService_longitude(String service_longitude) {
            this.service_longitude = service_longitude;
        }

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
        @SerializedName("service_list")
        @Expose
        private List<ServiceList> serviceList = null;

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

        public List<ServiceList> getServiceList() {
            return serviceList;
        }

        public void setServiceList(List<ServiceList> serviceList) {
            this.serviceList = serviceList;
        }

    }

}

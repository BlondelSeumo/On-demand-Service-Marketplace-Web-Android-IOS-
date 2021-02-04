package com.dreamguys.truelysell.datamodel.Phase3;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.dreamguys.truelysell.datamodel.BaseResponse;

public class DAOEditServiceDetails extends BaseResponse {

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

        @SerializedName("service_overview")
        @Expose
        private ServiceOverview serviceOverview;
        @SerializedName("service_image")
        @Expose
        private List<ServiceImage> serviceImage = null;

        public ServiceOverview getServiceOverview() {
            return serviceOverview;
        }

        public void setServiceOverview(ServiceOverview serviceOverview) {
            this.serviceOverview = serviceOverview;
        }

        public List<ServiceImage> getServiceImage() {
            return serviceImage;
        }

        public void setServiceImage(List<ServiceImage> serviceImage) {
            this.serviceImage = serviceImage;
        }

    }


    public static class ServiceImage {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("service_image")
        @Expose
        private String serviceImage;
        @SerializedName("service_details_image")
        @Expose
        private String serviceDetailsImage;
        @SerializedName("thumb_image")
        @Expose
        private String thumbImage;
        @SerializedName("mobile_image")
        @Expose
        private String mobileImage;
        @SerializedName("is_url")
        @Expose
        private String is_url;

        private Bitmap bitmapImage;

        public Bitmap getBitmapImage() {
            return bitmapImage;
        }

        public void setBitmapImage(Bitmap bitmapImage) {
            this.bitmapImage = bitmapImage;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getServiceImage() {
            return serviceImage;
        }

        public void setServiceImage(String serviceImage) {
            this.serviceImage = serviceImage;
        }

        public String getServiceDetailsImage() {
            return serviceDetailsImage;
        }

        public void setServiceDetailsImage(String serviceDetailsImage) {
            this.serviceDetailsImage = serviceDetailsImage;
        }

        public String getThumbImage() {
            return thumbImage;
        }

        public void setThumbImage(String thumbImage) {
            this.thumbImage = thumbImage;
        }

        public String getMobileImage() {
            return mobileImage;
        }

        public void setMobileImage(String mobileImage) {
            this.mobileImage = mobileImage;
        }

        public String getIs_url() {
            return is_url;
        }

        public void setIs_url(String is_url) {
            this.is_url = is_url;
        }
    }

    public class ServiceOverview {

        @SerializedName("service_id")
        @Expose
        private String serviceId;
        @SerializedName("service_title")
        @Expose
        private String serviceTitle;
        @SerializedName("service_amount")
        @Expose
        private String serviceAmount;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("subcategory")
        @Expose
        private String subcategory;
        @SerializedName("service_offered")
        @Expose
        private String serviceOffered;
        @SerializedName("service_location")
        @Expose
        private String serviceLocation;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("subcategory_name")
        @Expose
        private String subcategoryName;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("ratings")
        @Expose
        private String ratings;
        @SerializedName("views")
        @Expose
        private String views;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("service_latitude")
        @Expose
        private String latitude;
        @SerializedName("service_longitude")
        @Expose
        private String longitude;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(String subcategory) {
            this.subcategory = subcategory;
        }

        public String getServiceOffered() {
            return serviceOffered;
        }

        public void setServiceOffered(String serviceOffered) {
            this.serviceOffered = serviceOffered;
        }

        public String getServiceLocation() {
            return serviceLocation;
        }

        public void setServiceLocation(String serviceLocation) {
            this.serviceLocation = serviceLocation;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getSubcategoryName() {
            return subcategoryName;
        }

        public void setSubcategoryName(String subcategoryName) {
            this.subcategoryName = subcategoryName;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getRatings() {
            return ratings;
        }

        public void setRatings(String ratings) {
            this.ratings = ratings;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}

package com.dreamguys.truelysell.fragments.phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.dreamguys.truelysell.datamodel.BaseResponse;

public class GETServiceDetails extends BaseResponse {


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
        @SerializedName("seller_overview")
        @Expose
        private SellerOverview sellerOverview;
        @SerializedName("reviews")
        @Expose
        private List<Reviews> reviews = null;

        public ServiceOverview getServiceOverview() {
            return serviceOverview;
        }

        public void setServiceOverview(ServiceOverview serviceOverview) {
            this.serviceOverview = serviceOverview;
        }

        public SellerOverview getSellerOverview() {
            return sellerOverview;
        }

        public void setSellerOverview(SellerOverview sellerOverview) {
            this.sellerOverview = sellerOverview;
        }

        public List<Reviews> getReviews() {
            return reviews;
        }

        public void setReviews(List<Reviews> reviews) {
            this.reviews = reviews;
        }
    }

    public class Reviews {
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("review")
        @Expose
        private String review;
        @SerializedName("created")
        @Expose
        private String created;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
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

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }

    public class SellerOverview {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobileno")
        @Expose
        private String mobileno;
        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("country_code")
        @Expose
        private String countryCode;
        @SerializedName("services")
        @Expose
        private List<Service> services = null;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileno() {
            return mobileno;
        }

        public void setMobileno(String mobileno) {
            this.mobileno = mobileno;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public List<Service> getServices() {
            return services;
        }

        public void setServices(List<Service> services) {
            this.services = services;
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
        @SerializedName("service_image")
        @Expose
        private List<String> serviceImage = null;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("ratings")
        @Expose
        private String ratings;
        @SerializedName("rating_count")
        @Expose
        private String rating_count;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("views")
        @Expose
        private String views;
        @SerializedName("service_offered")
        @Expose
        private String service_offered;

        public String getRating_count() {
            return rating_count;
        }

        public void setRating_count(String rating_count) {
            this.rating_count = rating_count;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
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

        public List<String> getServiceImage() {
            return serviceImage;
        }

        public void setServiceImage(List<String> serviceImage) {
            this.serviceImage = serviceImage;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
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

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getService_offered() {
            return service_offered;
        }

        public void setService_offered(String service_offered) {
            this.service_offered = service_offered;
        }
    }

    public class Service {

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
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("ratings")
        @Expose
        private String ratings;
        @SerializedName("rating_count")
        @Expose
        private String ratingCount;
        @SerializedName("views")
        @Expose
        private String views;
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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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

        public String getRatingCount() {
            return ratingCount;
        }

        public void setRatingCount(String ratingCount) {
            this.ratingCount = ratingCount;
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

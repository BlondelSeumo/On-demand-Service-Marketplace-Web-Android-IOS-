package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.dreamguys.truelysell.datamodel.BaseResponse;

public class DAOBookingDetail extends BaseResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class BookingDetails {

        @SerializedName("booking_id")
        @Expose
        private String bookingId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("provider_id")
        @Expose
        private String providerId;
        @SerializedName("service_date")
        @Expose
        private String serviceDate;
        @SerializedName("from_time")
        @Expose
        private String fromTime;
        @SerializedName("to_time")
        @Expose
        private String toTime;
        @SerializedName("currency_code")
        @Expose
        private String currencyCode;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("user_rejected_reason")
        @Expose
        private String user_rejected_reason;
        @SerializedName("admin_comments")
        @Expose
        private String admin_comments;

        public String getBookingId() {
            return bookingId;
        }

        public void setBookingId(String bookingId) {
            this.bookingId = bookingId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public String getServiceDate() {
            return serviceDate;
        }

        public void setServiceDate(String serviceDate) {
            this.serviceDate = serviceDate;
        }

        public String getFromTime() {
            return fromTime;
        }

        public void setFromTime(String fromTime) {
            this.fromTime = fromTime;
        }

        public String getToTime() {
            return toTime;
        }

        public void setToTime(String toTime) {
            this.toTime = toTime;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_rejected_reason() {
            return user_rejected_reason;
        }

        public void setUser_rejected_reason(String user_rejected_reason) {
            this.user_rejected_reason = user_rejected_reason;
        }

        public String getAdmin_comments() {
            return admin_comments;
        }

        public void setAdmin_comments(String admin_comments) {
            this.admin_comments = admin_comments;
        }
    }

    public class Data {

        @SerializedName("booking_details")
        @Expose
        private BookingDetails bookingDetails;
        @SerializedName("service_details")
        @Expose
        private ServiceDetails serviceDetails;
        @SerializedName("personal_details")
        @Expose
        private UserDetails userDetails;

        public BookingDetails getBookingDetails() {
            return bookingDetails;
        }

        public void setBookingDetails(BookingDetails bookingDetails) {
            this.bookingDetails = bookingDetails;
        }

        public ServiceDetails getServiceDetails() {
            return serviceDetails;
        }

        public void setServiceDetails(ServiceDetails serviceDetails) {
            this.serviceDetails = serviceDetails;
        }

        public UserDetails getUserDetails() {
            return userDetails;
        }

        public void setUserDetails(UserDetails userDetails) {
            this.userDetails = userDetails;
        }

    }

    public class ServiceDetails {

        @SerializedName("service_id")
        @Expose
        private String service_id;
        @SerializedName("service_title")
        @Expose
        private String serviceTitle;
        @SerializedName("service_amount")
        @Expose
        private String serviceAmount;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("service_offered")
        @Expose
        private String serviceOffered;
        @SerializedName("service_location")
        @Expose
        private String serviceLocation;
        @SerializedName("service_latitude")
        @Expose
        private String serviceLatitude;
        @SerializedName("service_longitude")
        @Expose
        private String serviceLongitude;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("subcategory_name")
        @Expose
        private String subcategoryName;
        @SerializedName("service_image")
        @Expose
        private List<String> serviceImage = null;
        @SerializedName("total_views")
        @Expose
        private String totalViews;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("rating_count")
        @Expose
        private String ratingCount;
        @SerializedName("currency_code")
        @Expose
        private String currency_code;
        @SerializedName("is_rated")
        @Expose
        private String is_rated;

        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        public String getIs_rated() {
            return is_rated;
        }

        public void setIs_rated(String is_rated) {
            this.is_rated = is_rated;
        }

        public String getCurrency_code() {
            return currency_code;
        }

        public void setCurrency_code(String currency_code) {
            this.currency_code = currency_code;
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

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
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

        public String getServiceLatitude() {
            return serviceLatitude;
        }

        public void setServiceLatitude(String serviceLatitude) {
            this.serviceLatitude = serviceLatitude;
        }

        public String getServiceLongitude() {
            return serviceLongitude;
        }

        public void setServiceLongitude(String serviceLongitude) {
            this.serviceLongitude = serviceLongitude;
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

        public List<String> getServiceImage() {
            return serviceImage;
        }

        public void setServiceImage(List<String> serviceImage) {
            this.serviceImage = serviceImage;
        }

        public String getTotalViews() {
            return totalViews;
        }

        public void setTotalViews(String totalViews) {
            this.totalViews = totalViews;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getRatingCount() {
            return ratingCount;
        }

        public void setRatingCount(String ratingCount) {
            this.ratingCount = ratingCount;
        }

    }

    public class UserDetails {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("mobileno")
        @Expose
        private String mobileno;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("country_code")
        @Expose
        private String country_code;
        @SerializedName("token")
        @Expose
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobileno() {
            return mobileno;
        }

        public void setMobileno(String mobileno) {
            this.mobileno = mobileno;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

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

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

    }

}

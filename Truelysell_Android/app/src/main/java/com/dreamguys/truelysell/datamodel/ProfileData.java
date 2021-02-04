package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileData extends BaseResponse {

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

        @SerializedName("profile_details")
        @Expose
        private ProfileDetails profileDetails;

        public ProfileDetails getProfileDetails() {
            return profileDetails;
        }

        public void setProfileDetails(ProfileDetails profileDetails) {
            this.profileDetails = profileDetails;
        }

    }

    public class ProfileDetails {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("full_name")
        @Expose
        private String fullName;
        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("ic_card_image")
        @Expose
        private String icCardImage;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("mobile_no")
        @Expose
        private String mobileNo;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("is_active")
        @Expose
        private String isActive;
        @SerializedName("verified")
        @Expose
        private String verified;
        @SerializedName("unique_code")
        @Expose
        private String uniqueCode;
        @SerializedName("last_login")
        @Expose
        private String lastLogin;
        @SerializedName("token_valid")
        @Expose
        private String tokenValid;
        @SerializedName("tokenid")
        @Expose
        private String tokenid;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("register_through")
        @Expose
        private String registerThrough;
        @SerializedName("subscription_details")
        @Expose
        private SubscriptionDetails subscriptionDetails;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

        public String getIcCardImage() {
            return icCardImage;
        }

        public void setIcCardImage(String icCardImage) {
            this.icCardImage = icCardImage;
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

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public String getVerified() {
            return verified;
        }

        public void setVerified(String verified) {
            this.verified = verified;
        }

        public String getUniqueCode() {
            return uniqueCode;
        }

        public void setUniqueCode(String uniqueCode) {
            this.uniqueCode = uniqueCode;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

        public String getTokenValid() {
            return tokenValid;
        }

        public void setTokenValid(String tokenValid) {
            this.tokenValid = tokenValid;
        }

        public String getTokenid() {
            return tokenid;
        }

        public void setTokenid(String tokenid) {
            this.tokenid = tokenid;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getRegisterThrough() {
            return registerThrough;
        }

        public void setRegisterThrough(String registerThrough) {
            this.registerThrough = registerThrough;
        }

        public SubscriptionDetails getSubscriptionDetails() {
            return subscriptionDetails;
        }

        public void setSubscriptionDetails(SubscriptionDetails subscriptionDetails) {
            this.subscriptionDetails = subscriptionDetails;
        }
    }

    public class SubscriptionDetails {

        @SerializedName("expiry_date_time")
        @Expose
        private String expiryDateTime;
        @SerializedName("subscription_name")
        @Expose
        private String subscriptionName;

        public String getExpiryDateTime() {
            return expiryDateTime;
        }

        public void setExpiryDateTime(String expiryDateTime) {
            this.expiryDateTime = expiryDateTime;
        }

        public String getSubscriptionName() {
            return subscriptionName;
        }

        public void setSubscriptionName(String subscriptionName) {
            this.subscriptionName = subscriptionName;
        }

    }
}
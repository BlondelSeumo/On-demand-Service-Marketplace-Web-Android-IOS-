package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData extends BaseResponse {
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

        @SerializedName("user_details")
        @Expose
        private UserDetails userDetails;

        public UserDetails getUserDetails() {
            return userDetails;
        }

        public void setUserDetails(UserDetails userDetails) {
            this.userDetails = userDetails;
        }

    }

    public class UserDetails {

        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("subscribed_user")
        @Expose
        private Integer subscribedUser;
        @SerializedName("subscribed_msg")
        @Expose
        private String subscribedMsg;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile_no")
        @Expose
        private String mobileNo;
        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("role")
        @Expose
        private String role;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Integer getSubscribedUser() {
            return subscribedUser;
        }

        public void setSubscribedUser(Integer subscribedUser) {
            this.subscribedUser = subscribedUser;
        }

        public String getSubscribedMsg() {
            return subscribedMsg;
        }

        public void setSubscribedMsg(String subscribedMsg) {
            this.subscribedMsg = subscribedMsg;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}

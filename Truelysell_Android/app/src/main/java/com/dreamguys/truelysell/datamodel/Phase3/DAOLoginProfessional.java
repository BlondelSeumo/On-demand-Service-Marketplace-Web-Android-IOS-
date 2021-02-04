package com.dreamguys.truelysell.datamodel.Phase3;

import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DAOLoginProfessional extends BaseResponse {

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

        @SerializedName("provider_details")
        @Expose
        private ProviderDetails providerDetails;

        @SerializedName("user_details")
        @Expose
        private ProviderDetails providerDetails1;

        public ProviderDetails getProviderDetails() {
            return providerDetails;
        }

        public void setProviderDetails(ProviderDetails providerDetails) {
            this.providerDetails = providerDetails;
        }

        public ProviderDetails getProviderDetails1() {
            return providerDetails1;
        }

        public void setProviderDetails1(ProviderDetails providerDetails) {
            this.providerDetails1 = providerDetails;
        }

    }

    public class ProviderDetails {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobileno")
        @Expose
        private String mobileno;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("subcategory")
        @Expose
        private String subcategory;
        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("is_subscribed")
        @Expose
        private String is_subscribed;
        @SerializedName("share_code")
        @Expose
        private String shareCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShareCode() {
            return shareCode;
        }

        public void setShareCode(String shareCode) {
            this.shareCode = shareCode;
        }

        public String getIs_subscribed() {
            return is_subscribed;
        }

        public void setIs_subscribed(String is_subscribed) {
            this.is_subscribed = is_subscribed;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }

}

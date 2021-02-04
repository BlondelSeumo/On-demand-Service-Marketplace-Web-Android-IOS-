package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.dreamguys.truelysell.datamodel.BaseResponse;

public class DAOAccountDetails extends BaseResponse {

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
        @SerializedName("country_code")
        @Expose
        private String countryCode;
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
        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("account_holder_name")
        @Expose
        private String accountHolderName;
        @SerializedName("account_number")
        @Expose
        private String accountNumber;
        @SerializedName("account_iban")
        @Expose
        private String accountIban;
        @SerializedName("bank_name")
        @Expose
        private String bankName;
        @SerializedName("bank_address")
        @Expose
        private String bankAddress;
        @SerializedName("sort_code")
        @Expose
        private String sortCode;
        @SerializedName("routing_number")
        @Expose
        private String routingNumber;
        @SerializedName("account_ifsc")
        @Expose
        private String accountIfsc;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("delete_status")
        @Expose
        private String deleteStatus;
        @SerializedName("usertype")
        @Expose
        private String usertype;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("last_login")
        @Expose
        private String lastLogin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
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

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getAccountHolderName() {
            return accountHolderName;
        }

        public void setAccountHolderName(String accountHolderName) {
            this.accountHolderName = accountHolderName;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountIban() {
            return accountIban;
        }

        public void setAccountIban(String accountIban) {
            this.accountIban = accountIban;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankAddress() {
            return bankAddress;
        }

        public void setBankAddress(String bankAddress) {
            this.bankAddress = bankAddress;
        }

        public String getSortCode() {
            return sortCode;
        }

        public void setSortCode(String sortCode) {
            this.sortCode = sortCode;
        }

        public String getRoutingNumber() {
            return routingNumber;
        }

        public void setRoutingNumber(String routingNumber) {
            this.routingNumber = routingNumber;
        }

        public String getAccountIfsc() {
            return accountIfsc;
        }

        public void setAccountIfsc(String accountIfsc) {
            this.accountIfsc = accountIfsc;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDeleteStatus() {
            return deleteStatus;
        }

        public void setDeleteStatus(String deleteStatus) {
            this.deleteStatus = deleteStatus;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

    }

}

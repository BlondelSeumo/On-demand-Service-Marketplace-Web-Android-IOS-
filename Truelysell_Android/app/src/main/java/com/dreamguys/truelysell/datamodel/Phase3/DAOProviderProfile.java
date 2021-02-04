package com.dreamguys.truelysell.datamodel.Phase3;

import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DAOProviderProfile extends BaseResponse {

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
        @SerializedName("country_code")
        @Expose
        private String countryCode;
        @SerializedName("mobileno")
        @Expose
        private String mobileno;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("subcategory")
        @Expose
        private String subcategory;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("subcategory_name")
        @Expose
        private String subcategoryName;
        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("currency_code")
        @Expose
        private String currencyCode;
        @SerializedName("subscription_details")
        @Expose
        private SubscriptionDetails subscriptionDetails;

        @SerializedName("stripe_details")
        @Expose
        private StripeDetails stripeDetails;

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

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
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

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
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

        public SubscriptionDetails getSubscriptionDetails() {
            return subscriptionDetails;
        }

        public void setSubscriptionDetails(SubscriptionDetails subscriptionDetails) {
            this.subscriptionDetails = subscriptionDetails;
        }

        public StripeDetails getStripeDetails() {
            return stripeDetails;
        }

        public void setStripeDetails(StripeDetails stripeDetails) {
            this.stripeDetails = stripeDetails;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
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


    public class StripeDetails{
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
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
        @SerializedName("paypal_account")
        @Expose
        private String paypalAccount;
        @SerializedName("paypal_email_id")
        @Expose
        private String paypalEmailId;
        @SerializedName("pancard_no")
        @Expose
        private String pancardNo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getPaypalAccount() {
            return paypalAccount;
        }

        public void setPaypalAccount(String paypalAccount) {
            this.paypalAccount = paypalAccount;
        }

        public String getPaypalEmailId() {
            return paypalEmailId;
        }

        public void setPaypalEmailId(String paypalEmailId) {
            this.paypalEmailId = paypalEmailId;
        }

        public String getPancardNo() {
            return pancardNo;
        }

        public void setPancardNo(String pancardNo) {
            this.pancardNo = pancardNo;
        }
    }

}

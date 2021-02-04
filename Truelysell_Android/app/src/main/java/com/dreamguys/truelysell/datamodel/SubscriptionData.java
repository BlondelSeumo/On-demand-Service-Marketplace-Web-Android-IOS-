package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubscriptionData extends BaseResponse {

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

        @SerializedName("subscription_list")
        @Expose
        private ArrayList<SubscriptionList> subscriptionList = null;

        public ArrayList<SubscriptionList> getSubscriptionList() {
            return subscriptionList;
        }

        public void setSubscriptionList(ArrayList<SubscriptionList> subscriptionList) {
            this.subscriptionList = subscriptionList;
        }

    }

    public class SubscriptionList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("subscription_name")
        @Expose
        private String subscriptionName;
        @SerializedName("fee")
        @Expose
        private String fee;
        @SerializedName("currency_code")
        @Expose
        private String currencyCode;
        @SerializedName("duration")
        @Expose
        private String duration;
        @SerializedName("fee_description")
        @Expose
        private String feeDescription;
        @SerializedName("status")
        @Expose
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubscriptionName() {
            return subscriptionName;
        }

        public void setSubscriptionName(String subscriptionName) {
            this.subscriptionName = subscriptionName;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getFeeDescription() {
            return feeDescription;
        }

        public void setFeeDescription(String feeDescription) {
            this.feeDescription = feeDescription;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
}

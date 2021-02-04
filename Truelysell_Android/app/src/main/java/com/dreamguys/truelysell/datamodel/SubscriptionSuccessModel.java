package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscriptionSuccessModel extends BaseResponse {

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
        @SerializedName("subscription_id")
        @Expose
        private String subscriptionId;
        @SerializedName("subscriber_id")
        @Expose
        private String subscriberId;
        @SerializedName("subscription_date")
        @Expose
        private String subscriptionDate;
        @SerializedName("expiry_date_time")
        @Expose
        private String expiryDateTime;
        @SerializedName("is_subscribed")
        @Expose
        private String is_subscribed;

        public String getIs_subscribed() {
            return is_subscribed;
        }

        public void setIs_subscribed(String is_subscribed) {
            this.is_subscribed = is_subscribed;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubscriptionId() {
            return subscriptionId;
        }

        public void setSubscriptionId(String subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        public String getSubscriberId() {
            return subscriberId;
        }

        public void setSubscriberId(String subscriberId) {
            this.subscriberId = subscriberId;
        }

        public String getSubscriptionDate() {
            return subscriptionDate;
        }

        public void setSubscriptionDate(String subscriptionDate) {
            this.subscriptionDate = subscriptionDate;
        }

        public String getExpiryDateTime() {
            return expiryDateTime;
        }

        public void setExpiryDateTime(String expiryDateTime) {
            this.expiryDateTime = expiryDateTime;
        }

    }

}

package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hari on 13-12-2018.
 */

public class StripeDetailsModel extends BaseResponse {

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

        @SerializedName("publishable_key")
        @Expose
        private String publishable_key;
        @SerializedName("secret_key")
        @Expose
        private String secret_key;
        @SerializedName("razorpay_apikey")
        @Expose
        private String razorpayApikey;
        @SerializedName("razorpay_secret_key")
        @Expose
        private String razorpaySecretKey;
        @SerializedName("braintree_key")
        @Expose
        private String braintreeKey;
        @SerializedName("paypal_option")
        @Expose
        private String paypalOption;
        @SerializedName("stripe_option")
        @Expose
        private String stripeOption;
        @SerializedName("razor_option")
        @Expose
        private String razorOption;

        public String getPaypalOption() {
            return paypalOption;
        }

        public void setPaypalOption(String paypalOption) {
            this.paypalOption = paypalOption;
        }

        public String getStripeOption() {
            return stripeOption;
        }

        public void setStripeOption(String stripeOption) {
            this.stripeOption = stripeOption;
        }

        public String getRazorOption() {
            return razorOption;
        }

        public void setRazorOption(String razorOption) {
            this.razorOption = razorOption;
        }

        public String getRazorpayApikey() {
            return razorpayApikey;
        }

        public void setRazorpayApikey(String razorpayApikey) {
            this.razorpayApikey = razorpayApikey;
        }

        public String getRazorpaySecretKey() {
            return razorpaySecretKey;
        }

        public void setRazorpaySecretKey(String razorpaySecretKey) {
            this.razorpaySecretKey = razorpaySecretKey;
        }

        public String getBraintreeKey() {
            return braintreeKey;
        }

        public void setBraintreeKey(String braintreeKey) {
            this.braintreeKey = braintreeKey;
        }

        public String getPublishable_key() {
            return publishable_key;
        }

        public void setPublishable_key(String publishable_key) {
            this.publishable_key = publishable_key;
        }

        public String getSecret_key() {
            return secret_key;
        }

        public void setSecret_key(String secret_key) {
            this.secret_key = secret_key;
        }

    }
}

package com.dreamguys.truelysell.datamodel.Phase3;

import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaypalResponseToken extends BaseResponse {

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

        @SerializedName("braintree_key")
        @Expose
        private String braintreeKey;

        public String getBraintreeKey() {
            return braintreeKey;
        }

        public void setBraintreeKey(String braintreeKey) {
            this.braintreeKey = braintreeKey;
        }
    }
}

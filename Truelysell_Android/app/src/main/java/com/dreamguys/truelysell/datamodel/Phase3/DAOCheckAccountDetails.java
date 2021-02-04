package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.dreamguys.truelysell.BaseActivity;
import com.dreamguys.truelysell.datamodel.BaseResponse;

public class DAOCheckAccountDetails extends BaseResponse {

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

        @SerializedName("account_details")
        @Expose
        private String accountDetails;
        @SerializedName("availability_details")
        @Expose
        private String availabilityDetails;

        public String getAccountDetails() {
            return accountDetails;
        }

        public void setAccountDetails(String accountDetails) {
            this.accountDetails = accountDetails;
        }

        public String getAvailabilityDetails() {
            return availabilityDetails;
        }

        public void setAvailabilityDetails(String availabilityDetails) {
            this.availabilityDetails = availabilityDetails;
        }

    }

}

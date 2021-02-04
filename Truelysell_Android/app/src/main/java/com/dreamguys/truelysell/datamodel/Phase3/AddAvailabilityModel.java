package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.SerializedName;

import com.dreamguys.truelysell.datamodel.BaseResponse;

public class AddAvailabilityModel extends BaseResponse {

    @SerializedName("data")
    private Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data {

        @SerializedName("availability")
        private String mAvailability;
        @SerializedName("id")
        private String mId;
        @SerializedName("provider_id")
        private String mProviderId;

        public String getAvailability() {
            return mAvailability;
        }

        public void setAvailability(String availability) {
            mAvailability = availability;
        }

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getProviderId() {
            return mProviderId;
        }

        public void setProviderId(String providerId) {
            mProviderId = providerId;
        }

    }

}
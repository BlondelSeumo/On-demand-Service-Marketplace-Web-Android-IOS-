package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.dreamguys.truelysell.datamodel.BaseResponse;

public class DAOProviderDashboard extends BaseResponse {

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

        @SerializedName("service_count")
        @Expose
        private String serviceCount;
        @SerializedName("pending_service_count")
        @Expose
        private String pendingServiceCount;
        @SerializedName("inprogress_service_count")
        @Expose
        private String inprogressServiceCount;
        @SerializedName("complete_service_count")
        @Expose
        private String completeServiceCount;

        public String getServiceCount() {
            return serviceCount;
        }

        public void setServiceCount(String serviceCount) {
            this.serviceCount = serviceCount;
        }

        public String getPendingServiceCount() {
            return pendingServiceCount;
        }

        public void setPendingServiceCount(String pendingServiceCount) {
            this.pendingServiceCount = pendingServiceCount;
        }

        public String getInprogressServiceCount() {
            return inprogressServiceCount;
        }

        public void setInprogressServiceCount(String inprogressServiceCount) {
            this.inprogressServiceCount = inprogressServiceCount;
        }

        public String getCompleteServiceCount() {
            return completeServiceCount;
        }

        public void setCompleteServiceCount(String completeServiceCount) {
            this.completeServiceCount = completeServiceCount;
        }

    }

}

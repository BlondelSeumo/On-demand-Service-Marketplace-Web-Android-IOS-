package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProviderAvailableTimings extends BaseResponse {

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

        @SerializedName("availability_details")
        @Expose
        private List<Availability_detail> availability_details = null;

        public List<Availability_detail> getAvailability_details() {
            return availability_details;
        }

        public void setAvailability_details(List<Availability_detail> availability_details) {
            this.availability_details = availability_details;
        }

    }

    public class Availability_detail {

        @SerializedName("service_date")
        @Expose
        private String service_date;
        @SerializedName("service_day")
        @Expose
        private String service_day;
        @SerializedName("service_time")
        @Expose
        private String service_time;
        @SerializedName("is_selected")
        @Expose
        private String is_selected;

        public String getService_date() {
            return service_date;
        }

        public void setService_date(String service_date) {
            this.service_date = service_date;
        }

        public String getService_day() {
            return service_day;
        }

        public void setService_day(String service_day) {
            this.service_day = service_day;
        }

        public String getService_time() {
            return service_time;
        }

        public void setService_time(String service_time) {
            this.service_time = service_time;
        }

        public String getIs_selected() {
            return is_selected;
        }

        public void setIs_selected(String is_selected) {
            this.is_selected = is_selected;
        }

    }
}

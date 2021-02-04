package com.dreamguys.truelysell.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProviderHistoryModel extends BaseResponse {
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

        @SerializedName("booking_list")
        @Expose
        private List<Booking_list> booking_list = null;

        public List<Booking_list> getBooking_list() {
            return booking_list;
        }

        public void setBooking_list(List<Booking_list> booking_list) {
            this.booking_list = booking_list;
        }

    }

    public static class Booking_list implements Parcelable {

        @SerializedName("service_date")
        @Expose
        private String service_date;
        @SerializedName("service_time")
        @Expose
        private String service_time;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("contact_number")
        @Expose
        private String contact_number;
        @SerializedName("full_name")
        @Expose
        private String full_name;
        @SerializedName("profile_img")
        @Expose
        private String profile_img;
        @SerializedName("service_status")
        @Expose
        private String service_status;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("provider_id")
        @Expose
        private String provider_id;

        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("is_rate")
        @Expose
        private String is_rate;


        public Booking_list() {
            service_date = null;
            service_time = null;
            notes = null;
            latitude = null;
            longitude = null;
            title = null;
            contact_number = null;
            full_name = null;
            profile_img = null;
            service_status = null;
            id = null;
            provider_id = null;
            email = null;
            is_rate = null;


        }

        Booking_list(Parcel in) {
            service_date = in.readString();
            service_time = in.readString();
            notes = in.readString();
            latitude = in.readString();
            longitude = in.readString();
            title = in.readString();
            contact_number = in.readString();
            full_name = in.readString();
            profile_img = in.readString();
            service_status = in.readString();
            id = in.readString();
            provider_id = in.readString();
            email = in.readString();
            is_rate = in.readString();
        }

        public static final Creator<Booking_list> CREATOR = new Creator<Booking_list>() {
            @Override
            public Booking_list createFromParcel(Parcel in) {
                return new Booking_list(in);
            }

            @Override
            public Booking_list[] newArray(int size) {
                return new Booking_list[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(service_date);
            dest.writeString(service_time);
            dest.writeString(notes);
            dest.writeString(latitude);
            dest.writeString(longitude);
            dest.writeString(title);
            dest.writeString(contact_number);
            dest.writeString(full_name);
            dest.writeString(profile_img);
            dest.writeString(service_status);
            dest.writeString(id);
            dest.writeString(provider_id);
            dest.writeString(email);
            dest.writeString(is_rate);

        }


        public String getService_date() {
            return service_date;
        }

        public void setService_date(String service_date) {
            this.service_date = service_date;
        }

        public String getService_time() {
            return service_time;
        }

        public void setService_time(String service_time) {
            this.service_time = service_time;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getProfile_img() {
            return profile_img;
        }

        public void setProfile_img(String profile_img) {
            this.profile_img = profile_img;
        }

        public String getService_status() {
            return service_status;
        }

        public void setService_status(String service_status) {
            this.service_status = service_status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProvider_id() {
            return provider_id;
        }

        public void setProvider_id(String provider_id) {
            this.provider_id = provider_id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getIs_rate() {
            return is_rate;
        }

        public void setIs_rate(String is_rate) {
            this.is_rate = is_rate;
        }
    }
}

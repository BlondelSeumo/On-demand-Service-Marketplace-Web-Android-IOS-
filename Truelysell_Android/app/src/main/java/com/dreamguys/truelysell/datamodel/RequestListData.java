package com.dreamguys.truelysell.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestListData extends BaseResponse {

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

        @SerializedName("next_page")
        @Expose
        private Integer nextPage = -1;
        @SerializedName("current_page")
        @Expose
        private String currentPage;
        @SerializedName("total_pages")
        @Expose
        private Integer totalPages;
        @SerializedName("request_list")
        @Expose
        private ArrayList<RequestList> requestList = null;

        public Integer getNextPage() {
            return nextPage;
        }

        public void setNextPage(Integer nextPage) {
            this.nextPage = nextPage;
        }

        public String getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public ArrayList<RequestList> getRequestList() {
            return requestList;
        }

        public void setRequestList(ArrayList<RequestList> requestList) {
            this.requestList = requestList;
        }

    }

    public static class RequestList implements Parcelable {

        @SerializedName("r_id")
        @Expose
        private String rId;
        @SerializedName("requester_id")
        @Expose
        private String requesterId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("request_date")
        @Expose
        private String requestDate;
        @SerializedName("request_time")
        @Expose
        private String requestTime;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("currency_code")
        @Expose
        private String currencyCode;
        @SerializedName("contact_number")
        @Expose
        private String contactNumber;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("profile_contact_no")
        @Expose
        private String profileContactNo;

        public RequestList() {
            rId = null;
            requesterId = null;
            title = null;
            description = null;
            location = null;
            requestDate = null;
            requestTime = null;
            amount = null;
            currencyCode = null;
            contactNumber = null;
            latitude = null;
            longitude = null;
            status = null;
            username = null;
            email = null;
            profileImg = null;
            profileContactNo = null;
        }

        protected RequestList(Parcel in) {
            rId = in.readString();
            requesterId = in.readString();
            title = in.readString();
            description = in.readString();
            location = in.readString();
            requestDate = in.readString();
            requestTime = in.readString();
            amount = in.readString();
            currencyCode = in.readString();
            contactNumber = in.readString();
            latitude = in.readString();
            longitude = in.readString();
            status = in.readString();
            username = in.readString();
            email = in.readString();
            profileImg = in.readString();
            profileContactNo = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(rId);
            dest.writeString(requesterId);
            dest.writeString(title);
            dest.writeString(description);
            dest.writeString(location);
            dest.writeString(requestDate);
            dest.writeString(requestTime);
            dest.writeString(amount);
            dest.writeString(currencyCode);
            dest.writeString(contactNumber);
            dest.writeString(latitude);
            dest.writeString(longitude);
            dest.writeString(status);
            dest.writeString(username);
            dest.writeString(email);
            dest.writeString(profileImg);
            dest.writeString(profileContactNo);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<RequestList> CREATOR = new Creator<RequestList>() {
            @Override
            public RequestList createFromParcel(Parcel in) {
                return new RequestList(in);
            }

            @Override
            public RequestList[] newArray(int size) {
                return new RequestList[size];
            }
        };

        public String getRId() {
            return rId;
        }

        public void setRId(String rId) {
            this.rId = rId;
        }

        public String getRequesterId() {
            return requesterId;
        }

        public void setRequesterId(String requesterId) {
            this.requesterId = requesterId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getRequestDate() {
            return requestDate;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public String getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(String requestTime) {
            this.requestTime = requestTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

        public String getProfileContactNo() {
            return profileContactNo;
        }

        public void setProfileContactNo(String profileContactNo) {
            this.profileContactNo = profileContactNo;
        }

    }
}

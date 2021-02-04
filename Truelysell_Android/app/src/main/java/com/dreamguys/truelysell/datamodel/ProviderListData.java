package com.dreamguys.truelysell.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProviderListData extends BaseResponse {

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
        private Integer nextPage;
        @SerializedName("current_page")
        @Expose
        private String currentPage;
        @SerializedName("total_pages")
        @Expose
        private Integer totalPages;
        @SerializedName("provider_list")
        @Expose
        private ArrayList<ProviderList> providerList = null;

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

        public ArrayList<ProviderList> getProviderList() {
            return providerList;
        }

        public void setProviderList(ArrayList<ProviderList> providerList) {
            this.providerList = providerList;
        }
    }

    public static class ProviderList implements Parcelable {

        @SerializedName("p_id")
        @Expose
        private String pId;
        @SerializedName("provider_id")
        @Expose
        private String providerId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("start_date")
        @Expose
        private String startDate;
        @SerializedName("end_date")
        @Expose
        private String endDate;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("description_details")
        @Expose
        private String descriptionDetails;
        @SerializedName("contact_number")
        @Expose
        private String contactNumber;
        @SerializedName("availability")
        @Expose
        private String availability;
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

        @SerializedName("category_name")
        @Expose
        private String category_name;

        @SerializedName("subcategory_name")
        @Expose
        private String subcategory_name;

        @SerializedName("views")
        @Expose
        private String views;

        @SerializedName("category")
        @Expose
        private String category;

        @SerializedName("subcategory")
        @Expose
        private String subcategory;

        @SerializedName("rating")
        @Expose
        private String rating;


        public ProviderList() {
            pId = null;
            providerId = null;
            title = null;
            startDate = null;
            endDate = null;
            location = null;
            descriptionDetails = null;
            contactNumber = null;
            availability = null;
            latitude = null;
            longitude = null;
            status = null;
            username = null;
            email = null;
            profileImg = null;
            profileContactNo = null;
            category_name = null;
            subcategory_name = null;
            category = null;
            subcategory = null;
            views = null;
            rating = null;
        }

        protected ProviderList(Parcel in) {
            pId = in.readString();
            providerId = in.readString();
            title = in.readString();
            startDate = in.readString();
            endDate = in.readString();
            location = in.readString();
            descriptionDetails = in.readString();
            contactNumber = in.readString();
            availability = in.readString();
            latitude = in.readString();
            longitude = in.readString();
            status = in.readString();
            username = in.readString();
            email = in.readString();
            profileImg = in.readString();
            profileContactNo = in.readString();
            category_name = in.readString();
            subcategory_name = in.readString();
            category = in.readString();
            subcategory = in.readString();
            views = in.readString();
            rating = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(pId);
            dest.writeString(providerId);
            dest.writeString(title);
            dest.writeString(startDate);
            dest.writeString(endDate);
            dest.writeString(location);
            dest.writeString(descriptionDetails);
            dest.writeString(contactNumber);
            dest.writeString(availability);
            dest.writeString(latitude);
            dest.writeString(longitude);
            dest.writeString(status);
            dest.writeString(username);
            dest.writeString(email);
            dest.writeString(profileImg);
            dest.writeString(profileContactNo);
            dest.writeString(category_name);
            dest.writeString(subcategory_name);
            dest.writeString(category);
            dest.writeString(subcategory);
            dest.writeString(views);
            dest.writeString(rating);


        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ProviderList> CREATOR = new Creator<ProviderList>() {
            @Override
            public ProviderList createFromParcel(Parcel in) {
                return new ProviderList(in);
            }

            @Override
            public ProviderList[] newArray(int size) {
                return new ProviderList[size];
            }
        };

        public String getPId() {
            return pId;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDescriptionDetails() {
            return descriptionDetails;
        }

        public void setDescriptionDetails(String descriptionDetails) {
            this.descriptionDetails = descriptionDetails;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getAvailability() {
            return availability;
        }

        public void setAvailability(String availability) {
            this.availability = availability;
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

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getSubcategory_name() {
            return subcategory_name;
        }

        public void setSubcategory_name(String subcategory_name) {
            this.subcategory_name = subcategory_name;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }


        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(String subcategory) {
            this.subcategory = subcategory;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }
    }
}

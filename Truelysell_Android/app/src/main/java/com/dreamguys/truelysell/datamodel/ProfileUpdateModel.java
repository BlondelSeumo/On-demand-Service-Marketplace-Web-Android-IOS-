package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileUpdateModel extends BaseResponse {
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

        @SerializedName("profile_details")
        @Expose
        private ProfileDetails profileDetails;

        public ProfileDetails getProfileDetails() {
            return profileDetails;
        }

        public void setProfileDetails(ProfileDetails profileDetails) {
            this.profileDetails = profileDetails;
        }

    }

    public class ProfileDetails {

        @SerializedName("mobile_no")
        @Expose
        private String mobileNo;
        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("ic_card_image")
        @Expose
        private String icCardImage;

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

        public String getIcCardImage() {
            return icCardImage;
        }

        public void setIcCardImage(String icCardImage) {
            this.icCardImage = icCardImage;
        }

    }

}

package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscriptionImageUploadModel extends BaseResponse {

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

        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("ic_card_image")
        @Expose
        private String icCardImage;

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

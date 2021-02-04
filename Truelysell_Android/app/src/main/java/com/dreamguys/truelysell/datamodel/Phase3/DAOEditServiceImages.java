package com.dreamguys.truelysell.datamodel.Phase3;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DAOEditServiceImages {

    @SerializedName("id")
    @Expose
    private String id;
//    @SerializedName("service_image")
//    @Expose
//    private String serviceImage;
//    @SerializedName("service_details_image")
//    @Expose
//    private String serviceDetailsImage;
//    @SerializedName("thumb_image")
//    @Expose
//    private String thumbImage;
    @SerializedName("mobile_image")
    @Expose
    private String mobileImage;
    @SerializedName("is_url")
    @Expose
    private String is_url;

    private Bitmap bitmapImage;

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getServiceImage() {
//        return serviceImage;
//    }

//    public void setServiceImage(String serviceImage) {
//        this.serviceImage = serviceImage;
//    }
//
//    public String getServiceDetailsImage() {
//        return serviceDetailsImage;
//    }
//
//    public void setServiceDetailsImage(String serviceDetailsImage) {
//        this.serviceDetailsImage = serviceDetailsImage;
//    }
//
//    public String getThumbImage() {
//        return thumbImage;
//    }
//
//    public void setThumbImage(String thumbImage) {
//        this.thumbImage = thumbImage;
//    }

    public String getMobileImage() {
        return mobileImage;
    }

    public void setMobileImage(String mobileImage) {
        this.mobileImage = mobileImage;
    }

    public String getIs_url() {
        return is_url;
    }

    public void setIs_url(String is_url) {
        this.is_url = is_url;
    }

//    public Bitmap getBitmapImage() {
//        return bitmapImage;
//    }
//
//    public void setBitmapImage(Bitmap bitmapImage) {
//        this.bitmapImage = bitmapImage;
//    }
}

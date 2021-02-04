package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.dreamguys.truelysell.datamodel.BaseResponse;

public class DAOServiceSubCategories extends BaseResponse {


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

        @SerializedName("subcategory_list")
        @Expose
        private List<SubcategoryList> subcategoryList = null;

        public List<SubcategoryList> getSubcategoryList() {
            return subcategoryList;
        }

        public void setSubcategoryList(List<SubcategoryList> subcategoryList) {
            this.subcategoryList = subcategoryList;
        }

    }

    public class SubcategoryList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("subcategory_name")
        @Expose
        private String subcategoryName;
        @SerializedName("subcategory_image")
        @Expose
        private String subcategoryImage;

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubcategoryName() {
            return subcategoryName;
        }

        public void setSubcategoryName(String subcategoryName) {
            this.subcategoryName = subcategoryName;
        }

        public String getSubcategoryImage() {
            return subcategoryImage;
        }

        public void setSubcategoryImage(String subcategoryImage) {
            this.subcategoryImage = subcategoryImage;
        }

    }
}

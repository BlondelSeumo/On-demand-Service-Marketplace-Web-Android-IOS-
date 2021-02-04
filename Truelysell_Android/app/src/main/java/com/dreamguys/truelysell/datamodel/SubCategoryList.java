package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoryList extends BaseResponse {

    public class Category_list {

        @SerializedName("subcategory_id")
        @Expose
        private String subcategory_id;
        @SerializedName("subcategory_name")
        @Expose
        private String subcategory_name;
        @SerializedName("subcategory_image")
        @Expose
        private String subcategory_image;
        @SerializedName("category_id")
        @Expose
        private String category_id;

        @SerializedName("isChecked")
        @Expose
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getSubcategory_id() {
            return subcategory_id;
        }

        public void setSubcategory_id(String subcategory_id) {
            this.subcategory_id = subcategory_id;
        }

        public String getSubcategory_name() {
            return subcategory_name;
        }

        public void setSubcategory_name(String subcategory_name) {
            this.subcategory_name = subcategory_name;
        }

        public String getSubcategory_image() {
            return subcategory_image;
        }

        public void setSubcategory_image(String subcategory_image) {
            this.subcategory_image = subcategory_image;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

    }

    public class Data {

        @SerializedName("category_list")
        @Expose
        private List<Category_list> category_list = null;

        public List<Category_list> getCategory_list() {
            return category_list;
        }

        public void setCategory_list(List<Category_list> category_list) {
            this.category_list = category_list;
        }

    }

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


}

package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryList extends BaseResponse {

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

    public class Category_list {

        @SerializedName("catrgory_id")
        @Expose
        private String catrgory_id;
        @SerializedName("category_name")
        @Expose
        private String category_name;
        @SerializedName("category_image")
        @Expose
        private String category_image;
        @SerializedName("is_subcategory")
        @Expose
        private String is_subcategory;

        @SerializedName("is_checked")
        @Expose
        private boolean is_checked;

        public boolean isIs_checked() {
            return is_checked;
        }

        public void setIs_checked(boolean is_checked) {
            this.is_checked = is_checked;
        }

        public String getCatrgory_id() {
            return catrgory_id;
        }

        public void setCatrgory_id(String catrgory_id) {
            this.catrgory_id = catrgory_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getCategory_image() {
            return category_image;
        }

        public void setCategory_image(String category_image) {
            this.category_image = category_image;
        }

        public String getIs_subcategory() {
            return is_subcategory;
        }

        public void setIs_subcategory(String is_subcategory) {
            this.is_subcategory = is_subcategory;
        }

    }
}

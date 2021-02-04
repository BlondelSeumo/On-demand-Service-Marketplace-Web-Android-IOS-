package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LanguageListResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("language_value")
        @Expose
        private String languageValue;
        @SerializedName("tag")
        @Expose
        private String tag;

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLanguageValue() {
            return languageValue;
        }

        public void setLanguageValue(String languageValue) {
            this.languageValue = languageValue;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        @Override
        public String toString() {
            return language;
        }
    }

}
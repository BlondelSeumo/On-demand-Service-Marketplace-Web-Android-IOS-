package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguageSubModel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("placeholder")
    @Expose
    private String placeholder;
    @SerializedName("validation1")
    @Expose
    private String validation1;
    @SerializedName("validation2")
    @Expose
    private String validation2;
    @SerializedName("validation3")
    @Expose
    private String validation3;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getValidation1() {
        return validation1;
    }

    public void setValidation1(String validation1) {
        this.validation1 = validation1;
    }

    public String getValidation2() {
        return validation2;
    }

    public void setValidation2(String validation2) {
        this.validation2 = validation2;
    }

    public String getValidation3() {
        return validation3;
    }

    public void setValidation3(String validation3) {
        this.validation3 = validation3;
    }
}

package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DAOCountryCode {

    private List<CountryCodes> countryCodes = new ArrayList<>();


    public List<CountryCodes> getCountryCodes() {
        return countryCodes;
    }

    public void setCountryCodes(List<CountryCodes> countryCodes) {
        this.countryCodes = countryCodes;
    }

    public class CountryCodes {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("callingCodes")
        @Expose
        private List<String> callingCodes = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getCallingCodes() {
            return callingCodes;
        }

        public void setCallingCodes(List<String> callingCodes) {
            this.callingCodes = callingCodes;
        }
    }


}

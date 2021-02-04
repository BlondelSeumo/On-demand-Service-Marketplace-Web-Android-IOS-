package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DAOCountryCodeList {


    private List<CountryCodes> countryCodes = null;

    public List<CountryCodes> getCountryCodes() {
        return countryCodes;
    }

    public void setCountryCodes(List<CountryCodes> countryCodes) {
        this.countryCodes = countryCodes;
    }

    public static class CountryCodes {
        String countryName;
        String countryCode;

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }
    }


}

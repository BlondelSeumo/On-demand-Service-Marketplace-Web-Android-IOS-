package com.dreamguys.truelysell.datamodel.Phase3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.dreamguys.truelysell.datamodel.BaseResponse;

public class DAOWallet extends BaseResponse {

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

        @SerializedName("wallet_info")
        @Expose
        private WalletInfo walletInfo;

        public WalletInfo getWalletInfo() {
            return walletInfo;
        }

        public void setWalletInfo(WalletInfo walletInfo) {
            this.walletInfo = walletInfo;
        }

    }

    public class WalletInfo {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("wallet_amt")
        @Expose
        private String walletAmt;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("currency_code")
        @Expose
        private String currencycode;

        public String getCurrencycode() {
            return currencycode;
        }

        public void setCurrencycode(String currencycode) {
            this.currencycode = currencycode;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWalletAmt() {
            return walletAmt;
        }

        public void setWalletAmt(String walletAmt) {
            this.walletAmt = walletAmt;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

    }

}

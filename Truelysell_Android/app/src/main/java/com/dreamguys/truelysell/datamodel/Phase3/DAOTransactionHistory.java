package com.dreamguys.truelysell.datamodel.Phase3;

import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DAOTransactionHistory extends BaseResponse {


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
        @SerializedName("stripe_bank")
        @Expose
        private String stripeBank;

        public WalletInfo getWalletInfo() {
            return walletInfo;
        }

        public void setWalletInfo(WalletInfo walletInfo) {
            this.walletInfo = walletInfo;
        }

        public String getStripeBank() {
            return stripeBank;
        }

        public void setStripeBank(String stripeBank) {
            this.stripeBank = stripeBank;
        }

        public class WalletInfo {

            @SerializedName("wallet")
            @Expose
            private Wallet wallet;
            @SerializedName("wallet_history")
            @Expose
            private List<WalletHistory> walletHistory = null;

            public Wallet getWallet() {
                return wallet;
            }

            public void setWallet(Wallet wallet) {
                this.wallet = wallet;
            }

            public List<WalletHistory> getWalletHistory() {
                return walletHistory;
            }

            public void setWalletHistory(List<WalletHistory> walletHistory) {
                this.walletHistory = walletHistory;
            }

            public class WalletHistory {

                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("token")
                @Expose
                private String token;
                @SerializedName("user_provider_id")
                @Expose
                private String userProviderId;
                @SerializedName("type")
                @Expose
                private String type;
                @SerializedName("currency")
                @Expose
                private String currency;
                @SerializedName("current_wallet")
                @Expose
                private String currentWallet;
                @SerializedName("credit_wallet")
                @Expose
                private String creditWallet;
                @SerializedName("debit_wallet")
                @Expose
                private String debitWallet;
                @SerializedName("avail_wallet")
                @Expose
                private String availWallet;
                @SerializedName("total_amt")
                @Expose
                private String totalAmt;
                @SerializedName("txt_amt")
                @Expose
                private String txtAmt;
                @SerializedName("reason")
                @Expose
                private String reason;
                @SerializedName("created_at")
                @Expose
                private String createdAt;

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

                public String getUserProviderId() {
                    return userProviderId;
                }

                public void setUserProviderId(String userProviderId) {
                    this.userProviderId = userProviderId;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getCurrency() {
                    return currency;
                }

                public void setCurrency(String currency) {
                    this.currency = currency;
                }

                public String getCurrentWallet() {
                    return currentWallet;
                }

                public void setCurrentWallet(String currentWallet) {
                    this.currentWallet = currentWallet;
                }

                public String getCreditWallet() {
                    return creditWallet;
                }

                public void setCreditWallet(String creditWallet) {
                    this.creditWallet = creditWallet;
                }

                public String getDebitWallet() {
                    return debitWallet;
                }

                public void setDebitWallet(String debitWallet) {
                    this.debitWallet = debitWallet;
                }

                public String getAvailWallet() {
                    return availWallet;
                }

                public void setAvailWallet(String availWallet) {
                    this.availWallet = availWallet;
                }

                public String getTotalAmt() {
                    return totalAmt;
                }

                public void setTotalAmt(String totalAmt) {
                    this.totalAmt = totalAmt;
                }

                public String getTxtAmt() {
                    return txtAmt;
                }

                public void setTxtAmt(String txtAmt) {
                    this.txtAmt = txtAmt;
                }

                public String getReason() {
                    return reason;
                }

                public void setReason(String reason) {
                    this.reason = reason;
                }

                public String getCreatedAt() {
                    return createdAt;
                }

                public void setCreatedAt(String createdAt) {
                    this.createdAt = createdAt;
                }

            }

            public class Wallet {

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
                private String currencyCode;
                @SerializedName("total_credit")
                @Expose
                private String totalCredit;
                @SerializedName("total_debit")
                @Expose
                private Integer totalDebit;

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

                public String getCurrencyCode() {
                    return currencyCode;
                }

                public void setCurrencyCode(String currencyCode) {
                    this.currencyCode = currencyCode;
                }

                public String getTotalCredit() {
                    return totalCredit;
                }

                public void setTotalCredit(String totalCredit) {
                    this.totalCredit = totalCredit;
                }

                public Integer getTotalDebit() {
                    return totalDebit;
                }

                public void setTotalDebit(Integer totalDebit) {
                    this.totalDebit = totalDebit;
                }

            }
        }
    }
}

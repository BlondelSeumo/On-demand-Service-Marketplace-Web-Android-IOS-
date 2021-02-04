package com.dreamguys.truelysell.utils;

import android.content.Context;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.dreamguys.truelysell.datamodel.ChatDetailListData;
import com.dreamguys.truelysell.datamodel.DAOUpdateMyServiceStatus;
import com.dreamguys.truelysell.datamodel.EmptyData;
import com.dreamguys.truelysell.datamodel.LanguageListResponse;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.AddAvailabilityModel;
import com.dreamguys.truelysell.datamodel.Phase3.DAOAccountDetails;
import com.dreamguys.truelysell.datamodel.Phase3.DAOAvailableTimeSlots;
import com.dreamguys.truelysell.datamodel.Phase3.DAOBookingDetail;
import com.dreamguys.truelysell.datamodel.Phase3.DAOBookingList;
import com.dreamguys.truelysell.datamodel.Phase3.DAOCHATLIST;
import com.dreamguys.truelysell.datamodel.Phase3.DAOChatSentResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOCheckAccountDetails;
import com.dreamguys.truelysell.datamodel.Phase3.DAOGenerateOTP;
import com.dreamguys.truelysell.datamodel.Phase3.DAOLoginProfessional;
import com.dreamguys.truelysell.datamodel.Phase3.DAOMyRequests;
import com.dreamguys.truelysell.datamodel.Phase3.DAOMyServices;
import com.dreamguys.truelysell.datamodel.Phase3.DAOProviderDashboard;
import com.dreamguys.truelysell.datamodel.Phase3.DAOProviderProfile;
import com.dreamguys.truelysell.datamodel.Phase3.DAOSearchServices;
import com.dreamguys.truelysell.datamodel.Phase3.DAOServiceSubCategories;
import com.dreamguys.truelysell.datamodel.Phase3.DAOTransactionHistory;
import com.dreamguys.truelysell.datamodel.Phase3.DAOUserProfile;
import com.dreamguys.truelysell.datamodel.Phase3.DAOViewAllServices;
import com.dreamguys.truelysell.datamodel.Phase3.DAOWallet;
import com.dreamguys.truelysell.datamodel.Phase3.GETHomeList;
import com.dreamguys.truelysell.datamodel.Phase3.PaypalResponseToken;
import com.dreamguys.truelysell.datamodel.Phase3.ServiceCategories;
import com.dreamguys.truelysell.datamodel.ProviderAvailableTimings;
import com.dreamguys.truelysell.datamodel.ResponseHeader;
import com.dreamguys.truelysell.datamodel.StripeDetailsModel;
import com.dreamguys.truelysell.datamodel.SubscriptionData;
import com.dreamguys.truelysell.datamodel.SubscriptionSuccessModel;
import com.dreamguys.truelysell.fragments.phase3.GETServiceDetails;

import org.apache.http.HttpException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitHandler {

    private static ResponseHeader mBaseResponse = null;

    public static <T> void executeRetrofit(final Context mContext, Call<T> call, final String responeModel, final RetrofitResHandler retrofitResHandler, final boolean isLoadMore) {

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                ProgressDlg.dismissProgressDialog();
                switch (responeModel) {
                    case AppConstants.SUBSCRIPTION_DATA:
                        try {
                            SubscriptionData subsRes = (SubscriptionData) response.body();
                            mBaseResponse = subsRes.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.CHAT_DETAILLIST_DATA:
                        try {
                            ChatDetailListData chathistRes = (ChatDetailListData) response.body();
                            mBaseResponse = chathistRes.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;

                    case AppConstants.CHAT_SENDDETAILLIST_DATA:
                        try {
                            DAOChatSentResponse chathistRes = (DAOChatSentResponse) response.body();
                            mBaseResponse = chathistRes.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.SUBSCRIPTIONSUCCESS:
                        try {
                            SubscriptionSuccessModel subscriptionSuccessModel = (SubscriptionSuccessModel) response.body();
                            mBaseResponse = subscriptionSuccessModel.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.STRIPEDETAILS:
                        try {
                            StripeDetailsModel stripeDetailsModel = (StripeDetailsModel) response.body();
                            mBaseResponse = stripeDetailsModel.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;

                    case AppConstants.CATEGORIES:
                        try {
                            ServiceCategories categoryListModel = (ServiceCategories) response.body();
                            mBaseResponse = categoryListModel.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.SUBCATEGORIES:
                        try {
                            DAOServiceSubCategories subCategoryList = (DAOServiceSubCategories) response.body();
                            mBaseResponse = subCategoryList.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.SERVICECATEGORIES:
                        try {
                            ServiceCategories subCategoryList = (ServiceCategories) response.body();
                            mBaseResponse = subCategoryList.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.SERVICESUBCATEGORIES:
                        try {
                            DAOServiceSubCategories subCategoryList = (DAOServiceSubCategories) response.body();
                            mBaseResponse = subCategoryList.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.PROVIDERAVAIL:
                        try {
                            ProviderAvailableTimings subCategoryList = (ProviderAvailableTimings) response.body();
                            mBaseResponse = subCategoryList.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.HOMELIST:
                        try {
                            GETHomeList homeList = (GETHomeList) response.body();
                            mBaseResponse = homeList.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.PROFESSIONALLOGIN:
                        try {
                            DAOLoginProfessional daoLoginProfessional = (DAOLoginProfessional) response.body();
                            mBaseResponse = daoLoginProfessional.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.MYSERVICES:
                        try {
                            DAOMyServices daoMyServices = (DAOMyServices) response.body();
                            mBaseResponse = daoMyServices.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.PROFILE_DATA:
                        try {
                            DAOProviderProfile profRes = (DAOProviderProfile) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.MOBILEOTP:
                        try {
                            DAOGenerateOTP chatCount = (DAOGenerateOTP) response.body();
                            mBaseResponse = chatCount.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;

                    case AppConstants.GET_AVAILABILITY:
                        try {
                            AddAvailabilityModel profRes = (AddAvailabilityModel) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.ADD_AVAILABILITY:
                        try {
                            BaseResponse profRes = (BaseResponse) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.SERVICEDETAIL:
                        try {
                            GETServiceDetails profRes = (GETServiceDetails) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.AVAILABLETIMESLOTS:
                        try {
                            DAOAvailableTimeSlots profRes = (DAOAvailableTimeSlots) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.SEARCHSERVICES:
                        try {
                            DAOSearchServices profRes = (DAOSearchServices) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.VIEWALLSERVICES:
                        try {
                            DAOViewAllServices profRes = (DAOViewAllServices) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.BOOKINGLIST:
                        try {
                            DAOBookingList profRes = (DAOBookingList) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.USER_PROFILE_DATA:
                    case AppConstants.USER_PROFILE_UPDATE_DATA:
                        try {
                            DAOUserProfile profRes = (DAOUserProfile) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.MYREQUESTS:
                        try {
                            DAOMyRequests profRes = (DAOMyRequests) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.BOOKINGDETAIL:
                        try {
                            DAOBookingDetail profRes = (DAOBookingDetail) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.PROVIDERDASHBOARD:
                        try {
                            DAOProviderDashboard profRes = (DAOProviderDashboard) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case AppConstants.GETACCOUNTDETAILS:
                        try {
                            DAOAccountDetails profRes = (DAOAccountDetails) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.CHECKACCOUNTDETAILS:
                        try {
                            DAOCheckAccountDetails profRes = (DAOCheckAccountDetails) response.body();
                            mBaseResponse = profRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.CHAT_HISTORYLIST_DATA:
                        try {
                            DAOCHATLIST chathistRes = (DAOCHATLIST) response.body();
                            mBaseResponse = chathistRes.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.WALLETDETAILS:
                        try {
                            DAOWallet chathistRes = (DAOWallet) response.body();
                            mBaseResponse = chathistRes.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.TRANSACTIONLIST:
                        try {
                            DAOTransactionHistory chathistRes = (DAOTransactionHistory) response.body();
                            mBaseResponse = chathistRes.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;
                    case AppConstants.UPDATEMYSERVICESTATUS:
                        try {
                            DAOUpdateMyServiceStatus chathistRes = (DAOUpdateMyServiceStatus) response.body();
                            mBaseResponse = chathistRes.getResponseHeader();
                        } catch (Exception e) {
                        }
                        break;

                    case AppConstants.GETLANGUAGELIST:
                        try {
                            LanguageListResponse chathistRes = (LanguageListResponse) response.body();
                            mBaseResponse = chathistRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case AppConstants.GETLANGUAGEAPPDATA:
                        try {
                            LanguageResponse chathistRes = (LanguageResponse) response.body();
                            mBaseResponse = chathistRes.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case AppConstants.GETPAYPALTOKEN:
                        try {
                            PaypalResponseToken paypalResponseToken = (PaypalResponseToken) response.body();
                            mBaseResponse = paypalResponseToken.getResponseHeader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case AppConstants.BOOKINGSTATUS:
                    case AppConstants.SIGNUP_DATA:
                    case AppConstants.REQUEST_ACCEPT_DATA:
                    case AppConstants.CREATE_REQUEST_DATA:
                    case AppConstants.UPDATE_REQUEST_DATA:
                    case AppConstants.DELETE_REQUEST_DATA:
                    case AppConstants.CREATE_PROVIDER_DATA:
                    case AppConstants.UPDATE_PROVIDER_DATA:
                    case AppConstants.DELETE_PROVIDER_DATA:
                    case AppConstants.REQUEST_COMPLETE_DATA:
                    case AppConstants.CHANGEPASSWORD:
                    case AppConstants.FORGOT_PASSWORD:
                    case AppConstants.SUBS_SUCCESS_DATA:
                    case AppConstants.VIEWS:
                    case AppConstants.DELETESERVICE:
                    case AppConstants.PROFILE_UPDATE_DATA:
                    case AppConstants.UPDATE_SERVICE:
                    case AppConstants.DELETESERVICEIMAGES:
                    case AppConstants.BOOKSERVICE:
                    case AppConstants.UPDATETOKEN:
                    case AppConstants.TOPUPWALLET:
                    case AppConstants.WITHDRAWWALLET:
                        try {
                            EmptyData reqAccRes = (EmptyData) response.body();
                            mBaseResponse = reqAccRes.getResponseHeader();
                        } catch (Exception e) {

                        }
                        break;
                }

                if (response.isSuccessful()) {
                    if (mBaseResponse != null && mBaseResponse.getResponseCode() != null
                            && mBaseResponse.getResponseCode().equalsIgnoreCase("200")) {
                        retrofitResHandler.onSuccess(response.body(), isLoadMore, responeModel);
                    } else {
                        if (mBaseResponse.getResponseMessage() != null)
                            AppUtils.showToast(mContext, mBaseResponse.getResponseMessage());
                        retrofitResHandler.onResponseFailure(response.body(), isLoadMore, responeModel);
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                ProgressDlg.dismissProgressDialog();
                if (t instanceof UnknownHostException || t instanceof HttpException || t instanceof ConnectException || t instanceof SocketException || t instanceof SocketTimeoutException)
                    AppUtils.showToast(mContext, mContext.getString(R.string.txt_server_error));
                retrofitResHandler.onRequestFailure(null, isLoadMore, responeModel);
            }
        });
    }

    public interface RetrofitResHandler<T> {

        void onSuccess(T myRes, boolean isLoadMore, String responseType);

        //Always check myRes for null. OnFailure the myRes will be null.
        void onResponseFailure(T myRes, boolean isLoadMore, String responseType);

        void onRequestFailure(T myRes, boolean isLoadMore, String responseType);
    }
}

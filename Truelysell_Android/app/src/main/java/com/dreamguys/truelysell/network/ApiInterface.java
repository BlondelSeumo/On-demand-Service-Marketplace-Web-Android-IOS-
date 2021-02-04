package com.dreamguys.truelysell.network;

import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.dreamguys.truelysell.datamodel.CurrencyListResponse;
import com.dreamguys.truelysell.datamodel.DAOUpdateMyServiceStatus;
import com.dreamguys.truelysell.datamodel.EmptyData;
import com.dreamguys.truelysell.datamodel.GETReviewTypes;
import com.dreamguys.truelysell.datamodel.LanguageListResponse;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.LogoutData;
import com.dreamguys.truelysell.datamodel.POSTRatingsProvider;
import com.dreamguys.truelysell.datamodel.Phase3.AddAvailabilityModel;
import com.dreamguys.truelysell.datamodel.Phase3.DAOAccountDetails;
import com.dreamguys.truelysell.datamodel.Phase3.DAOAvailableTimeSlots;
import com.dreamguys.truelysell.datamodel.Phase3.DAOBookingDetail;
import com.dreamguys.truelysell.datamodel.Phase3.DAOBookingList;
import com.dreamguys.truelysell.datamodel.Phase3.DAOCHATLIST;
import com.dreamguys.truelysell.datamodel.Phase3.DAOChatDetails;
import com.dreamguys.truelysell.datamodel.Phase3.DAOChatSentResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOCheckAccountDetails;
import com.dreamguys.truelysell.datamodel.Phase3.DAOEditServiceDetails;
import com.dreamguys.truelysell.datamodel.Phase3.DAOGenerateOTP;
import com.dreamguys.truelysell.datamodel.Phase3.DAOLoginProfessional;
import com.dreamguys.truelysell.datamodel.Phase3.DAOMyRequests;
import com.dreamguys.truelysell.datamodel.Phase3.DAOMyServices;
import com.dreamguys.truelysell.datamodel.Phase3.DAOProviderDashboard;
import com.dreamguys.truelysell.datamodel.Phase3.DAOProviderProfile;
import com.dreamguys.truelysell.datamodel.Phase3.DAOSearchServices;
import com.dreamguys.truelysell.datamodel.Phase3.DAOServiceSubCategories;
import com.dreamguys.truelysell.datamodel.Phase3.DAOSubCategoryServices;
import com.dreamguys.truelysell.datamodel.Phase3.DAOTransactionHistory;
import com.dreamguys.truelysell.datamodel.Phase3.DAOUserProfile;
import com.dreamguys.truelysell.datamodel.Phase3.DAOViewAllServices;
import com.dreamguys.truelysell.datamodel.Phase3.DAOWallet;
import com.dreamguys.truelysell.datamodel.Phase3.GETHomeList;
import com.dreamguys.truelysell.datamodel.Phase3.LoginTypeResponse;
import com.dreamguys.truelysell.datamodel.Phase3.NotificationResponse;
import com.dreamguys.truelysell.datamodel.Phase3.PaypalResponseToken;
import com.dreamguys.truelysell.datamodel.Phase3.ServiceCategories;
import com.dreamguys.truelysell.datamodel.StripeDetailsModel;
import com.dreamguys.truelysell.datamodel.SubscriptionData;
import com.dreamguys.truelysell.datamodel.SubscriptionSuccessModel;
import com.dreamguys.truelysell.fragments.phase3.GETServiceDetails;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("api/api/subscription")
    Call<SubscriptionData> getSubsDetails(@Header("token") String token, @Header("language") String language);

    @FormUrlEncoded
    @POST("api/chat")
    Call<DAOChatSentResponse> postSendChatMessage(@Field("content") String content, @Field("to") String to,
                                                  @Header("token") String token, @Header("language") String language);

    @FormUrlEncoded
    @POST("api/subscription_success")
    Call<SubscriptionSuccessModel> postSuccessSubscription(@Field("subscription_id") String subscription_id,
                                                           @Field("transaction_id") String transaction_id,
                                                           @Field("payment_details") String payment_details,
                                                           @Header("token") String token,
                                                           @Header("language") String language);


    @GET("api/stripe_details")
    Call<StripeDetailsModel> getStripeDetails(@Header("token") String token);

    //Phase 3

    @FormUrlEncoded
     @POST("api/demo-home") //play store
//    @POST("api/home") //codecanyon
    Call<GETHomeList> getHomeList(@Header("token") String token, @Field("latitude") String latitude, @Field("longitude") String longitude);

    @GET("api/service-details")
    Call<GETServiceDetails> getServiceDetails(@Query("id") String id, @Header("token") String token);

    @GET("api/category")
    Call<ServiceCategories> getServiceCategories(@Header("token") String token);

    @FormUrlEncoded
    @POST("api/subcategory")
    Call<DAOServiceSubCategories> postServiceSubCategory(@Field("category") String category, @Header("token") String token);

    @GET("https://restcountries.eu/rest/v2/all")
    Call<JSONArray> getCountryCode();


    @FormUrlEncoded
    @POST("api/api/provider_signin")
    Call<DAOLoginProfessional> postProfessionalLogin(@FieldMap Map<String, String> params, @Header("token") String token);

    @FormUrlEncoded
    @POST("api/api/generate_otp_provider")
    Call<DAOGenerateOTP> postMobileOTP(@FieldMap Map<String, String> params, @Header("token") String token);

    @FormUrlEncoded
    @POST("api/api/user_signin")
    Call<DAOLoginProfessional> postUserLogin(@FieldMap Map<String, String> params, @Header("token") String token);

    @FormUrlEncoded
    @POST("api/api/generate_otp_user")
    Call<DAOGenerateOTP> postUserMobileOTP(@FieldMap Map<String, String> params, @Header("token") String token);


    @Multipart
    @POST("api/add_service")
    Call<EmptyData> postCreateService(@Part("service_title") RequestBody service_title,
                                      @Part("service_location") RequestBody service_location,
                                      @Part("category") RequestBody category,
                                      @Part("subcategory") RequestBody subcategory,
                                      @Part("service_latitude") RequestBody service_latitude,
                                      @Part("service_longitude") RequestBody service_longitude,
                                      @Part("service_amount") RequestBody service_amount,
                                      @Part("about") RequestBody about,
                                      @Part("service_offered") RequestBody service_offered,
                                      @Part List<MultipartBody.Part> files,
                                      @Header("token") String token);

    @FormUrlEncoded
    @POST("api/api/my_service")
    Call<DAOMyServices> getMyServices(@Header("token") String token, @Field("type") String type);

    @FormUrlEncoded
    @POST("api/delete_service")
    Call<EmptyData> postDeleteService(@Header("token") String token, @Field("id") String id);

    @GET("api/api/profile")
    Call<DAOProviderProfile> getProfileData(@Header("token") String token, @Header("language") String language);

    @Multipart
    @POST("api/api/update_provider")
    Call<DAOProviderProfile> postProfileUpdate(@Part("name") RequestBody RequestBody,
                                               @Part("category") RequestBody category,
                                               @Part("subcategory") RequestBody subcategory,
                                               @Part("user_currency") RequestBody currency,
                                               @Part MultipartBody.Part files,
                                               @Header("token") String token);

    @GET("api/edit_service")
    Call<DAOEditServiceDetails> getServiceInfo(@Query("id") String id, @Header("token") String token);

    @Multipart
    @POST("api/update_service")
    Call<EmptyData> updateService(@Part("id") RequestBody id,
                                  @Part("service_title") RequestBody service_title,
                                  @Part("service_location") RequestBody service_location,
                                  @Part("category") RequestBody category,
                                  @Part("subcategory") RequestBody subcategory,
                                  @Part("service_latitude") RequestBody service_latitude,
                                  @Part("service_longitude") RequestBody service_longitude,
                                  @Part("service_amount") RequestBody service_amount,
                                  @Part("about") RequestBody about,
                                  @Part("service_offered") RequestBody service_offered,
                                  @Part List<MultipartBody.Part> files,
                                  @Header("token") String token);

    @FormUrlEncoded
    @POST("api/delete_serviceimage")
    Call<EmptyData> postDeleteServiceImages(@Header("token") String token, @Field("id") String id, @Field("service_id") String service_id);

    @FormUrlEncoded
    @POST("api/all-services")
    Call<DAOViewAllServices> getViewAllServices(@Field("type") String type, @Header("token") String token, @Field("latitude") String latitude, @Field("longitude") String longitude);


    //Add availability
    @FormUrlEncoded
    @POST("api/update_availability")
    Call<BaseResponse> addAvailability(@Header("token") String token, @Field("availability") String availability);

    //Get Availability
    @GET("api/availability")
    Call<AddAvailabilityModel> getAvailability(@Header("token") String token);

    @FormUrlEncoded
    @POST("api/service_availability")
    Call<DAOAvailableTimeSlots> getAvailableTimeSlots(@Header("token") String token, @Field("service_id") String service_id, @Field("date") String date);

    @FormUrlEncoded
    @POST("api/book_service")
    Call<EmptyData> postBookService(@FieldMap Map<String, String> params, @Header("token") String token);

    @FormUrlEncoded
    @POST("api/api/search_services")
    Call<DAOSearchServices> searchServices(@Header("token") String token, @Field("text") String text, @Field("latitude") String latitude, @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("api/views")
    Call<EmptyData> updateViews(@Header("token") String token, @Field("service_id") String service_id);

    @FormUrlEncoded
    @POST("api/all-services")
    Call<DAOViewAllServices> viewAllServices(@Header("token") String token, @Field("service_id") String service_id);

    @FormUrlEncoded
    @POST("api/api/bookinglist")
    Call<DAOBookingList> bookingList(@Header("token") String token, @Field("type") String type, @Field("status") String status);

    @GET("api/api/user_profile")
    Call<DAOUserProfile> getUserProfileData(@Header("token") String token, @Header("language") String language);

    @Multipart
    @POST("api/api/update_user")
    Call<DAOUserProfile> postUpdateUserProfile(@Part("name") RequestBody RequestBody,
                                               @Part("type") RequestBody type,
                                               @Part("user_currency") RequestBody currency,
                                               @Part MultipartBody.Part files,
                                               @Header("token") String token);

    @GET("api/api/requestlist_provider")
    Call<DAOMyRequests> getMyBookingRequest(@Header("token") String token);

    @FormUrlEncoded
    @POST("api/logout")
    Call<LogoutData> postUserLogout(@Field("device_type") String device_type, @Field("deviceid") String device_id, @Header("token") String token, @Field("type") String type);

    @FormUrlEncoded
    @POST("api/get_services_from_subid")
    Call<DAOSubCategoryServices> getSubCategoryServices(@Field("subcategory_id") String subcategory_id, @Header("token") String token, @Field("latitude") String latitude, @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("api/api/bookingdetail")
    Call<DAOBookingDetail> bookingDetail(@Header("token") String token, @Field("type") String type, @Field("booking_id") String booking_id);

    @FormUrlEncoded
    @POST("api/api/update_booking")
    Call<EmptyData> updateBookingStatus(@Header("token") String token, @FieldMap Map<String, String> params);

    @GET("api/get_provider_dashboard_infos")
    Call<DAOProviderDashboard> getProviderDashboard(@Header("token") String token);

    @FormUrlEncoded
    @POST("api/delete_account")
    Call<BaseResponse> postDeleteAccount(@Header("token") String token, @Field("type") String type);

    @GET("api/review_type")
    Call<GETReviewTypes> getReviewsType(@Header("token") String token);

    @FormUrlEncoded
    @POST("api/rate_review")
    Call<POSTRatingsProvider> postRatingsProvider(@Header("token") String token, @Field("rating") String rating, @Field("review") String review, @Field("service_id") String service_id, @Field("booking_id") String booking_id, @Field("type") String type);

    @FormUrlEncoded
    @POST("api/stripe_account_details")
    Call<BaseResponse> postRegisterStripeDetails(@FieldMap HashMap<String, String> accountDetails, @Header("token") String token);

    @GET("api/details")
    Call<DAOCheckAccountDetails> checkAccountDetails(@Header("token") String token, @Query("type") String type);

    @GET("api/account_details")
    Call<DAOAccountDetails> getAccountDetails(@Header("token") String token, @Query("type") String type);

    @POST("api/get-chat-list")
    Call<DAOCHATLIST> postChatList(@Header("token") String token);

    @FormUrlEncoded
    @POST("api/get-chat-history")
    Call<DAOChatDetails> postChatHistoryList(@Header("token") String token, @Field("to_token") String to_token);

    @FormUrlEncoded
    @POST("api/flash-device-token")
    Call<EmptyData> updateDeviceToken(@Header("token") String token, @Field("device_type") String device_type, @Field("device_token") String device_token);

    @GET("api/get-notification-list")
    Call<NotificationResponse> getNotificationList(@Header("token") String token);

    @POST("api/get-wallet")
    Call<DAOWallet> postWalletDetails(@Header("token") String token);

    @FormUrlEncoded
    @POST("api/add-user-wallet")
    Call<BaseResponse> postTopupWallet(@Header("token") String token, @Field("tokenid") String tokenid,
                                       @Field("amount") String amount, @Field("currency") String currency,
                                       @Field("paytype") String paytype);

    @FormUrlEncoded
    @POST("api/withdraw-provider")
    Call<BaseResponse> postWithdrawWallet(@Header("token") String token, @Field("tokenid") String tokenid, @Field("amount") String amount);

    @POST("api/wallet-history")
    Call<DAOTransactionHistory> postTransactionList(@Header("token") String token);

    @FormUrlEncoded
    @POST("api/update-myservice-status")
    Call<DAOUpdateMyServiceStatus> postUpdateMyserviceStatus(@Header("token") String token, @Field("status") String status, @Field("service_id") String service_id);

    @GET("api/api/language_list")
    Call<LanguageListResponse> getLanguageList(@Header("token") String token);

    @GET("api/api/currency_list")
    Call<CurrencyListResponse> getCurrencyList(@Header("token") String token);

    @FormUrlEncoded
    @POST("api/api/language")
    Call<LanguageResponse> getAppLanguageData(@Header("token") String token,
                                              @Field("language") String language);

    @FormUrlEncoded
    @POST("api/api/wallet_withdraw")
    Call<BaseResponse> walletWithdraw(@Header("token") String token,
                                      @Field("amount") String amount,
                                      @Field("currency_code") String currencyCode);

    @GET("api/api/braintreeKey")
    Call<PaypalResponseToken> getPaypalToken(@Header("token") String token);

    @FormUrlEncoded
    @POST("api/api/BraintreePaypal")
    Call<BaseResponse> callBrainTreeAddWallet(@Header("token") String token,
                                              @Field("amount") String amount,
                                              @Field("orderID") String orderID,
                                              @Field("payload_nonce") String payload_nonce);

    @GET("api/api/getlogin_type")
    Call<LoginTypeResponse> getLoginType(@Header("token") String token);

    @FormUrlEncoded
    @POST("api/api/forget_password")
    Call<BaseResponse> callForgotPassword(@Header("token") String token,
                                          @Field("email") String email,
                                          @Field("mode") String mode);

    @FormUrlEncoded
    @POST("api/api/userchangepassword")
    Call<BaseResponse> changePassword(@Header("token") String token,
                                      @Field("user_id") String user_id,
                                      @Field("user_type") String user_type,
                                      @Field("current_password") String current_password,
                                      @Field("confirm_password") String confirm_password);
}
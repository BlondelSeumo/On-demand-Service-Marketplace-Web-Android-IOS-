package com.dreamguys.truelysell.utils;

import org.json.JSONArray;

public class AppConstants {

         public static final String BASE_URL = "https://truelysell.com/"; //Change Your API URL Here
//    public static final String BASE_URL = "https://truelysell.dreamguystech.com/"; //Change Your API URL Here

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final int REQUEST_PERMISSIONS = 0;
    public static final String deviceType = "Android";
    //User details
    public static final String USER_TOKEN = "user_token";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_SUBS_TYPE = "user_subs_type";
    public static final String USER_PROFILE_IMG = "USER_PROFILE_IMG";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PHONE = "1";
    //Retrofit API response model data
    public static final String SIGNUP_DATA = "SignUpData";
    public static final String SUBSCRIPTION_DATA = "SubscriptionData";
    public static final String REQUEST_ACCEPT_DATA = "RequestAcceptData";
    public static final String REQUEST_COMPLETE_DATA = "RequestCompleteData";
    public static final String CREATE_REQUEST_DATA = "CreateRequestData";
    public static final String UPDATE_REQUEST_DATA = "UpdateRequestData";
    public static final String DELETE_REQUEST_DATA = "DeleteRequestData";
    public static final String CREATE_PROVIDER_DATA = "CreateProviderData";
    public static final String UPDATE_PROVIDER_DATA = "UpdateProviderData";
    public static final String DELETE_PROVIDER_DATA = "DeleteProviderData";
    public static final String PROFILE_DATA = "ProfileData";
    public static final String PROFILE_UPDATE_DATA = "ProfileUpdateData";
    public static final String CHAT_HISTORYLIST_DATA = "ChatHistoryListData";
    public static final String CHAT_DETAILLIST_DATA = "ChatDetailListData";
    public static final String CHAT_SENDDETAILLIST_DATA = "ChatSendDetailListData";
    public static final String SUBS_SUCCESS_DATA = "SUBS_SUCCESS_DATA";
    public static final String CHANGEPASSWORD = "ChangePasswordListData";
    public static final String FORGOT_PASSWORD = "ForgotPassword";
    public static final String ADD_AVAILABILITY = "AddAvailability";
    public static final String GET_AVAILABILITY = "GetAvailability";

    public static final String APP_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd";
    public static final String APP_THEME = "AppTheme";
    public static final String LANGUAGE_SET = "LanguageSet";
    public static final String PRIMARYAPPTHEME = "PRIMARYAPPTHEME";
    public static final String SECONDARYAPPTHEME = "SECONDARYAPPTHEME";
    public static String Language = "Language";
    public static final String COLOR_LIST = "ColorList";
    public static final String SEARCH_REQUEST = "1001";
    public static final String SEARCH_PROVIDER = "1002";

    public static final String MY_LONGITUDE = "MY_LONGITUDE";
    public static final String MY_LATITUDE = "MY_LATITUDE";
    public static final String MY_ADDRESS = "MY_ADDRESS";
    public static final String MY_LANGUAGE = "MY_LANGUAGE";

    public static final String PAGE_LOGIN = "LOGIN";
    public static final String PAGE_CREATE_PROVIDER = "CREATE_PROVIDER";
    public static final String PAGE_SETTINGS = "SETTINGS";
    public static final String PAGE_MY_PROFILE = "MY_PROFILE";
    public static final String SUBSCRIPTIONSUCCESS = "SUBSCRIPTIONSUCCESS";
    public static final String STRIPEDETAILS = "STRIPEDETAILS";
    public static final String CATEGORIES = "CATEGORIES";
    public static final String SubCatID = "SubCatID";
    public static final String SUBCATEGORIES = "SUBCATEGORIES";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String VIEWS = "VIEWS";
    public static final String PROVIDERAVAIL = "PROVIDERAVAIL";
    public static final String TermsConditionsURL = "https://dreamguys.co.in/terms-and-conditions/";
    public static final String ContactUsURL = "https://dreamguys.co.in/contactus/";
    public static final String CATNAME = "CATNAME";
    public static final String RATINGS = "RATINGS";
    public static final String ReviewTypes = "ReviewTypes";
    public static final String URL = "URL";
    public static final String TbTitle = "TbTitle";
    //Fragment
    public static final String HOMEFRAGMENT = "HOMEFRAGMENT";
    public static final String CHATFRAGMENT = "CHATFRAGMENT";
    public static final String SETTINGSFRAGMENT = "SETTINGSFRAGMENT";
    public static final String HOMELIST = "HOMELIST";
    public static final String BOOKINGFRAGMENT = "BOOKINGFRAGMENT";
    public static final String SERVICEDETAIL = "SERVICEDETAIL";
    public static final String SERVICEID = "SERVICEID";
    public static final String SERVICETITLE = "SERVICETITLE";
    public static final String SERVICECATEGORIES = "SERVICECATEGORIES";
    public static final String SERVICESUBCATEGORIES = "SERVICESUBCATEGORIES";
    public static final String MOBILEOTP = "MOBILEOTP";
    public static final String PROFESSIONALLOGIN = "PROFESSIONALLOGIN";
    public static final String PNAME = "PNAME";
    public static final String PEMAIL = "PEMAIL";
    public static final String PMOBILENO = "PMOBILENO";
    public static final String PCATEGORY = "PCATEGORY";
    public static final String PSUBCATEGORY = "PSUBCATEGORY";
    public static final String PIMAGE = "PIMAGE";
    public static final String PROVIDERDASHBOARD = "PROVIDERDASHBOARD";
    public static final String MYSERVICES = "MYSERVICES";
    public static final String DELETESERVICE = "DELETESERVICE";
    public static final String EDITSERVICEINFO = "EDITSERVICEINFO";
    public static final String PAGE_EDIT_SERVICE = "PAGE_EDIT_SERVICE";
    public static final String UPDATE_SERVICE = "UPDATE_SERVICE";
    public static final String DELETESERVICEIMAGES = "DELETESERVICEIMAGES";
    public static final String FROMPAGE = "FROMPAGE";
    public static final String ACCOUNTSETTINGS = "ACCOUNTSETTINGS";
    public static final String HOME = "HOME";
    public static final String VIEWALLSERVICES = "VIEWALLSERVICES";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String AVAILABLETIMESLOTS = "AVAILABLETIMESLOTS";
    public static final String BOOKSERVICE = "BOOKSERVICE";
    public static final String SERVICEAMOUNT = "SERVICEAMOUNT";
    public static final String CURRENCY = "CURRENCY";
    public static final String SEARCHSERVICES = "SEARCHSERVICES";
    public static final String SEARCHTEXT = "SEARCHTEXT";
    public static final String BOOKINGLIST = "BOOKINGLIST";
    public static final String USER_PROFILE_DATA = "USER_PROFILE_DATA";
    public static final String USER_PROFILE_UPDATE_DATA = "USER_PROFILE_UPDATE_DATA";
    public static final String MYREQUESTS = "MYREQUESTS";
    public static final String VIEWALL = "VIEWALL";
    public static final String VIEWALLTYPE = "VIEWALLTYPE";
    public static final String SUBCATEGORIESSERVICES = "SUBCATEGORIESSERVICES";
    public static final String MY_CITYLOCATION = "MY_CITYLOCATION";
    public static final String BOOKINGID = "BOOKINGID";
    public static final String BOOKINGDETAIL = "BOOKINGDETAIL";
    public static final String BOOKINGSTATUS = "BOOKINGSTATUS";
    public static final String SERVICESTATUS = "SERVICESTATUS";
    public static final String PAGE_MY_DASHBOARD = "PAGE_MY_DASHBOARD";
    public static final String GETACCOUNTDETAILS = "GETACCOUNTDETAILS";
    public static final String CHECKACCOUNTDETAILS = "CHECKACCOUNTDETAILS";
    public static final String UPDATETOKEN = "UPDATETOKEN";
    public static final String NOTIFICATION_LIST = "NOTIFICATION_LIST";
    public static final String CASH_AMOUNT = "CASH_AMOUNT";
    public static final String WALLETDETAILS = "WALLETDETAILS";
    public static final String WALLETAMOUNT = "WALLETAMOUNT";
    public static final String TOPUPWALLET = "TOPUPWALLET";
    public static final String BRAINTREEADDWALLET = "BRAINTREEADDWALLET";
    public static final String CURRENCYCODE = "CURRENCYCODE";
    public static final String TOPUP = "TOPUP";
    public static final String WITHDRAW = "WITHDRAW";
    public static final String WITHDRAWWALLET = "WITHDRAWWALLET";
    public static final String TRANSACTIONLIST = "TRANSACTIONLIST";
    public static final String UPDATEMYSERVICESTATUS = "UPDATEMYSERVICESTATUS";
    public static final String LOGINTYPE = "LOGINTYPE";
    public static final String LOGINTYPESTR = "LOGINTYPESTR";
    public static final String FORGOTPASSWORD = "FORGOTPASSWORD";

    public static int isFromWhichCard = -1;

    //refreshed Token
    public static String refreshedToken = "refreshedToken";

    // broadcast receiver intent filters
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String GROUP_KEY_WORK_EMAIL = "com.dreamguys.truelysell";

    public static String chatFrom = "chatFrom";

    public static String chatUsername = "chatUsername";
    public static String chatImg = "chatImg";
    public static String StripePrice = "StripePrice";
    public static String StripeSubId = "StripeSubId";
    public static String StripeSubName = "StripeSubName";
    public static String DefaultCurrency = "USD";
    public static String currentActivity = "";

    public static String localeName = "ar";
    public static JSONArray country_code_jsonResponse;
    public static final String ISSUBSCRIBED = "ISSUBSCRIBED";
    public static final String SHARECODE = "SHARECODE";
    public static final String DEFAULTTOKEN = "8338d6ff4f0878b222f312494c1621a9";
    public static final String PUBLICKEY = "pk_test_5J1tjkjdwBGS9TdcYm2a5dk2";

    public static final String GETLANGUAGELIST = "GETLANGUAGELIST";
    public static final String GETCURRENCYLIST = "GETCURRENCYLIST";
    public static final String GETLANGUAGEAPPDATA = "GETLANGUAGEAPPDATA";
    public static final String GETPAYPALTOKEN = "GETPAYPALTOKEN";

    public static String stripe_Acc_name = "stripe_Acc_name";
    public static String stripe_Acc_num = "stripe_Acc_num";
    public static String stripe_Iban = "stripe_Iban";
    public static String stripe_bank_name = "stripe_bank_name";
    public static String stripe_bank_address = "stripe_bank_address";
    public static String stripe_sort_code = "stripe_sort_code";
    public static String stripe_routing_number = "stripe_routing_number";
    public static String stripe_account_ifsc = "stripe_account_ifsc";


}

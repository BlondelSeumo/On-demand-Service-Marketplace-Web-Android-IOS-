package com.dreamguys.truelysell.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguageResponse extends BaseResponse {

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

        @SerializedName("language")
        @Expose
        private Language language;

        public Language getLanguage() {
            return language;
        }

        public void setLanguage(Language language) {
            this.language = language;
        }

        public class Language {

            @SerializedName("register_screen")
            @Expose
            private RegisterScreen registerScreen;
            @SerializedName("common_strings")
            @Expose
            private CommonStrings commonStrings;
            @SerializedName("home_screen")
            @Expose
            private HomeScreen homeScreen;
            @SerializedName("view_all_services")
            @Expose
            private ViewAllServices viewAllServices;
            @SerializedName("create_service")
            @Expose
            private CreateService createService;
            @SerializedName("tab_bar_title")
            @Expose
            private TabBarTitle tabBarTitle;
            @SerializedName("chat_screen")
            @Expose
            private ChatScreen chatScreen;
            @SerializedName("settings_screen")
            @Expose
            private SettingsScreen settingsScreen;
            @SerializedName("profile_screen")
            @Expose
            private ProfileScreen profileScreen;
            @SerializedName("provider_availability_screen")
            @Expose
            private ProviderAvailabilityScreen providerAvailabilityScreen;
            @SerializedName("wallet_screen")
            @Expose
            private WalletScreen walletScreen;
            @SerializedName("subscriptionscreen")
            @Expose
            private SubscriptionScreen subscriptionScreen;
            @SerializedName("booking_detail_service")
            @Expose
            private BookingDetailService bookingDetailService;
            @SerializedName("booking_service")
            @Expose
            private BookingService bookingService;
            @SerializedName("account_settings_screen")
            @Expose
            private AccountSettingsScreen accountSettingsScreen;
            @SerializedName("change_password")
            @Expose
            private ChangePassword changePassword;
            @SerializedName("Email_Login")
            @Expose
            private EmailLogin EmailLogin;

            public Language.EmailLogin getEmailLogin() {
                return EmailLogin;
            }

            public void setEmailLogin(Language.EmailLogin emailLogin) {
                EmailLogin = emailLogin;
            }

            public ChangePassword getChangePassword() {
                return changePassword;
            }

            public void setChangePassword(ChangePassword changePassword) {
                this.changePassword = changePassword;
            }

            public RegisterScreen getRegisterScreen() {
                return registerScreen;
            }

            public void setRegisterScreen(RegisterScreen registerScreen) {
                this.registerScreen = registerScreen;
            }

            public CommonStrings getCommonStrings() {
                return commonStrings;
            }

            public void setCommonStrings(CommonStrings commonStrings) {
                this.commonStrings = commonStrings;
            }

            public HomeScreen getHomeScreen() {
                return homeScreen;
            }

            public void setHomeScreen(HomeScreen homeScreen) {
                this.homeScreen = homeScreen;
            }

            public ViewAllServices getViewAllServices() {
                return viewAllServices;
            }

            public void setViewAllServices(ViewAllServices viewAllServices) {
                this.viewAllServices = viewAllServices;
            }

            public CreateService getCreateService() {
                return createService;
            }

            public void setCreateService(CreateService createService) {
                this.createService = createService;
            }

            public TabBarTitle getTabBarTitle() {
                return tabBarTitle;
            }

            public void setTabBarTitle(TabBarTitle tabBarTitle) {
                this.tabBarTitle = tabBarTitle;
            }

            public ChatScreen getChatScreen() {
                return chatScreen;
            }

            public void setChatScreen(ChatScreen chatScreen) {
                this.chatScreen = chatScreen;
            }

            public SettingsScreen getSettingsScreen() {
                return settingsScreen;
            }

            public void setSettingsScreen(SettingsScreen settingsScreen) {
                this.settingsScreen = settingsScreen;
            }

            public ProfileScreen getProfileScreen() {
                return profileScreen;
            }

            public void setProfileScreen(ProfileScreen profileScreen) {
                this.profileScreen = profileScreen;
            }

            public ProviderAvailabilityScreen getProviderAvailabilityScreen() {
                return providerAvailabilityScreen;
            }

            public void setProviderAvailabilityScreen(ProviderAvailabilityScreen providerAvailabilityScreen) {
                this.providerAvailabilityScreen = providerAvailabilityScreen;
            }

            public WalletScreen getWalletScreen() {
                return walletScreen;
            }

            public void setWalletScreen(WalletScreen walletScreen) {
                this.walletScreen = walletScreen;
            }

            public SubscriptionScreen getSubscriptionScreen() {
                return subscriptionScreen;
            }

            public void setSubscriptionScreen(SubscriptionScreen subscriptionScreen) {
                this.subscriptionScreen = subscriptionScreen;
            }

            public BookingDetailService getBookingDetailService() {
                return bookingDetailService;
            }

            public void setBookingDetailService(BookingDetailService bookingDetailService) {
                this.bookingDetailService = bookingDetailService;
            }

            public BookingService getBookingService() {
                return bookingService;
            }

            public void setBookingService(BookingService bookingService) {
                this.bookingService = bookingService;
            }

            public AccountSettingsScreen getAccountSettingsScreen() {
                return accountSettingsScreen;
            }

            public void setAccountSettingsScreen(AccountSettingsScreen accountSettingsScreen) {
                this.accountSettingsScreen = accountSettingsScreen;
            }

            public class RegisterScreen {

                @SerializedName("lbl_select_service_here")
                @Expose
                private LblSelectServiceHere lblSelectServiceHere;
                @SerializedName("lbl_get_started")
                @Expose
                private LblGetStarted lblGetStarted;
                @SerializedName("lbl_login")
                @Expose
                private LblLogin lblLogin;
                @SerializedName("lbl_service_you_provide")
                @Expose
                private LblServiceYouProvide lblServiceYouProvide;
                @SerializedName("lbl_choose_subcategory")
                @Expose
                private LblChooseSubcategory lblChooseSubcategory;
                @SerializedName("lbl_select_subcategory")
                @Expose
                private LblSelectSubcategory lblSelectSubcategory;
                @SerializedName("btn_next")
                @Expose
                private BtnNext btnNext;
                @SerializedName("btn_previous")
                @Expose
                private BtnPrevious btnPrevious;
                @SerializedName("btn_code")
                @Expose
                private BtnCode btnCode;
                @SerializedName("txt_fld_mobile_num")
                @Expose
                private TxtFldMobileNum txtFldMobileNum;
                @SerializedName("lbl_already_professional")
                @Expose
                private LblAlreadyProfessional lblAlreadyProfessional;
                @SerializedName("lbl_register")
                @Expose
                private LblRegister lblRegister;
                @SerializedName("txt_fld_name")
                @Expose
                private TxtFldName txtFldName;
                @SerializedName("txt_fld_email")
                @Expose
                private TxtFldEmail txtFldEmail;
                @SerializedName("txt_fld_reference_code")
                @Expose
                private TxtFldReferenceCode txtFldReferenceCode;
                @SerializedName("lbl_access_code")
                @Expose
                private LblAccessCode lblAccessCode;
                @SerializedName("lbl_otp_content")
                @Expose
                private LblOtpContent lblOtpContent;
                @SerializedName("btn_select_category")
                @Expose
                private BtnSelectCategory btnSelectCategory;
                @SerializedName("lbl_login_as_professional")
                @Expose
                private LblLoginAsProfessional lblLoginAsProfessional;
                @SerializedName("lbl_join_as_professional")
                @Expose
                private LblJoinAsProfessional lblJoinAsProfessional;
                @SerializedName("lbl_select_country")
                @Expose
                private LblSelectCountry lblSelectCountry;
                @SerializedName("btn_done")
                @Expose
                private BtnDone btnDone;
                @SerializedName("btn_cancel")
                @Expose
                private BtnCancel btnCancel;
                @SerializedName("lbl_login_as_user")
                @Expose
                private LblLoginAsUser lbl_login_as_user;
                @SerializedName("lbl_already_user")
                @Expose
                private LblAlreadyUser lbl_already_user;
                @SerializedName("join_as_user")
                @Expose
                private JoinAsUser join_as_user;
                @SerializedName("btn_resend_otp")
                @Expose
                private btnResendOtp btn_resend_otp;
                @SerializedName("btn_submit")
                @Expose
                private BtnSubmit btn_submit;
                @SerializedName("lbl_dint_receive_otp")
                @Expose
                private Lbl_dint_receive_otp lbl_dint_receive_otp;
                @SerializedName("lbl_verify")
                @Expose
                private Lbl_verify lbl_verify;

                public class LblSelectServiceHere extends LanguageSubModel {

                }

                public class Lbl_verify extends LanguageSubModel {

                }

                public class Lbl_dint_receive_otp extends LanguageSubModel {

                }

                public LblSelectServiceHere getLblSelectServiceHere() {
                    return lblSelectServiceHere;
                }

                public void setLblSelectServiceHere(LblSelectServiceHere lblSelectServiceHere) {
                    this.lblSelectServiceHere = lblSelectServiceHere;
                }

                public class LblGetStarted extends LanguageSubModel {

                }

                public LblGetStarted getLblGetStarted() {
                    return lblGetStarted;
                }

                public void setLblGetStarted(LblGetStarted lblGetStarted) {
                    this.lblGetStarted = lblGetStarted;
                }

                public class LblLogin extends LanguageSubModel {

                }

                public LblLogin getLblLogin() {
                    return lblLogin;
                }

                public void setLblLogin(LblLogin lblLogin) {
                    this.lblLogin = lblLogin;
                }

                public class LblServiceYouProvide extends LanguageSubModel {

                }

                public LblServiceYouProvide getLblServiceYouProvide() {
                    return lblServiceYouProvide;
                }

                public void setLblServiceYouProvide(LblServiceYouProvide lblServiceYouProvide) {
                    this.lblServiceYouProvide = lblServiceYouProvide;
                }

                public class LblChooseSubcategory extends LanguageSubModel {

                }

                public LblChooseSubcategory getLblChooseSubcategory() {
                    return lblChooseSubcategory;
                }

                public void setLblChooseSubcategory(LblChooseSubcategory lblChooseSubcategory) {
                    this.lblChooseSubcategory = lblChooseSubcategory;
                }

                public class LblSelectSubcategory extends LanguageSubModel {

                }

                public LblSelectSubcategory getLblSelectSubcategory() {
                    return lblSelectSubcategory;
                }

                public void setLblSelectSubcategory(LblSelectSubcategory lblSelectSubcategory) {
                    this.lblSelectSubcategory = lblSelectSubcategory;
                }

                public class BtnNext extends LanguageSubModel {

                }

                public BtnNext getBtnNext() {
                    return btnNext;
                }

                public void setBtnNext(BtnNext btnNext) {
                    this.btnNext = btnNext;
                }

                public class BtnPrevious extends LanguageSubModel {

                }

                public BtnPrevious getBtnPrevious() {
                    return btnPrevious;
                }

                public void setBtnPrevious(BtnPrevious btnPrevious) {
                    this.btnPrevious = btnPrevious;
                }

                public class BtnCode extends LanguageSubModel {

                }

                public BtnCode getBtnCode() {
                    return btnCode;
                }

                public void setBtnCode(BtnCode btnCode) {
                    this.btnCode = btnCode;
                }

                public class TxtFldMobileNum extends LanguageSubModel {

                }

                public TxtFldMobileNum getTxtFldMobileNum() {
                    return txtFldMobileNum;
                }

                public void setTxtFldMobileNum(TxtFldMobileNum txtFldMobileNum) {
                    this.txtFldMobileNum = txtFldMobileNum;
                }

                public class LblAlreadyProfessional extends LanguageSubModel {

                }

                public LblAlreadyProfessional getLblAlreadyProfessional() {
                    return lblAlreadyProfessional;
                }

                public void setLblAlreadyProfessional(LblAlreadyProfessional lblAlreadyProfessional) {
                    this.lblAlreadyProfessional = lblAlreadyProfessional;
                }

                public class LblRegister extends LanguageSubModel {

                }

                public LblRegister getLblRegister() {
                    return lblRegister;
                }

                public void setLblRegister(LblRegister lblRegister) {
                    this.lblRegister = lblRegister;
                }

                public class TxtFldName extends LanguageSubModel {

                }

                public TxtFldName getTxtFldName() {
                    return txtFldName;
                }

                public void setTxtFldName(TxtFldName txtFldName) {
                    this.txtFldName = txtFldName;
                }

                public class TxtFldEmail extends LanguageSubModel {

                }

                public TxtFldEmail getTxtFldEmail() {
                    return txtFldEmail;
                }

                public void setTxtFldEmail(TxtFldEmail txtFldEmail) {
                    this.txtFldEmail = txtFldEmail;
                }

                public class TxtFldReferenceCode extends LanguageSubModel {

                }

                public TxtFldReferenceCode getTxtFldReferenceCode() {
                    return txtFldReferenceCode;
                }

                public void setTxtFldReferenceCode(TxtFldReferenceCode txtFldReferenceCode) {
                    this.txtFldReferenceCode = txtFldReferenceCode;
                }

                public class LblAccessCode extends LanguageSubModel {

                }

                public LblAccessCode getLblAccessCode() {
                    return lblAccessCode;
                }

                public void setLblAccessCode(LblAccessCode lblAccessCode) {
                    this.lblAccessCode = lblAccessCode;
                }

                public class LblOtpContent extends LanguageSubModel {

                }

                public LblOtpContent getLblOtpContent() {
                    return lblOtpContent;
                }

                public void setLblOtpContent(LblOtpContent lblOtpContent) {
                    this.lblOtpContent = lblOtpContent;
                }

                public class BtnSelectCategory extends LanguageSubModel {

                }

                public BtnSelectCategory getBtnSelectCategory() {
                    return btnSelectCategory;
                }

                public void setBtnSelectCategory(BtnSelectCategory btnSelectCategory) {
                    this.btnSelectCategory = btnSelectCategory;
                }

                public class LblLoginAsProfessional extends LanguageSubModel {

                }

                public LblLoginAsProfessional getLblLoginAsProfessional() {
                    return lblLoginAsProfessional;
                }

                public void setLblLoginAsProfessional(LblLoginAsProfessional lblLoginAsProfessional) {
                    this.lblLoginAsProfessional = lblLoginAsProfessional;
                }

                public class LblJoinAsProfessional extends LanguageSubModel {

                }

                public LblJoinAsProfessional getLblJoinAsProfessional() {
                    return lblJoinAsProfessional;
                }

                public void setLblJoinAsProfessional(LblJoinAsProfessional lblJoinAsProfessional) {
                    this.lblJoinAsProfessional = lblJoinAsProfessional;
                }

                public class LblSelectCountry extends LanguageSubModel {

                }

                public LblSelectCountry getLblSelectCountry() {
                    return lblSelectCountry;
                }

                public void setLblSelectCountry(LblSelectCountry lblSelectCountry) {
                    this.lblSelectCountry = lblSelectCountry;
                }

                public class BtnDone extends LanguageSubModel {

                }

                public BtnDone getBtnDone() {
                    return btnDone;
                }

                public void setBtnDone(BtnDone btnDone) {
                    this.btnDone = btnDone;
                }

                public class BtnCancel extends LanguageSubModel {

                }

                public BtnCancel getBtnCancel() {
                    return btnCancel;
                }

                public void setBtnCancel(BtnCancel btnCancel) {
                    this.btnCancel = btnCancel;
                }

                public class LblLoginAsUser extends LanguageSubModel {

                }

                public LblLoginAsUser getLbl_login_as_user() {
                    return lbl_login_as_user;
                }

                public void setLbl_login_as_user(LblLoginAsUser lbl_login_as_user) {
                    this.lbl_login_as_user = lbl_login_as_user;
                }

                public class LblAlreadyUser extends LanguageSubModel {

                }

                public LblAlreadyUser getLbl_already_user() {
                    return lbl_already_user;
                }

                public void setLbl_already_user(LblAlreadyUser lbl_already_user) {
                    this.lbl_already_user = lbl_already_user;
                }

                public class JoinAsUser extends LanguageSubModel {

                }

                public JoinAsUser getJoin_as_user() {
                    return join_as_user;
                }

                public void setJoin_as_user(JoinAsUser join_as_user) {
                    this.join_as_user = join_as_user;
                }

                public class btnResendOtp extends LanguageSubModel {

                }

                public btnResendOtp getBtn_resend_otp() {
                    return btn_resend_otp;
                }

                public void setBtn_resend_otp(btnResendOtp btn_resend_otp) {
                    this.btn_resend_otp = btn_resend_otp;
                }

                public class BtnSubmit extends LanguageSubModel {

                }

                public BtnSubmit getBtn_submit() {
                    return btn_submit;
                }

                public void setBtn_submit(BtnSubmit btn_submit) {
                    this.btn_submit = btn_submit;
                }

                public Lbl_dint_receive_otp getLbl_dint_receive_otp() {
                    return lbl_dint_receive_otp;
                }

                public void setLbl_dint_receive_otp(Lbl_dint_receive_otp lbl_dint_receive_otp) {
                    this.lbl_dint_receive_otp = lbl_dint_receive_otp;
                }

                public Lbl_verify getLbl_verify() {
                    return lbl_verify;
                }

                public void setLbl_verify(Lbl_verify lbl_verify) {
                    this.lbl_verify = lbl_verify;
                }
            }

            public class CommonStrings {
                @SerializedName("btn_yes")
                @Expose
                private BtnYes btnYes;
                @SerializedName("btn_no")
                @Expose
                private BtnNo btnNo;
                @SerializedName("btn_update")
                @Expose
                private BtnUpdate btnUpdate;
                @SerializedName("lbl_choose")
                @Expose
                private LblChoose lblChoose;
                @SerializedName("btn_cancel")
                @Expose
                private BtnCancel btnCancel;
                @SerializedName("btn_camera")
                @Expose
                private BtnCamera btnCamera;
                @SerializedName("btn_gallery")
                @Expose
                private BtnGallery btnGallery;
                @SerializedName("txt_updating")
                @Expose
                private TxtUpdating txtUpdating;
                @SerializedName("btn_apply")
                @Expose
                private BtnApply btnApply;
                @SerializedName("btn_done")
                @Expose
                private BtnDone btnDone;
                @SerializedName("lbl_truely")
                @Expose
                private LblTruely lblTruely;
                @SerializedName("lbl_sell")
                @Expose
                private LblSell lblSell;
                @SerializedName("lbl_worlds_largest")
                @Expose
                private LblWorldsLargest lblWorldsLargest;
                @SerializedName("lbl_market_place")
                @Expose
                private LblMarketPlace lblMarketPlace;
                @SerializedName("lbl_view_all")
                @Expose
                private LblViewAll lblViewAll;
                @SerializedName("lbl_popular_service")
                @Expose
                private LblPopularService lblPopularService;
                @SerializedName("lbl_newly_added_service")
                @Expose
                private LblNewlyAddedService lblNewlyAddedService;
                @SerializedName("lbl_view_more")
                @Expose
                private LblViewMore lblViewMore;
                @SerializedName("lbl_internet")
                @Expose
                private LblInternet lblInternet;

                public class BtnYes extends LanguageSubModel {
                }

                public class BtnNo extends LanguageSubModel {
                }

                public class BtnUpdate extends LanguageSubModel {
                }

                public class LblChoose extends LanguageSubModel {
                }

                public class BtnCancel extends LanguageSubModel {
                }

                public class BtnCamera extends LanguageSubModel {
                }

                public class BtnGallery extends LanguageSubModel {
                }

                public class TxtUpdating extends LanguageSubModel {
                }

                public class BtnApply extends LanguageSubModel {
                }

                public class BtnDone extends LanguageSubModel {
                }

                public class LblTruely extends LanguageSubModel {
                }

                public class LblSell extends LanguageSubModel {
                }

                public class LblWorldsLargest extends LanguageSubModel {
                }

                public class LblMarketPlace extends LanguageSubModel {
                }

                public class LblViewAll extends LanguageSubModel {
                }

                public class LblPopularService extends LanguageSubModel {
                }

                public class LblNewlyAddedService extends LanguageSubModel {
                }

                public class LblViewMore extends LanguageSubModel {

                }

                public class LblInternet extends LanguageSubModel {
                }

                public BtnYes getBtnYes() {
                    return btnYes;
                }

                public void setBtnYes(BtnYes btnYes) {
                    this.btnYes = btnYes;
                }

                public BtnNo getBtnNo() {
                    return btnNo;
                }

                public void setBtnNo(BtnNo btnNo) {
                    this.btnNo = btnNo;
                }

                public BtnUpdate getBtnUpdate() {
                    return btnUpdate;
                }

                public void setBtnUpdate(BtnUpdate btnUpdate) {
                    this.btnUpdate = btnUpdate;
                }

                public LblChoose getLblChoose() {
                    return lblChoose;
                }

                public void setLblChoose(LblChoose lblChoose) {
                    this.lblChoose = lblChoose;
                }

                public BtnCancel getBtnCancel() {
                    return btnCancel;
                }

                public void setBtnCancel(BtnCancel btnCancel) {
                    this.btnCancel = btnCancel;
                }

                public BtnCamera getBtnCamera() {
                    return btnCamera;
                }

                public void setBtnCamera(BtnCamera btnCamera) {
                    this.btnCamera = btnCamera;
                }

                public BtnGallery getBtnGallery() {
                    return btnGallery;
                }

                public void setBtnGallery(BtnGallery btnGallery) {
                    this.btnGallery = btnGallery;
                }

                public TxtUpdating getTxtUpdating() {
                    return txtUpdating;
                }

                public void setTxtUpdating(TxtUpdating txtUpdating) {
                    this.txtUpdating = txtUpdating;
                }

                public BtnApply getBtnApply() {
                    return btnApply;
                }

                public void setBtnApply(BtnApply btnApply) {
                    this.btnApply = btnApply;
                }

                public BtnDone getBtnDone() {
                    return btnDone;
                }

                public void setBtnDone(BtnDone btnDone) {
                    this.btnDone = btnDone;
                }

                public LblTruely getLblTruely() {
                    return lblTruely;
                }

                public void setLblTruely(LblTruely lblTruely) {
                    this.lblTruely = lblTruely;
                }

                public LblSell getLblSell() {
                    return lblSell;
                }

                public void setLblSell(LblSell lblSell) {
                    this.lblSell = lblSell;
                }

                public LblWorldsLargest getLblWorldsLargest() {
                    return lblWorldsLargest;
                }

                public void setLblWorldsLargest(LblWorldsLargest lblWorldsLargest) {
                    this.lblWorldsLargest = lblWorldsLargest;
                }

                public LblMarketPlace getLblMarketPlace() {
                    return lblMarketPlace;
                }

                public void setLblMarketPlace(LblMarketPlace lblMarketPlace) {
                    this.lblMarketPlace = lblMarketPlace;
                }

                public LblViewAll getLblViewAll() {
                    return lblViewAll;
                }

                public void setLblViewAll(LblViewAll lblViewAll) {
                    this.lblViewAll = lblViewAll;
                }

                public LblPopularService getLblPopularService() {
                    return lblPopularService;
                }

                public void setLblPopularService(LblPopularService lblPopularService) {
                    this.lblPopularService = lblPopularService;
                }

                public LblNewlyAddedService getLblNewlyAddedService() {
                    return lblNewlyAddedService;
                }

                public void setLblNewlyAddedService(LblNewlyAddedService lblNewlyAddedService) {
                    this.lblNewlyAddedService = lblNewlyAddedService;
                }

                public LblViewMore getLblViewMore() {
                    return lblViewMore;
                }

                public void setLblViewMore(LblViewMore lblViewMore) {
                    this.lblViewMore = lblViewMore;
                }

                public LblInternet getLblInternet() {
                    return lblInternet;
                }

                public void setLblInternet(LblInternet lblInternet) {
                    this.lblInternet = lblInternet;
                }
            }

            public class HomeScreen {
                @SerializedName("txt_fld_search")
                @Expose
                private TxtFldSearch txtFldSearch;
                @SerializedName("lbl_no_popular_service")
                @Expose
                private LblNoPopularService lblNoPopularService;
                @SerializedName("lbl_no_newly_added_service")
                @Expose
                private LblNoNewlyAddedService lblNoNewlyAddedService;
                @SerializedName("lbl_no_categories_found")
                @Expose
                private LblNoCategoriesFound lblNoCategoriesFound;
                @SerializedName("lbl_my_services")
                @Expose
                private LblMyServices lblMyServices;
                @SerializedName("lbl_buyer_request")
                @Expose
                private LblBuyerRequest lblBuyerRequest;
                @SerializedName("lbl_inprogress_services")
                @Expose
                private LblInprogressServices lblInprogressServices;
                @SerializedName("lbl_completed_services")
                @Expose
                private LblCompletedServices lblCompletedServices;
                @SerializedName("lbl_filter_status_type")
                @Expose
                private LblFilterStatusType lblFilterStatusType;
                @SerializedName("lbl_filter_all")
                @Expose
                private LblFilterAll lblFilterAll;
                @SerializedName("lbl_filter_active")
                @Expose
                private LblFilterActive lblFilterActive;
                @SerializedName("lbl_filter_inactive")
                @Expose
                private LblFilterInactive lblFilterInactive;
                @SerializedName("lbl_no_service")
                @Expose
                private LblNoService lblNoService;
                @SerializedName("lbl_no_records_found")
                @Expose
                private LblNoRecordsFound lblNoRecordsFound;
                @SerializedName("lbl_booking_list")
                @Expose
                private LblBookingList lblBookingList;
                @SerializedName("lbl_booking")
                @Expose
                private LblBooking lblBooking;
                @SerializedName("lbl_related_services")
                @Expose
                private LblRelatedServices lblRelatedServices;
                @SerializedName("lbl_view_map")
                @Expose
                private LblViewMap lblViewMap;
                @SerializedName("lbl_default_otp_msg")
                @Expose
                private Lbl_default_otp_msg lbl_default_otp_msg;


                public class TxtFldSearch extends LanguageSubModel {
                }

                public class Lbl_default_otp_msg extends LanguageSubModel {
                }

                public class LblNoPopularService extends LanguageSubModel {
                }

                public class LblNoNewlyAddedService extends LanguageSubModel {
                }

                public class LblNoCategoriesFound extends LanguageSubModel {
                }

                public class LblMyServices extends LanguageSubModel {
                }

                public class LblBuyerRequest extends LanguageSubModel {
                }

                public class LblInprogressServices extends LanguageSubModel {
                }

                public class LblCompletedServices extends LanguageSubModel {
                }

                public class LblFilterStatusType extends LanguageSubModel {
                }

                public class LblFilterAll extends LanguageSubModel {
                }

                public class LblFilterActive extends LanguageSubModel {
                }

                public class LblFilterInactive extends LanguageSubModel {
                }

                public class LblNoService extends LanguageSubModel {
                }

                public class LblNoRecordsFound extends LanguageSubModel {
                }

                public class LblBookingList extends LanguageSubModel {
                }

                public class LblBooking extends LanguageSubModel {
                }

                public class LblRelatedServices extends LanguageSubModel {
                }

                public class LblViewMap extends LanguageSubModel {
                }

                public TxtFldSearch getTxtFldSearch() {
                    return txtFldSearch;
                }

                public void setTxtFldSearch(TxtFldSearch txtFldSearch) {
                    this.txtFldSearch = txtFldSearch;
                }

                public LblNoPopularService getLblNoPopularService() {
                    return lblNoPopularService;
                }

                public void setLblNoPopularService(LblNoPopularService lblNoPopularService) {
                    this.lblNoPopularService = lblNoPopularService;
                }

                public LblNoNewlyAddedService getLblNoNewlyAddedService() {
                    return lblNoNewlyAddedService;
                }

                public void setLblNoNewlyAddedService(LblNoNewlyAddedService lblNoNewlyAddedService) {
                    this.lblNoNewlyAddedService = lblNoNewlyAddedService;
                }

                public LblNoCategoriesFound getLblNoCategoriesFound() {
                    return lblNoCategoriesFound;
                }

                public void setLblNoCategoriesFound(LblNoCategoriesFound lblNoCategoriesFound) {
                    this.lblNoCategoriesFound = lblNoCategoriesFound;
                }

                public LblMyServices getLblMyServices() {
                    return lblMyServices;
                }

                public void setLblMyServices(LblMyServices lblMyServices) {
                    this.lblMyServices = lblMyServices;
                }

                public LblBuyerRequest getLblBuyerRequest() {
                    return lblBuyerRequest;
                }

                public void setLblBuyerRequest(LblBuyerRequest lblBuyerRequest) {
                    this.lblBuyerRequest = lblBuyerRequest;
                }

                public LblInprogressServices getLblInprogressServices() {
                    return lblInprogressServices;
                }

                public void setLblInprogressServices(LblInprogressServices lblInprogressServices) {
                    this.lblInprogressServices = lblInprogressServices;
                }

                public LblCompletedServices getLblCompletedServices() {
                    return lblCompletedServices;
                }

                public void setLblCompletedServices(LblCompletedServices lblCompletedServices) {
                    this.lblCompletedServices = lblCompletedServices;
                }

                public LblFilterStatusType getLblFilterStatusType() {
                    return lblFilterStatusType;
                }

                public void setLblFilterStatusType(LblFilterStatusType lblFilterStatusType) {
                    this.lblFilterStatusType = lblFilterStatusType;
                }

                public LblFilterAll getLblFilterAll() {
                    return lblFilterAll;
                }

                public void setLblFilterAll(LblFilterAll lblFilterAll) {
                    this.lblFilterAll = lblFilterAll;
                }

                public LblFilterActive getLblFilterActive() {
                    return lblFilterActive;
                }

                public void setLblFilterActive(LblFilterActive lblFilterActive) {
                    this.lblFilterActive = lblFilterActive;
                }

                public LblFilterInactive getLblFilterInactive() {
                    return lblFilterInactive;
                }

                public void setLblFilterInactive(LblFilterInactive lblFilterInactive) {
                    this.lblFilterInactive = lblFilterInactive;
                }

                public LblNoService getLblNoService() {
                    return lblNoService;
                }

                public void setLblNoService(LblNoService lblNoService) {
                    this.lblNoService = lblNoService;
                }

                public LblNoRecordsFound getLblNoRecordsFound() {
                    return lblNoRecordsFound;
                }

                public void setLblNoRecordsFound(LblNoRecordsFound lblNoRecordsFound) {
                    this.lblNoRecordsFound = lblNoRecordsFound;
                }

                public LblBookingList getLblBookingList() {
                    return lblBookingList;
                }

                public void setLblBookingList(LblBookingList lblBookingList) {
                    this.lblBookingList = lblBookingList;
                }

                public LblBooking getLblBooking() {
                    return lblBooking;
                }

                public void setLblBooking(LblBooking lblBooking) {
                    this.lblBooking = lblBooking;
                }

                public LblRelatedServices getLblRelatedServices() {
                    return lblRelatedServices;
                }

                public void setLblRelatedServices(LblRelatedServices lblRelatedServices) {
                    this.lblRelatedServices = lblRelatedServices;
                }

                public LblViewMap getLblViewMap() {
                    return lblViewMap;
                }

                public void setLblViewMap(LblViewMap lblViewMap) {
                    this.lblViewMap = lblViewMap;
                }

                public Lbl_default_otp_msg getLbl_default_otp_msg() {
                    return lbl_default_otp_msg;
                }

                public void setLbl_default_otp_msg(Lbl_default_otp_msg lbl_default_otp_msg) {
                    this.lbl_default_otp_msg = lbl_default_otp_msg;
                }
            }

            public class ViewAllServices {
                @SerializedName("toolbar_title")
                @Expose
                private ToolbarTitle toolbarTitle;
                @SerializedName("lbl_popular")
                @Expose
                private LblPopular lblPopular;
                @SerializedName("FEATURED")
                @Expose
                private FEATURED fEATURED;
                @SerializedName("lbl_newest")
                @Expose
                private LblNewest lblNewest;
                @SerializedName("lbl_popular_services")
                @Expose
                private LblPopularServices lblPopularServices;
                @SerializedName("lbl_featured_services")
                @Expose
                private LblFeaturedServices lblFeaturedServices;
                @SerializedName("lbl_new_service")
                @Expose
                private LblNewService lblNewService;
                @SerializedName("lbl_view_on_map")
                @Expose
                private LblViewOnMap lblViewOnMap;
                @SerializedName("lbl_book")
                @Expose
                private LblBook lblBook;
                @SerializedName("lbl_newly_added_service")
                @Expose
                private LblNewlyAddedService lbl_newly_added_service;

                public class ToolbarTitle extends LanguageSubModel {
                }

                public class LblNewlyAddedService extends LanguageSubModel {
                }

                public class LblPopular extends LanguageSubModel {
                }

                public class FEATURED extends LanguageSubModel {
                }

                public class LblNewest extends LanguageSubModel {
                }

                public class LblPopularServices extends LanguageSubModel {
                }

                public class LblFeaturedServices extends LanguageSubModel {
                }

                public class LblNewService extends LanguageSubModel {
                }

                public class LblViewOnMap extends LanguageSubModel {
                }

                public class LblBook extends LanguageSubModel {
                }

                public ToolbarTitle getToolbarTitle() {
                    return toolbarTitle;
                }

                public void setToolbarTitle(ToolbarTitle toolbarTitle) {
                    this.toolbarTitle = toolbarTitle;
                }

                public LblPopular getLblPopular() {
                    return lblPopular;
                }

                public void setLblPopular(LblPopular lblPopular) {
                    this.lblPopular = lblPopular;
                }

                public FEATURED getFEATURED() {
                    return fEATURED;
                }

                public void setFEATURED(FEATURED fEATURED) {
                    this.fEATURED = fEATURED;
                }

                public LblNewest getLblNewest() {
                    return lblNewest;
                }

                public void setLblNewest(LblNewest lblNewest) {
                    this.lblNewest = lblNewest;
                }

                public LblPopularServices getLblPopularServices() {
                    return lblPopularServices;
                }

                public void setLblPopularServices(LblPopularServices lblPopularServices) {
                    this.lblPopularServices = lblPopularServices;
                }

                public LblFeaturedServices getLblFeaturedServices() {
                    return lblFeaturedServices;
                }

                public void setLblFeaturedServices(LblFeaturedServices lblFeaturedServices) {
                    this.lblFeaturedServices = lblFeaturedServices;
                }

                public LblNewService getLblNewService() {
                    return lblNewService;
                }

                public void setLblNewService(LblNewService lblNewService) {
                    this.lblNewService = lblNewService;
                }

                public LblViewOnMap getLblViewOnMap() {
                    return lblViewOnMap;
                }

                public void setLblViewOnMap(LblViewOnMap lblViewOnMap) {
                    this.lblViewOnMap = lblViewOnMap;
                }

                public LblBook getLblBook() {
                    return lblBook;
                }

                public void setLblBook(LblBook lblBook) {
                    this.lblBook = lblBook;
                }

                public LblNewlyAddedService getLbl_newly_added_service() {
                    return lbl_newly_added_service;
                }

                public void setLbl_newly_added_service(LblNewlyAddedService lbl_newly_added_service) {
                    this.lbl_newly_added_service = lbl_newly_added_service;
                }
            }

            public class CreateService {

                @SerializedName("lbl_create_service")
                @Expose
                private LblCreateService lblCreateService;
                @SerializedName("lbl_information")
                @Expose
                private LblInformation lblInformation;
                @SerializedName("lbl_gallery")
                @Expose
                private LblGallery lblGallery;
                @SerializedName("lbl_title")
                @Expose
                private LblTitle lblTitle;
                @SerializedName("lbl_service_location")
                @Expose
                private LblServiceLocation lblServiceLocation;
                @SerializedName("lbl_hint_category")
                @Expose
                private LblHintCategory lblHintCategory;
                @SerializedName("lbl_hint_subcategory")
                @Expose
                private LblHintSubcategory lblHintSubcategory;
                @SerializedName("lbl_service_amount")
                @Expose
                private LblServiceAmount lblServiceAmount;
                @SerializedName("lbl_hint_Description")
                @Expose
                private LblHintDescription lblHintDescription;
                @SerializedName("lbl_browse_from_gallery")
                @Expose
                private LblBrowseFromGallery lblBrowseFromGallery;
                @SerializedName("lbl_upload")
                @Expose
                private LblUpload lblUpload;
                @SerializedName("lbl_service_next")
                @Expose
                private LblServiceNext lblServiceNext;
                @SerializedName("lbl_err_minimun_image")
                @Expose
                private LblErrMinimunImage lblErrMinimunImage;

                public class LblCreateService extends LanguageSubModel {
                }

                public class LblInformation extends LanguageSubModel {
                }

                public class LblGallery extends LanguageSubModel {
                }

                public class LblTitle extends LanguageSubModel {
                }

                public class LblHintCategory extends LanguageSubModel {
                }

                public class LblHintSubcategory extends LanguageSubModel {
                }

                public class LblServiceAmount extends LanguageSubModel {
                }

                public class LblHintDescription extends LanguageSubModel {
                }

                public class LblServiceLocation extends LanguageSubModel {
                }

                public class LblBrowseFromGallery extends LanguageSubModel {
                }

                public class LblUpload extends LanguageSubModel {
                }

                public class LblServiceNext extends LanguageSubModel {
                }

                public class LblErrMinimunImage extends LanguageSubModel {
                }

                public LblCreateService getLblCreateService() {
                    return lblCreateService;
                }

                public void setLblCreateService(LblCreateService lblCreateService) {
                    this.lblCreateService = lblCreateService;
                }

                public LblInformation getLblInformation() {
                    return lblInformation;
                }

                public void setLblInformation(LblInformation lblInformation) {
                    this.lblInformation = lblInformation;
                }

                public LblGallery getLblGallery() {
                    return lblGallery;
                }

                public void setLblGallery(LblGallery lblGallery) {
                    this.lblGallery = lblGallery;
                }

                public LblTitle getLblTitle() {
                    return lblTitle;
                }

                public void setLblTitle(LblTitle lblTitle) {
                    this.lblTitle = lblTitle;
                }

                public LblServiceLocation getLblServiceLocation() {
                    return lblServiceLocation;
                }

                public void setLblServiceLocation(LblServiceLocation lblServiceLocation) {
                    this.lblServiceLocation = lblServiceLocation;
                }

                public LblHintCategory getLblHintCategory() {
                    return lblHintCategory;
                }

                public void setLblHintCategory(LblHintCategory lblHintCategory) {
                    this.lblHintCategory = lblHintCategory;
                }

                public LblHintSubcategory getLblHintSubcategory() {
                    return lblHintSubcategory;
                }

                public void setLblHintSubcategory(LblHintSubcategory lblHintSubcategory) {
                    this.lblHintSubcategory = lblHintSubcategory;
                }

                public LblServiceAmount getLblServiceAmount() {
                    return lblServiceAmount;
                }

                public void setLblServiceAmount(LblServiceAmount lblServiceAmount) {
                    this.lblServiceAmount = lblServiceAmount;
                }

                public LblHintDescription getLblHintDescription() {
                    return lblHintDescription;
                }

                public void setLblHintDescription(LblHintDescription lblHintDescription) {
                    this.lblHintDescription = lblHintDescription;
                }

                public LblBrowseFromGallery getLblBrowseFromGallery() {
                    return lblBrowseFromGallery;
                }

                public void setLblBrowseFromGallery(LblBrowseFromGallery lblBrowseFromGallery) {
                    this.lblBrowseFromGallery = lblBrowseFromGallery;
                }

                public LblUpload getLblUpload() {
                    return lblUpload;
                }

                public void setLblUpload(LblUpload lblUpload) {
                    this.lblUpload = lblUpload;
                }

                public LblServiceNext getLblServiceNext() {
                    return lblServiceNext;
                }

                public void setLblServiceNext(LblServiceNext lblServiceNext) {
                    this.lblServiceNext = lblServiceNext;
                }

                public LblErrMinimunImage getLblErrMinimunImage() {
                    return lblErrMinimunImage;
                }

                public void setLblErrMinimunImage(LblErrMinimunImage lblErrMinimunImage) {
                    this.lblErrMinimunImage = lblErrMinimunImage;
                }
            }

            public class TabBarTitle {
                @SerializedName("tab_home")
                @Expose
                private TabHome tabHome;
                @SerializedName("tab_chat")
                @Expose
                private TabChat tabChat;
                @SerializedName("tab_bookings")
                @Expose
                private TabBookings tabBookings;
                @SerializedName("tab_settings")
                @Expose
                private TabSettings tabSettings;

                public class TabHome extends LanguageSubModel {
                }

                public class TabChat extends LanguageSubModel {
                }

                public class TabBookings extends LanguageSubModel {
                }

                public class TabSettings extends LanguageSubModel {
                }

                public TabHome getTabHome() {
                    return tabHome;
                }

                public void setTabHome(TabHome tabHome) {
                    this.tabHome = tabHome;
                }

                public TabChat getTabChat() {
                    return tabChat;
                }

                public void setTabChat(TabChat tabChat) {
                    this.tabChat = tabChat;
                }

                public TabBookings getTabBookings() {
                    return tabBookings;
                }

                public void setTabBookings(TabBookings tabBookings) {
                    this.tabBookings = tabBookings;
                }

                public TabSettings getTabSettings() {
                    return tabSettings;
                }

                public void setTabSettings(TabSettings tabSettings) {
                    this.tabSettings = tabSettings;
                }
            }

            public class ChatScreen {

                @SerializedName("lbl_no_chat")
                @Expose
                private LblNoChat lblNoChat;
                @SerializedName("txt_enter_message")
                @Expose
                private TxtEnterMessage txtEnterMessage;
                @SerializedName("lbl_chat_list_title")
                @Expose
                private LblChatListTitle lblChatListTitle;

                public class LblNoChat extends LanguageSubModel {
                }

                public class TxtEnterMessage extends LanguageSubModel {
                }

                public class LblChatListTitle extends LanguageSubModel {
                }

                public LblNoChat getLblNoChat() {
                    return lblNoChat;
                }

                public void setLblNoChat(LblNoChat lblNoChat) {
                    this.lblNoChat = lblNoChat;
                }

                public TxtEnterMessage getTxtEnterMessage() {
                    return txtEnterMessage;
                }

                public void setTxtEnterMessage(TxtEnterMessage txtEnterMessage) {
                    this.txtEnterMessage = txtEnterMessage;
                }

                public LblChatListTitle getLblChatListTitle() {
                    return lblChatListTitle;
                }

                public void setLblChatListTitle(LblChatListTitle lblChatListTitle) {
                    this.lblChatListTitle = lblChatListTitle;
                }
            }

            public class SettingsScreen {
                @SerializedName("lbl_settings_title")
                @Expose
                private LblSettingsTitle lblSettingsTitle;
                @SerializedName("lbl_regional")
                @Expose
                private LblRegional lblRegional;
                @SerializedName("lbl_others")
                @Expose
                private LblOthers lblOthers;
                @SerializedName("lbl_edit_profile")
                @Expose
                private LblEditProfile lblEditProfile;
                @SerializedName("lbl_notifications")
                @Expose
                private LblNotifications lblNotifications;
                @SerializedName("lbl_wallet")
                @Expose
                private LblWallet lblWallet;
                @SerializedName("lbl_language")
                @Expose
                private LblLanguage lblLanguage;
                @SerializedName("lbl_change_color")
                @Expose
                private LblChangeColor lblChangeColor;
                @SerializedName("lbl_change_location")
                @Expose
                private LblChangeLocation lblChangeLocation;
                @SerializedName("lbl_suggession")
                @Expose
                private LblSuggession lblSuggession;
                @SerializedName("lbl_tearms_and_condition")
                @Expose
                private LblTearmsAndCondition lblTearmsAndCondition;
                @SerializedName("lbl_share_app")
                @Expose
                private LblShareApp lblShareApp;
                @SerializedName("lbl_rate_app")
                @Expose
                private LblRateApp lblRateApp;
                @SerializedName("lbl_logout")
                @Expose
                private LblLogout lblLogout;
                @SerializedName("alrt_want_to_logout")
                @Expose
                private AlrtWantToLogout alrtWantToLogout;
                @SerializedName("lbl_choose_lang")
                @Expose
                private LblChooseLang lblChooseLang;
                @SerializedName("lbl_reference_code")
                @Expose
                private LblReferenceCode lblReferenceCode;
                @SerializedName("lbl_reference_text")
                @Expose
                private LblReferenceText lblReferenceText;
                @SerializedName("lbl_notification_list")
                @Expose
                private LblNotificationList lblNotificationList;
                @SerializedName("lbl_primary_color")
                @Expose
                private LblPrimaryColor lblPrimaryColor;
                @SerializedName("lbl_secondary_color")
                @Expose
                private LblSecondaryColor lblSecondaryColor;
                @SerializedName("lbl_location")
                @Expose
                private LblLocation lblLocation;
                @SerializedName("txt_notification")
                @Expose
                private TxtNotification txtNotification;
                @SerializedName("lbl_account_settings")
                @Expose
                private Lbl_account_settings lbl_account_settings;
                @SerializedName("lbl_account_type")
                @Expose
                private Lbl_account_type lbl_account_type;

                public class Lbl_account_type extends LanguageSubModel {
                }

                public class Lbl_account_settings extends LanguageSubModel {
                }

                public class LblSettingsTitle extends LanguageSubModel {
                }

                public class LblRegional extends LanguageSubModel {
                }

                public class LblOthers extends LanguageSubModel {
                }

                public class LblEditProfile extends LanguageSubModel {
                }

                public class LblNotifications extends LanguageSubModel {
                }

                public class LblWallet extends LanguageSubModel {
                }

                public class LblLanguage extends LanguageSubModel {
                }

                public class LblChangeColor extends LanguageSubModel {
                }

                public class LblChangeLocation extends LanguageSubModel {
                }

                public class LblSuggession extends LanguageSubModel {
                }

                public class LblTearmsAndCondition extends LanguageSubModel {
                }

                public class LblShareApp extends LanguageSubModel {
                }

                public class LblRateApp extends LanguageSubModel {
                }

                public class LblLogout extends LanguageSubModel {
                }

                public class AlrtWantToLogout extends LanguageSubModel {
                }

                public class LblChooseLang extends LanguageSubModel {
                }

                public class LblReferenceCode extends LanguageSubModel {
                }

                public class LblReferenceText extends LanguageSubModel {
                }

                public class LblNotificationList extends LanguageSubModel {
                }

                public class LblPrimaryColor extends LanguageSubModel {
                }

                public class LblSecondaryColor extends LanguageSubModel {
                }

                public class LblLocation extends LanguageSubModel {
                }

                public class TxtNotification extends LanguageSubModel {
                }

                public Lbl_account_settings getLbl_account_settings() {
                    return lbl_account_settings;
                }

                public void setLbl_account_settings(Lbl_account_settings lbl_account_settings) {
                    this.lbl_account_settings = lbl_account_settings;
                }

                public Lbl_account_type getLbl_account_type() {
                    return lbl_account_type;
                }

                public void setLbl_account_type(Lbl_account_type lbl_account_type) {
                    this.lbl_account_type = lbl_account_type;
                }

                public LblSettingsTitle getLblSettingsTitle() {
                    return lblSettingsTitle;
                }

                public void setLblSettingsTitle(LblSettingsTitle lblSettingsTitle) {
                    this.lblSettingsTitle = lblSettingsTitle;
                }

                public LblRegional getLblRegional() {
                    return lblRegional;
                }

                public void setLblRegional(LblRegional lblRegional) {
                    this.lblRegional = lblRegional;
                }

                public LblOthers getLblOthers() {
                    return lblOthers;
                }

                public void setLblOthers(LblOthers lblOthers) {
                    this.lblOthers = lblOthers;
                }

                public LblEditProfile getLblEditProfile() {
                    return lblEditProfile;
                }

                public void setLblEditProfile(LblEditProfile lblEditProfile) {
                    this.lblEditProfile = lblEditProfile;
                }

                public LblNotifications getLblNotifications() {
                    return lblNotifications;
                }

                public void setLblNotifications(LblNotifications lblNotifications) {
                    this.lblNotifications = lblNotifications;
                }

                public LblWallet getLblWallet() {
                    return lblWallet;
                }

                public void setLblWallet(LblWallet lblWallet) {
                    this.lblWallet = lblWallet;
                }

                public LblLanguage getLblLanguage() {
                    return lblLanguage;
                }

                public void setLblLanguage(LblLanguage lblLanguage) {
                    this.lblLanguage = lblLanguage;
                }

                public LblChangeColor getLblChangeColor() {
                    return lblChangeColor;
                }

                public void setLblChangeColor(LblChangeColor lblChangeColor) {
                    this.lblChangeColor = lblChangeColor;
                }

                public LblChangeLocation getLblChangeLocation() {
                    return lblChangeLocation;
                }

                public void setLblChangeLocation(LblChangeLocation lblChangeLocation) {
                    this.lblChangeLocation = lblChangeLocation;
                }

                public LblSuggession getLblSuggession() {
                    return lblSuggession;
                }

                public void setLblSuggession(LblSuggession lblSuggession) {
                    this.lblSuggession = lblSuggession;
                }

                public LblTearmsAndCondition getLblTearmsAndCondition() {
                    return lblTearmsAndCondition;
                }

                public void setLblTearmsAndCondition(LblTearmsAndCondition lblTearmsAndCondition) {
                    this.lblTearmsAndCondition = lblTearmsAndCondition;
                }

                public LblShareApp getLblShareApp() {
                    return lblShareApp;
                }

                public void setLblShareApp(LblShareApp lblShareApp) {
                    this.lblShareApp = lblShareApp;
                }

                public LblRateApp getLblRateApp() {
                    return lblRateApp;
                }

                public void setLblRateApp(LblRateApp lblRateApp) {
                    this.lblRateApp = lblRateApp;
                }

                public LblLogout getLblLogout() {
                    return lblLogout;
                }

                public void setLblLogout(LblLogout lblLogout) {
                    this.lblLogout = lblLogout;
                }

                public AlrtWantToLogout getAlrtWantToLogout() {
                    return alrtWantToLogout;
                }

                public void setAlrtWantToLogout(AlrtWantToLogout alrtWantToLogout) {
                    this.alrtWantToLogout = alrtWantToLogout;
                }

                public LblChooseLang getLblChooseLang() {
                    return lblChooseLang;
                }

                public void setLblChooseLang(LblChooseLang lblChooseLang) {
                    this.lblChooseLang = lblChooseLang;
                }

                public LblReferenceCode getLblReferenceCode() {
                    return lblReferenceCode;
                }

                public void setLblReferenceCode(LblReferenceCode lblReferenceCode) {
                    this.lblReferenceCode = lblReferenceCode;
                }

                public LblReferenceText getLblReferenceText() {
                    return lblReferenceText;
                }

                public void setLblReferenceText(LblReferenceText lblReferenceText) {
                    this.lblReferenceText = lblReferenceText;
                }

                public LblNotificationList getLblNotificationList() {
                    return lblNotificationList;
                }

                public void setLblNotificationList(LblNotificationList lblNotificationList) {
                    this.lblNotificationList = lblNotificationList;
                }

                public LblPrimaryColor getLblPrimaryColor() {
                    return lblPrimaryColor;
                }

                public void setLblPrimaryColor(LblPrimaryColor lblPrimaryColor) {
                    this.lblPrimaryColor = lblPrimaryColor;
                }

                public LblSecondaryColor getLblSecondaryColor() {
                    return lblSecondaryColor;
                }

                public void setLblSecondaryColor(LblSecondaryColor lblSecondaryColor) {
                    this.lblSecondaryColor = lblSecondaryColor;
                }

                public LblLocation getLblLocation() {
                    return lblLocation;
                }

                public void setLblLocation(LblLocation lblLocation) {
                    this.lblLocation = lblLocation;
                }

                public TxtNotification getTxtNotification() {
                    return txtNotification;
                }

                public void setTxtNotification(TxtNotification txtNotification) {
                    this.txtNotification = txtNotification;
                }
            }

            public class ProfileScreen {

                @SerializedName("txt_subscription_plan")
                @Expose
                private TxtSubscriptionPlan txtSubscriptionPlan;
                @SerializedName("txt_name")
                @Expose
                private TxtName txtName;
                @SerializedName("lbl_email")
                @Expose
                private LblEmail lblEmail;
                @SerializedName("lbl_mobile_number")
                @Expose
                private LblMobileNumber lblMobileNumber;
                @SerializedName("lbl_choose_currency")
                @Expose
                private LblChooseCurrency lblChooseCurrency;
                @SerializedName("lbl_Category")
                @Expose
                private LblCategory lblCategory;
                @SerializedName("lbl_sub_category")
                @Expose
                private LblSubCategory lblSubCategory;
                @SerializedName("lbl_no_category")
                @Expose
                private LblNoCategory lblNoCategory;
                @SerializedName("lbl_no_sub_category")
                @Expose
                private LblNoSubCategory lblNoSubCategory;
                @SerializedName("lbl_my_profile")
                @Expose
                private LblMyProfile lblMyProfile;
                @SerializedName("btn_availability")
                @Expose
                private BtnAvailability btnAvailability;
                @SerializedName("btn_update_profile")
                @Expose
                private BtnUpdateProfile btnUpdateProfile;

                public class TxtSubscriptionPlan extends LanguageSubModel {
                }

                public class TxtName extends LanguageSubModel {
                }

                public class LblEmail extends LanguageSubModel {
                }

                public class LblMobileNumber extends LanguageSubModel {
                }

                public class LblChooseCurrency extends LanguageSubModel {
                }

                public class LblCategory extends LanguageSubModel {
                }

                public class LblSubCategory extends LanguageSubModel {
                }

                public class LblNoCategory extends LanguageSubModel {
                }

                public class LblNoSubCategory extends LanguageSubModel {
                }

                public class LblMyProfile extends LanguageSubModel {
                }

                public class BtnAvailability extends LanguageSubModel {
                }

                public class BtnUpdateProfile extends LanguageSubModel {
                }

                public TxtSubscriptionPlan getTxtSubscriptionPlan() {
                    return txtSubscriptionPlan;
                }

                public void setTxtSubscriptionPlan(TxtSubscriptionPlan txtSubscriptionPlan) {
                    this.txtSubscriptionPlan = txtSubscriptionPlan;
                }

                public TxtName getTxtName() {
                    return txtName;
                }

                public void setTxtName(TxtName txtName) {
                    this.txtName = txtName;
                }

                public LblEmail getLblEmail() {
                    return lblEmail;
                }

                public void setLblEmail(LblEmail lblEmail) {
                    this.lblEmail = lblEmail;
                }

                public LblMobileNumber getLblMobileNumber() {
                    return lblMobileNumber;
                }

                public void setLblMobileNumber(LblMobileNumber lblMobileNumber) {
                    this.lblMobileNumber = lblMobileNumber;
                }

                public LblChooseCurrency getLblChooseCurrency() {
                    return lblChooseCurrency;
                }

                public void setLblChooseCurrency(LblChooseCurrency lblChooseCurrency) {
                    this.lblChooseCurrency = lblChooseCurrency;
                }

                public LblCategory getLblCategory() {
                    return lblCategory;
                }

                public void setLblCategory(LblCategory lblCategory) {
                    this.lblCategory = lblCategory;
                }

                public LblSubCategory getLblSubCategory() {
                    return lblSubCategory;
                }

                public void setLblSubCategory(LblSubCategory lblSubCategory) {
                    this.lblSubCategory = lblSubCategory;
                }

                public LblNoCategory getLblNoCategory() {
                    return lblNoCategory;
                }

                public void setLblNoCategory(LblNoCategory lblNoCategory) {
                    this.lblNoCategory = lblNoCategory;
                }

                public LblNoSubCategory getLblNoSubCategory() {
                    return lblNoSubCategory;
                }

                public void setLblNoSubCategory(LblNoSubCategory lblNoSubCategory) {
                    this.lblNoSubCategory = lblNoSubCategory;
                }

                public LblMyProfile getLblMyProfile() {
                    return lblMyProfile;
                }

                public void setLblMyProfile(LblMyProfile lblMyProfile) {
                    this.lblMyProfile = lblMyProfile;
                }

                public BtnAvailability getBtnAvailability() {
                    return btnAvailability;
                }

                public void setBtnAvailability(BtnAvailability btnAvailability) {
                    this.btnAvailability = btnAvailability;
                }

                public BtnUpdateProfile getBtnUpdateProfile() {
                    return btnUpdateProfile;
                }

                public void setBtnUpdateProfile(BtnUpdateProfile btnUpdateProfile) {
                    this.btnUpdateProfile = btnUpdateProfile;
                }
            }

            public class ProviderAvailabilityScreen {
                @SerializedName("lbl_provider_business_hrs")
                @Expose
                private LblProviderBusinessHrs lblProviderBusinessHrs;
                @SerializedName("lbl_saturday")
                @Expose
                private LblSaturday lblSaturday;
                @SerializedName("lbl_sunday")
                @Expose
                private LblSunday lblSunday;
                @SerializedName("lbl_monday")
                @Expose
                private LblMonday lblMonday;
                @SerializedName("lbl_tuesday")
                @Expose
                private LblTuesday lblTuesday;
                @SerializedName("lbl_wednesday")
                @Expose
                private LblWednesday lblWednesday;
                @SerializedName("lbl_thursday")
                @Expose
                private LblThursday lblThursday;
                @SerializedName("lbl_friday")
                @Expose
                private LblFriday lblFriday;
                @SerializedName("lbl_all_days")
                @Expose
                private LblAllDays lblAllDays;
                @SerializedName("alrt_select_from_and_to_time")
                @Expose
                private AlrtSelectFromAndToTime alrtSelectFromAndToTime;
                @SerializedName("alrt_select_days")
                @Expose
                private AlrtSelectDays alrtSelectDays;
                @SerializedName("alrt_select_from_time")
                @Expose
                private AlrtSelectFromTime alrtSelectFromTime;
                @SerializedName("alrt_select_to_time")
                @Expose
                private AlrtSelectToTime alrtSelectToTime;
                @SerializedName("alrt_select_dates")
                @Expose
                private AlrtSelectDates alrtSelectDates;
                @SerializedName("lbl_from")
                @Expose
                private LblFrom lbl_from;
                @SerializedName("lbl_to")
                @Expose
                private LblTo lbl_to;
                @SerializedName("lbl_any_one")
                @Expose
                private LblAnyOne lblAnyOne;
                @SerializedName("btn_submit")
                @Expose
                private BtnSubmit btn_submit;

                public class LblProviderBusinessHrs extends LanguageSubModel {
                }

                public class LblSaturday extends LanguageSubModel {
                }

                public class LblSunday extends LanguageSubModel {
                }

                public class LblMonday extends LanguageSubModel {
                }

                public class LblTuesday extends LanguageSubModel {
                }

                public class LblWednesday extends LanguageSubModel {
                }

                public class LblThursday extends LanguageSubModel {
                }

                public class LblFriday extends LanguageSubModel {
                }

                public class LblAllDays extends LanguageSubModel {
                }

                public class AlrtSelectFromAndToTime extends LanguageSubModel {
                }

                public class AlrtSelectDays extends LanguageSubModel {
                }

                public class AlrtSelectFromTime extends LanguageSubModel {
                }

                public class AlrtSelectToTime extends LanguageSubModel {
                }

                public class AlrtSelectDates extends LanguageSubModel {
                }

                public class LblFrom extends LanguageSubModel {
                }

                public class LblTo extends LanguageSubModel {
                }

                public class LblAnyOne extends LanguageSubModel {
                }

                public class BtnSubmit extends LanguageSubModel {
                }

                public LblProviderBusinessHrs getLblProviderBusinessHrs() {
                    return lblProviderBusinessHrs;
                }

                public void setLblProviderBusinessHrs(LblProviderBusinessHrs lblProviderBusinessHrs) {
                    this.lblProviderBusinessHrs = lblProviderBusinessHrs;
                }

                public LblSaturday getLblSaturday() {
                    return lblSaturday;
                }

                public void setLblSaturday(LblSaturday lblSaturday) {
                    this.lblSaturday = lblSaturday;
                }

                public LblSunday getLblSunday() {
                    return lblSunday;
                }

                public void setLblSunday(LblSunday lblSunday) {
                    this.lblSunday = lblSunday;
                }

                public LblMonday getLblMonday() {
                    return lblMonday;
                }

                public void setLblMonday(LblMonday lblMonday) {
                    this.lblMonday = lblMonday;
                }

                public LblTuesday getLblTuesday() {
                    return lblTuesday;
                }

                public void setLblTuesday(LblTuesday lblTuesday) {
                    this.lblTuesday = lblTuesday;
                }

                public LblWednesday getLblWednesday() {
                    return lblWednesday;
                }

                public void setLblWednesday(LblWednesday lblWednesday) {
                    this.lblWednesday = lblWednesday;
                }

                public LblThursday getLblThursday() {
                    return lblThursday;
                }

                public void setLblThursday(LblThursday lblThursday) {
                    this.lblThursday = lblThursday;
                }

                public LblFriday getLblFriday() {
                    return lblFriday;
                }

                public void setLblFriday(LblFriday lblFriday) {
                    this.lblFriday = lblFriday;
                }

                public LblAllDays getLblAllDays() {
                    return lblAllDays;
                }

                public void setLblAllDays(LblAllDays lblAllDays) {
                    this.lblAllDays = lblAllDays;
                }

                public AlrtSelectFromAndToTime getAlrtSelectFromAndToTime() {
                    return alrtSelectFromAndToTime;
                }

                public void setAlrtSelectFromAndToTime(AlrtSelectFromAndToTime alrtSelectFromAndToTime) {
                    this.alrtSelectFromAndToTime = alrtSelectFromAndToTime;
                }

                public AlrtSelectDays getAlrtSelectDays() {
                    return alrtSelectDays;
                }

                public void setAlrtSelectDays(AlrtSelectDays alrtSelectDays) {
                    this.alrtSelectDays = alrtSelectDays;
                }

                public AlrtSelectFromTime getAlrtSelectFromTime() {
                    return alrtSelectFromTime;
                }

                public void setAlrtSelectFromTime(AlrtSelectFromTime alrtSelectFromTime) {
                    this.alrtSelectFromTime = alrtSelectFromTime;
                }

                public AlrtSelectToTime getAlrtSelectToTime() {
                    return alrtSelectToTime;
                }

                public void setAlrtSelectToTime(AlrtSelectToTime alrtSelectToTime) {
                    this.alrtSelectToTime = alrtSelectToTime;
                }

                public AlrtSelectDates getAlrtSelectDates() {
                    return alrtSelectDates;
                }

                public void setAlrtSelectDates(AlrtSelectDates alrtSelectDates) {
                    this.alrtSelectDates = alrtSelectDates;
                }

                public LblFrom getLbl_from() {
                    return lbl_from;
                }

                public void setLbl_from(LblFrom lbl_from) {
                    this.lbl_from = lbl_from;
                }

                public LblTo getLbl_to() {
                    return lbl_to;
                }

                public void setLbl_to(LblTo lbl_to) {
                    this.lbl_to = lbl_to;
                }

                public LblAnyOne getLblAnyOne() {
                    return lblAnyOne;
                }

                public void setLblAnyOne(LblAnyOne lblAnyOne) {
                    this.lblAnyOne = lblAnyOne;
                }

                public BtnSubmit getBtn_submit() {
                    return btn_submit;
                }

                public void setBtn_submit(BtnSubmit btn_submit) {
                    this.btn_submit = btn_submit;
                }
            }

            public class WalletScreen {
                @SerializedName("wallet_title")
                @Expose
                private WalletTitle walletTitle;
                @SerializedName("lbl_transaction_history")
                @Expose
                private LblTransactionHistory lblTransactionHistory;
                @SerializedName("lbl_withdraw_fund")
                @Expose
                private LblWithdrawFund lblWithdrawFund;
                @SerializedName("lbl_total_wallet_balance")
                @Expose
                private LblTotalWalletBalance lblTotalWalletBalance;
                @SerializedName("lbl_tot_amt")
                @Expose
                private LblTotAmt lblTotAmt;
                @SerializedName("lbl_tax_amt")
                @Expose
                private LblTaxAmt lblTaxAmt;
                @SerializedName("lbl_no_trans_found")
                @Expose
                private LblNoTransFound lblNoTransFound;
                @SerializedName("lbl_contact_stripe_admin")
                @Expose
                private LblContactStripeAdmin lblContactStripeAdmin;
                @SerializedName("lbl_withdraw_wallet")
                @Expose
                private LblWithdrawWallet lblWithdrawWallet;
                @SerializedName("lbl_topup_wallet")
                @Expose
                private LblTopupWallet lblTopupWallet;
                @SerializedName("lbl_add_cash")
                @Expose
                private LblAddCash lblAddCash;
                @SerializedName("lbl_add_card")
                @Expose
                private LblAddCard lblAddCard;
                @SerializedName("lbl_current_bal")
                @Expose
                private LblCurrentBal lblCurrentBal;
                @SerializedName("lbl_debit_credit_card")
                @Expose
                private LblDebitCreditCard lblDebitCreditCard;
                @SerializedName("lbl_card_expiry")
                @Expose
                private LblCardExpiry lblCardExpiry;
                @SerializedName("lbl_cvv")
                @Expose
                private LblCvv lblCvv;
                @SerializedName("lbl_privacy_msg")
                @Expose
                private LblPrivacyMsg lblPrivacyMsg;
                @SerializedName("btn_withdraw_cash")
                @Expose
                private BtnWithdrawCash btnWithdrawCash;
                @SerializedName("btn_add_cash")
                @Expose
                private BtnAddCash btnAddCash;
                @SerializedName("txt_fld_card_num")
                @Expose
                private TxtFldCardNum txtFldCardNum;
                @SerializedName("txt_fld_exp_mnth")
                @Expose
                private TxtFldExpMnth txtFldExpMnth;
                @SerializedName("txt_fld_cvv")
                @Expose
                private TxtFldCvv txtFldCvv;
                @SerializedName("lbl_enter_less_than_one")
                @Expose
                private LblEnterLessThanOne lblEnterLessThanOne;
                @SerializedName("lbl_enter_amt_less_than_wallet")
                @Expose
                private LblEnterAmtLessThanWallet lblEnterAmtLessThanWallet;
                @SerializedName("lbl_enter_amt_to_proceed")
                @Expose
                private LblEnterAmtToProceed lblEnterAmtToProceed;
                @SerializedName("lbl_view_all")
                @Expose
                private LblViewAll lblViewAll;
                @SerializedName("txt_commission")
                @Expose
                private TxtCommission txtCommission;
                @SerializedName("lbl_card_number_to_proceed")
                @Expose
                private LblCardNumberProceed lblCardNumberProceed;
                @SerializedName("lbl_expiry_month_year")
                @Expose
                private LblExpiryMonthYear lblExpiryMonthYear;
                @SerializedName("lbl_enter_cvv")
                @Expose
                private LblEnterCvv lblEnterCvv;
                @SerializedName("lbl_valid_card_details")
                @Expose
                private LblValidCardDetails lblValidCardDetails;
                @SerializedName("lbl_enter_cash")
                @Expose
                private LblEnterCash lblEnterCash;

                public class LblEnterCash extends LanguageSubModel {
                }

                public class WalletTitle extends LanguageSubModel {
                }

                public class LblCardNumberProceed extends LanguageSubModel {
                }

                public class LblExpiryMonthYear extends LanguageSubModel {
                }

                public class LblEnterCvv extends LanguageSubModel {
                }

                public class LblTransactionHistory extends LanguageSubModel {
                }

                public class LblWithdrawFund extends LanguageSubModel {
                }

                public class LblTotalWalletBalance extends LanguageSubModel {
                }

                public class LblTotAmt extends LanguageSubModel {
                }

                public class LblTaxAmt extends LanguageSubModel {
                }

                public class LblNoTransFound extends LanguageSubModel {
                }

                public class LblContactStripeAdmin extends LanguageSubModel {
                }

                public class LblWithdrawWallet extends LanguageSubModel {
                }

                public class LblTopupWallet extends LanguageSubModel {
                }

                public class LblAddCash extends LanguageSubModel {
                }

                public class LblAddCard extends LanguageSubModel {
                }

                public class LblCurrentBal extends LanguageSubModel {
                }

                public class LblDebitCreditCard extends LanguageSubModel {
                }

                public class LblCvv extends LanguageSubModel {
                }

                public class LblCardExpiry extends LanguageSubModel {
                }

                public class LblPrivacyMsg extends LanguageSubModel {
                }

                public class BtnWithdrawCash extends LanguageSubModel {
                }

                public class BtnAddCash extends LanguageSubModel {
                }

                public class TxtFldCardNum extends LanguageSubModel {
                }

                public class TxtFldExpMnth extends LanguageSubModel {
                }

                public class TxtFldCvv extends LanguageSubModel {
                }

                public class LblEnterLessThanOne extends LanguageSubModel {
                }

                public class LblEnterAmtLessThanWallet extends LanguageSubModel {
                }

                public class LblEnterAmtToProceed extends LanguageSubModel {
                }

                public class LblViewAll extends LanguageSubModel {
                }

                public class TxtCommission extends LanguageSubModel {
                }

                public class LblValidCardDetails extends LanguageSubModel {
                }

                public LblValidCardDetails getLblValidCardDetails() {
                    return lblValidCardDetails;
                }

                public void setLblValidCardDetails(LblValidCardDetails lblValidCardDetails) {
                    this.lblValidCardDetails = lblValidCardDetails;
                }

                public WalletTitle getWalletTitle() {
                    return walletTitle;
                }

                public void setWalletTitle(WalletTitle walletTitle) {
                    this.walletTitle = walletTitle;
                }

                public LblTransactionHistory getLblTransactionHistory() {
                    return lblTransactionHistory;
                }

                public void setLblTransactionHistory(LblTransactionHistory lblTransactionHistory) {
                    this.lblTransactionHistory = lblTransactionHistory;
                }

                public LblWithdrawFund getLblWithdrawFund() {
                    return lblWithdrawFund;
                }

                public void setLblWithdrawFund(LblWithdrawFund lblWithdrawFund) {
                    this.lblWithdrawFund = lblWithdrawFund;
                }

                public LblTotalWalletBalance getLblTotalWalletBalance() {
                    return lblTotalWalletBalance;
                }

                public void setLblTotalWalletBalance(LblTotalWalletBalance lblTotalWalletBalance) {
                    this.lblTotalWalletBalance = lblTotalWalletBalance;
                }

                public LblTotAmt getLblTotAmt() {
                    return lblTotAmt;
                }

                public void setLblTotAmt(LblTotAmt lblTotAmt) {
                    this.lblTotAmt = lblTotAmt;
                }

                public LblTaxAmt getLblTaxAmt() {
                    return lblTaxAmt;
                }

                public void setLblTaxAmt(LblTaxAmt lblTaxAmt) {
                    this.lblTaxAmt = lblTaxAmt;
                }

                public LblNoTransFound getLblNoTransFound() {
                    return lblNoTransFound;
                }

                public void setLblNoTransFound(LblNoTransFound lblNoTransFound) {
                    this.lblNoTransFound = lblNoTransFound;
                }

                public LblContactStripeAdmin getLblContactStripeAdmin() {
                    return lblContactStripeAdmin;
                }

                public void setLblContactStripeAdmin(LblContactStripeAdmin lblContactStripeAdmin) {
                    this.lblContactStripeAdmin = lblContactStripeAdmin;
                }

                public LblWithdrawWallet getLblWithdrawWallet() {
                    return lblWithdrawWallet;
                }

                public void setLblWithdrawWallet(LblWithdrawWallet lblWithdrawWallet) {
                    this.lblWithdrawWallet = lblWithdrawWallet;
                }

                public LblTopupWallet getLblTopupWallet() {
                    return lblTopupWallet;
                }

                public void setLblTopupWallet(LblTopupWallet lblTopupWallet) {
                    this.lblTopupWallet = lblTopupWallet;
                }

                public LblAddCash getLblAddCash() {
                    return lblAddCash;
                }

                public void setLblAddCash(LblAddCash lblAddCash) {
                    this.lblAddCash = lblAddCash;
                }

                public LblAddCard getLblAddCard() {
                    return lblAddCard;
                }

                public void setLblAddCard(LblAddCard lblAddCard) {
                    this.lblAddCard = lblAddCard;
                }

                public LblCurrentBal getLblCurrentBal() {
                    return lblCurrentBal;
                }

                public void setLblCurrentBal(LblCurrentBal lblCurrentBal) {
                    this.lblCurrentBal = lblCurrentBal;
                }

                public LblDebitCreditCard getLblDebitCreditCard() {
                    return lblDebitCreditCard;
                }

                public void setLblDebitCreditCard(LblDebitCreditCard lblDebitCreditCard) {
                    this.lblDebitCreditCard = lblDebitCreditCard;
                }

                public LblCardExpiry getLblCardExpiry() {
                    return lblCardExpiry;
                }

                public void setLblCardExpiry(LblCardExpiry lblCardExpiry) {
                    this.lblCardExpiry = lblCardExpiry;
                }

                public LblCvv getLblCvv() {
                    return lblCvv;
                }

                public void setLblCvv(LblCvv lblCvv) {
                    this.lblCvv = lblCvv;
                }

                public LblPrivacyMsg getLblPrivacyMsg() {
                    return lblPrivacyMsg;
                }

                public void setLblPrivacyMsg(LblPrivacyMsg lblPrivacyMsg) {
                    this.lblPrivacyMsg = lblPrivacyMsg;
                }

                public BtnWithdrawCash getBtnWithdrawCash() {
                    return btnWithdrawCash;
                }

                public void setBtnWithdrawCash(BtnWithdrawCash btnWithdrawCash) {
                    this.btnWithdrawCash = btnWithdrawCash;
                }

                public BtnAddCash getBtnAddCash() {
                    return btnAddCash;
                }

                public void setBtnAddCash(BtnAddCash btnAddCash) {
                    this.btnAddCash = btnAddCash;
                }

                public TxtFldCardNum getTxtFldCardNum() {
                    return txtFldCardNum;
                }

                public void setTxtFldCardNum(TxtFldCardNum txtFldCardNum) {
                    this.txtFldCardNum = txtFldCardNum;
                }

                public TxtFldExpMnth getTxtFldExpMnth() {
                    return txtFldExpMnth;
                }

                public void setTxtFldExpMnth(TxtFldExpMnth txtFldExpMnth) {
                    this.txtFldExpMnth = txtFldExpMnth;
                }

                public TxtFldCvv getTxtFldCvv() {
                    return txtFldCvv;
                }

                public void setTxtFldCvv(TxtFldCvv txtFldCvv) {
                    this.txtFldCvv = txtFldCvv;
                }

                public LblEnterLessThanOne getLblEnterLessThanOne() {
                    return lblEnterLessThanOne;
                }

                public void setLblEnterLessThanOne(LblEnterLessThanOne lblEnterLessThanOne) {
                    this.lblEnterLessThanOne = lblEnterLessThanOne;
                }

                public LblEnterAmtLessThanWallet getLblEnterAmtLessThanWallet() {
                    return lblEnterAmtLessThanWallet;
                }

                public void setLblEnterAmtLessThanWallet(LblEnterAmtLessThanWallet lblEnterAmtLessThanWallet) {
                    this.lblEnterAmtLessThanWallet = lblEnterAmtLessThanWallet;
                }

                public LblEnterAmtToProceed getLblEnterAmtToProceed() {
                    return lblEnterAmtToProceed;
                }

                public void setLblEnterAmtToProceed(LblEnterAmtToProceed lblEnterAmtToProceed) {
                    this.lblEnterAmtToProceed = lblEnterAmtToProceed;
                }

                public LblViewAll getLblViewAll() {
                    return lblViewAll;
                }

                public void setLblViewAll(LblViewAll lblViewAll) {
                    this.lblViewAll = lblViewAll;
                }

                public TxtCommission getTxtCommission() {
                    return txtCommission;
                }

                public void setTxtCommission(TxtCommission txtCommission) {
                    this.txtCommission = txtCommission;
                }

                public LblCardNumberProceed getLblCardNumberProceed() {
                    return lblCardNumberProceed;
                }

                public void setLblCardNumberProceed(LblCardNumberProceed lblCardNumberProceed) {
                    this.lblCardNumberProceed = lblCardNumberProceed;
                }

                public LblExpiryMonthYear getLblExpiryMonthYear() {
                    return lblExpiryMonthYear;
                }

                public void setLblExpiryMonthYear(LblExpiryMonthYear lblExpiryMonthYear) {
                    this.lblExpiryMonthYear = lblExpiryMonthYear;
                }

                public LblEnterCvv getLblEnterCvv() {
                    return lblEnterCvv;
                }

                public void setLblEnterCvv(LblEnterCvv lblEnterCvv) {
                    this.lblEnterCvv = lblEnterCvv;
                }

                public LblEnterCash getLblEnterCash() {
                    return lblEnterCash;
                }

                public void setLblEnterCash(LblEnterCash lblEnterCash) {
                    this.lblEnterCash = lblEnterCash;
                }
            }

            public class SubscriptionScreen {
                @SerializedName("lbl_buy_subscription")
                @Expose
                private LblBuySubscription lblBuySubscription;
                @SerializedName("lbl_thank_you_upgrade")
                @Expose
                private LblThankYouUpgrade lblThankYouUpgrade;
                @SerializedName("btn_go_subscription")
                @Expose
                private BtnGoSubscription btnGoSubscription;
                @SerializedName("lbl_title_subscription")
                @Expose
                private LblTitleSubscription lblTitleSubscription;
                @SerializedName("lbl_buy_now")
                @Expose
                private LblBuyNow lblBuyNow;
                @SerializedName("lbl_skip_now")
                @Expose
                private LblSkipNow lblSkipNow;
                @SerializedName("lbl_subscripe_now")
                @Expose
                private LblSubscripeNow lblSubscripeNow;
                @SerializedName("lbl_thankyou_desc")
                @Expose
                private LblThankyouDesc lblThankyouDesc;

                public class LblBuySubscription extends LanguageSubModel {
                }

                public class LblThankYouUpgrade extends LanguageSubModel {
                }

                public class BtnGoSubscription extends LanguageSubModel {
                }

                public class LblTitleSubscription extends LanguageSubModel {
                }

                public class LblBuyNow extends LanguageSubModel {
                }

                public class LblSkipNow extends LanguageSubModel {
                }

                public class LblSubscripeNow extends LanguageSubModel {
                }

                public class LblThankyouDesc extends LanguageSubModel {
                }

                public LblBuySubscription getLblBuySubscription() {
                    return lblBuySubscription;
                }

                public void setLblBuySubscription(LblBuySubscription lblBuySubscription) {
                    this.lblBuySubscription = lblBuySubscription;
                }

                public LblThankYouUpgrade getLblThankYouUpgrade() {
                    return lblThankYouUpgrade;
                }

                public void setLblThankYouUpgrade(LblThankYouUpgrade lblThankYouUpgrade) {
                    this.lblThankYouUpgrade = lblThankYouUpgrade;
                }

                public BtnGoSubscription getBtnGoSubscription() {
                    return btnGoSubscription;
                }

                public void setBtnGoSubscription(BtnGoSubscription btnGoSubscription) {
                    this.btnGoSubscription = btnGoSubscription;
                }

                public LblTitleSubscription getLblTitleSubscription() {
                    return lblTitleSubscription;
                }

                public void setLblTitleSubscription(LblTitleSubscription lblTitleSubscription) {
                    this.lblTitleSubscription = lblTitleSubscription;
                }

                public LblBuyNow getLblBuyNow() {
                    return lblBuyNow;
                }

                public void setLblBuyNow(LblBuyNow lblBuyNow) {
                    this.lblBuyNow = lblBuyNow;
                }

                public LblSkipNow getLblSkipNow() {
                    return lblSkipNow;
                }

                public void setLblSkipNow(LblSkipNow lblSkipNow) {
                    this.lblSkipNow = lblSkipNow;
                }

                public LblSubscripeNow getLblSubscripeNow() {
                    return lblSubscripeNow;
                }

                public void setLblSubscripeNow(LblSubscripeNow lblSubscripeNow) {
                    this.lblSubscripeNow = lblSubscripeNow;
                }

                public LblThankyouDesc getLblThankyouDesc() {
                    return lblThankyouDesc;
                }

                public void setLblThankyouDesc(LblThankyouDesc lblThankyouDesc) {
                    this.lblThankyouDesc = lblThankyouDesc;
                }
            }

            public class BookingDetailService {
                @SerializedName("lbl_overview")
                @Expose
                private LblOverview lblOverview;
                @SerializedName("lbl_about_buyer")
                @Expose
                private LblAboutBuyer lblAboutBuyer;
                @SerializedName("lbl_booking_info")
                @Expose
                private LblBookingInfo lblBookingInfo;
                @SerializedName("btn_accept_request")
                @Expose
                private BtnAcceptRequest btnAcceptRequest;
                @SerializedName("btn_reject_request")
                @Expose
                private BtnRejectRequest btnRejectRequest;
                @SerializedName("lbl_location")
                @Expose
                private LblLocation lblLocation;
                @SerializedName("lbl_appoinment_slot")
                @Expose
                private LblAppoinmentSlot lblAppoinmentSlot;
                @SerializedName("lbl_message_from_buyer")
                @Expose
                private LblMessageFromBuyer lblMessageFromBuyer;
                @SerializedName("lbl_admin_comments")
                @Expose
                private LblAdminComments lblAdminComments;
                @SerializedName("lbl_rejected_reason")
                @Expose
                private LblRejectedReason lblRejectedReason;
                @SerializedName("lbl_views")
                @Expose
                private LblViews lblViews;
                @SerializedName("lbl_description")
                @Expose
                private LblDescription lblDescription;
                @SerializedName("lbl_to_time")
                @Expose
                private LblToTime lblToTime;
                @SerializedName("lbl_from_time")
                @Expose
                private LblFromTime lblFromTime;
                @SerializedName("lbl_rate_now")
                @Expose
                private LblRateNow lblRateNow;
                @SerializedName("lbl_service_offered")
                @Expose
                private LblServiceOffered lblServiceOffered;
                @SerializedName("lbl_about_seller")
                @Expose
                private LblAboutSeller lblAboutSeller;
                @SerializedName("lbl_no-services_found")
                @Expose
                private LblNoServicesFound lblNoServicesFound;
                @SerializedName("lbl_no_reviews_found")
                @Expose
                private LblNoReviewsFound lblNoReviewsFound;
                @SerializedName("lbl_your_location")
                @Expose
                private LblYourLocation lblYourLocation;
                @SerializedName("lbl_service_experience")
                @Expose
                private LblServiceExperience lblServiceExperience;
                @SerializedName("lbl_cancel_service")
                @Expose
                private LblCancelService lblCancelService;
                @SerializedName("lbl_view_on_map")
                @Expose
                private LblViewMap lblViewMap;
                @SerializedName("lbl_from")
                @Expose
                private LblFrom lbl_from;
                @SerializedName("lbl_to")
                @Expose
                private LblTo lbl_to;
                @SerializedName("lbl_other_services")
                @Expose
                private Lbl_other_services lbl_other_services;

                public class LblFrom extends LanguageSubModel {
                }

                public class Lbl_other_services extends LanguageSubModel {
                }

                public class LblTo extends LanguageSubModel {
                }

                public class LblOverview extends LanguageSubModel {
                }

                public class LblAboutBuyer extends LanguageSubModel {
                }

                public class LblBookingInfo extends LanguageSubModel {
                }

                public class BtnAcceptRequest extends LanguageSubModel {
                }

                public class BtnRejectRequest extends LanguageSubModel {
                }

                public class LblLocation extends LanguageSubModel {
                }

                public class LblAppoinmentSlot extends LanguageSubModel {
                }

                public class LblMessageFromBuyer extends LanguageSubModel {
                }

                public class LblAdminComments extends LanguageSubModel {
                }

                public class LblRejectedReason extends LanguageSubModel {
                }

                public class LblViews extends LanguageSubModel {
                }

                public class LblDescription extends LanguageSubModel {
                }

                public class LblToTime extends LanguageSubModel {
                }

                public class LblFromTime extends LanguageSubModel {
                }

                public class LblRateNow extends LanguageSubModel {
                }

                public class LblServiceOffered extends LanguageSubModel {
                }

                public class LblAboutSeller extends LanguageSubModel {
                }

                public class LblNoServicesFound extends LanguageSubModel {
                }

                public class LblNoReviewsFound extends LanguageSubModel {
                }

                public class LblYourLocation extends LanguageSubModel {
                }

                public class LblServiceExperience extends LanguageSubModel {
                }

                public class LblCancelService extends LanguageSubModel {
                }

                public class LblViewMap extends LanguageSubModel {
                }

                public LblOverview getLblOverview() {
                    return lblOverview;
                }

                public void setLblOverview(LblOverview lblOverview) {
                    this.lblOverview = lblOverview;
                }

                public LblAboutBuyer getLblAboutBuyer() {
                    return lblAboutBuyer;
                }

                public void setLblAboutBuyer(LblAboutBuyer lblAboutBuyer) {
                    this.lblAboutBuyer = lblAboutBuyer;
                }

                public LblBookingInfo getLblBookingInfo() {
                    return lblBookingInfo;
                }

                public void setLblBookingInfo(LblBookingInfo lblBookingInfo) {
                    this.lblBookingInfo = lblBookingInfo;
                }

                public BtnAcceptRequest getBtnAcceptRequest() {
                    return btnAcceptRequest;
                }

                public void setBtnAcceptRequest(BtnAcceptRequest btnAcceptRequest) {
                    this.btnAcceptRequest = btnAcceptRequest;
                }

                public BtnRejectRequest getBtnRejectRequest() {
                    return btnRejectRequest;
                }

                public void setBtnRejectRequest(BtnRejectRequest btnRejectRequest) {
                    this.btnRejectRequest = btnRejectRequest;
                }

                public LblLocation getLblLocation() {
                    return lblLocation;
                }

                public void setLblLocation(LblLocation lblLocation) {
                    this.lblLocation = lblLocation;
                }

                public LblAppoinmentSlot getLblAppoinmentSlot() {
                    return lblAppoinmentSlot;
                }

                public void setLblAppoinmentSlot(LblAppoinmentSlot lblAppoinmentSlot) {
                    this.lblAppoinmentSlot = lblAppoinmentSlot;
                }

                public LblMessageFromBuyer getLblMessageFromBuyer() {
                    return lblMessageFromBuyer;
                }

                public void setLblMessageFromBuyer(LblMessageFromBuyer lblMessageFromBuyer) {
                    this.lblMessageFromBuyer = lblMessageFromBuyer;
                }

                public LblAdminComments getLblAdminComments() {
                    return lblAdminComments;
                }

                public void setLblAdminComments(LblAdminComments lblAdminComments) {
                    this.lblAdminComments = lblAdminComments;
                }

                public LblRejectedReason getLblRejectedReason() {
                    return lblRejectedReason;
                }

                public void setLblRejectedReason(LblRejectedReason lblRejectedReason) {
                    this.lblRejectedReason = lblRejectedReason;
                }

                public LblViews getLblViews() {
                    return lblViews;
                }

                public void setLblViews(LblViews lblViews) {
                    this.lblViews = lblViews;
                }

                public LblDescription getLblDescription() {
                    return lblDescription;
                }

                public void setLblDescription(LblDescription lblDescription) {
                    this.lblDescription = lblDescription;
                }

                public LblToTime getLblToTime() {
                    return lblToTime;
                }

                public void setLblToTime(LblToTime lblToTime) {
                    this.lblToTime = lblToTime;
                }

                public LblFromTime getLblFromTime() {
                    return lblFromTime;
                }

                public void setLblFromTime(LblFromTime lblFromTime) {
                    this.lblFromTime = lblFromTime;
                }

                public LblRateNow getLblRateNow() {
                    return lblRateNow;
                }

                public void setLblRateNow(LblRateNow lblRateNow) {
                    this.lblRateNow = lblRateNow;
                }

                public LblServiceOffered getLblServiceOffered() {
                    return lblServiceOffered;
                }

                public void setLblServiceOffered(LblServiceOffered lblServiceOffered) {
                    this.lblServiceOffered = lblServiceOffered;
                }

                public LblAboutSeller getLblAboutSeller() {
                    return lblAboutSeller;
                }

                public void setLblAboutSeller(LblAboutSeller lblAboutSeller) {
                    this.lblAboutSeller = lblAboutSeller;
                }

                public LblNoServicesFound getLblNoServicesFound() {
                    return lblNoServicesFound;
                }

                public void setLblNoServicesFound(LblNoServicesFound lblNoServicesFound) {
                    this.lblNoServicesFound = lblNoServicesFound;
                }

                public LblNoReviewsFound getLblNoReviewsFound() {
                    return lblNoReviewsFound;
                }

                public void setLblNoReviewsFound(LblNoReviewsFound lblNoReviewsFound) {
                    this.lblNoReviewsFound = lblNoReviewsFound;
                }

                public LblYourLocation getLblYourLocation() {
                    return lblYourLocation;
                }

                public void setLblYourLocation(LblYourLocation lblYourLocation) {
                    this.lblYourLocation = lblYourLocation;
                }

                public LblServiceExperience getLblServiceExperience() {
                    return lblServiceExperience;
                }

                public void setLblServiceExperience(LblServiceExperience lblServiceExperience) {
                    this.lblServiceExperience = lblServiceExperience;
                }

                public LblCancelService getLblCancelService() {
                    return lblCancelService;
                }

                public void setLblCancelService(LblCancelService lblCancelService) {
                    this.lblCancelService = lblCancelService;
                }

                public LblViewMap getLblViewMap() {
                    return lblViewMap;
                }

                public void setLblViewMap(LblViewMap lblViewMap) {
                    this.lblViewMap = lblViewMap;
                }

                public LblFrom getLbl_from() {
                    return lbl_from;
                }

                public void setLbl_from(LblFrom lbl_from) {
                    this.lbl_from = lbl_from;
                }

                public LblTo getLbl_to() {
                    return lbl_to;
                }

                public void setLbl_to(LblTo lbl_to) {
                    this.lbl_to = lbl_to;
                }

                public Lbl_other_services getLbl_other_services() {
                    return lbl_other_services;
                }

                public void setLbl_other_services(Lbl_other_services lbl_other_services) {
                    this.lbl_other_services = lbl_other_services;
                }
            }

            public class BookingService {
                @SerializedName("lbl_pending")
                @Expose
                private LblPending lblPending;
                @SerializedName("lbl_inprogress")
                @Expose
                private LblInprogress lblInprogress;
                @SerializedName("lbl_completed")
                @Expose
                private LblCompleted lblCompleted;
                @SerializedName("lbl_cancelled")
                @Expose
                private LblCancelled lblCancelled;
                @SerializedName("lbl_choose_service_type")
                @Expose
                private LblChooseServiceType lblChooseServiceType;
                @SerializedName("lbl_choose_type")
                @Expose
                private LblChooseType lblChooseType;
                @SerializedName("lbl_active_your_service")
                @Expose
                private LblActiveYourService lblActiveYourService;
                @SerializedName("lbl_inactive_your_service")
                @Expose
                private LblInactiveYourService lblInactiveYourService;
                @SerializedName("lbl_service_detail")
                @Expose
                private LblServiceDetail lblServiceDetail;
                @SerializedName("lbl_mark_as_complete")
                @Expose
                private LblMarkAsComplete lblMarkAsComplete;
                @SerializedName("lbl_book_services")
                @Expose
                private LblBookServices lblBookServices;
                @SerializedName("lbl_time_date")
                @Expose
                private LblTimeDate lblTimeDate;
                @SerializedName("lbl_location")
                @Expose
                private LblLocation lblLocation;
                @SerializedName("lbl_description")
                @Expose
                private LblDescription lblDescription;
                @SerializedName("lbl_calendar")
                @Expose
                private LblCalendar lblCalendar;
                @SerializedName("lbl_message_to_provider")
                @Expose
                private LblMessageToProvider lblMessageToProvider;
                @SerializedName("lbl_payment_details")
                @Expose
                private LblPaymentDetails lblPaymentDetails;
                @SerializedName("lbl_from")
                @Expose
                private LblFrom lblFrom;
                @SerializedName("lbl_to")
                @Expose
                private LblTo lblTo;
                @SerializedName("lbl_timings")
                @Expose
                private LblTimings lblTimings;
                @SerializedName("lbl_leave_description")
                @Expose
                private LblLeaveDescription lblLeaveDescription;
                @SerializedName("lbl_insufficient_balance")
                @Expose
                private LblInsufficientBalance lblInsufficientBalance;
                @SerializedName("lbl_book_now")
                @Expose
                private LblBookNow lblBookNow;
                @SerializedName("txt_view_leave_comments")
                @Expose
                private TxtViewLeaveComments txtViewLeaveComments;
                @SerializedName("lbl_reviews")
                @Expose
                private LblReviews lblReviews;
                @SerializedName("lbl_other_related_service")
                @Expose
                private LblOtherRelatedService lblOtherRelatedService;
                @SerializedName("lbl_accepted")
                @Expose
                private LblAccepted lblAccepted;
                @SerializedName("lbl_select_review_type")
                @Expose
                private LblSelectReviewType lblSelectReviewType;
                @SerializedName("lbl_map")
                @Expose
                private LblMap lbl_map;
                @SerializedName("lbl_rejected")
                @Expose
                private LblRejected lbl_rejected;
                @SerializedName("lbl_delete_title")
                @Expose
                private LblDeleteTitle lbl_delete_title;
                @SerializedName("lbl_yes")
                @Expose
                private Lbl_yes lbl_yes;
                @SerializedName("lbl_no")
                @Expose
                private Lbl_no lbl_no;

                public class Lbl_yes extends LanguageSubModel {
                }

                public class Lbl_no extends LanguageSubModel {
                }

                public class LblDeleteTitle extends LanguageSubModel {
                }

                public class LblRejected extends LanguageSubModel {
                }

                public class LblPending extends LanguageSubModel {
                }

                public class LblMap extends LanguageSubModel {
                }

                public class LblBookServices extends LanguageSubModel {
                }

                public class LblLeaveDescription extends LanguageSubModel {
                }

                public class LblInsufficientBalance extends LanguageSubModel {
                }

                public class LblBookNow extends LanguageSubModel {
                }

                public class TxtViewLeaveComments extends LanguageSubModel {
                }

                public class LblReviews extends LanguageSubModel {
                }

                public class LblOtherRelatedService extends LanguageSubModel {
                }

                public class LblAccepted extends LanguageSubModel {
                }

                public class LblSelectReviewType extends LanguageSubModel {
                }

                public class LblPaymentDetails extends LanguageSubModel {
                }

                public class LblFrom extends LanguageSubModel {
                }

                public class LblTo extends LanguageSubModel {
                }

                public class LblTimings extends LanguageSubModel {
                }

                public class LblTimeDate extends LanguageSubModel {
                }

                public class LblLocation extends LanguageSubModel {
                }

                public class LblDescription extends LanguageSubModel {
                }

                public class LblCalendar extends LanguageSubModel {
                }

                public class LblMessageToProvider extends LanguageSubModel {
                }

                public class LblInprogress extends LanguageSubModel {
                }

                public class LblCompleted extends LanguageSubModel {
                }

                public class LblCancelled extends LanguageSubModel {
                }

                public class LblChooseServiceType extends LanguageSubModel {
                }

                public class LblChooseType extends LanguageSubModel {
                }

                public class LblActiveYourService extends LanguageSubModel {
                }

                public class LblInactiveYourService extends LanguageSubModel {
                }

                public class LblServiceDetail extends LanguageSubModel {
                }

                public class LblMarkAsComplete extends LanguageSubModel {
                }

                public Lbl_yes getLbl_yes() {
                    return lbl_yes;
                }

                public void setLbl_yes(Lbl_yes lbl_yes) {
                    this.lbl_yes = lbl_yes;
                }

                public Lbl_no getLbl_no() {
                    return lbl_no;
                }

                public void setLbl_no(Lbl_no lbl_no) {
                    this.lbl_no = lbl_no;
                }

                public LblDeleteTitle getLbl_delete_title() {
                    return lbl_delete_title;
                }

                public void setLbl_delete_title(LblDeleteTitle lbl_delete_title) {
                    this.lbl_delete_title = lbl_delete_title;
                }

                public LblPending getLblPending() {
                    return lblPending;
                }

                public void setLblPending(LblPending lblPending) {
                    this.lblPending = lblPending;
                }

                public LblInprogress getLblInprogress() {
                    return lblInprogress;
                }

                public void setLblInprogress(LblInprogress lblInprogress) {
                    this.lblInprogress = lblInprogress;
                }

                public LblCompleted getLblCompleted() {
                    return lblCompleted;
                }

                public void setLblCompleted(LblCompleted lblCompleted) {
                    this.lblCompleted = lblCompleted;
                }

                public LblCancelled getLblCancelled() {
                    return lblCancelled;
                }

                public void setLblCancelled(LblCancelled lblCancelled) {
                    this.lblCancelled = lblCancelled;
                }

                public LblChooseServiceType getLblChooseServiceType() {
                    return lblChooseServiceType;
                }

                public void setLblChooseServiceType(LblChooseServiceType lblChooseServiceType) {
                    this.lblChooseServiceType = lblChooseServiceType;
                }

                public LblChooseType getLblChooseType() {
                    return lblChooseType;
                }

                public void setLblChooseType(LblChooseType lblChooseType) {
                    this.lblChooseType = lblChooseType;
                }

                public LblActiveYourService getLblActiveYourService() {
                    return lblActiveYourService;
                }

                public void setLblActiveYourService(LblActiveYourService lblActiveYourService) {
                    this.lblActiveYourService = lblActiveYourService;
                }

                public LblInactiveYourService getLblInactiveYourService() {
                    return lblInactiveYourService;
                }

                public void setLblInactiveYourService(LblInactiveYourService lblInactiveYourService) {
                    this.lblInactiveYourService = lblInactiveYourService;
                }

                public LblServiceDetail getLblServiceDetail() {
                    return lblServiceDetail;
                }

                public void setLblServiceDetail(LblServiceDetail lblServiceDetail) {
                    this.lblServiceDetail = lblServiceDetail;
                }

                public LblMarkAsComplete getLblMarkAsComplete() {
                    return lblMarkAsComplete;
                }

                public void setLblMarkAsComplete(LblMarkAsComplete lblMarkAsComplete) {
                    this.lblMarkAsComplete = lblMarkAsComplete;
                }

                public LblBookServices getLblBookServices() {
                    return lblBookServices;
                }

                public void setLblBookServices(LblBookServices lblBookServices) {
                    this.lblBookServices = lblBookServices;
                }

                public LblTimeDate getLblTimeDate() {
                    return lblTimeDate;
                }

                public void setLblTimeDate(LblTimeDate lblTimeDate) {
                    this.lblTimeDate = lblTimeDate;
                }

                public LblLocation getLblLocation() {
                    return lblLocation;
                }

                public void setLblLocation(LblLocation lblLocation) {
                    this.lblLocation = lblLocation;
                }

                public LblDescription getLblDescription() {
                    return lblDescription;
                }

                public void setLblDescription(LblDescription lblDescription) {
                    this.lblDescription = lblDescription;
                }

                public LblCalendar getLblCalendar() {
                    return lblCalendar;
                }

                public void setLblCalendar(LblCalendar lblCalendar) {
                    this.lblCalendar = lblCalendar;
                }

                public LblMessageToProvider getLblMessageToProvider() {
                    return lblMessageToProvider;
                }

                public void setLblMessageToProvider(LblMessageToProvider lblMessageToProvider) {
                    this.lblMessageToProvider = lblMessageToProvider;
                }

                public LblPaymentDetails getLblPaymentDetails() {
                    return lblPaymentDetails;
                }

                public void setLblPaymentDetails(LblPaymentDetails lblPaymentDetails) {
                    this.lblPaymentDetails = lblPaymentDetails;
                }

                public LblFrom getLblFrom() {
                    return lblFrom;
                }

                public void setLblFrom(LblFrom lblFrom) {
                    this.lblFrom = lblFrom;
                }

                public LblTo getLblTo() {
                    return lblTo;
                }

                public void setLblTo(LblTo lblTo) {
                    this.lblTo = lblTo;
                }

                public LblTimings getLblTimings() {
                    return lblTimings;
                }

                public void setLblTimings(LblTimings lblTimings) {
                    this.lblTimings = lblTimings;
                }

                public LblLeaveDescription getLblLeaveDescription() {
                    return lblLeaveDescription;
                }

                public void setLblLeaveDescription(LblLeaveDescription lblLeaveDescription) {
                    this.lblLeaveDescription = lblLeaveDescription;
                }

                public LblInsufficientBalance getLblInsufficientBalance() {
                    return lblInsufficientBalance;
                }

                public void setLblInsufficientBalance(LblInsufficientBalance lblInsufficientBalance) {
                    this.lblInsufficientBalance = lblInsufficientBalance;
                }

                public LblBookNow getLblBookNow() {
                    return lblBookNow;
                }

                public void setLblBookNow(LblBookNow lblBookNow) {
                    this.lblBookNow = lblBookNow;
                }

                public TxtViewLeaveComments getTxtViewLeaveComments() {
                    return txtViewLeaveComments;
                }

                public void setTxtViewLeaveComments(TxtViewLeaveComments txtViewLeaveComments) {
                    this.txtViewLeaveComments = txtViewLeaveComments;
                }

                public LblReviews getLblReviews() {
                    return lblReviews;
                }

                public void setLblReviews(LblReviews lblReviews) {
                    this.lblReviews = lblReviews;
                }

                public LblOtherRelatedService getLblOtherRelatedService() {
                    return lblOtherRelatedService;
                }

                public void setLblOtherRelatedService(LblOtherRelatedService lblOtherRelatedService) {
                    this.lblOtherRelatedService = lblOtherRelatedService;
                }

                public LblAccepted getLblAccepted() {
                    return lblAccepted;
                }

                public void setLblAccepted(LblAccepted lblAccepted) {
                    this.lblAccepted = lblAccepted;
                }

                public LblSelectReviewType getLblSelectReviewType() {
                    return lblSelectReviewType;
                }

                public void setLblSelectReviewType(LblSelectReviewType lblSelectReviewType) {
                    this.lblSelectReviewType = lblSelectReviewType;
                }

                public LblMap getLbl_map() {
                    return lbl_map;
                }

                public void setLbl_map(LblMap lbl_map) {
                    this.lbl_map = lbl_map;
                }

                public LblRejected getLbl_rejected() {
                    return lbl_rejected;
                }

                public void setLbl_rejected(LblRejected lbl_rejected) {
                    this.lbl_rejected = lbl_rejected;
                }
            }

            public class AccountSettingsScreen {
                @SerializedName("lbl_title_account_settings")
                @Expose
                private LblTitleAccountSettings lblTitleAccountSettings;
                @SerializedName("lbl_account_holder_name")
                @Expose
                private LblAccountHolderName lblAccountHolderName;
                @SerializedName("lbl_acc_number")
                @Expose
                private LblAccNumber lblAccNumber;
                @SerializedName("lbl_iban")
                @Expose
                private LblIban lblIban;
                @SerializedName("lbl_bank_name")
                @Expose
                private LblBankName lblBankName;
                @SerializedName("lbl_ban_address")
                @Expose
                private LblBanAddress lblBanAddress;
                @SerializedName("lbl_sort_code")
                @Expose
                private LblSortCode lblSortCode;
                @SerializedName("lbl_swift_code")
                @Expose
                private LblSwiftCode lblSwiftCode;
                @SerializedName("lbl_ifsc_code")
                @Expose
                private LblIfscCode lblIfscCode;
                @SerializedName("lbl_btn_update")
                @Expose
                private LblBtnUpdate lblBtnUpdate;

                public class LblTitleAccountSettings extends LanguageSubModel {
                }

                public class LblAccountHolderName extends LanguageSubModel {
                }

                public class LblAccNumber extends LanguageSubModel {
                }

                public class LblIban extends LanguageSubModel {
                }

                public class LblBankName extends LanguageSubModel {
                }

                public class LblBanAddress extends LanguageSubModel {
                }

                public class LblSortCode extends LanguageSubModel {
                }

                public class LblSwiftCode extends LanguageSubModel {
                }

                public class LblIfscCode extends LanguageSubModel {
                }

                public class LblBtnUpdate extends LanguageSubModel {
                }


                public LblTitleAccountSettings getLblTitleAccountSettings() {
                    return lblTitleAccountSettings;
                }

                public void setLblTitleAccountSettings(LblTitleAccountSettings lblTitleAccountSettings) {
                    this.lblTitleAccountSettings = lblTitleAccountSettings;
                }

                public LblAccountHolderName getLblAccountHolderName() {
                    return lblAccountHolderName;
                }

                public void setLblAccountHolderName(LblAccountHolderName lblAccountHolderName) {
                    this.lblAccountHolderName = lblAccountHolderName;
                }

                public LblAccNumber getLblAccNumber() {
                    return lblAccNumber;
                }

                public void setLblAccNumber(LblAccNumber lblAccNumber) {
                    this.lblAccNumber = lblAccNumber;
                }

                public LblIban getLblIban() {
                    return lblIban;
                }

                public void setLblIban(LblIban lblIban) {
                    this.lblIban = lblIban;
                }

                public LblBankName getLblBankName() {
                    return lblBankName;
                }

                public void setLblBankName(LblBankName lblBankName) {
                    this.lblBankName = lblBankName;
                }

                public LblBanAddress getLblBanAddress() {
                    return lblBanAddress;
                }

                public void setLblBanAddress(LblBanAddress lblBanAddress) {
                    this.lblBanAddress = lblBanAddress;
                }

                public LblSortCode getLblSortCode() {
                    return lblSortCode;
                }

                public void setLblSortCode(LblSortCode lblSortCode) {
                    this.lblSortCode = lblSortCode;
                }

                public LblSwiftCode getLblSwiftCode() {
                    return lblSwiftCode;
                }

                public void setLblSwiftCode(LblSwiftCode lblSwiftCode) {
                    this.lblSwiftCode = lblSwiftCode;
                }

                public LblIfscCode getLblIfscCode() {
                    return lblIfscCode;
                }

                public void setLblIfscCode(LblIfscCode lblIfscCode) {
                    this.lblIfscCode = lblIfscCode;
                }

                public LblBtnUpdate getLblBtnUpdate() {
                    return lblBtnUpdate;
                }

                public void setLblBtnUpdate(LblBtnUpdate lblBtnUpdate) {
                    this.lblBtnUpdate = lblBtnUpdate;
                }
            }

            public class ChangePassword {

                @SerializedName("txt_current_password")
                @Expose
                private TxtCurrentPassword txtCurrentPassword;
                @SerializedName("txt_new_password")
                @Expose
                private TxtNewPassword txtNewPassword;
                @SerializedName("txt_confirm_password")
                @Expose
                private TxtConfirmPassword txtConfirmPassword;
                @SerializedName("lbl_change_password")
                @Expose
                private LblChangePassword lblChangePassword;
                @SerializedName("btn_submit")
                @Expose
                private BtnSubmit btnSubmit;

                public class BtnSubmit extends LanguageSubModel {
                }

                public class LblChangePassword extends LanguageSubModel {
                }

                public class TxtConfirmPassword extends LanguageSubModel {
                }

                public class TxtCurrentPassword extends LanguageSubModel {
                }

                public class TxtNewPassword extends LanguageSubModel {
                }

                public TxtCurrentPassword getTxtCurrentPassword() {
                    return txtCurrentPassword;
                }

                public void setTxtCurrentPassword(TxtCurrentPassword txtCurrentPassword) {
                    this.txtCurrentPassword = txtCurrentPassword;
                }

                public TxtNewPassword getTxtNewPassword() {
                    return txtNewPassword;
                }

                public void setTxtNewPassword(TxtNewPassword txtNewPassword) {
                    this.txtNewPassword = txtNewPassword;
                }

                public TxtConfirmPassword getTxtConfirmPassword() {
                    return txtConfirmPassword;
                }

                public void setTxtConfirmPassword(TxtConfirmPassword txtConfirmPassword) {
                    this.txtConfirmPassword = txtConfirmPassword;
                }

                public LblChangePassword getLblChangePassword() {
                    return lblChangePassword;
                }

                public void setLblChangePassword(LblChangePassword lblChangePassword) {
                    this.lblChangePassword = lblChangePassword;
                }

                public BtnSubmit getBtnSubmit() {
                    return btnSubmit;
                }

                public void setBtnSubmit(BtnSubmit btnSubmit) {
                    this.btnSubmit = btnSubmit;
                }
            }


            public class EmailLogin {
                @SerializedName("btn_forgot_password")
                @Expose
                private btn_forgot_password btn_forgot_password;
                @SerializedName("lbl_password")
                @Expose
                private lbl_password lbl_password;
                @SerializedName("btn_submit")
                @Expose
                private btn_submit btn_submit;

                public class btn_forgot_password extends LanguageSubModel {
                }

                public class btn_submit extends LanguageSubModel {
                }

                public class lbl_password extends LanguageSubModel {
                }

                public btn_forgot_password getBtn_forgot_password() {
                    return btn_forgot_password;
                }

                public Language.EmailLogin.btn_submit getBtn_submit() {
                    return btn_submit;
                }

                public void setBtn_submit(Language.EmailLogin.btn_submit btn_submit) {
                    this.btn_submit = btn_submit;
                }

                public void setBtn_forgot_password(btn_forgot_password btn_forgot_password) {
                    this.btn_forgot_password = btn_forgot_password;
                }

                public lbl_password getLbl_password() {
                    return lbl_password;
                }

                public void setLbl_password(lbl_password lbl_password) {
                    this.lbl_password = lbl_password;
                }
            }
        }

    }
}
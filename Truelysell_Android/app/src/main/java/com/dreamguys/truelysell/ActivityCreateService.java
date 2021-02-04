package com.dreamguys.truelysell;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.adapters.ViewPagerAdapter;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.EmptyData;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOCheckAccountDetails;
import com.dreamguys.truelysell.fragments.phase3.ChooseServiceImagesFragment;
import com.dreamguys.truelysell.fragments.phase3.CreateServiceInfoFragment;
import com.dreamguys.truelysell.interfaces.OnSetMyLocation;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.dreamguys.truelysell.viewwidgets.CustomViewPager;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ActivityCreateService extends AppCompatActivity implements TabLayout.OnTabSelectedListener, OnSetMyLocation, RetrofitHandler.RetrofitResHandler {

    @BindView(R.id.tab_create_service)
    TabLayout tabCreateService;
    @BindView(R.id.viewpager_create_service)
    CustomViewPager viewpagerCreateService;
    CreateServiceInfoFragment createServiceInfoFragment;
    ChooseServiceImagesFragment chooseServiceImagesFragment;
    public ProviderData providerData;
    public LanguageModel.Request_and_provider_list requestAndProviderList = new LanguageModel().new Request_and_provider_list();
    public LanguageModel.Common_used_texts commonData = new LanguageModel().new Common_used_texts();
    public static Context mContext;
    RequestBody serviceTitle, serviceLocation, category, subCategory, serviceLatitude, serviceLongitude, serviceAmount, about, token, serviceOffered;
    public String availability = "";
    public List<MultipartBody.Part> serviceImages = new ArrayList<>();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_titlename)
    TextView tvTitlename;
    private boolean firstTime = true;
    LanguageResponse.Data.Language.CreateService createServiceStringsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);
        ButterKnife.bind(this);
        mContext = this;
        getLocaleData();

        providerData = new ProviderData();

        tvTitlename.setText(AppUtils.cleanLangStr(this,
                createServiceStringsList.getLblCreateService().getName(), R.string.txt_create_service));


        createServiceInfoFragment = new CreateServiceInfoFragment();
        createServiceInfoFragment.myCreateServiceInfoFragment(ActivityCreateService.this);
        chooseServiceImagesFragment = new ChooseServiceImagesFragment();
        chooseServiceImagesFragment.myCreateServiceInfoFragment(ActivityCreateService.this);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(createServiceInfoFragment, AppUtils.cleanLangStr(this,
                createServiceStringsList.getLblInformation().getName(), R.string.txt_information));
        adapter.addFragment(chooseServiceImagesFragment, AppUtils.cleanLangStr(this,
                createServiceStringsList.getLblGallery().getName(), R.string.txt_gallery));

        viewpagerCreateService.setAdapter(adapter);
        viewpagerCreateService.setOffscreenPageLimit(2);
        viewpagerCreateService.disableScroll(true);

        //Tab
        tabCreateService.setupWithViewPager(viewpagerCreateService);
        tabCreateService.setOnTabSelectedListener(this);

        LinearLayout tabStrip = ((LinearLayout) tabCreateService.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        viewpagerCreateService.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    viewpagerCreateService.disableScroll(true);
                } else if (position == 1) {
                    viewpagerCreateService.disableScroll(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onLocationSet(String latitude, String longitude, String address) {
        createServiceInfoFragment.setLocationData(latitude, longitude, address);
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {

        switch (responseType) {
            case AppConstants.CREATE_PROVIDER_DATA:
                EmptyData emptyData = (EmptyData) myRes;
                Toast.makeText(mContext, ((EmptyData) myRes).getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case AppConstants.CHECKACCOUNTDETAILS:
                DAOCheckAccountDetails daoCheckAccountDetails = (DAOCheckAccountDetails) myRes;
                if (daoCheckAccountDetails.getData() != null && daoCheckAccountDetails.getData().getAvailabilityDetails() != null) {
                    availability = daoCheckAccountDetails.getData().getAvailabilityDetails();
                }
                break;
        }

    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    public class ProviderData {
        private String title;
        private String contactNo;
        private String location;
        private String provLat;
        private String provLng;
        private String fromDate;
        private String toDate;
        private String descListData;
        private String availListData;
        private String catID;
        private String subCatID;
        private String cost;
        private String about;

        public List<Bitmap> getServiceImages() {
            return serviceImages;
        }

        public void setServiceImages(List<Bitmap> serviceImages) {
            this.serviceImages = serviceImages;
        }

        private List<Bitmap> serviceImages = new ArrayList<>();

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getCatID() {
            return catID;
        }

        public void setCatID(String catID) {
            this.catID = catID;
        }

        public String getSubCatID() {
            return subCatID;
        }

        public void setSubCatID(String subCatID) {
            this.subCatID = subCatID;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getProvLat() {
            return provLat;
        }

        public void setProvLat(String provLat) {
            this.provLat = provLat;
        }

        public String getProvLng() {
            return provLng;
        }

        public void setProvLng(String provLng) {
            this.provLng = provLng;
        }

        public String getFromDate() {
            return fromDate;
        }

        public void setFromDate(String fromDate) {
            this.fromDate = fromDate;
        }

        public String getToDate() {
            return toDate;
        }

        public void setToDate(String toDate) {
            this.toDate = toDate;
        }

        public String getDescListData() {
            return descListData;
        }

        public void setDescListData(String descListData) {
            this.descListData = descListData;
        }

        public String getAvailListData() {
            return availListData;
        }

        public void setAvailListData(String availListData) {
            this.availListData = availListData;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextInputEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public boolean validateData(EditText etTxt, String etVal, String msg) {
        String msg2 = "";
        if (etTxt != null) {
            if (etTxt.getId() == R.id.et_title) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_txt_title);
                    AppUtils.showToast(ActivityCreateService.this, msg2);
                    return false;
                } else {
                }

            } else if (etTxt.getId() == R.id.et_location) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_txt_addr);
                    AppUtils.showToast(ActivityCreateService.this, msg2);
                    return false;
                } else {
                }
            } else if (etTxt.getId() == R.id.et_contact_no) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_txt_contact_no);
                    AppUtils.showToast(ActivityCreateService.this, msg2);
                    return false;
                } else {
                }
            } else if (etTxt.getId() == R.id.et_category) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_category);
                    AppUtils.showToast(ActivityCreateService.this, msg2);
                    return false;
                } else {
                }
            } else if (etTxt.getId() == R.id.et_subcategory) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_subcategory);
//                    etTxt.setBackground(getResources().getDrawable(R.drawable.shape_rect_round_corner_red));
                    AppUtils.showToast(ActivityCreateService.this, msg2);
                    return false;
                } else {
//                    etTxt.setBackground(getResources().getDrawable(R.drawable.shape_rect_round_corner));
                }
            } else if (etTxt.getId() == R.id.et_cost) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_cost);
//                    etTxt.setBackground(getResources().getDrawable(R.drawable.shape_rect_round_corner_red));
                    AppUtils.showToast(ActivityCreateService.this, msg2);
                    return false;
                } else {
//                    etTxt.setBackground(getResources().getDrawable(R.drawable.shape_rect_round_corner));
                }
            } else if (etTxt.getId() == R.id.et_about) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_txt_desc_empty);
//                    etTxt.setBackground(getResources().getDrawable(R.drawable.shape_rect_round_corner_red));
                    AppUtils.showToast(ActivityCreateService.this, msg2);
                    return false;
                } else {
//                    etTxt.setBackground(getResources().getDrawable(R.drawable.shape_rect_round_corner));
                }
            } else {
                msg2 = msg;
                AppUtils.showToast(ActivityCreateService.this, msg2);
                return false;
            }

        }
        return true;
    }

    public boolean validatePhoneNum(EditText etContactNo, String contactNo, String s) {
        if (etContactNo.getText().toString().length() < 10 || etContactNo.getText().toString().length() > 15) {
            s = AppUtils.cleanLangStr(this, /*requestAndProviderList.getLg6_contact_number_()*/"", R.string.err_txt_contact_no);
//            etContactNo.setBackground(getResources().getDrawable(R.drawable.shape_rect_round_corner_red));
            AppUtils.showToast(ActivityCreateService.this, s);
            return false;
        }
        return true;
    }

    private void getLocaleData() {
        try {
            String createDataStr = PreferenceStorage.getKey(CommonLangModel.CreateService);
            createServiceStringsList = new Gson().fromJson(createDataStr, LanguageResponse.Data.Language.CreateService.class);
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.request_and_provider_list);
            requestAndProviderList = new Gson().fromJson(commonDataStr, LanguageModel.Request_and_provider_list.class);
            commonData = new Gson().fromJson(PreferenceStorage.getKey(CommonLangModel.common_used_texts), LanguageModel.Common_used_texts.class);
        } catch (Exception e) {
            requestAndProviderList = new LanguageModel().new Request_and_provider_list();
            commonData = new LanguageModel().new Common_used_texts();
            createServiceStringsList = new LanguageResponse().new Data().new Language().new CreateService();

        }
    }

    public void gotoNext() {
        viewpagerCreateService.setCurrentItem(1);
    }

    public void postDataToServer() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.clearDialog();
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            if (providerData != null) {
                try {
                    String fromDate = AppUtils.formatDateToServer(providerData.getFromDate());
                    String toDate = AppUtils.formatDateToServer(providerData.getToDate());

                    serviceTitle = RequestBody.create(MediaType.parse("text/plain"), providerData.getTitle());
                    serviceLocation = RequestBody.create(MediaType.parse("text/plain"), providerData.getLocation());
                    category = RequestBody.create(MediaType.parse("text/plain"), providerData.getCatID());
                    subCategory = RequestBody.create(MediaType.parse("text/plain"), providerData.getSubCatID());
                    serviceLatitude = RequestBody.create(MediaType.parse("text/plain"), providerData.getProvLat());
                    serviceLongitude = RequestBody.create(MediaType.parse("text/plain"), providerData.getProvLng());
                    serviceAmount = RequestBody.create(MediaType.parse("text/plain"), providerData.getCost());
                    about = RequestBody.create(MediaType.parse("text/plain"), providerData.getAbout());
                    serviceOffered = RequestBody.create(MediaType.parse("text/plain"), providerData.getDescListData());
                    token = RequestBody.create(MediaType.parse("text/plain"), PreferenceStorage.getKey(AppConstants.USER_TOKEN));

                    for (int i = 0; i < providerData.getServiceImages().size(); i++) {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        providerData.getServiceImages().get(i).compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes.toByteArray());
//                        serviceImages.add(MultipartBody.Part.createFormData("images[]", "serviceImage" + (i + 1) + ".jpg", requestFile));
                        serviceImages.add(MultipartBody.Part.createFormData("images[]", "serviceImage" + ".jpg", requestFile));
                    }

                    Call<EmptyData> classificationCall = apiService.postCreateService(serviceTitle,
                            serviceLocation, category, subCategory,
                            serviceLatitude, serviceLongitude, serviceAmount, about, serviceOffered, serviceImages, PreferenceStorage.getKey(AppConstants.USER_TOKEN));

//                          providerDa Call<EmptyData> classificationCall = apiService.postProviderData(providerData.getTitle(),
////                            providerData.getDescListData(), providerData.getLocation(),
////                     ta.getContactNo(), providerData.getProvLat(), providerData.getProvLng(), providerData.getCatID(), providerData.getSubCatID(), PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.MY_LANGUAGE));

                    RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.CREATE_PROVIDER_DATA, this, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            AppUtils.showToast(getApplicationContext(), AppUtils.cleanLangStr(this, commonData.getLg7_please_enable_i(), R.string.txt_enable_internet));
        }
    }

    public void checkAccounDetails() {
        if (AppUtils.isNetworkAvailable(this)) {
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<DAOCheckAccountDetails> classificationCall = apiService.checkAccountDetails(PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.USER_TYPE));
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.CHECKACCOUNTDETAILS, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

    public void checkAvailabiltySlots() {
        if (!availability.isEmpty() && availability.equalsIgnoreCase("0")) {
            Intent callAccDetailsAct = new Intent(this, ChangeAvailabilityActivity.class);
            AppUtils.showCustomAlertDialog(this, "Update Availability in Profile to Create a Service", callAccDetailsAct);
        } else {
            postDataToServer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAccounDetails();
    }
}

package com.dreamguys.truelysell;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dreamguys.truelysell.adapters.ViewPagerAdapter;
import com.dreamguys.truelysell.datamodel.EmptyData;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.Phase3.DAOEditServiceDetails;
import com.dreamguys.truelysell.fragments.phase3.EditServiceImagesFragment;
import com.dreamguys.truelysell.fragments.phase3.EditServiceInfoFragment;
import com.dreamguys.truelysell.interfaces.OnSetMyLocation;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.dreamguys.truelysell.viewwidgets.CustomViewPager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class EditServiceActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler, OnSetMyLocation {

    public static Context mContext;

    @BindView(R.id.tab_create_provider)
    TabLayout tabCreateProvider;
    @BindView(R.id.pager_create_provider)
    CustomViewPager pagerCreateProvider;

    ViewPagerAdapter adapter;
    EditServiceInfoFragment createProvideInfoFragment;
    EditServiceImagesFragment createProvideAvailFragment;
    public ProviderData providerData;
    public LanguageModel.Request_and_provider_list langReqProvData = new LanguageModel().new Request_and_provider_list();
    String serviceID = "", serviceTitle = "";
    int viewType;
    RequestBody ID, serviceTitlename, serviceLocation, category, subCategory, serviceLatitude, serviceLongitude, serviceAmount, about, token, serviceOffered;

    public List<MultipartBody.Part> serviceImages = new ArrayList<>();
    public List<MultipartBody.Part> serviceImages1 = new ArrayList<>();
    List<DAOEditServiceDetails.ServiceImage> filteredTeams1 = new ArrayList<>();
    private int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentActivity(this);
        setContentView(R.layout.activity_create_provider);
        ButterKnife.bind(this);
        mContext = this;
        setToolBarTitle(AppUtils.cleanLangStr(this, langReqProvData.getLg6_provide(), R.string.txt_provider));

        if (AppUtils.isThemeChanged(this)) {
            tabCreateProvider.setSelectedTabIndicatorColor(AppUtils.getPrimaryAppTheme(this));
            tabCreateProvider.setTabTextColors(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
        }

        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);
        serviceID = getIntent().getStringExtra(AppConstants.SERVICEID);
        serviceTitle = getIntent().getStringExtra(AppConstants.SERVICETITLE);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        providerData = new ProviderData();
        getServiceInfo();
        viewType = getIntent().getIntExtra("ViewType", 0);

        pagerCreateProvider.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    pagerCreateProvider.disableScroll(true);
                } else if (position == 1) {
                    pagerCreateProvider.disableScroll(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolBarTitle(serviceTitle);

    }

    public boolean validateData(EditText etTxt, String etVal, String msg) {
        String msg2 = "";
        if (etTxt != null) {
            if (etTxt.getId() == R.id.et_title) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_txt_title);
                    AppUtils.showToast(EditServiceActivity.this, msg2);
                    return false;
                } else if (etTxt.getText().toString().length() > 30) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_max_characters);
                    AppUtils.showToast(EditServiceActivity.this, msg2);
                    return false;
                } else {
                }

            } else if (etTxt.getId() == R.id.et_location) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_txt_addr);
                    AppUtils.showToast(EditServiceActivity.this, msg2);
                    return false;
                } else {
                }
            } else if (etTxt.getId() == R.id.et_contact_no) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_txt_contact_no);
                    AppUtils.showToast(EditServiceActivity.this, msg2);
                    return false;
                } else if (etTxt.getText().toString().length() < 10 || etTxt.getText().toString().length() > 15) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_txt_contact_no);
                    AppUtils.showToast(EditServiceActivity.this, msg2);
                    return false;
                } else {
                }
            } else if (etTxt.getId() == R.id.et_category) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_category);
                    AppUtils.showToast(EditServiceActivity.this, msg2);
                    return false;
                } else {
                }
            } else if (etTxt.getId() == R.id.et_subcategory) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_subcategory);
                    AppUtils.showToast(EditServiceActivity.this, msg2);
                    return false;
                } else {
                }
            } else if (etTxt.getId() == R.id.et_cost) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_cost);
                    AppUtils.showToast(EditServiceActivity.this, msg2);
                    return false;
                } else {
                }
            } else if (etTxt.getId() == R.id.et_about) {
                if (etTxt.getText().toString().isEmpty()) {
                    msg2 = AppUtils.cleanLangStr(this, msg, R.string.err_txt_desc_empty);
                    AppUtils.showToast(EditServiceActivity.this, msg2);
                    return false;
                } else {
                }
            } else {
                msg2 = msg;
                AppUtils.showToast(EditServiceActivity.this, msg2);
                return false;
            }
        }
        return true;
    }

    public void postDataToServer() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.clearDialog();
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            if (providerData != null) {
                try {
                    ID = RequestBody.create(MediaType.parse("text/plain"), serviceID);
                    serviceTitlename = RequestBody.create(MediaType.parse("text/plain"), providerData.getTitle());
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
                        if (providerData.getServiceImages().get(i).getIs_url().equalsIgnoreCase("0")) {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            providerData.getServiceImages().get(i).getBitmapImage().compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes.toByteArray());
                            serviceImages.add(MultipartBody.Part.createFormData("images[]", "serviceImage" + (i + 1) + ".jpg", requestFile));
                        }
                    }
                    Call<EmptyData> classificationCall = apiService.updateService(ID, serviceTitlename,
                            serviceLocation, category, subCategory,
                            serviceLatitude, serviceLongitude, serviceAmount, about, serviceOffered, serviceImages, PreferenceStorage.getKey(AppConstants.USER_TOKEN));
                    RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.UPDATE_SERVICE, this, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            AppUtils.showToast(getApplicationContext(), AppUtils.cleanLangStr(this, commonData.getLg7_please_enable_i(), R.string.txt_enable_internet));
        }
    }

    @Override
    public void onLocationSet(String latitude, String longitude, String address) {
        createProvideInfoFragment.setLocationData(latitude, longitude, address);
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseModel) {

        switch (responseModel) {
            case AppConstants.EDITSERVICEINFO:
                DAOEditServiceDetails daoEditServiceDetails = (DAOEditServiceDetails) myRes;
                createProvideInfoFragment = new EditServiceInfoFragment();
                createProvideInfoFragment.myCreateServiceInfoFragment(this, daoEditServiceDetails.getData().getServiceOverview());
                adapter.addFragment(createProvideInfoFragment, AppUtils.cleanLangStr(this, langReqProvData.getLg6_info(), R.string.txt_info));

                createProvideAvailFragment = new EditServiceImagesFragment();
                createProvideAvailFragment.myCreateServiceInfoFragment(this, daoEditServiceDetails.getData().getServiceImage(), daoEditServiceDetails.getData().getServiceOverview().getServiceId());
                adapter.addFragment(createProvideAvailFragment, AppUtils.cleanLangStr(this, langReqProvData.getLg6_availability(), R.string.txt_availability));

                pagerCreateProvider.setAdapter(adapter);
                pagerCreateProvider.setOffscreenPageLimit(2);
                pagerCreateProvider.disableScroll(true);

                //Tab
                tabCreateProvider.setupWithViewPager(pagerCreateProvider);
                tabCreateProvider.getTabAt(0).setText(AppUtils.cleanLangStr(this,
                        createServiceStringsList.getLblInformation().getName(), R.string.txt_information));
                tabCreateProvider.getTabAt(1).setText(AppUtils.cleanLangStr(this,
                        createServiceStringsList.getLblGallery().getName(), R.string.txt_gallery));
                LinearLayout tabStrip = ((LinearLayout) tabCreateProvider.getChildAt(0));
                for (int i = 0; i < tabStrip.getChildCount(); i++) {
                    tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                }

                break;
            case AppConstants.UPDATE_SERVICE:
                EmptyData emptyData = (EmptyData) myRes;
                AppUtils.showToast(EditServiceActivity.this, emptyData.getResponseHeader().getResponseMessage());
                EditServiceActivity.this.finish();
                break;
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseModel) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseModel) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void gotoNext() {
        pagerCreateProvider.setCurrentItem(1);
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

        public List<DAOEditServiceDetails.ServiceImage> getServiceImages() {
            return serviceImages;
        }

        public void setServiceImages(List<DAOEditServiceDetails.ServiceImage> serviceImages) {
            this.serviceImages = serviceImages;
        }

        private List<DAOEditServiceDetails.ServiceImage> serviceImages = new ArrayList<>();

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

    public void getServiceInfo() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.clearDialog();
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<DAOEditServiceDetails> classificationCall = apiService.getServiceInfo(serviceID, PreferenceStorage.getKey(AppConstants.USER_TOKEN));
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.EDITSERVICEINFO, this, false);
        } else {
            AppUtils.showToast(getApplicationContext(), AppUtils.cleanLangStr(this, commonData.getLg7_please_enable_i(), R.string.txt_enable_internet));
        }
    }
}

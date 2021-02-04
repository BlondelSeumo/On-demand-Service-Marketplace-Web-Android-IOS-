package com.dreamguys.truelysell.fragments.phase3;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.ActivityCreateService;
import com.dreamguys.truelysell.MapActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.adapters.DialogEditCategoryListAdapter;
import com.dreamguys.truelysell.adapters.DialogEditSubCategoryListAdapter;
import com.dreamguys.truelysell.datamodel.CategoryList;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOServiceSubCategories;
import com.dreamguys.truelysell.datamodel.Phase3.ServiceCategories;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MultipartBody;
import retrofit2.Call;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CreateServiceInfoFragment extends Fragment implements RetrofitHandler.RetrofitResHandler {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.ll_desc_parent)
    LinearLayout llDescParent;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_contact_no)
    EditText etContactNo;
    @BindView(R.id.btn_provider_next)
    Button btnProviderNext;
    Unbinder unbinder;

    int descCount = 0;
    ArrayList<String> descDataList = new ArrayList<>();
    ActivityCreateService mActivity;
    Context mContext;
    LanguageModel.Request_and_provider_list requestAndProviderList = new LanguageModel().new Request_and_provider_list();
    LanguageModel.Common_used_texts commonData = new LanguageModel().new Common_used_texts();


    public String latitude, longitude, address;
    @BindView(R.id.et_category)
    EditText etCategory;
    @BindView(R.id.et_subcategory)
    EditText etSubcategory;
    @BindView(R.id.et_cost)
    EditText etCost;
    @BindView(R.id.et_about)
    EditText etAbout;
    public String cat_id = "", subCatID = "";
    GridLayoutManager gridLayoutManager;
    public Button btnDone;
    public List<CategoryList.Category_list> category_lists = new ArrayList<>();
    DialogEditCategoryListAdapter categoryListAdapter;
    DialogEditSubCategoryListAdapter subCategoryListAdapter;
    public Button btnCatDone, btnsubCatDone;
    String category;
    String subcategory;
    AlertDialog dialog, sucCatDialog;
    List<ServiceCategories.CategoryList> category_list = new ArrayList<>();
    List<DAOServiceSubCategories.SubcategoryList> subcategory_list = new ArrayList<>();
    LayoutInflater CatInflater, subCatInflater;
    View catCustomView, subCatCustomView;
    RecyclerView rvCategoryList, rvsubCategoryList;
    TextView tvTitle, tvsubCatTitle;
    @BindView(R.id.iv_provider_img)
    ImageView ivProviderImg;
    @BindView(R.id.iv_upload)
    ImageView ivUpload;
    @BindView(R.id.txt_upload)
    TextView txtUpload;
    private BottomSheetDialog attachChooser;
    private static final int RC_LOAD_IMG_CAMERA = 101;
    private static final int RC_LOAD_IMG_BROWSER = 102;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    MultipartBody.Part profileImg;
    LanguageResponse.Data.Language.CreateService createServiceStringsList;

    public void myCreateServiceInfoFragment(ActivityCreateService createProviderActivity) {
        this.mActivity = createProviderActivity;
        this.mContext = createProviderActivity.getBaseContext();
        requestAndProviderList = createProviderActivity.requestAndProviderList;
        commonData = createProviderActivity.commonData;
//        etTitle.addTextChangedListener(new createProviderTextWatcher(etTitle));
//        etLocation.addTextChangedListener(new createProviderTextWatcher(etLocation));
//        etContactNo.addTextChangedListener(new createProviderTextWatcher(etContactNo));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.fragment_create_service, container, false);
        unbinder = ButterKnife.bind(this, mView);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnProviderNext.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            }
        }
        getLocaleData();
        initCategories();
        initSubCategories();


//        tvTxtTitle.setText(AppUtils.cleanLangStr(mContext, requestAndProviderList.getLg6_title(), R.string.txt_title));
//        tvTxtLocation.setText(AppUtils.cleanLangStr(mContext, requestAndProviderList.getLg6_location(), R.string.txt_location));
//        tvTxtCont.setText(AppUtils.cleanLangStr(mContext, requestAndProviderList.getLg6_contact_number(), R.string.txt_cont_num));
//        tvTxtCategory.setText(AppUtils.cleanLangStr(mContext, commonData.getLg7_category(), R.string.txt_category));
//        tvTxtSubcategory.setText(AppUtils.cleanLangStr(mContext, commonData.getLg7_sub_category(), R.string.txt_subcategory));

//        btnProviderNext.setText(AppUtils.cleanLangStr(mContext, requestAndProviderList.getLg6_next(), R.string.txt_next));
//        btnProviderNext.setBackgroundColor(mActivity.appColor);

        addDescView(false);
        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callMapActivity = new Intent(mContext, MapActivity.class);
                callMapActivity.putExtra("From", AppConstants.PAGE_CREATE_PROVIDER);
                callMapActivity.putExtra("Latitude", latitude);
                callMapActivity.putExtra("Longitude", longitude);
                callMapActivity.putExtra("Address", address);
                AppUtils.appStartIntent(mContext, callMapActivity);
            }
        });

        //Hide Keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);




        /*etContactNo.setFilters(new InputFilter[]{new InputFilterMinMax(1,15)});*/

        etTitle.requestFocus();

        categoryListAdapter = new DialogEditCategoryListAdapter(getActivity(), category_list, dialog, etCategory, etSubcategory, cat_id, subCatID, btnCatDone);
        subCategoryListAdapter = new DialogEditSubCategoryListAdapter(getActivity(), subcategory_list, sucCatDialog, etSubcategory, subCatID, btnsubCatDone);

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*if (!etTitle.getText().toString().isEmpty()) {
                    mActivity.validateData(etTitle, etTitle.getText().toString(), AppUtils.cleanLangStr(getActivity(),
                            createServiceStringsList.getLblTitle().getValidation1(), R.string.err_txt_title));
                }*/
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return mView;
    }

    private void initSubCategories() {
        subCatInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        subCatCustomView = subCatInflater.inflate(R.layout.dialog_category, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(subCatCustomView);
        alertDialogBuilder.setCancelable(false);
        sucCatDialog = alertDialogBuilder.create();
        rvsubCategoryList = subCatCustomView.findViewById(R.id.rv_categorylist);
        tvsubCatTitle = subCatCustomView.findViewById(R.id.tv_title);
        btnsubCatDone = subCatCustomView.findViewById(R.id.btn_done);
        sucCatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        btnsubCatDone.setBackgroundColor(mActivity.appColor);
    }

    private void initCategories() {
        CatInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        catCustomView = CatInflater.inflate(R.layout.dialog_category, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(catCustomView);
        alertDialogBuilder.setCancelable(false);
        dialog = alertDialogBuilder.create();
        rvCategoryList = catCustomView.findViewById(R.id.rv_categorylist);
        tvTitle = catCustomView.findViewById(R.id.tv_title);
        btnCatDone = catCustomView.findViewById(R.id.btn_done);
//        btnCatDone.setBackgroundColor(mActivity.appColor);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private synchronized void addDescView(boolean isNewData, String... descVal) {
        descCount++;
        if (descCount > 1) {
            for (int i = 0; i < llDescParent.getChildCount(); i++) {
                ((LinearLayout) llDescParent.getChildAt(i)).findViewById(R.id.btn_desc_add).setVisibility(View.GONE);
                ((LinearLayout) llDescParent.getChildAt(i)).findViewById(R.id.btn_desc_remove).setVisibility(View.VISIBLE);
            }
        }
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View inflatedLayout = inflater.inflate(R.layout.layout_service_desc, null, false);
//        TextView tvTxtDesc = (TextView) inflatedLayout.findViewById(R.id.tv_txt_desc);
//        tvTxtDesc.setText(AppUtils.cleanLangStr(mContext, requestAndProviderList.getLg6_desc(), R.string.txt_desc_pt) + " " + descCount);
        final EditText tvDesc = (EditText) inflatedLayout.findViewById(R.id.et_decs);
        final ImageView ivDescription = (ImageView) inflatedLayout.findViewById(R.id.iv_description);
        if (isNewData && descVal != null && descVal.length > 0) {
            tvDesc.setText(descVal[0]);
        }
        Button btnDescAdd = (Button) inflatedLayout.findViewById(R.id.btn_desc_add);
        btnDescAdd.setTag("Add_" + descCount);
        btnDescAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvDesc != null && tvDesc.getText().toString() != null
                        && !tvDesc.getText().toString().isEmpty())
                    addDescView(false);
                else
                    AppUtils.showToast(getActivity(), AppUtils.cleanLangStr(getActivity(), "Enter Service Offered", R.string.err_txt_service_offered_empty));


            }
        });

        Button btnDescRemove = (Button) inflatedLayout.findViewById(R.id.btn_desc_remove);
        btnDescRemove.setTag("Remove_" + descCount);
        llDescParent.addView(inflatedLayout);
        for (int i = 0; i < llDescParent.getChildCount(); i++) {
            Button btnAdd = (Button) (((LinearLayout) llDescParent.getChildAt(i)).findViewById(R.id.btn_desc_add));
            Button btnRemove = (Button) (((LinearLayout) llDescParent.getChildAt(i)).findViewById(R.id.btn_desc_remove));
            if (i == llDescParent.getChildCount() - 1) {
                EditText etDesc = (EditText) (((LinearLayout) llDescParent.getChildAt(i)).findViewById(R.id.et_decs));
                etDesc.requestFocus();
            }
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeDescView(v.getTag().toString());
                }
            });
        }
    }

    private void removeDescView(String tag) {
        Log.d("TAG", "Remove tag = " + tag);
        int index = Integer.parseInt(tag.split("_")[1]);
        llDescParent.removeViewAt(index - 1);
        getDescData();
        llDescParent.removeAllViews();
        descCount = 0;

        if (descDataList.size() > 0) {
            for (int i = 0; i < descDataList.size(); i++) {
                addDescView(true, descDataList.get(i));
            }
        } else {
            addDescView(false);
        }


    }

    private void getDescData() {
        descDataList.clear();
        for (int i = 0; i < llDescParent.getChildCount(); i++) {
            EditText descVal = (EditText) (((LinearLayout) llDescParent.getChildAt(i)).findViewById(R.id.et_decs));
            if (!descVal.getText().toString().trim().isEmpty())
                descDataList.add(descVal.getText().toString().trim());
        }
    }

    public void setLocationData(String latitude, String longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
//        etLocation.setBackground(getResources().getDrawable(R.drawable.shape_rect_round_corner));
        etLocation.setText(address);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.btn_provider_next)
    public void onViewClicked() {
        String title = etTitle.getText().toString().trim();
        String contactNo = etContactNo.getText().toString();
        String location = etLocation.getText().toString();


        String locLat = latitude, locLng = longitude;
        getDescData();

        if (mActivity.validateData(etTitle, title, AppUtils.cleanLangStr(getActivity(), createServiceStringsList.getLblTitle().getValidation1(), R.string.err_txt_title))
                && mActivity.validateData(etLocation, location, AppUtils.cleanLangStr(getActivity(), createServiceStringsList.getLblServiceLocation().getValidation1(), R.string.err_txt_addr))
                && mActivity.validateData(null, locLat, AppUtils.cleanLangStr(getActivity(), /*requestAndProviderList.getLg6_error_in_locati()*/"", R.string.err_txt_latlng))
                && mActivity.validateData(null, locLng, AppUtils.cleanLangStr(getActivity(), /*requestAndProviderList.getLg6_error_in_locati()*/"", R.string.err_txt_latlng))
//                && mActivity.validateData(etContactNo, contactNo, AppUtils.cleanLangStr(mContext, /*requestAndProviderList.getLg6_contact_number_()*/"", R.string.err_txt_contact_no))
//                && mActivity.validatePhoneNum(etContactNo, contactNo, AppUtils.cleanLangStr(mContext, /*requestAndProviderList.getLg6_contact_number_()*/"", R.string.err_txt_contact_no))
                && mActivity.validateData(etCategory, "", AppUtils.cleanLangStr(getActivity(), createServiceStringsList.getLblHintCategory().getValidation1(), R.string.err_category))
                && mActivity.validateData(etSubcategory, "", AppUtils.cleanLangStr(getActivity(), createServiceStringsList.getLblHintSubcategory().getValidation1(), R.string.err_subcategory))
                && mActivity.validateData(etCost, "", AppUtils.cleanLangStr(getActivity(), createServiceStringsList.getLblServiceAmount().getValidation1(), R.string.err_cost))
                && mActivity.validateData(etAbout, "", AppUtils.cleanLangStr(getActivity(), createServiceStringsList.getLblHintDescription().getValidation1(), R.string.err_txt_desc_empty))) {

//            && mActivity.validateData(etSubcategory, "", AppUtils.cleanLangStr(mContext, requestAndProviderList.getLg6_contact_number_(), R.string.err_txt_contact_no))


            mActivity.providerData.setTitle(title);
//            mActivity.providerData.setContactNo(contactNo);
            mActivity.providerData.setLocation(location);
            mActivity.providerData.setProvLat(locLat);
            mActivity.providerData.setProvLng(locLng);
            mActivity.providerData.setCatID(categoryListAdapter.cat_id);
            mActivity.providerData.setSubCatID(subCategoryListAdapter.subCatID);
            mActivity.providerData.setCost(etCost.getText().toString());
            mActivity.providerData.setAbout(etAbout.getText().toString());

            if (descDataList == null || descDataList.size() == 0) {
                AppUtils.showToast(mContext, AppUtils.cleanLangStr(getActivity(), /*requestAndProviderList.getLg6_please_enter_at()*/"", R.string.err_txt_desc_atleast_1));
                return;
            }
            JSONArray descArr = new JSONArray(descDataList);
            mActivity.providerData.setDescListData(descArr.toString());
//            mActivity.providerData.setDescListData(descDataList);
//            mActivity.postDataToServer();
            mActivity.gotoNext();
        }
    }

    @OnClick({R.id.et_category, R.id.et_subcategory, R.id.iv_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_category:
                if (AppUtils.isNetworkAvailable(getActivity())) {
//                    etCategory.setText("");
//                    cat_id = "";

                    ProgressDlg.showProgressDialog(getActivity(), null, null);
                    ApiInterface apiService =
                            ApiClient.getClientNoHeader().create(ApiInterface.class);
                    try {
                        Call<ServiceCategories> classificationCall = apiService.getServiceCategories(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
                        RetrofitHandler.executeRetrofit(getActivity(), classificationCall, AppConstants.CATEGORIES, this, false);
                    } catch (Exception e) {
                        ProgressDlg.dismissProgressDialog();
                        e.printStackTrace();
                    }

                } else {
                    AppUtils.showToast(getActivity(), getString(R.string.txt_enable_internet));
                }
                break;
            case R.id.et_subcategory:

                if (categoryListAdapter.isAvaialble) {
                    if (!etCategory.getText().toString().isEmpty()) {
                        if (AppUtils.isNetworkAvailable(getActivity())) {

                            ProgressDlg.showProgressDialog(getActivity(), null, null);
                            ApiInterface apiService =
                                    ApiClient.getClientNoHeader().create(ApiInterface.class);
                            try {
                                Call<DAOServiceSubCategories> classificationCall = apiService.postServiceSubCategory(categoryListAdapter.cat_id, PreferenceStorage.getKey(AppConstants.USER_TOKEN));
                                RetrofitHandler.executeRetrofit(getActivity(), classificationCall, AppConstants.SUBCATEGORIES, this, false);
                            } catch (Exception e) {
                                ProgressDlg.dismissProgressDialog();
                                e.printStackTrace();
                            }

                        } else {
                            AppUtils.showToast(getActivity(), getString(R.string.txt_enable_internet));
                        }
                    } else {
                        Toast.makeText(mActivity, "Select Category first", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mActivity, "No Sub Category Available", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {

        if (myRes instanceof ServiceCategories) {
            ServiceCategories categoryList = (ServiceCategories) myRes;
            if (categoryList.getData().getCategoryList().size() > 0) {
                showCategoryPopupWindow(categoryList.getData().getCategoryList());
            } else {
                Toast.makeText(mContext, "No Categories Available", Toast.LENGTH_SHORT).show();
            }

        } else if (myRes instanceof DAOServiceSubCategories) {
            DAOServiceSubCategories subCategoryList = (DAOServiceSubCategories) myRes;
            if (subCategoryList.getData().getSubcategoryList() != null && subCategoryList.getData().getSubcategoryList().size() > 0) {
                showSubCategoryPopupWindow(subCategoryList.getData().getSubcategoryList());
            } else {
                Toast.makeText(mContext, "No SubCategories Available", Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    private class createProviderTextWatcher implements TextWatcher {
        private View view;

        private createProviderTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (view.getId() == R.id.et_title) {
                if (!etTitle.getText().toString().isEmpty()) {

//                    etTitle.setBackground(getResources().getDrawable(R.drawable.shape_rect_round_corner));
                    mActivity.validateData(etTitle, etTitle.getText().toString(), AppUtils.cleanLangStr(getActivity(), createServiceStringsList.getLblTitle().getValidation1(), R.string.err_txt_title));
                }
            } else if (view.getId() == R.id.et_location) {
                if (etLocation.getText().toString().isEmpty()) {
//                    etLocation.setBackground(getResources().getDrawable(R.drawable.shape_rect_round_corner));
                    mActivity.validateData(etLocation, etLocation.getText().toString(), AppUtils.cleanLangStr(getActivity(), createServiceStringsList.getLblServiceLocation().getValidation1(), R.string.err_txt_addr));
                }
            } else if (view.getId() == R.id.et_contact_no) {
                if (etContactNo.getText().toString().isEmpty()) {
//                    etContactNo.setBackground(getResources().getDrawable(R.drawable.shape_rect_round_corner));
                    mActivity.validateData(etContactNo, etContactNo.getText().toString(), AppUtils.cleanLangStr(getActivity(), /*requestAndProviderList.getLg6_contact_number_()*/"", R.string.err_txt_contact_no));
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    public void showCategoryPopupWindow(List<ServiceCategories.CategoryList> categoryList) {
        tvTitle.setText(AppUtils.cleanLangStr(getActivity(),
                createServiceStringsList.getLblHintCategory().getValidation1(), R.string.txt_category));
        category_list.clear();
        category_list.addAll(categoryList);
//        category = categoryListAdapter.cat_id.split(",");
//        for (int i = 0; i < category.length; i++) {
//            for (int i1 = 0; i1 < category_list.size(); i1++) {
//                if (category_list.get(i1).getId().equalsIgnoreCase(category[i])) {
//                    category_list.get(i1).setIs_checked(true);
//                }
//            }
//        }
        if (categoryListAdapter.cat_id != null && !categoryListAdapter.cat_id.isEmpty())
            for (int i1 = 0; i1 < category_list.size(); i1++) {
                if (category_list.get(i1).getId().equalsIgnoreCase(categoryListAdapter.cat_id)) {
                    category_list.get(i1).setChecked(true);
                }
            }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvCategoryList.setLayoutManager(linearLayoutManager);
        rvCategoryList.setAdapter(categoryListAdapter);
        categoryListAdapter.notifyDataSetChanged();
        dialog.show();
    }

    public void showSubCategoryPopupWindow(List<DAOServiceSubCategories.SubcategoryList> subCategory_list) {
        subcategory_list.clear();
        subcategory_list.addAll(subCategory_list);
//        subcategory = subCategoryListAdapter.subCatID.split(",");
//        for (int i = 0; i < subcategory.length; i++) {
//            for (int i1 = 0; i1 < subCategory_list.size(); i1++) {
//                if (subCategory_list.get(i1).getId().equalsIgnoreCase(subcategory[i])) {
//                    subCategory_list.get(i1).setChecked(true);
//                }
//            }
//        }
        if (subCategoryListAdapter.subCatID != null && !subCategoryListAdapter.subCatID.isEmpty())
            for (int i1 = 0; i1 < subCategory_list.size(); i1++) {
                if (subCategory_list.get(i1).getId().equalsIgnoreCase(subCategoryListAdapter.subCatID)) {
                    subCategory_list.get(i1).setChecked(true);
                }
            }

        tvsubCatTitle.setText(AppUtils.cleanLangStr(getActivity(),
                createServiceStringsList.getLblHintSubcategory().getValidation1(), R.string.txt_subcategory));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvsubCategoryList.setLayoutManager(linearLayoutManager);
        rvsubCategoryList.setAdapter(subCategoryListAdapter);
        subCategoryListAdapter.notifyDataSetChanged();
        sucCatDialog.show();

    }

    private void getLocaleData() {
        try {
            String createDataStr = PreferenceStorage.getKey(CommonLangModel.CreateService);
            createServiceStringsList = new Gson().fromJson(createDataStr, LanguageResponse.Data.Language.CreateService.class);

            etTitle.setHint(AppUtils.cleanLangStr(getActivity(),
                    createServiceStringsList.getLblTitle().getName(), R.string.txt_title));
            etLocation.setHint(AppUtils.cleanLangStr(getActivity(),
                    createServiceStringsList.getLblServiceLocation().getName(), R.string.hint_service_location));
            etCategory.setHint(AppUtils.cleanLangStr(getActivity(),
                    createServiceStringsList.getLblHintCategory().getName(), R.string.txt_category));
            etSubcategory.setHint(AppUtils.cleanLangStr(getActivity(),
                    createServiceStringsList.getLblHintSubcategory().getName(), R.string.txt_subcategory));
            etCost.setHint(AppUtils.cleanLangStr(getActivity(),
                    createServiceStringsList.getLblServiceAmount().getName(), R.string.txt_service_amount));
            etAbout.setHint(AppUtils.cleanLangStr(getActivity(),
                    createServiceStringsList.getLblHintDescription().getName(), R.string.txt_description));
            btnProviderNext.setText(AppUtils.cleanLangStr(getActivity(),
                    createServiceStringsList.getLblServiceNext().getName(), R.string.txt_next));
        } catch (Exception e) {
            createServiceStringsList = new LanguageResponse().new Data().new Language().new CreateService();
        }
    }

}

package com.dreamguys.truelysell;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.adapters.ReviewTypeListAdapter;
import com.dreamguys.truelysell.datamodel.GETReviewTypes;
import com.dreamguys.truelysell.datamodel.POSTRatingsProvider;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class RateProviderActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler {


    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.rb_rating)
    AppCompatRatingBar rbRating;
    @BindView(R.id.et_comments)
    EditText etComments;
    @BindView(R.id.btn_send_feeedback)
    Button btnSendFeeedback;
    String providerID = "", booking_id = "";
    List<GETReviewTypes.Ratings_type_list> daoReviewtypes = new ArrayList<>();
    ReviewTypeListAdapter reviewTypeListAdapter;
    @BindView(R.id.rv_types)
    RecyclerView rvTypes;
    @BindView(R.id.txt_how_was_exp)
    TextView txtHowWasExp;
    @BindView(R.id.txt_leave_your_comments)
    TextView txtLeaveYourComments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rate_provider);
        ButterKnife.bind(this);
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);
        setToolBarTitle("Post Your Review");
        providerID = getIntent().getStringExtra(AppConstants.SERVICEID);
        booking_id = getIntent().getStringExtra(AppConstants.BOOKINGID);
        getRatingTypes();

        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnSendFeeedback.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            }
        }

    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.ReviewTypes:
                GETReviewTypes getReviewTypes = (GETReviewTypes) myRes;
                if (getReviewTypes.getData().getRatings_type_list() != null && getReviewTypes.getData().getRatings_type_list().size() > 0) {
                    daoReviewtypes.addAll(getReviewTypes.getData().getRatings_type_list());
                    GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
                    rvTypes.setLayoutManager(linearLayoutManager);
                    reviewTypeListAdapter = new ReviewTypeListAdapter(this, daoReviewtypes, tvType);
                    rvTypes.setAdapter(reviewTypeListAdapter);
                }
                break;
            case AppConstants.RATINGS:
                POSTRatingsProvider postRatingsProvider = (POSTRatingsProvider) myRes;
                PreferenceStorage.setKey("isRated", true);
                Toast.makeText(this, postRatingsProvider.getResponse().getResponse_message(), Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @OnClick({R.id.tv_type, R.id.btn_send_feeedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_type:
                break;
            case R.id.btn_send_feeedback:
                if (rbRating.getRating() == 0) {
                    Toast.makeText(RateProviderActivity.this, "Please fill rating", Toast.LENGTH_SHORT).show();
                } else if (reviewTypeListAdapter.typeID.isEmpty()) {
                    Toast.makeText(RateProviderActivity.this, "Please choose service experience", Toast.LENGTH_SHORT).show();
                } else if (etComments.getText().toString().isEmpty()) {
                    Toast.makeText(RateProviderActivity.this, "Please type reviews", Toast.LENGTH_SHORT).show();
                } else {
                    postRatings(rbRating.getRating(), etComments.getText().toString());
                }
                break;
        }
    }

    public void postRatings(float rating, String review) {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<POSTRatingsProvider> classificationCall = apiService.postRatingsProvider(PreferenceStorage.getKey(AppConstants.USER_TOKEN), String.valueOf(rating), review, providerID, booking_id, reviewTypeListAdapter.typeID);
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.RATINGS, this, false);
        } else {
            AppUtils.showToast(getApplicationContext(), AppUtils.cleanLangStr(this, "", R.string.txt_enable_internet));
        }
    }

    public void getRatingTypes() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<GETReviewTypes> classificationCall = apiService.getReviewsType(AppConstants.DEFAULTTOKEN);
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.ReviewTypes, this, false);
        } else {
            AppUtils.showToast(getApplicationContext(), AppUtils.cleanLangStr(this, "", R.string.txt_enable_internet));
        }
    }
}

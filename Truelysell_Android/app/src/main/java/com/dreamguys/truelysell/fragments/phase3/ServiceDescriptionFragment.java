package com.dreamguys.truelysell.fragments.phase3;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.dreamguys.truelysell.ActivityBookService;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.dreamguys.truelysell.wallet.CardListActivity;
import com.dreamguys.truelysell.wallet.WalletDashBoard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ServiceDescriptionFragment extends Fragment implements OnCardFormSubmitListener, RetrofitHandler.RetrofitResHandler {


    protected CardForm mCardForm;
    ActivityBookService mActivity;
    Context mContext;
    Unbinder unbinder;
    ApiInterface apiInterface;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.text_current_balance)
    public TextView tvCurrentBalance;
    @BindView(R.id.iv_popular_services)
    ImageView ivPopularServices;
    @BindView(R.id.iv_payment)
    ImageView ivPayment;
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.btn_booknow)
    Button btnBooknow;
    @BindView(R.id.txt_msg_to_professional)
    TextView txtMsgToProfessional;
    @BindView(R.id.txt_wallet_details)
    TextView txtWalletDetails;
    @BindView(R.id.label_current_balance)
    TextView labelCurrentBalance;


    public void mServiceDescriptionFragment(ActivityBookService createProviderActivity) {
        this.mActivity = createProviderActivity;
        this.mContext = createProviderActivity.getBaseContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView;
        mView = inflater.inflate(R.layout.fragment_book_payment, container, false);
        unbinder = ButterKnife.bind(this, mView);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        mCardForm = mView.findViewById(R.id.card_form);
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .setup(getActivity());

        tvCurrentBalance.setText(Html.fromHtml(mActivity.currency) + mActivity.walletAmount);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnBooknow.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                btnPrevious.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                ivPayment.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                ivPopularServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));

            }
        }
        return mView;
    }

    @Override
    public void onCardFormSubmit() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_previous, R.id.btn_booknow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_previous:
                mActivity.gotoNext(1);
                break;
            case R.id.btn_booknow:
//                if (!mActivity.account_details.isEmpty() && mActivity.account_details.equalsIgnoreCase("0")) {
                if (!mActivity.walletAmount.isEmpty() && mActivity.walletAmount.equalsIgnoreCase("0")) {
//                    Intent callAccDetailsAct = new Intent(getActivity(), StripeSettings.class);
                    Intent callAccDetailsAct = new Intent(getActivity(), CardListActivity.class);
                    callAccDetailsAct.putExtra(AppConstants.WALLETAMOUNT, mActivity.walletAmount);
                    callAccDetailsAct.putExtra(AppConstants.FROMPAGE, AppConstants.TOPUP);
                    callAccDetailsAct.putExtra(AppConstants.CURRENCY, mActivity.currency);
                    callAccDetailsAct.putExtra(AppConstants.CURRENCYCODE, mActivity.currencyCode);
//                    AppUtils.showCustomAlertDialog(getActivity(), "Update Account Details in Settings to Book a Service", callAccDetailsAct);
                    AppUtils.showCustomAlertDialog(getActivity(), "Add Cash in wallet to book a service", callAccDetailsAct);
                } else {
                    mActivity.bookServiceDetails.setNotes(etDescription.getText().toString().trim());
//                    mActivity.generateCardPayment(mCardForm);
                    if (mActivity.bookServiceDetails.getFromTime().isEmpty() && mActivity.bookServiceDetails.getToTime().isEmpty()) {
                        Toast.makeText(mActivity, "Choose TimeSlots", Toast.LENGTH_SHORT).show();
                    } else if (mActivity.bookServiceDetails.getLocation().isEmpty()) {
                        Toast.makeText(mActivity, "Choose your location", Toast.LENGTH_SHORT).show();
                    } else if (mActivity.bookServiceDetails.getNotes().isEmpty()) {
                        AppUtils.showToast(getActivity(), getString(R.string.err_txt_desc_empty));
                    } else if (Double.parseDouble(mActivity.bookServiceDetails.getAmount()) > Double.parseDouble(mActivity.walletAmount)) {
                        Intent callAccDetailsAct = new Intent(getActivity(), WalletDashBoard.class);
                        AppUtils.showCustomAlertDialog(getActivity(), "You do not have sufficient balance in your wallet account. Please Topup to book the service.", callAccDetailsAct);
                    }
//                    else if (mCardForm.getCardNumber().isEmpty() && mCardForm.getExpirationMonth().isEmpty() && mCardForm.getExpirationYear().isEmpty()
//                            && mCardForm.getCvv().isEmpty()) {
//                        AppUtils.showToast(getActivity(), getString(R.string.err_payment));
//                    }
                    else {
                        mActivity.generateCardPayment(mCardForm);
                    }
                }
                break;
        }
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    public void submitCard() {
        // TODO: replace with your own test key
        /*final String publishableApiKey = BuildConfig.DEBUG ?
                "pk_test_6pRNASCoBOKtIshFeQd4XMUh" :
                getString(R.string.com_stripe_publishable_key);*/

        if (etDescription.getText().toString().isEmpty()) {
            AppUtils.showToast(getActivity(), getString(R.string.err_txt_desc_empty));
            return;
        } else if (mCardForm.getCardNumber().isEmpty() && mCardForm.getExpirationMonth().isEmpty() && mCardForm.getExpirationYear().isEmpty()
                && mCardForm.getCvv().isEmpty()) {
            AppUtils.showToast(getActivity(), getString(R.string.err_payment));
        } else {
            mActivity.bookServiceDetails.setNotes(etDescription.getText().toString().trim());
            mActivity.generateCardPayment(mCardForm);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        tvCurrentBalance.setText(Html.fromHtml(mActivity.currency) + mActivity.walletAmount);
    }
}
package com.dreamguys.truelysell.fragments.phase3;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOBookingDetail;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HistoryBookingInfoFragment extends Fragment {


    Unbinder unbinder;
    DAOBookingDetail.BookingDetails bookingDetails;
    @BindView(R.id.iv_appnt_slot)
    ImageView ivAppntSlot;
    @BindView(R.id.tv_booked_date)
    TextView tvBookedDate;
    @BindView(R.id.tv_fromtime)
    TextView tvFromtime;
    @BindView(R.id.tv_totime)
    TextView tvTotime;
    @BindView(R.id.iv_buyer_msg)
    ImageView ivBuyerMsg;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.ll_booking_info)
    LinearLayout llBookingInfo;
    @BindView(R.id.et_rejected_reason)
    EditText etRejectedReason;
    @BindView(R.id.et_admin_comments)
    EditText etAdminComments;
    @BindView(R.id.rl_rejected_reason)
    RelativeLayout rlRejectedReason;
    @BindView(R.id.rl_admin_comments)
    RelativeLayout rlAdminComments;
    @BindView(R.id.iv_rejected_reason)
    ImageView ivRejectedReason;
    @BindView(R.id.iv_admin_comments)
    ImageView ivAdminComments;
    @BindView(R.id.tv_from)
    TextView tvFrom;
    @BindView(R.id.tv_to)
    TextView tvTo;
    @BindView(R.id.txt_appointment_slot)
    TextView txtAppointmentSlot;
    @BindView(R.id.txt_message_from_buyers)
    TextView txtMessageFromBuyers;
    @BindView(R.id.txt_rejected_reason)
    TextView txtRejectedReason;
    LanguageResponse.Data.Language.BookingDetailService bookingServiceScreenList;

    public HistoryBookingInfoFragment() {
    }

    public HistoryBookingInfoFragment(DAOBookingDetail.BookingDetails bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView;
        mView = inflater.inflate(R.layout.fragment_booking_info, container, false);
        unbinder = ButterKnife.bind(this, mView);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvFrom.setTextColor(AppUtils.getPrimaryAppTheme(getActivity()));
                tvTo.setTextColor(AppUtils.getPrimaryAppTheme(getActivity()));
                ivAppntSlot.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                ivBuyerMsg.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                ivAdminComments.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                ivRejectedReason.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));

            }
        }

        tvBookedDate.setText(bookingDetails.getServiceDate());
        tvFromtime.setText(bookingDetails.getFromTime());
        tvTotime.setText(bookingDetails.getToTime());
        etDescription.setText(bookingDetails.getNotes());

        if (!bookingDetails.getUser_rejected_reason().isEmpty()) {
            etRejectedReason.setText(bookingDetails.getUser_rejected_reason());
        } else {
            rlRejectedReason.setVisibility(View.GONE);
            etRejectedReason.setVisibility(View.GONE);
        }

        if (!bookingDetails.getAdmin_comments().isEmpty()) {
            etRejectedReason.setText(bookingDetails.getAdmin_comments());
        } else {
            rlAdminComments.setVisibility(View.GONE);
            etAdminComments.setVisibility(View.GONE);
        }
        getLocale();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getLocale(){
        try {
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.BookingDetailService);
            bookingServiceScreenList = new Gson().fromJson(commonDataStr, LanguageResponse.Data.Language.BookingDetailService.class);

            txtAppointmentSlot.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblAppoinmentSlot().getName(),
                    R.string.txt_appointment_slot));
            tvFrom.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLbl_from().getName(),
                    R.string.txt_from));
            tvTo.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLbl_to().getName(),
                    R.string.txt_to));
            txtMessageFromBuyers.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblMessageFromBuyer().getName(),
                    R.string.txt_message_from_buyers));
            etDescription.setHint(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblDescription().getName(),
                    R.string.txt_description));
            txtRejectedReason.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblRejectedReason().getName(),
                    R.string.txt_rejected_reason));
            etRejectedReason.setHint(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblRejectedReason().getName(),
                    R.string.txt_rejected_reason));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

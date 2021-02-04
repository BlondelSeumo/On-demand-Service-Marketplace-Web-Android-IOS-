package com.dreamguys.truelysell.fragments.phase3;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.adapters.ServiceReviewsAdapter;

public class ServiceCommentsFragment extends Fragment {


    @BindView(R.id.rv_other_services)
    RecyclerView rvOtherServices;
    Unbinder unbinder;
    LinearLayoutManager linearLayoutManager;
    List<GETServiceDetails.Reviews> mReviews;
    @BindView(R.id.tv_no_records_found)
    TextView tvNoRecordsFound;

    public ServiceCommentsFragment() {
    }

    public ServiceCommentsFragment(List<GETServiceDetails.Reviews> reviews) {
        this.mReviews = reviews;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView;
        mView = inflater.inflate(R.layout.fragment_seller_reviews, container, false);
        unbinder = ButterKnife.bind(this, mView);

        tvNoRecordsFound.setText("No Reviews Available");

        if (mReviews.size() > 0) {
            rvOtherServices.setVisibility(View.VISIBLE);
            tvNoRecordsFound.setVisibility(View.GONE);
            linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        homePopularServicesAdapter = new HomePopularServicesAdapter(getContext(), getHomeList.getData().getServiceList());
            rvOtherServices.setLayoutManager(linearLayoutManager);
            rvOtherServices.setAdapter(new ServiceReviewsAdapter(getActivity(), mReviews));
        } else {
            rvOtherServices.setVisibility(View.GONE);
            tvNoRecordsFound.setVisibility(View.VISIBLE);
        }


        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

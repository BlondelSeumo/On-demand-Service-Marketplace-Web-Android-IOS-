package com.dreamguys.truelysell.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.SubscriptionActivity;
import com.dreamguys.truelysell.datamodel.SubscriptionData;
import com.dreamguys.truelysell.payment.StripePayActivity;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    SubscriptionActivity ordersListActivity;
    Context mContext;
    List<SubscriptionData.SubscriptionList> itemsData;
    String fromPage;

    public SubscriptionAdapter(SubscriptionActivity mActivity, List<SubscriptionData.SubscriptionList> itemsData, String fromPage) {
        this.ordersListActivity = mActivity;
        this.mContext = mActivity.getBaseContext();
        this.itemsData = itemsData;
        this.fromPage = fromPage;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_subscription, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        if (AppUtils.isThemeChanged(mContext)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.btnBuyNow.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
            }
        }

        viewHolder.tvSubsType.setText(AppUtils.cleanString(mContext, itemsData.get(position).getSubscriptionName()));
        viewHolder.tvSubsPriceType.setText(AppUtils.cleanString(mContext, String.valueOf(Html.fromHtml(itemsData.get(position).getCurrencyCode()))));
        viewHolder.tvSubsPrice.setText(AppUtils.cleanString(mContext, "" + itemsData.get(position).getFee()));
        viewHolder.tvSubsValidUpto.setText(AppUtils.cleanString(mContext, "/" + itemsData.get(position).getFeeDescription()));

//        viewHolder.btnBuyNow.setText(ordersListActivity.subscription_used_texts.getLg9_buy_now());
//        viewHolder.btnBuyNow.setBackgroundColor(ordersListActivity.appColor);
    }

    public void updateRecyclerView(Context mContext, List<SubscriptionData.SubscriptionList> itemsData) {
        this.itemsData = itemsData;
        notifyDataSetChanged();
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_subs_type)
        TextView tvSubsType;
        @BindView(R.id.tv_subs_price)
        TextView tvSubsPrice;
        @BindView(R.id.tv_subs_price_type)
        TextView tvSubsPriceType;
        @BindView(R.id.tv_subs_valid_upto)
        TextView tvSubsValidUpto;
        @BindView(R.id.btn_buy_now)
        Button btnBuyNow;
        @BindView(R.id.card_view)
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            btnBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        if (Float.parseFloat(itemsData.get(getAdapterPosition()).getFee()) > 0) {
                            Intent callPaymentAct = new Intent(mContext, StripePayActivity.class);
                            callPaymentAct.putExtra(AppConstants.StripePrice, itemsData.get(getAdapterPosition()).getFee());
                            callPaymentAct.putExtra(AppConstants.StripeSubId, itemsData.get(getAdapterPosition()).getId());
                            callPaymentAct.putExtra(AppConstants.StripeSubName, itemsData.get(getAdapterPosition()).getSubscriptionName());
                            callPaymentAct.putExtra("FromPage", fromPage);
                            AppUtils.appStartIntent(mContext, callPaymentAct);
                            ordersListActivity.finish();
                        } else {

                            ordersListActivity.subscribeUser(itemsData.get(getAdapterPosition()).getId());

//                            Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

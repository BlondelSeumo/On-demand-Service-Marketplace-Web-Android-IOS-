package com.dreamguys.truelysell.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOTransactionHistory;
import com.dreamguys.truelysell.utils.AppUtils;

public class WalletTransactionHistoryAdapter extends RecyclerView.Adapter<WalletTransactionHistoryAdapter.ViewHolder> {


    Context mContext;
    List<DAOTransactionHistory.Data.WalletInfo.WalletHistory> mData = new ArrayList<>();
    LanguageResponse.Data.Language.WalletScreen walletScreenList;

    public WalletTransactionHistoryAdapter(Context mContext, List<DAOTransactionHistory.Data.WalletInfo.WalletHistory> data,
                                           LanguageResponse.Data.Language.WalletScreen walletScreenList) {
        this.mContext = mContext;
        this.mData = data;
        this.walletScreenList = walletScreenList;
    }

    @NonNull
    @Override
    public WalletTransactionHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_wallet_transaction_history, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletTransactionHistoryAdapter.ViewHolder holder, int position) {

        try {
            holder.tvTransactionName.setText(mData.get(position).getReason());
            holder.tvPaidAmount.setText(AppUtils.cleanLangStr(mContext,
                    walletScreenList.getLblTotAmt().getName(), R.string.txt_total_amt) + Html.fromHtml(mData.get(position).getCurrency()) + mData.get(position).getTotalAmt());
            holder.tvTaxAmount.setText(AppUtils.cleanLangStr(mContext,
                    walletScreenList.getTxtCommission().getName(), R.string.txt_commission) + " : " + Html.fromHtml(mData.get(position).getCurrency()) + mData.get(position).getTxtAmt());

            if (mData.get(position).getCreditWallet().equalsIgnoreCase("0")) {
                holder.tvTotalAmount.setText(Html.fromHtml(mData.get(position).getCurrency()) + mData.get(position).getDebitWallet());
                holder.ivType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_debit_100));
                holder.tvTotalAmount.setTextColor(mContext.getResources().getColor(R.color.colorRed));
            } else {
                holder.tvTotalAmount.setText(Html.fromHtml(mData.get(position).getCurrency()) + mData.get(position).getCreditWallet());
                holder.ivType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_credit_100));
                holder.tvTotalAmount.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = simpleDateFormat.parse(mData.get(position).getCreatedAt());
            holder.tvDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void bindData() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTransactionName, tvPaidAmount, tvTaxAmount, tvDate, tvTotalAmount;
        ImageView ivType;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTransactionName = itemView.findViewById(R.id.tv_transaction_name);
            tvPaidAmount = itemView.findViewById(R.id.tv_total_amount);
            tvTaxAmount = itemView.findViewById(R.id.tv_tax_amount);
            tvTotalAmount = itemView.findViewById(R.id.tv_transaction_amount);
            tvDate = itemView.findViewById(R.id.tv_transaction_date);
            ivType = itemView.findViewById(R.id.iv_credittype);
        }
    }
}

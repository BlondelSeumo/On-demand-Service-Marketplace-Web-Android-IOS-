package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.GETReviewTypes;
import com.dreamguys.truelysell.interfaces.OnLoadMoreListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewTypeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    int viewType;
    public OnLoadMoreListener mOnLoadMoreListener;
    List<GETReviewTypes.Ratings_type_list> category_list;
    TextView tvType;
    AlertDialog dialog;
    public String typeID = "";
    private int selectedPosition = -1;


    private int SELF = 1, LOADING = 2;

    public ReviewTypeListAdapter(Context mContext, List<GETReviewTypes.Ratings_type_list> category_list, AlertDialog dialog, TextView tvType) {
        this.mContext = mContext;
        this.category_list = category_list;
        this.dialog = dialog;
        this.tvType = tvType;
    }

    public ReviewTypeListAdapter(Context mContext, List<GETReviewTypes.Ratings_type_list> category_list, TextView tvType) {
        this.mContext = mContext;
        this.category_list = category_list;
        this.tvType = tvType;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_reviewtype, parent, false);
        return new ProviderViewHolder(itemView);

    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final ProviderViewHolder providerViewHolder = (ProviderViewHolder) viewHolder;
        providerViewHolder.tvTypename.setText(category_list.get(position).getName());

        if (selectedPosition == position) {
            providerViewHolder.itemView.setSelected(true); //using selector drawable
            providerViewHolder.tvTypename.setTextColor(ContextCompat.getColor(providerViewHolder.tvTypename.getContext(), R.color.colorPrimary));
        } else {
            providerViewHolder.itemView.setSelected(false);
            providerViewHolder.tvTypename.setTextColor(ContextCompat.getColor(providerViewHolder.tvTypename.getContext(), android.R.color.black));
        }

        providerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition);
                selectedPosition = providerViewHolder.getAdapterPosition();
                notifyItemChanged(selectedPosition);
                typeID = category_list.get(position).getId();
                tvType.setText(category_list.get(position).getName());
//                dialog.dismiss();
            }
        });

    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return category_list.size();
    }


    public class ProviderViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_typename)
        TextView tvTypename;

        public ProviderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

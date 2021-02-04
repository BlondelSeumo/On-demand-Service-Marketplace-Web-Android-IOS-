package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.SubCategoryListActivityNew;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.Phase3.ServiceCategories;
import com.dreamguys.truelysell.datamodel.ProviderListData;
import com.dreamguys.truelysell.interfaces.OnLoadMoreListener;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;
import com.dreamguys.truelysell.viewwidgets.ViewBinderHelper;

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    public ArrayList<ProviderListData.ProviderList> itemsData = new ArrayList<>();
    int viewType;
    LanguageModel.Request_and_provider_list langReqProvData = new LanguageModel().new Request_and_provider_list();
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    public OnLoadMoreListener mOnLoadMoreListener;
    List<ServiceCategories.CategoryList> category_list;

    private int SELF = 1, LOADING = 2;

    public CategoryListAdapter(Context mContext, List<ServiceCategories.CategoryList> category_list) {
        this.mContext = mContext;
        this.category_list = category_list;
    }

    public CategoryListAdapter(Activity mActivity, Context mContext, ArrayList<ProviderListData.ProviderList> itemsData, int viewType) {
        this.mActivity = mActivity;
        this.mContext = mContext;
        this.itemsData = itemsData;
        this.viewType = viewType;
        // uncomment if you want to open only one row at a time
        binderHelper.setOpenOnlyOne(true);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
       /* // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_provider, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;*/
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_subcategories_list, parent, false);
        return new ProviderViewHolder(itemView);


//        if (viewType == LOADING) {
//            itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_item, parent, false);
//            return new LoadingViewHolder(itemView);
//        } else {
//            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_provider, parent, false);
//            return new CategoryListAdapter.ProviderViewHolder(itemView);
//        }

    }

//    @Override
//    public int getItemViewType(int position) {
//        if (itemsData.get(position) == null) {
//            return LOADING;
//        } else {
//            return SELF;
//        }
//    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        ProviderViewHolder providerViewHolder = (ProviderViewHolder) viewHolder;

        providerViewHolder.tvSubcatname.setText(category_list.get(position).getCategoryName());

        Picasso.get()
                .load(AppConstants.BASE_URL + category_list.get(position).getCategoryImage())
                .placeholder(R.drawable.ic_service_placeholder)
                .error(R.drawable.ic_service_placeholder)
                .into(providerViewHolder.ivSubcatImage);
        providerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callSubCategoryList = new Intent(mContext, SubCategoryListActivityNew.class);
//                callSubCategoryList.putExtra(AppConstants.CatID, category_list.get(position).getId());
                callSubCategoryList.putExtra(AppConstants.SubCatID, category_list.get(position).getId());
                callSubCategoryList.putExtra(AppConstants.CATNAME, category_list.get(position).getCategoryName());
                mContext.startActivity(callSubCategoryList);
            }
        });

    }

    public void updateRecyclerView(Context mContext, ArrayList<ProviderListData.ProviderList> itemsData) {
        this.mContext = mContext;
        this.itemsData.addAll(itemsData);
        notifyDataSetChanged();
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return category_list.size();
    }


    public class ProviderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_subcat_image)
        CircleImageView ivSubcatImage;
        @BindView(R.id.tv_subcatname)
        TextView tvSubcatname;

        public ProviderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }
}

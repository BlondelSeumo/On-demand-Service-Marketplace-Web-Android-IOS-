package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.Phase3.ServiceCategories;
import com.dreamguys.truelysell.datamodel.ProviderListData;
import com.dreamguys.truelysell.interfaces.OnLoadMoreListener;
import com.dreamguys.truelysell.viewwidgets.ViewBinderHelper;

public class ServiceCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    public ArrayList<ProviderListData.ProviderList> itemsData = new ArrayList<>();
    int viewType;
    LanguageModel.Request_and_provider_list langReqProvData = new LanguageModel().new Request_and_provider_list();
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    public OnLoadMoreListener mOnLoadMoreListener;
    //    List<CategoryList.Category_list> category_list;
    public String cat_id = "";
    AlertDialog dialog;
    TextView tvCategory;
    int appColor;
    List<ServiceCategories.CategoryList> categoryList;
    BottomSheetDialog attachChooser;

    public ServiceCategoriesAdapter(Context mContext, List<ServiceCategories.CategoryList> categoryList, TextView tvCatetory, BottomSheetDialog attachChooser, String catID) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.tvCategory = tvCatetory;
        this.attachChooser = attachChooser;
        this.cat_id = catID;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_service_categories, parent, false);
        return new CategoryViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) viewHolder;
        categoryViewHolder.tvCatName.setText(categoryList.get(position).getCategoryName());

//        Glide.with(mContext)
//                .load(AppConstants.BASE_URL + categoryList.get(position).getCategoryImage())
//                .into(categoryViewHolder.ivCatImg);

        categoryViewHolder.ivCatImg.setVisibility(View.GONE);

        categoryViewHolder.tvCatName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCategory.setText(categoryList.get(position).getCategoryName());
                cat_id = categoryList.get(position).getId();
                attachChooser.dismiss();
            }
        });
    }

    public void updateRecyclerView(Context mContext, ArrayList<ProviderListData.ProviderList> itemsData) {
        this.mContext = mContext;
        this.itemsData.addAll(itemsData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_cat_name)
        TextView tvCatName;
        @BindView(R.id.iv_cat_img)
        ImageView ivCatImg;

        public CategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

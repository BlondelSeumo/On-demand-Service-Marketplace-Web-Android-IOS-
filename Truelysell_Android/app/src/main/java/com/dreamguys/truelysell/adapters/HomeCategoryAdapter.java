package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamguys.truelysell.CategoryListActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.SubCategoryListActivityNew;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.GETHomeList;
import com.dreamguys.truelysell.datamodel.ProviderListData;
import com.dreamguys.truelysell.interfaces.OnLoadMoreListener;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;
import com.dreamguys.truelysell.viewwidgets.ViewBinderHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import afu.org.checkerframework.checker.nullness.qual.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    public ArrayList<ProviderListData.ProviderList> itemsData = new ArrayList<>();
    int viewType;
    LanguageModel.Request_and_provider_list langReqProvData = new LanguageModel().new Request_and_provider_list();
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    public OnLoadMoreListener mOnLoadMoreListener;
    public String cat_id = "";
    AlertDialog dialog;
    TextView tvCategory;
    int appColor;
    List<GETHomeList.CategoryList> categoryList;
    private int VIEW_TYPE_FOOTER = 0;
    private int VIEW_TYPE_CELL = 1;
    LanguageResponse.Data.Language.CommonStrings commonStringsList;


    public HomeCategoryAdapter(Context mContext, List<GETHomeList.CategoryList> categoryList,
                               LanguageResponse.Data.Language.CommonStrings commonStringsList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.commonStringsList = commonStringsList;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        if (viewType == VIEW_TYPE_CELL) {
            View itemView;
            itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_categories, parent, false);
            return new CategoryViewHolder(itemView);
            //Create viewholder for your default cell
        } else {
            View itemView;
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_viewmore, parent, false);
            return new ViewMoreViewHolder(itemView);
            //Create viewholder for your footer view
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (getItemCount() >= 5 || getItemCount() < 5) {
            if (viewHolder instanceof CategoryViewHolder) {
                CategoryViewHolder categoryViewHolder = (CategoryViewHolder) viewHolder;

                categoryViewHolder.tvCatName.setText(categoryList.get(position).getCategoryName());

                Picasso.get()
                        .load(AppConstants.BASE_URL + categoryList.get(position).getCategoryImage())
                        .placeholder(R.drawable.ic_service_placeholder)
                        .into(categoryViewHolder.ivCatImg);


//        categoryViewHolder.tvCatName.setText(category_list.get(position).getCategory_name());
//
//        if (category_list.get(position).isIs_checked()) {
//            categoryViewHolder.tvCatName.setTextColor(appColor);
//        } else {
//            categoryViewHolder.tvCatName.setTextColor(mContext.getResources().getColor(R.color.colorDarkGray));
//        }
//
                categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callSubCategoryList = new Intent(mContext, SubCategoryListActivityNew.class);
//                callSubCategoryList.putExtra(AppConstants.CatID, category_list.get(position).getId());
                        callSubCategoryList.putExtra(AppConstants.SubCatID, categoryList.get(position).getId());
                        callSubCategoryList.putExtra(AppConstants.CATNAME, categoryList.get(position).getCategoryName());
                        mContext.startActivity(callSubCategoryList);
                    }
                });
            } else {
                final ViewMoreViewHolder holder = (ViewMoreViewHolder) viewHolder;
                holder.ivViewmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callUserList = new Intent(mContext, CategoryListActivity.class);
//                    callUserList.putExtra(AppConstants.QuestionID, question_id);
                        mContext.startActivity(callUserList);
                    }
                });
            }
        } else {
            final ViewMoreViewHolder holder = (ViewMoreViewHolder) viewHolder;
            holder.ivViewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callUserList = new Intent(mContext, CategoryListActivity.class);
//                    callUserList.putExtra(AppConstants.QuestionID, question_id);
                    mContext.startActivity(callUserList);
                }
            });
        }
    }

    public void updateRecyclerView(Context mContext, ArrayList<ProviderListData.ProviderList> itemsData) {
        this.mContext = mContext;
        this.itemsData.addAll(itemsData);
        notifyDataSetChanged();
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (categoryList.size() >= 5) {
            return categoryList.size() + 1;
        } else {
            return categoryList.size();
        }

//        return category_list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() > 5) {
            return (position + 1 == getItemCount()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
        } else {
            return VIEW_TYPE_CELL;
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_cat_img)
        CircleImageView ivCatImg;
        @BindView(R.id.tv_cat_name)
        TextView tvCatName;

        public CategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class ViewMoreViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_viewmore)
        ImageView ivViewmore;
        @BindView(R.id.txt_view_more)
        TextView txt_view_more;

        public ViewMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            txt_view_more.setText(AppUtils.cleanLangStr(mContext,
                    commonStringsList.getLblViewMore().getName(), R.string.txt_view_more));
        }
    }
}

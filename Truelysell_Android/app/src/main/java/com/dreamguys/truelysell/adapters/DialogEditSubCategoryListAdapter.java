package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.Phase3.DAOServiceSubCategories;
import com.dreamguys.truelysell.datamodel.ProviderListData;
import com.dreamguys.truelysell.interfaces.OnLoadMoreListener;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.viewwidgets.ViewBinderHelper;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogEditSubCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    public ArrayList<ProviderListData.ProviderList> itemsData = new ArrayList<>();
    int viewType;
    LanguageModel.Request_and_provider_list langReqProvData = new LanguageModel().new Request_and_provider_list();
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    public OnLoadMoreListener mOnLoadMoreListener;
    List<DAOServiceSubCategories.SubcategoryList> category_list;
    public String cat_id = "", subCatID = "";
    AlertDialog dialog;
    EditText tvCategory;
    Button btnDone;


    public DialogEditSubCategoryListAdapter(Context mContext, List<DAOServiceSubCategories.SubcategoryList> category_list, AlertDialog dialog, EditText etCategory, String subCatID, Button btnDone) {
        this.mContext = mContext;
        this.dialog = dialog;
        this.tvCategory = etCategory;
        this.category_list = category_list;
        this.subCatID = subCatID;
        this.btnDone = btnDone;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.dlg_adapter_categories_list, parent, false);
        return new CategoryViewHolder(itemView);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) viewHolder;
        categoryViewHolder.setIsRecyclable(false);
        categoryViewHolder.tvCatName.setText(category_list.get(position).getSubcategoryName());

        if (category_list.get(position).isChecked()) {
            categoryViewHolder.tvCatName.setChecked(true);
        }

        Picasso.get()
                .load(AppConstants.BASE_URL + category_list.get(position).getSubcategoryImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(categoryViewHolder.ivCatImg);


        categoryViewHolder.tvCatName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String catName = "";
                catName = category_list.get(position).getSubcategoryName();
                subCatID = category_list.get(position).getId();
                tvCategory.setText(catName);
                dialog.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String catName = "";
                cat_id = "";
                subCatID = "";
                for (int i = 0; i < category_list.size(); i++) {
                    if (category_list.get(i).isChecked()) {
                        catName = category_list.get(i).getSubcategoryName();
                        subCatID = category_list.get(i).getId();
                    }
                }
                if (!catName.isEmpty())
                    tvCategory.setText(catName.substring(0, catName.length() - 1));
                dialog.dismiss();
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


    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_cat_img)
        ImageView ivCatImg;
        @BindView(R.id.tv_cat_name)
        CheckBox tvCatName;

        public CategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }
}

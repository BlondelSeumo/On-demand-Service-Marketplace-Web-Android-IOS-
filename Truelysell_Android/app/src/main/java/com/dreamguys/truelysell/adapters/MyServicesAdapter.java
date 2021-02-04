package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dreamguys.truelysell.ActivityServiceDetail;
import com.dreamguys.truelysell.EditServiceActivity;
import com.dreamguys.truelysell.MyProviderServicesActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOMyServices;
import com.dreamguys.truelysell.datamodel.ProviderListData;
import com.dreamguys.truelysell.interfaces.OnLoadMoreListener;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;
import com.dreamguys.truelysell.viewwidgets.ViewBinderHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    Context mContext;
    public ArrayList<ProviderListData.ProviderList> itemsData = new ArrayList<>();
    int viewType;
    LanguageModel.Request_and_provider_list langReqProvData = new LanguageModel().new Request_and_provider_list();
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    public OnLoadMoreListener mOnLoadMoreListener;
    List<DAOMyServices.Datum> daoMyServices;
    public String cat_id = "";
    AlertDialog dialog;
    TextView tvCategory;
    int appColor;
    MyProviderServicesActivity myProviderServicesActivity;
    LanguageResponse.Data.Language.BookingService bookingServiceScreenList;


    public MyServicesAdapter(Context mContext, List<DAOMyServices.Datum> daoMyServices, AlertDialog dialog, TextView etCategory, String cat_ID, int appColor) {
        this.mContext = mContext;
        this.dialog = dialog;
        this.tvCategory = etCategory;
        this.daoMyServices = daoMyServices;
        this.cat_id = cat_ID;
        this.appColor = appColor;
    }

    public MyServicesAdapter(Context mContext, List<DAOMyServices.Datum> daoMyServices,
                             MyProviderServicesActivity myProviderServicesActivity,
                             LanguageResponse.Data.Language.BookingService bookingServiceScreenList) {
        this.mContext = mContext;
        this.daoMyServices = daoMyServices;
        this.myProviderServicesActivity = myProviderServicesActivity;
        this.bookingServiceScreenList = bookingServiceScreenList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_myservices, parent, false);
        return new MyServicesViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        MyServicesViewHolder myServicesViewHolder = (MyServicesViewHolder) viewHolder;


        if (AppUtils.isThemeChanged(mContext)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myServicesViewHolder.ivEdit.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
                myServicesViewHolder.ivDelete.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
            }
        }

        myServicesViewHolder.tvServiceName.setText(daoMyServices.get(position).getServiceTitle());
        myServicesViewHolder.tvCategory.setText(daoMyServices.get(position).getCategoryName());
        myServicesViewHolder.rbRating.setRating(Float.parseFloat(daoMyServices.get(position).getRatings()));
        myServicesViewHolder.tvRating.setText("(" + daoMyServices.get(position).getRatingCount() + ")");
        myServicesViewHolder.tvServiceCost.setText(Html.fromHtml(daoMyServices.get(position).getCurrency()) + daoMyServices.get(position).getServiceAmount());

        Glide.with(mContext)
                .load(AppConstants.BASE_URL + daoMyServices.get(position).getServiceImage())
                .apply(new RequestOptions().error(R.drawable.ic_service_placeholder).placeholder(R.drawable.ic_service_placeholder).transforms(new CenterCrop(), new RoundedCorners(40)))
                .into(myServicesViewHolder.ivServiceImage);

        if (daoMyServices.get(position).getIs_active().equalsIgnoreCase("1")) {
            myServicesViewHolder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.active_status));
            myServicesViewHolder.ivStatus.setText("Active");
            myServicesViewHolder.ivStatus.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
        } else {
            myServicesViewHolder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.inactive_status));
            myServicesViewHolder.ivStatus.setText("Inactive");
            myServicesViewHolder.ivStatus.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

        myServicesViewHolder.ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (daoMyServices.get(position).getIs_active().equalsIgnoreCase("1")) {
                    myProviderServicesActivity.showAlertDialog("2", daoMyServices.get(position).getServiceId(),
                            AppUtils.cleanLangStr(mContext, bookingServiceScreenList.getLblInactiveYourService().getName(),
                                    R.string.txt_inactive_service));
                } else {
                    myProviderServicesActivity.showAlertDialog("1", daoMyServices.get(position).getServiceId(),
                            AppUtils.cleanLangStr(mContext, bookingServiceScreenList.getLblActiveYourService().getName(),
                                    R.string.txt_active_service));
                }
            }
        });


        myServicesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callServiceDetailAct = new Intent(mContext, ActivityServiceDetail.class);
                callServiceDetailAct.putExtra(AppConstants.FROMPAGE, AppConstants.MYSERVICES);
                callServiceDetailAct.putExtra(AppConstants.SERVICEID, daoMyServices.get(position).getServiceId());
                callServiceDetailAct.putExtra(AppConstants.SERVICETITLE, daoMyServices.get(position).getServiceTitle());
                mContext.startActivity(callServiceDetailAct);

            }
        });

        myServicesViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(AppUtils.cleanLangStr(mContext, bookingServiceScreenList.getLbl_delete_title().getName(), R.string.txt_delete_service)) //TODO:
                        .setCancelable(false)
                        .setPositiveButton(AppUtils.cleanLangStr(mContext, bookingServiceScreenList.getLbl_yes().getName(), R.string.txt_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { //TODO:
                                //PreferenceStorage.clearPref();
                                myProviderServicesActivity.postDeleteService(daoMyServices.get(position).getServiceId());
                                dialog.dismiss();
//                                userLogout(); //TODO:
                            }
                        })
                        .setNegativeButton(AppUtils.cleanLangStr(mContext, bookingServiceScreenList.getLbl_no().getName(), R.string.txt_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { //TODO:
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        myServicesViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callEditServiceAct = new Intent(mContext, EditServiceActivity.class);
                callEditServiceAct.putExtra(AppConstants.SERVICETITLE, daoMyServices.get(position).getServiceTitle());
                callEditServiceAct.putExtra(AppConstants.SERVICEID, daoMyServices.get(position).getServiceId());
                mContext.startActivity(callEditServiceAct);

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
        return daoMyServices.size();
    }


    public class MyServicesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_service_image)
        ImageView ivServiceImage;
        @BindView(R.id.tv_service_name)
        TextView tvServiceName;
        @BindView(R.id.rb_rating)
        RatingBar rbRating;
        @BindView(R.id.tv_rating)
        TextView tvRating;
        @BindView(R.id.iv_userimage)
        CircleImageView ivUserimage;
        @BindView(R.id.iv_edit)
        ImageView ivEdit;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.iv_status)
        TextView ivStatus;
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.tv_service_cost)
        TextView tvServiceCost;
        @BindView(R.id.tv_viewonmap)
        TextView tvViewonmap;
        @BindView(R.id.txt_inprogress)
        TextView txtInprogress;

        public MyServicesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }
}

package com.dreamguys.truelysell.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import com.dreamguys.truelysell.R;

public class ServiceImagesAdapter extends PagerAdapter {


    Context mContext;
    List<String> imagesList;
    LayoutInflater inflater;
    ImageView ivServiceImages;


    public ServiceImagesAdapter(Context mContext/*, List<String> imageList*/) {
        this.mContext = mContext;
//        this.imagesList = imageList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.adapter_service_collections, collection, false);
        ivServiceImages = layout.findViewById(R.id.iv_serviceimages);
        collection.addView(layout);

        Glide.with(mContext)
                .load(R.drawable.ic_service_placeholder)
                .apply(new RequestOptions().error(R.drawable.ic_service_placeholder).placeholder(R.drawable.ic_service_placeholder).transforms(new CenterCrop(), new RoundedCorners(40)))
                .into(ivServiceImages);

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }
}

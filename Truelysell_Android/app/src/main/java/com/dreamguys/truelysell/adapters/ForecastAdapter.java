package com.dreamguys.truelysell.adapters;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.utils.AppConstants;

/**
 * Created by yarolegovich on 08.03.2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private RecyclerView parentRecycler;
    private List<String> data;

    public ForecastAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecycler = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_service_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int iconTint = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorGray_light);
        Glide.with(holder.itemView.getContext())
                .load(AppConstants.BASE_URL + data.get(position))
                .apply(new RequestOptions().placeholder(R.drawable.ic_service_placeholder))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }

//        public void showText() {
//            int parentHeight = ((View) imageView.getParent()).getHeight();
//            float scale = (parentHeight - textView.getHeight()) / (float) imageView.getHeight();
//            imageView.setPivotX(imageView.getWidth() * 0.5f);
//            imageView.setPivotY(0);
//            imageView.animate().scaleX(scale)
//                    .withEndAction(new Runnable() {
//                        @Override
//                        public void run() {
//                            textView.setVisibility(View.VISIBLE);
//                            imageView.setColorFilter(Color.BLACK);
//                        }
//                    })
//                    .scaleY(scale).setDuration(200)
//                    .start();
//        }
//
//        public void hideText() {
//            imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.grayIconTint));
//            textView.setVisibility(View.INVISIBLE);
//            imageView.animate().scaleX(1f).scaleY(1f)
//                    .setDuration(200)
//                    .start();
//        }

        @Override
        public void onClick(View v) {
            parentRecycler.smoothScrollToPosition(getAdapterPosition());
        }
    }

}

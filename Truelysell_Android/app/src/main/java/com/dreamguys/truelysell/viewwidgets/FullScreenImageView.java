package com.dreamguys.truelysell.viewwidgets;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dreamguys.truelysell.R;

public class FullScreenImageView extends AppCompatActivity {

    @BindView(R.id.iv_image_fullscreen)
    TouchImageView ivImageFullscreen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_imageview);
        ButterKnife.bind(this);

        String imagePath = getIntent().getStringExtra("FilePath");
        Picasso.get()
                .load("file://" + new File(imagePath).getAbsolutePath())
                .placeholder(R.drawable.ic_pic_view)
                .error(R.drawable.ic_pic_view)
                .into(ivImageFullscreen);
    }

}

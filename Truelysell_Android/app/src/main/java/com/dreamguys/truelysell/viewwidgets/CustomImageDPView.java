package com.dreamguys.truelysell.viewwidgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.interfaces.OnFileActivityResult;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;

public class CustomImageDPView extends ConstraintLayout implements OnFileActivityResult {

    public final static int VIEW_MODE = 0;
    public final static int ADD_MODE = 1;
    private BottomSheetDialog attachChooser;
    private Uri cameraImageUri;
    private boolean mShowAddButton;
    private boolean imageOnly;
    View infView;
    File imageFile;

    CircleImageView ivPicture;
    LanguageModel.Common_used_texts commonTextData = new LanguageModel().new Common_used_texts();
    ImageView btnAddAttach;

    public CustomImageDPView(Context context) {
        super(context);
        init(context);
    }

    public CustomImageDPView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageDPView, 0, 0);

        try {
            setShowAddButton(a.getBoolean(R.styleable.CustomImageDPView_showAddButton, false));
            imageOnly = a.getBoolean(R.styleable.CustomImageDPView_imageOnly, false);
        } finally {
            a.recycle();
        }
        init(context);
    }

    public CustomImageDPView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageDPView, 0, 0);

        try {
            setShowAddButton(a.getBoolean(R.styleable.CustomImageDPView_showAddButton, false));
            imageOnly = a.getBoolean(R.styleable.CustomImageDPView_imageOnly, false);
        } finally {
            a.recycle();
        }
        init(context);
    }

    public boolean isShowAddButton() {
        return mShowAddButton;
    }

    public void setShowAddButton(boolean mShowAddButton) {
        this.mShowAddButton = mShowAddButton;
        invalidate();
        requestLayout();
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        infView = (View) inflater.inflate(R.layout.custom_imagedp_layout, null, false);

        getLocaleData();

        setBackgroundResource(android.R.color.transparent);
        btnAddAttach = (ImageView) infView.findViewById(R.id.iv_picture_add);
        ivPicture = (CircleImageView) infView.findViewById(R.id.iv_picture);


        try {
            btnAddAttach.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAttachment(v);
                }
            });
        } catch (Exception e) {
            Log.e(null, e.getLocalizedMessage());
        }

        ivPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = (String) getTag();
                Log.d("TAG", "filePath = " + filePath);
                if (filePath != null && !filePath.isEmpty()) {
                    Intent fullScreen = new Intent(getContext(), FullScreenImageView.class);
                    fullScreen.putExtra("FilePath", filePath);
                    getContext().startActivity(fullScreen);
                }
            }
        });
        if (mShowAddButton) {
            btnAddAttach.setVisibility(View.VISIBLE);
            infView.setBackgroundResource(android.R.color.transparent);
        } else {
            btnAddAttach.setVisibility(View.GONE);
            infView.setBackgroundResource(android.R.color.transparent);
        }
        addView(infView);
    }

    public void setViewMode(int mode) {
        CircleImageView addAttachmentBtn = (CircleImageView) ((FragmentActivity) getContext()).findViewById(R.id.iv_picture_add);
        RelativeLayout attachLayout = (RelativeLayout) ((FragmentActivity) getContext()).findViewById(R.id.custom_attachment_layout);
        switch (mode) {
            case 0:
                mShowAddButton = true;
                addAttachmentBtn.setVisibility(View.GONE);
                attachLayout.setBackgroundResource(android.R.color.transparent);
                break;
            case 1:
                mShowAddButton = false;
                addAttachmentBtn.setVisibility(View.VISIBLE);
                attachLayout.setBackgroundResource(android.R.color.transparent);
                break;
            case 2:
                mShowAddButton = true;
                addAttachmentBtn.setVisibility(View.INVISIBLE);
                attachLayout.setBackgroundResource(android.R.color.transparent);
                break;
        }
    }

    public void addAttachment(View v) {
        attachChooser = new BottomSheetDialog(getContext());
        attachChooser.setContentView(((FragmentActivity) getContext()).getLayoutInflater().inflate(R.layout.popup_add_attach_options,
                new LinearLayout(getContext())));
        attachChooser.show();
        LinearLayout btnStartCamera = (LinearLayout) attachChooser.findViewById(R.id.btn_from_camera);
        LinearLayout btnStartFileBrowser = (LinearLayout) attachChooser.findViewById(R.id.btn_from_local);
        TextView tvGallery, tvCamera;
        tvGallery = (TextView) attachChooser.findViewById(R.id.tv_txt_gallery);
        tvCamera = (TextView) attachChooser.findViewById(R.id.tv_txt_camera);

        tvCamera.setText(AppUtils.cleanLangStr(getContext(), commonTextData.getLg7_camera(), R.string.txt_camera));
        tvGallery.setText(AppUtils.cleanLangStr(getContext(), commonTextData.getLg7_gallery(), R.string.txt_gallery));


        btnStartCamera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startCamera();
            }
        });
        btnStartFileBrowser.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startFileBrowser();
            }
        });
    }

    public void startCamera() {
        attachChooser.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            imageFile = AppUtils.createImageFile(getContext());
            cameraImageUri = AppUtils.createImageFile(getContext(), imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFile);
        } catch (Exception e) {

        }
        try {
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                myStartActivityForResult((FragmentActivity) getContext(),
                        intent, 2, this);
            }
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), AppUtils.cleanLangStr(getContext(), commonTextData.getLg7_please_install_2(), R.string.code_install_camera_app), Toast.LENGTH_LONG).show();
        }
    }

    public void startFileBrowser() {
        attachChooser.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        intent.addCategory(Intent.CATEGORY_DEFAULT);
        try {
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                myStartActivityForResult((FragmentActivity) getContext(),
                        Intent.createChooser(intent, AppUtils.cleanLangStr(getContext(), commonTextData.getLg7_choose_picture(), R.string.code_select_a_file)), 1, this);
            }
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), getContext().getString(R.string.code_install_file_manager), Toast.LENGTH_LONG).show(); //TODO: lang
        }
    }

    public void addImageDP(final String filePath) {
        if (filePath == null)
            return;
        Bitmap d = new BitmapDrawable(getResources(), new File(filePath).getAbsolutePath()).getBitmap();
        int nh = (int) (160 * (512.0 / 160));
        Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
        ivPicture.setImageBitmap(scaled);
        //ivPicture.setImageURI(Uri.parse(filePath));
        /*Picasso.with(getContext())
                .load("file://" + new File(filePath).getAbsolutePath())
                *//*.load(Uri.parse(filePath))*//*
                .into(ivPicture);*/
    }

    public void customActivityResultCheck(int requestCode, int resultCode, Intent data) {
        Log.d("TAG", "data = " + data + " && cameraImageUri = " + cameraImageUri);
        if (resultCode == FragmentActivity.RESULT_OK) {
            String fileP;
            if (data != null && data.getData() != null)
                fileP = AppUtils.resolveFileUri(getContext(), data.getData());
            else {
                if (requestCode == 2)
                    fileP = imageFile.getAbsolutePath();
                else
                    fileP = AppUtils.resolveFileUri(getContext(), cameraImageUri);
            }
            Log.d("TAG", "fileP = " + fileP);
            setTag(fileP);
            addImageDP(fileP);
        }
    }

    @Override
    public void onFileActivityResult(int requestCode, int resultCode, Intent data) {
        customActivityResultCheck(requestCode, resultCode, data);
    }

    public static void myStartActivityForResult(FragmentActivity act, Intent in, int requestCode, OnFileActivityResult cb) {
        Fragment aux = new FragmentForResult(cb);
        FragmentManager fm = act.getSupportFragmentManager();
        fm.beginTransaction().add(aux, "FRAGMENT_TAG").commit();
        fm.executePendingTransactions();
        aux.startActivityForResult(in, requestCode);
    }


    @SuppressLint("ValidFragment")
    public static class FragmentForResult extends Fragment {
        private OnFileActivityResult cb;

        public FragmentForResult(OnFileActivityResult cb) {
            this.cb = cb;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (cb != null)
                cb.onFileActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    private void getLocaleData() {
        try {
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.common_used_texts);
            commonTextData = new Gson().fromJson(commonDataStr, LanguageModel.Common_used_texts.class);
        } catch (Exception e) {
            commonTextData = new LanguageModel().new Common_used_texts();
        }
    }


}

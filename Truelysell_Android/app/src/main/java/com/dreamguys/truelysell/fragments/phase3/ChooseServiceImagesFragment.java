package com.dreamguys.truelysell.fragments.phase3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dreamguys.truelysell.ActivityCreateService;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ChooseServiceImagesFragment extends Fragment {


    @BindView(R.id.iv_service_image)
    ImageView ivServiceImage;
    @BindView(R.id.ll_service_images)
    LinearLayout llServiceImages;
    @BindView(R.id.btn_provider_next)
    Button btnProviderNext;
    Unbinder unbinder;
    @BindView(R.id.tv_browse_gallery)
    TextView tvBrowseGallery;
    private BottomSheetDialog attachChooser;
    private static final int RC_LOAD_IMG_CAMERA = 101;
    private static final int RC_LOAD_IMG_BROWSER = 102;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    MultipartBody.Part profileImg;
    ArrayList<Bitmap> serviceImages = new ArrayList<>();
    ActivityCreateService mActivity;
    Context mContext;
    LanguageResponse.Data.Language.CreateService createServiceStringsList;

    public void myCreateServiceInfoFragment(ActivityCreateService createProviderActivity) {
        this.mActivity = createProviderActivity;
        this.mContext = createProviderActivity.getBaseContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_upload_service_images, container, false);
        unbinder = ButterKnife.bind(this, mView);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_CONTACTS);
        permissions.add(CAMERA);
        getLocaleData();
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnProviderNext.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        return mView;
    }

    private void selectImage() {
        attachChooser = new BottomSheetDialog(getActivity());
        attachChooser.setContentView((getActivity()).getLayoutInflater().inflate(R.layout.popup_add_attach_options,
                new LinearLayout(getActivity())));
        attachChooser.show();

        LinearLayout btnStartCamera = (LinearLayout) attachChooser.findViewById(R.id.btn_from_camera);
        LinearLayout btnStartFileBrowser = (LinearLayout) attachChooser.findViewById(R.id.btn_from_local);
        btnStartCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                attachChooser.dismiss();
                if (AppUtils.checkPermission(getActivity()))
                    cameraIntent();
            }
        });
        btnStartFileBrowser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                attachChooser.dismiss();
                if (AppUtils.checkPermission(getActivity()))
                    galleryIntent();
            }
        });
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RC_LOAD_IMG_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, RC_LOAD_IMG_BROWSER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AppConstants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (requestCode == RC_LOAD_IMG_CAMERA)
                        cameraIntent();
                    else if (requestCode == RC_LOAD_IMG_BROWSER)
                        galleryIntent();
                } else {
                }
                break;

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel(AppUtils.cleanLangStr(getActivity(), "These permissions are mandatory for the application. Please allow access.", R.string.err_txt_permission),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setTitle("Required App Permission")
                .setCancelable(false)
                .setPositiveButton(AppUtils.cleanLangStr(getActivity(), "Ok", R.string.ok), okListener)
                //.setNegativeButton(AppUtils.cleanLangStr(this, commonData.getLg7_cancel(), R.string.txt_cancel), null)
                .create()
                .show();
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap thumbnail = null;
        Bitmap scaled = null;
        try {
            if (data != null) {
                thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                int nh = (int) (160 * (512.0 / 160));
                thumbnail = Bitmap.createScaledBitmap(thumbnail, 512, nh, true);
                if (thumbnail != null) {
                    scaled = thumbnail;
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    String encodeImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes.toByteArray());
                    profileImg = MultipartBody.Part.createFormData("profile_img", "profile_img.jpg", requestFile);
                }
                serviceImages.add(scaled);
//                ivServiceImage.setImageBitmap(scaled);
                addServiceImages();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void onCaptureImageResult(Intent data) {
//        try {
//            if (data != null) {
//                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                int nh = (int) (160 * (512.0 / 160));
//                thumbnail = Bitmap.createScaledBitmap(thumbnail, 512, nh, true);
//                Bitmap scaled = thumbnail;
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                if (thumbnail != null) {
//                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//                    String encodeImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
//                }
//                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes.toByteArray());
//                profileImg = MultipartBody.Part.createFormData("profile_img", "profile_img.jpg", requestFile);
//                //imagePartList.add(icCardImg);
//                //ivIcCard.setImageBitmap(thumbnail);
//                ivProviderImg.setImageBitmap(scaled);
//            }
//        } catch (Exception e) {
//        }
//    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();
        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_LOAD_IMG_BROWSER) {
            onSelectFromGalleryResult(data);
        }
//        else if (requestCode == RC_LOAD_IMG_CAMERA) {
//            onCaptureImageResult(data);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_browse_gallery, R.id.btn_provider_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_browse_gallery:
                if (AppUtils.checkPermission(getActivity()))
                    galleryIntent();
                break;
            case R.id.btn_provider_next:
                if (serviceImages.size() >= 1) {
                    mActivity.providerData.setServiceImages(serviceImages);
                    mActivity.checkAvailabiltySlots();
                } else {
                    Toast.makeText(mActivity, "Add minimum 1 image to create a service", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    public void addServiceImages() {
        if (serviceImages.size() > 0) {
            llServiceImages.removeAllViews();
            for (int i = 0; i < serviceImages.size(); i++) {
                LayoutInflater inflater = getLayoutInflater();
                View inflatedLayout = inflater.inflate(R.layout.list_item_service_images, llServiceImages, false);
                ImageView ivServiceImg = inflatedLayout.findViewById(R.id.iv_service_image);
                ImageView ivExit = inflatedLayout.findViewById(R.id.iv_exit);
                ivServiceImg.setId(i);
                Glide.with(getActivity())
                        .load(serviceImages.get(i))
                        .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(30)))
                        .into(ivServiceImg);

//                ivServiceImg.setImageBitmap(serviceImages.get(i));
                ivExit.setId(i);

                final int finalI = i;
                ivExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        serviceImages.remove(finalI);
                        addServiceImages();
                    }
                });
                llServiceImages.addView(inflatedLayout);
            }
        } else {
            llServiceImages.removeAllViews();
        }
    }

    private void getLocaleData() {
        try {
            String createDataStr = PreferenceStorage.getKey(CommonLangModel.CreateService);
            createServiceStringsList = new Gson().fromJson(createDataStr, LanguageResponse.Data.Language.CreateService.class);

            tvBrowseGallery.setText(AppUtils.cleanLangStr(getActivity(),
                    createServiceStringsList.getLblBrowseFromGallery().getName(), R.string.browse_from_gallery));
            btnProviderNext.setText(AppUtils.cleanLangStr(getActivity(),
                    createServiceStringsList.getLblUpload().getName(), R.string.txt_upload));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.moos.selector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SystemPictureSelector pictureSelector;
    private String savedPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".png";

    private Button button;
    private FrameLayout container;
    private ImageView imageView;
    private int REQUEST_TAKE_PHOTO_PERMISSION = 1112;
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {

        button = findViewById(R.id.btn_select);
        //container = findViewById(R.id.image_container);
        imageView = findViewById(R.id.image_selected);
        imageView.setOnClickListener(this);
        button.setOnClickListener(this);

        SystemPictureSelector.Builder builder = new SystemPictureSelector.Builder(this);
        builder.isCropped(true)
                .setCropSize(3, 2)
                .setOutputSize(1200, 1200)
                .setOutputPath(savedPath)
                .setOnSelectListener(new SystemPictureSelector.OnSystemPictureSelectListener() {
                    @Override
                    public void onSelectedSuccess(File file) {

                        Uri uri = Uri.fromFile(file);
                        Log.e(TAG, "onSelectedSuccess: "+uri.toString() );

                        Glide.clear(imageView);
                        Glide.with(MainActivity.this)
                                .load(file)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(new SimpleTarget<GlideDrawable>() {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        imageView.setImageDrawable(resource);
                                    }
                                });
                    }

                    @Override
                    public void onSelectedMessage(String msg) {

                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
        pictureSelector = builder.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        pictureSelector.bindingActivityForResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_selected:

                showSelectDialog();
                break;

            case R.id.btn_select:

                showSelectDialog();
                break;
        }
    }


    private void showSelectDialog(){
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View selectView = LayoutInflater.from(this).inflate(R.layout.dialog_layout,null);
        TextView bt_camera = selectView.findViewById(R.id.btn_by_camera);
        TextView bt_gallery = selectView.findViewById(R.id.btn_by_gallery);
        TextView bt_cancel = selectView.findViewById(R.id.btn_cancel);
        dialog.setContentView(selectView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) selectView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        selectView.measure(0,0);
        behavior.setPeekHeight(selectView.getMeasuredHeight());

        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //申请摄像头权限
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_TAKE_PHOTO_PERMISSION);

                } else {
                    pictureSelector.getSystemPhotoByCamera();
                }
            }
        });

        bt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                pictureSelector.getSystemPhotoByGallery();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功，可以拍照
                pictureSelector.getSystemPhotoByCamera();
            } else {
                Toast.makeText(MainActivity.this,"您拒绝了权限，该功能不可用\n可在应用设置里授权拍照哦",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

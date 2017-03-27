package com.markdevelopers.cardetector.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.markdevelopers.cardetector.R;
import com.markdevelopers.cardetector.common.BaseActivity;
import com.markdevelopers.cardetector.data.remote.models.VerifyCar;
import com.markdevelopers.cardetector.mylib.Config;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Archish on 3/24/2017.
 */

public class DetailsActivity extends BaseActivity implements CarDetectorContract.CarDetectorView {
    EditText etData, etName, etState;
    FrameLayout fImage;
    FloatingActionButton fabUpload;
    Button bSave;
    ImageView ivImage, ivCloseButton;
    final private int MY_PERMISSIONS_REQUEST_CAMERA = 123;
    private static final int ADDRESS_CAMERA_IMAGE = 1850;
    private static final int ADDRESS_GALLERY_IMAGE = 1851;
    String path = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();
        Intent i = getIntent();
        String data = i.getStringExtra("data");
        etData.setText(data);
    }

    private void initViews() {
        etData = (EditText) findViewById(R.id.etData);
        etName = (EditText) findViewById(R.id.etName);
        etState = (EditText) findViewById(R.id.etState);
        fImage = (FrameLayout) findViewById(R.id.fImage);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivCloseButton = (ImageView) findViewById(R.id.ivCloseButton);
        fabUpload = (FloatingActionButton) findViewById(R.id.fabUpload);
        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog();
            }
        });
        bSave = (Button) findViewById(R.id.bSave);
        final CarDetectorPresenter carDetectorPresenter = new CarDetectorPresenter(this);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean status = validate();
                if (status) {
                    if (path != null) {
                        File file = new File(path);
                        showProgressDialog();
                        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                        carDetectorPresenter.sendData(etData.getText().toString(), etName.getText().toString(), etState.getText().toString(), fileToUpload, filename);
                    } else {
                        carDetectorPresenter.sendData(etData.getText().toString(), etName.getText().toString(), etState.getText().toString(), null, null);
                    }
                }
            }
        });
        ivCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fImage.setVisibility(View.GONE);
                path = "";
            }
        });

    }

    private boolean validate() {
        if (etData.getText().toString().isEmpty())
            return false;
        return true;
    }

    private void showImageDialog() {
        Dialog dialog = new Dialog(DetailsActivity.this);
        dialog.setContentView(R.layout.dialog_image);
        dialog.setTitle("Select Image");
        TextView tvTakePhoto = (TextView) dialog.findViewById(R.id.tvTakePhoto);
        TextView tvGallery = (TextView) dialog.findViewById(R.id.tvGallery);
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeAddressPhotoWrapper();
            }
        });
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, ADDRESS_GALLERY_IMAGE);
            }
        });
        dialog.show();

    }

    private void takeAddressPhotoWrapper() {

        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("ExternalStorage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(DetailsActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
                return;
            }
            ActivityCompat.requestPermissions(DetailsActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                    MY_PERMISSIONS_REQUEST_CAMERA);
            return;
        }
        takeAddressPhoto();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case ADDRESS_CAMERA_IMAGE:
                    Uri cameraImageUri = Uri.fromFile(new File(path));
                    Picasso.with(getApplicationContext()).load(cameraImageUri).fit().into(ivImage);
                    fImage.setVisibility(View.VISIBLE);
                    break;
                case ADDRESS_GALLERY_IMAGE:
                    Uri selectedImageGallery = data.getData();
                    fImage.setVisibility(View.VISIBLE);
                    path = getRealPathFromURI(selectedImageGallery);
                    Picasso.with(getApplicationContext()).load(selectedImageGallery).fit().into(ivImage);
                    fImage.setVisibility(View.VISIBLE);

            }

        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DetailsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void takeAddressPhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + Config.DIR);

        if (!folder.exists()) {
            folder.mkdir();
        }
        final Calendar c = Calendar.getInstance();
        String dateTime = c.get(Calendar.DAY_OF_MONTH) + "-"
                + ((c.get(Calendar.MONTH)) + 1) + "-"
                + c.get(Calendar.YEAR) + "-"
                + c.get(Calendar.HOUR) + "-"
                + c.get(Calendar.MINUTE) + "-"
                + c.get(Calendar.SECOND);
        path = String.format(Environment.getExternalStorageDirectory() + "/" + Config.DIR + "/%s.png",
                dateTime);

        File photo = new File(path);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        startActivityForResult(intent, ADDRESS_CAMERA_IMAGE);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    @Override
    public void onResponse(VerifyCar verifyCar) {
        dismissProgressDialog();
        if (verifyCar.isStatus()) {
            Toast.makeText(getApplicationContext(), verifyCar.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), verifyCar.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

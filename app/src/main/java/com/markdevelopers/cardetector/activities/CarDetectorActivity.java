package com.markdevelopers.cardetector.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.markdevelopers.cardetector.R;
import com.markdevelopers.cardetector.common.BaseActivity;
import com.markdevelopers.cardetector.data.remote.models.VerifyCar;
import com.markdevelopers.cardetector.mylib.ArcOcrScanner;
import com.markdevelopers.cardetector.mylib.ArcOcrScannerListener;
import com.markdevelopers.cardetector.mylib.Config;
import com.markdevelopers.cardetector.mylib.ImageProcessingThread;
import com.squareup.picasso.Picasso;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CarDetectorActivity extends BaseActivity implements ArcOcrScannerListener, CarDetectorContract.CarDetectorView {

    ArcOcrScanner arcOcrScanner;
    EditText etOCR;
    ProgressDialog mProgressDialog;
    Button bScan, bVerify, bAbout;
    ImageView ivImage;
    CarDetectorPresenter carDetectorPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detector);
        requestPermission();
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_car_detector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mVerify:
                if (validate()) {
                    showProgressDialog();
                    carDetectorPresenter.sendData(etOCR.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid number plate", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validate() {
        if (etOCR.getText().toString().isEmpty())
            return false;
        return true;
    }

    private void initViews() {
        etOCR = (EditText) findViewById(R.id.etOCR);
        bScan = (Button) findViewById(R.id.bScan);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        bScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog();
            }
        });
        arcOcrScanner = new ArcOcrScanner(CarDetectorActivity.this, "CarDetector",
                Config.REQUEST_CODE_CAPTURE_IMAGE, "eng");

        // Set ocrScannerListener
        carDetectorPresenter = new CarDetectorPresenter(this);
        arcOcrScanner.setOcrScannerListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Call onImageTaken() information onActivityResult.
        if (resultCode == RESULT_OK && requestCode == Config.REQUEST_CODE_CAPTURE_IMAGE) {
            Uri pickedImage = arcOcrScanner.getUriImage();
            Picasso.with(getApplicationContext()).load(pickedImage).fit().centerCrop().into(ivImage);
            arcOcrScanner.onImageTaken();
        } else if (resultCode == RESULT_OK && requestCode == 1002) {
            Uri selectedImageGallery = data.getData();
            Picasso.with(getApplicationContext()).load(selectedImageGallery).fit().centerCrop().into(ivImage);
            ImageProcessingThread thread = new ImageProcessingThread(this,
                    getRealPathFromURI(selectedImageGallery), "CarDetector", getParent(), "eng");
            thread.execute();
        }
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
    public void onOcrScanStarted(String filePath) {
        mProgressDialog = new ProgressDialog(CarDetectorActivity.this);
        mProgressDialog.setMessage("Scanning...");
        mProgressDialog.show();
    }

    @Override
    public void onOcrScanFinished(Bitmap bitmap, String recognizedText) {
//        String ocrText = removeAllSpecialChar(recognizedText);
        etOCR.setText(recognizedText.replaceAll("\\W",""));
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

//    private String removeAllSpecialChar(String c) {
//        Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
//        Matcher match = pt.matcher(c);
//        while (match.find()) {
//            String s = match.group();
//            c = c.replaceAll("\\" + s, "");
//        }
//        return c;
//    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 1001);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                            showMessageOKCancel("You need to allow access all the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                                        1001);
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
        new AlertDialog.Builder(CarDetectorActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void showImageDialog() {
        final Dialog dialog = new Dialog(CarDetectorActivity.this);
        dialog.setContentView(R.layout.dialog_image);
        dialog.setTitle("Select Image");
        TextView tvTakePhoto = (TextView) dialog.findViewById(R.id.tvTakePhoto);
        TextView tvGallery = (TextView) dialog.findViewById(R.id.tvGallery);
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arcOcrScanner.takePicture();
                dialog.dismiss();
            }
        });
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1002);
                dialog.dismiss();

            }
        });


        dialog.show();

    }

    @Override
    public void onResponse(VerifyCar verifyCar) {
        dismissProgressDialog();
        if (verifyCar.isStatus()) {
            Toast.makeText(getApplicationContext(), verifyCar.getMessage(), Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(getApplicationContext(), verifyCar.getMessage(), Toast.LENGTH_LONG).show();
            showDetailsActivity();
        }
    }

    @Override
    public void onNetworkException(Throwable e) {
        super.onNetworkException(e);
    }

    private void showDetailsActivity() {

        new AlertDialog.Builder(CarDetectorActivity.this)
                .setTitle("Information")
                .setMessage("Do you want to save?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent detailsActivity = new Intent(CarDetectorActivity.this, DetailsActivity.class);
                        detailsActivity.putExtra("data", etOCR.getText().toString());
                        startActivity(detailsActivity);
                    }
                })
                .setCancelable(false)
                .show();
    }
}

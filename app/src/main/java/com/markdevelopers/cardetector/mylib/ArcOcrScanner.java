package com.markdevelopers.cardetector.mylib;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.Calendar;

public class ArcOcrScanner {

    protected Activity mActivity;
    private String directoryPathOriginal;
    private String filePathOriginal;
    private int requestCode;
    private ArcOcrScannerListener mOcrScannerListener;
    private String trainedDataCode;
    private Uri ofImage;

    public ArcOcrScanner(Activity activity, String directoryPath, int requestCode, String trainedDataCode) {
        this.mActivity = activity;
        this.directoryPathOriginal = directoryPath;
        this.requestCode = requestCode;
        this.trainedDataCode = trainedDataCode;
    }

    public void takePicture() {
        Intent e = new Intent("android.media.action.IMAGE_CAPTURE");
        this.filePathOriginal = FileUtils.getDirectory(this.directoryPathOriginal) + File.separator + Calendar.getInstance().getTimeInMillis() + ".jpg";
        e.putExtra("output", Uri.fromFile(new File(this.filePathOriginal)));
        setUriImage(Uri.fromFile(new File(this.filePathOriginal)));
        startActivity(e);
    }

    public void onImageTaken() {
        Log.d(Config.TAG, "onImageTaken with path " + this.filePathOriginal);
        ImageProcessingThread thread = new ImageProcessingThread(this.mOcrScannerListener,
                this.filePathOriginal, this.directoryPathOriginal, this.mActivity, this.trainedDataCode);
        thread.execute();
    }

    private void setUriImage(Uri ofImage) {
        this.ofImage = ofImage;
    }

    public Uri getUriImage() {
        return ofImage;
    }

    private void startActivity(Intent intent) {
        if (this.mActivity != null) {
            this.mActivity.startActivityForResult(intent, this.requestCode);
        }
    }

    public void setOcrScannerListener(ArcOcrScannerListener mOcrScannerListener) {
        this.mOcrScannerListener = mOcrScannerListener;
    }


}

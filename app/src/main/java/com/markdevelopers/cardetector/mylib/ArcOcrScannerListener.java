package com.markdevelopers.cardetector.mylib;

import android.graphics.Bitmap;

public interface ArcOcrScannerListener {

    public void onOcrScanStarted(String filePath);

    public void onOcrScanFinished(Bitmap bitmap, String recognizedText);
}

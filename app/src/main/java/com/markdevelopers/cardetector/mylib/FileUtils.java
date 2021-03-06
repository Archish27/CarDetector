package com.markdevelopers.cardetector.mylib;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FileUtils {

    public static String getDirectory(String folderName) {
        File directory = null;
        directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return directory.getAbsolutePath();
    }

    public static String getTessdataDirectory(String directoryPath) {
        File tessdataDirectory = new File(directoryPath + "/tessdata");
        if (tessdataDirectory.mkdirs()) {
            Log.d(Config.TAG, "tessdata directory created");
        }
        return tessdataDirectory.getAbsolutePath();
    }
}

package com.mdove.easycopy.utils;

import java.io.File;

public class FileUtils {

    public static File mkImageFile(String path) {
        File imageFile = new File(path);
        File dir = imageFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdir();
        }
        return imageFile;
    }
}

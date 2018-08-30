package com.gerenvip.filescaner.media;

import android.webkit.MimeTypeMap;

/**
 * @author wangwei on 2017/7/6.
 *         wangwei@jiandaola.com
 */
public class MediaFile {

    static abstract class Singleton<T> {
        private volatile T mInstance;

        protected abstract T create();

        public final T get() {
            if (mInstance == null) {
                synchronized (this) {
                    if (mInstance == null) {
                        mInstance = create();
                    }

                }
            }
            return mInstance;
        }

        public final void release() {
            mInstance = null;
        }
    }

    private final MimeTypeMap mMimeTypeMapInstance;

    private MediaFile() {
        mMimeTypeMapInstance = MimeTypeMap.getSingleton();
    }

    private static Singleton<MediaFile> gInstance = new Singleton<MediaFile>() {
        @Override
        protected MediaFile create() {
            return new MediaFile();
        }
    };

    public static MediaFile getSingleton() {
        return gInstance.get();
    }

    public boolean hasMimeType(String mimeType) {
        boolean has = mMimeTypeMapInstance.hasMimeType(mimeType);
        if (!has) {
            return CopySysMediaFile.isMimeTypeMedia(mimeType);
        }
        return has;
    }

    public String getFileExtensionFromUrl(String url) {
        return mMimeTypeMapInstance.getFileExtensionFromUrl(url);
    }

    public String getMimeTypeFromExtension(String extension) {
        return mMimeTypeMapInstance.getMimeTypeFromExtension(extension);
    }

    public String getMimeTypeForPath(String path) {
        return CopySysMediaFile.getMimeTypeForFile(path);
    }

    /**
     * Return true if the given extension has a registered MIME type.
     *
     * @param extension A file extension without the leading '.'
     * @return True iff there is an extension entry in the map.
     */
    public boolean hasExtension(String extension) {
        return mMimeTypeMapInstance.hasExtension(extension);
    }

    public String getExtensionFromMimeType(String mimeType) {
        return mMimeTypeMapInstance.getExtensionFromMimeType(mimeType);
    }

    /**
     * 通过 文件路径判断是否是音频文件
     *
     * @param path
     * @return
     */
    public boolean isAudioFileTypeByPath(String path) {
        CopySysMediaFile.MediaFileType fileType = CopySysMediaFile.getMediaFileTypeForPath(path);
        if (fileType == null) {
            return false;
        }
        return CopySysMediaFile.isAudioFileType(fileType.fileType);
    }

    /**
     * 通过mimeType 判断文件是否是音频文件
     *
     * @param mimeType 例如 audio/mpeg 对应 MP3 文件
     * @return
     */
    public boolean isAudioFileTypeByMimeType(String mimeType) {
        int fileType = CopySysMediaFile.getFileTypeForMimeType(mimeType);
        return CopySysMediaFile.isAudioFileType(fileType);
    }

    /**
     * 通过extension 判断文件是否是音频文件
     *
     * @param extension
     * @return
     */
    public boolean isAudioFileTypeByExtension(String extension) {
        CopySysMediaFile.MediaFileType fileType = CopySysMediaFile.getMediaFileTypeForExtension(extension);
        if (fileType == null) {
            return false;
        }
        return CopySysMediaFile.isAudioFileType(fileType.fileType);
    }

    /**
     * 通过 文件路径 判断文件是否是视频文件
     *
     * @param path
     * @return
     */
    public boolean isVideoFileTypeByPath(String path) {
        CopySysMediaFile.MediaFileType fileType = CopySysMediaFile.getMediaFileTypeForPath(path);
        if (fileType == null) {
            return false;
        }
        return CopySysMediaFile.isVideoFileType(fileType.fileType);
    }

    /**
     * 通过 mimeType 判断文件是否是视频文件
     *
     * @param mimeType 例如 video/mp4 对应 MP4 文件
     * @return
     */
    public boolean isVideoFileTypeByMimeType(String mimeType) {
        int fileType = CopySysMediaFile.getFileTypeForMimeType(mimeType);
        return CopySysMediaFile.isVideoFileType(fileType);
    }

    /**
     * 通过extension 判断文件是否是视频文件
     *
     * @param extension
     * @return
     */
    public boolean isVideoFileTypeByExtension(String extension) {
        CopySysMediaFile.MediaFileType fileType = CopySysMediaFile.getMediaFileTypeForExtension(extension);
        if (fileType == null) {
            return false;
        }
        return CopySysMediaFile.isVideoFileType(fileType.fileType);
    }

    /**
     * 通过 文件路径 判断文件是否是图片文件
     *
     * @param path
     * @return
     */
    public boolean isImageFileTypeByPath(String path) {
        CopySysMediaFile.MediaFileType fileType = CopySysMediaFile.getMediaFileTypeForPath(path);
        if (fileType == null) {
            return false;
        }
        return CopySysMediaFile.isImageFileType(fileType.fileType);
    }

    /**
     * 通过 mimeType 判断文件是否是图片文件
     *
     * @param mimeType 例如 image/png 对应 PNG 文件
     * @return
     */
    public boolean isImageFileTypeByMimeType(String mimeType) {
        int fileType = CopySysMediaFile.getFileTypeForMimeType(mimeType);
        return CopySysMediaFile.isImageFileType(fileType);
    }

    /**
     * 通过 extension 判断文件是否是图片文件
     *
     * @param extension png or .png 也兼容文件路径
     * @return
     */
    public boolean isImageFileTypeByExtension(String extension) {
        CopySysMediaFile.MediaFileType fileType = CopySysMediaFile.getMediaFileTypeForExtension(extension);
        if (fileType == null) {
            return false;
        }
        return CopySysMediaFile.isImageFileType(fileType.fileType);
    }

    public boolean isMimeTypeMedia(String mimeType) {
        return CopySysMediaFile.isMimeTypeMedia(mimeType);
    }
}

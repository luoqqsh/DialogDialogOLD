package com.example.ffmpeg_opencv;

public class testUtil {
    static {
        System.loadLibrary("native-lib");
    }
    public static native String stringFromJNI();
}

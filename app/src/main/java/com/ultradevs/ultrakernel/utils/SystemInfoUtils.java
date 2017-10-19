package com.ultradevs.ultrakernel.utils;

import android.os.Build;

import java.io.IOException;

/**
 * Created by ahmedhady on 17/10/17.
 */

public class SystemInfoUtils {
    
    public static String Android_Version() {
        return Build.VERSION.RELEASE;
    }
    public static String Android_Name() {
        if (Android_Version().startsWith("4.4")){
            return "KitKat";
        }
        if (Android_Version().startsWith("5")){
            return "Lollipop";
        }
        if (Android_Version().startsWith("6")){
            return "Marshmallow";
        }
        if (Android_Version().startsWith("7")){
            return "Nougat";
        }
        if (Android_Version().startsWith("8")){
            return "Oreo";
        }
        return null;
    }
    public static String Android_Sdk_Version(){
        return Build.VERSION.SDK;
    }
    public static String Android_system_patch_Version(){
        return Build.VERSION.SECURITY_PATCH;
    }
    public static String Android_device_board(){
        return Build.BOARD;
    }
    public static String Android_device_manuf(){
        return Build.MANUFACTURER;
    }

    public static String Android_device_name(){
        return Build.DEVICE;
    }

    public static String Android_device_kernel(){
        return System.getProperty("os.version");
    }
}

package com.transsion.sdk.demo;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class PreferenceUtils {

    public static String readString(Context context,String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    public static void writeString(Context context,String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).apply();
    }

    public static boolean readBoolean(Context context,String key) {
        return getSharedPreferences(context).getBoolean(key, false);
    }
    public static boolean readBoolean(Context context,String key,boolean defValue) {
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    public static void writeBoolean(Context context,String key, boolean value) {
        getSharedPreferences(context).edit().putBoolean(key, value).apply();
    }

    public static int readInt(Context context,String key) {
        return getSharedPreferences(context).getInt(key, 0);
    }

    public static int readInt(Context context,String key, int value) {
        return getSharedPreferences(context).getInt(key, value);
    }

    public static void writeInt(Context context,String key, int value) {
        getSharedPreferences(context).edit().putInt(key, value).apply();
    }

    public static long readLong(Context context,String key) {
        return getSharedPreferences(context).getLong(key, 0);
    }

    public static void writeLong(Context context,String key, long value) {
        getSharedPreferences(context).edit().putLong(key, value).apply();
    }

    public static void remove(Context context,String key) {
        getSharedPreferences(context).edit().remove(key).apply();
    }

    public static void removeAll(Context context) {
        getSharedPreferences(context).edit().clear().apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("TAnalytics", Context.MODE_PRIVATE);
    }

    private static SharedPreferences getSharedPreferences(Context context,String name){
        return context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    public static String readString(Context context,String key,String name) {
        return getSharedPreferences(context,name).getString(key, "");
    }

    public static void writeString(Context context,String key, String value,String name) {
        getSharedPreferences(context,name).edit().putString(key, value).apply();
    }

    public static long readLong(Context context,String key,String name) {
        return getSharedPreferences(context,name).getLong(key, 0);
    }

    public static void writeLong(Context context,String key, long value,String name) {
        getSharedPreferences(context,name).edit().putLong(key, value).apply();
    }

    public static boolean readBoolean(Context context,String key,boolean defValue,String name) {
        return getSharedPreferences(context,name).getBoolean(key, defValue);
    }

    public static void writeBoolean(Context context,String key, boolean value,String name) {
        getSharedPreferences(context,name).edit().putBoolean(key, value).apply();
    }

    public static int readInt(Context context,String key,String name) {
        return getSharedPreferences(context,name).getInt(key, 0);
    }

    public static int readInt(Context context,String key, int value,String name) {
        return getSharedPreferences(context,name).getInt(key, value);
    }

    public static void writeInt(Context context,String key, int value,String name) {
        getSharedPreferences(context,name).edit().putInt(key, value).apply();
    }

    public static void claerLog(Context context,String name){
        getSharedPreferences(context,name).edit().clear().apply();
    }

    public static Map<String,?> getAll(Context context,String name){
        return getSharedPreferences(context,name).getAll();
    }

}

package com.transsion.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * usage
 *
 * @author 孙鹏
 * @data 2017/6/13
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class SharedPreferencesUtil {

    private static final String TAG = "SharedPreferencesUtil";

    private static Map<String, SharedPreferencesUtil> sharePreferBank = new HashMap<String, SharedPreferencesUtil>();
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private SharedPreferencesCompat.EditorCompat mEditorCompat = SharedPreferencesCompat.EditorCompat.getInstance();

    /**
     * default
     */
    private static final int DEFAULT_INT = 0;
    private static final float DEFAULT_FLOAT = 0.0f;
    private static final String DEFAULT_STRING = "";
    private static final boolean DEFAULT_BOOLEAN = false;
    private static final long DEFAULT_LONG = 0L;
    private static final Set<String> DEFAULT_STRING_SET = new HashSet<>(0);
    private static final Object DEFAULT_OBJECT = null;

    /**
     * 上下文需在application的onCreate()方法绑定
     */
    private static Context mContext = null;


    /**
     * 上下文需在application的onCreate()方法绑定
     */
    public static void bindApplication(@NonNull Context context) {
        mContext = context;
    }


    /**
     * 构造方法
     *
     * @param spName
     * @throws Exception
     */
    private SharedPreferencesUtil(String spName) {
        if (mContext != null) {
            mSharedPreferences = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
            mEditor = mSharedPreferences.edit();
        }
    }

    /**
     * 带操作文件名字的享元
     */
    public static synchronized SharedPreferencesUtil getInstance(String spName) {
        if (TextUtils.isEmpty(spName)) {
            throw new RuntimeException("please make sure you have valid file name");
        }
        SharedPreferencesUtil spUtil = sharePreferBank.get(spName);
        if (spUtil == null) {
            spUtil = new SharedPreferencesUtil(spName);
            sharePreferBank.put(spName, spUtil);
        }
        return spUtil;
    }


    /**
     * 存放字符串
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            mEditor.putString(key, value);
            mEditorCompat.apply(mEditor);
        }
    }

    /**
     * 获得字符串
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        String value = DEFAULT_STRING;
        if (!TextUtils.isEmpty(key)) {
            value = this.getString(key, value);
        }
        return value;
    }

    /**
     * 获得字符串
     *
     * @param key
     * @return
     */
    public String getString(String key, String defaultValue) {
        String value = defaultValue;
        if (!TextUtils.isEmpty(key)) {
            value = mSharedPreferences.getString(key, value);
        }
        return value;
    }

    /**
     * 存放Internet值
     *
     * @param key
     * @param value
     */
    public void putInt(String key, int value) {
        if (!TextUtils.isEmpty(key)) {
            mEditor.putInt(key, value);
            mEditorCompat.apply(mEditor);
        }
    }

    /**
     * 获取Internet值
     *
     * @param key
     */
    public int getInt(String key) {
        int value = DEFAULT_INT;
        if (!TextUtils.isEmpty(key)) {
            value = this.getInt(key, value);
        }
        return value;
    }

    /**
     * 获取Internet值
     *
     * @param key
     */
    public int getInt(String key, int defaultValue) {
        int value = defaultValue;
        if (!TextUtils.isEmpty(key)) {
            value = mSharedPreferences.getInt(key, value);
        }
        return value;
    }

    public void putBoolean(String key, boolean value) {
        if (!TextUtils.isEmpty(key)) {
            mEditor.putBoolean(key, value);
            mEditorCompat.apply(mEditor);
        }
    }

    /**
     * 获取Blooen值
     *
     * @param key
     */
    public boolean getBoolean(String key) {
        boolean result = DEFAULT_BOOLEAN;
        if (!TextUtils.isEmpty(key)) {
            result = mSharedPreferences.getBoolean(key, result);
        }
        return result;
    }

    /**
     * 获取Blooen值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        boolean result = defaultValue;
        if (!TextUtils.isEmpty(key)) {
            result = mSharedPreferences.getBoolean(key, result);
        }
        return result;
    }

    /**
     * 存放Long值
     *
     * @param key
     * @param value
     */
    public void putLong(String key, long value) {
        if (!TextUtils.isEmpty(key)) {
            mEditor.putLong(key, value);
            mEditorCompat.apply(mEditor);
        }
    }

    /**
     * 获取Long值
     *
     * @param key
     * @return
     */
    public long getLong(String key) {
        long value = DEFAULT_LONG;
        if (!TextUtils.isEmpty(key)) {
            value = mSharedPreferences.getLong(key, value);
        }
        return value;
    }

    /**
     * 获取Long值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLong(String key, long defaultValue) {
        long value = defaultValue;
        if (!TextUtils.isEmpty(key)) {
            value = mSharedPreferences.getLong(key, value);
        }
        return value;
    }

    /**
     * 存放float值
     *
     * @param key
     * @param value
     */
    public void putFloat(String key, float value) {
        if (!TextUtils.isEmpty(key)) {
            mEditor.putFloat(key, value);
            mEditorCompat.apply(mEditor);
        }
    }

    /**
     * 获取float值
     *
     * @param key
     * @return
     */
    public float getFloat(String key) {
        float value = DEFAULT_FLOAT;
        if (!TextUtils.isEmpty(key)) {
            value = this.getFloat(key, value);
        }
        return value;
    }

    /**
     * 获取float值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public float getFloat(String key, float defaultValue) {
        float value = defaultValue;
        if (!TextUtils.isEmpty(key)) {
            value = mSharedPreferences.getFloat(key, value);
        }
        return value;
    }

    /**
     * 存放Set数组
     */
    public void putStringSet(String key, Set<String> value) {
        if (!TextUtils.isEmpty(key)) {
            mEditor.putStringSet(key, value);
            mEditorCompat.apply(mEditor);
        }
    }

    /**
     * 获取set数组
     *
     * @param key
     * @return
     */
    public Set<String> getStringSet(String key) {
        Set<String> set = DEFAULT_STRING_SET;
        if (!TextUtils.isEmpty(key)) {
            set = mSharedPreferences.getStringSet(key, null);
        }
        return set;
    }

    /**
     * 获取set数组
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        Set<String> set = defaultValue;
        if (!TextUtils.isEmpty(key)) {
            set = mSharedPreferences.getStringSet(key, set);
        }
        return set;
    }


    /**
     * 存储可序列化对象
     *
     * @param key
     * @param object
     */
    public void putObject(String key, Serializable object) {
        if (object != null && !TextUtils.isEmpty(key)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(bos);
                oos.writeObject(object);
                String bytesToBaseString = new String(Base64.encode(bos.toByteArray(), 0), "UTF-8");
                mEditor.putString(key, bytesToBaseString);
                mEditorCompat.apply(mEditor);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                    if (oos != null) {
                        oos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获得序列化对象
     *
     * @param key
     * @return
     */
    public Object getObject(String key) {
        Object value = DEFAULT_OBJECT;
        if (!TextUtils.isEmpty(key)) {
            return this.getObject(key, value);
        }
        return value;
    }

    /**
     * 获得序列化对象
     *
     * @param key
     * @return
     */
    public Object getObject(String key, Object defaultValue) {
        Object value = defaultValue;
        if (!TextUtils.isEmpty(key)) {
            String bytesToBaseString = this.getString(key, "");
            ByteArrayInputStream bais = null;
            ObjectInputStream localObjectInputStream = null;
            try {
                bais = new ByteArrayInputStream((Base64.decode(bytesToBaseString.getBytes("UTF-8"), 0)));
                localObjectInputStream = new ObjectInputStream(bais);
                value = localObjectInputStream.readObject();
                localObjectInputStream.close();
                bais.close();
                if (value != null) {
                    return value;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (localObjectInputStream != null) {
                        localObjectInputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }


    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public void remove(String key) {
        mEditor.remove(key);
        mEditorCompat.apply(mEditor);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        mEditor.clear();
        mEditorCompat.apply(mEditor);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

}
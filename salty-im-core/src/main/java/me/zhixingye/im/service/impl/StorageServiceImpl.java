package me.zhixingye.im.service.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.protobuf.MessageLite;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.im.util.AESUtil;
import me.zhixingye.im.util.Base64Util;
import me.zhixingye.im.util.MD5Util;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */

@SuppressLint("ApplySharedPref")
public class StorageServiceImpl extends BasicServiceImpl implements StorageService {

    private static final String TAG = "StorageServiceImpl";

    private static final String AES_KET_ALIAS = "AES_Salty";

    private static final byte[] IV = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};

    private Key mAESKey;

    private String mUserStorageName;

    public StorageServiceImpl() {
        mAESKey = AESUtil.generateAESKeyInAndroidKeyStore(AES_KET_ALIAS, 192);
        if (mAESKey == null) {
            Logger.w(TAG, "generateAESKeyInAndroidKeyStore fail");
            mAESKey = AESUtil.generateAESKey(192);
        }
    }

    @Override
    public void putStringToStorage(String name, String key, String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        putByteArrayToStorage(name, key, value.getBytes());
    }

    @Override
    public void putBooleanToStorage(String name, String key, boolean value) {
        putStringToStorage(name, key, String.valueOf(value));
    }

    @Override
    public void putFloatToStorage(String name, String key, float value) {
        putStringToStorage(name, key, String.valueOf(value));
    }

    @Override
    public void putLongToStorage(String name, String key, long value) {
        putStringToStorage(name, key, String.valueOf(value));
    }

    @Override
    public void putByteArrayToStorage(String name, String key, byte[] value) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(key) || value == null) {
            return;
        }
        SharedPreferences sp = getSharedPreferences(name);
        if (sp == null) {
            return;
        }
        String encryptedKey = getEncryptedKey(key);
        String encryptedValue;
        if (mAESKey == null) {
            encryptedValue = Base64Util.encodeToString(value);
        } else {
            byte[] data = AESUtil.encrypt(value, mAESKey, IV);
            if (data == null || data.length <= 0) {
                Logger.e(TAG, "Encrypt fail,key=" + key);
                return;
            }
            encryptedValue = Base64Util.encodeToString(data);
        }
        sp.edit().putString(encryptedKey, encryptedValue).apply();
    }


    @Override
    public boolean getBooleanFromStorage(String name, String key, boolean defValue) {
        String strValue = getStringFromStorage(name, key, String.valueOf(defValue));
        try {
            return Boolean.getBoolean(strValue);
        } catch (Exception e) {
            Logger.i(TAG, "getBooleanFromStorage error key: " + key, e);
            return defValue;
        }
    }

    @Override
    public float getFloatFromStorage(String name, String key, float defValue) {
        String strValue = getStringFromStorage(name, key, String.valueOf(defValue));
        try {
            return Float.parseFloat(strValue);
        } catch (Exception e) {
            Logger.i(TAG, "getFloatFromStorage error key: " + key, e);
            return defValue;
        }
    }

    @Override
    public long getLongFromStorage(String name, String key, long defValue) {
        String strValue = getStringFromStorage(name, key, String.valueOf(defValue));
        try {
            return Long.parseLong(strValue);
        } catch (Exception e) {
            Logger.i(TAG, "getLongFromStorage error key: " + key, e);
            return defValue;
        }
    }

    @Override
    public String getStringFromStorage(String name, String key, String defValue) {
        byte[] value = getByteArrayFromStorage(name, key);
        if (value == null) {
            return defValue;
        }
        return new String(value);
    }

    @Override
    public byte[] getByteArrayFromStorage(String name, String key) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(key)) {
            return null;
        }
        SharedPreferences sp = getSharedPreferences(name);
        if (sp == null) {
            return null;
        }
        String encryptedKey = getEncryptedKey(key);
        if (!sp.contains(encryptedKey)) {
            return null;
        }
        String encryptedValue = sp.getString(encryptedKey, null);
        if (encryptedValue == null || mAESKey == null) {
            return null;
        }
        byte[] data = AESUtil.decrypt(Base64Util.decode(encryptedValue), mAESKey, IV);
        if (data == null || data.length <= 0) {
            Logger.e(TAG, "decrypt fail,key=" + key);
            return null;
        }
        return data;
    }


    @Override
    public void removeFromStorage(String name, String key) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(key)) {
            return;
        }
        String encryptedKey = getEncryptedKey(key);
        SharedPreferences sp = getSharedPreferences(name);
        if (sp == null || !sp.contains(encryptedKey)) {
            return;
        }
        sp.edit().remove(encryptedKey).apply();
    }

    @Override
    public void putStringToUserStorage(String key, String value) {
        putStringToStorage(mUserStorageName, key, value);
    }

    @Override
    public void putBooleanToUserStorage(String key, boolean value) {
        putBooleanToStorage(mUserStorageName, key, value);
    }

    @Override
    public void putFloatToUserStorage(String key, float value) {
        putFloatToStorage(mUserStorageName, key, value);
    }

    @Override
    public void putLongToUserStorage(String key, long value) {
        putLongToStorage(mUserStorageName, key, value);
    }

    @Override
    public void putByteArrayToUserStorage(String key, byte[] value) {
        putByteArrayToStorage(mUserStorageName, key, value);
    }

    @Override
    public String getStringFromUserStorage(String key, String defValue) {
        return getStringFromStorage(mUserStorageName, key, defValue);
    }

    @Override
    public boolean getBooleanFromUserStorage(String key, boolean defValue) {
        return getBooleanFromStorage(mUserStorageName, key, defValue);
    }

    @Override
    public float getFloatFromUserStorage(String key, float defValue) {
        return getFloatFromStorage(mUserStorageName, key, defValue);
    }

    @Override
    public long getLongFromUserStorage(String key, long defValue) {
        return getLongFromStorage(mUserStorageName, key, defValue);
    }

    @Override
    public byte[] getByteArrayFromUserStorage(String key) {
        return getByteArrayFromStorage(mUserStorageName, key);
    }

    @Override
    public void removeFromUserStorage(String key) {
        removeFromStorage(mUserStorageName, key);
    }

    private void setUserStoreName(@Nullable String userStoreName) {
        mUserStorageName = userStoreName;
    }

    private SharedPreferences getSharedPreferences(@Nullable String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        return IMCore.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    private String getEncryptedKey(@NonNull String key) {
        return MD5Util.encrypt16(key);
    }

}

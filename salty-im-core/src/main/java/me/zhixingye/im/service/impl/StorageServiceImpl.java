package me.zhixingye.im.service.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

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
        String encryptedKey = getEncryptedKey(key);
        if (TextUtils.isEmpty(encryptedKey)) {
            return;
        }
        SharedPreferences sp = getSharedPreferences(name);
        if (sp == null) {
            return;
        }
        if (TextUtils.isEmpty(encryptedKey) || mAESKey == null) {
            sp.edit().putString(encryptedKey, value).apply();
            return;
        }
        byte[] data = AESUtil.encrypt(value.getBytes(StandardCharsets.UTF_8), mAESKey, IV);
        if (data == null || data.length <= 0) {
            Logger.e(TAG, "Encrypt fail,key=" + key);
            return;
        }
        String saveValue = Base64Util.encodeToString(data);
        sp.edit().putString(encryptedKey, saveValue).apply();
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
        if (value == null) {
            return;
        }
        putByteArrayToStorage(name, key, Base64Util.encode(value));
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
    public byte[] getByteArrayFromStorage(String name, String key) {
        String strValue = getStringFromStorage(name, key, null);
        if (strValue == null) {
            return null;
        }
        return Base64Util.decode(key);
    }

    @Override
    public String getStringFromStorage(String name, String key, String defValue) {
        String encryptedKey = getEncryptedKey(key);
        if (TextUtils.isEmpty(encryptedKey)) {
            return defValue;
        }
        SharedPreferences sp = getSharedPreferences(name);
        if (sp == null) {
            return defValue;
        }
        String encryptedValue = sp.getString(encryptedKey, defValue);
        if (TextUtils.isEmpty(encryptedValue) || TextUtils.equals(encryptedValue, defValue) || mAESKey == null) {
            return encryptedValue;
        }
        byte[] data = AESUtil.decrypt(Base64Util.decode(encryptedValue), mAESKey, IV);
        if (data == null || data.length <= 0) {
            Logger.e(TAG, "decrypt fail,key=" + key);
            return defValue;
        }
        return new String(data, StandardCharsets.UTF_8);
    }


    @Override
    public void removeFromStorage(String name, String key) {
        String encryptedKey = getEncryptedKey(key);
        if (TextUtils.isEmpty(encryptedKey)) {
            return;
        }
        SharedPreferences sp = getSharedPreferences(name);
        if (sp == null) {
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

    private String getEncryptedKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return key;
        }
        return MD5Util.encrypt16(key);
    }

}

package me.zhixingye.im.service;

import androidx.annotation.Nullable;

import com.google.protobuf.MessageLite;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public interface StorageService extends BasicService {
    void putStringToStorage(String name, String key, String value);

    void putBooleanToStorage(String name, String key, boolean value);

    void putFloatToStorage(String name, String key, float value);

    void putLongToStorage(String name, String key, long value);

    void putByteArrayToStorage(String name, String key, byte[] value);

    String getStringFromStorage(String name, String key, String defValue);

    boolean getBooleanFromStorage(String name, String key, boolean defValue);

    float getFloatFromStorage(String name, String key, float defValue);

    long getLongFromStorage(String name, String key, long defValue);

    @Nullable
    byte[] getByteArrayFromStorage(String name, String key);

    void removeFromStorage(String name, String key);

    void putStringToUserStorage(String key, String value);

    void putBooleanToUserStorage(String key, boolean value);

    void putFloatToUserStorage(String key, float value);

    void putLongToUserStorage(String key, long value);

    void putByteArrayToUserStorage(String key, byte[] value);

    String getStringFromUserStorage(String key, String defValue);

    boolean getBooleanFromUserStorage(String key, boolean defValue);

    float getFloatFromUserStorage(String key, float defValue);

    long getLongFromUserStorage(String key, long defValue);

    @Nullable
    byte[] getByteArrayFromUserStorage(String key);

    void removeFromUserStorage(String key);
}

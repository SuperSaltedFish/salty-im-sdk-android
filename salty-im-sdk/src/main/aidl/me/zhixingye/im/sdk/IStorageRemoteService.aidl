// IStorageRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteCallback;

interface IStorageRemoteService {
    void putBooleanToStorage(String name, String key, boolean value);

    void putFloatToStorage(String name, String key, float value);

    void putLongToStorage(String name, String key, long value);

    void putByteArrayToStorage(String name, String key, out byte[] value);

    void putStringToStorage(String name, String key, String value);

    String getStringFromStorage(String name, String key, String defValue);

    boolean getBooleanFromStorage(String name, String key, boolean defValue);

    float getFloatFromStorage(String name, String key, float defValue);

    long getLongFromStorage(String name, String key, long defValue);

    byte[] getByteArrayFromStorage(String name, String key);

    void removeFromStorage(String name, String key);

    void putStringToUserStorage(String key, String value);

    void putBooleanToUserStorage(String key, boolean value);

    void putFloatToUserStorage(String key, float value);

    void putLongToUserStorage(String key, long value);

    void putByteArrayToUserStorage(String key, out byte[] value);

    String getStringFromUserStorage(String key, String defValue);

    boolean getBooleanFromUserStorage(String key, boolean defValue);

    float getFloatFromUserStorage(String key, float defValue);

    long getLongFromUserStorage(String key, long defValue);

    byte[] getByteArrayFromUserStorage(String key);

    void removeFromUserStorage(String key);
}

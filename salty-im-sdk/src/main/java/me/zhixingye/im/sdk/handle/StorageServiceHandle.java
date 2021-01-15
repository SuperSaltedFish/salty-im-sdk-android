package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IStorageServiceHandle;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class StorageServiceHandle extends IStorageServiceHandle.Stub {

    @Override
    public void putBooleanToStorage(String name, String key, boolean value) {
        IMCore.get().getStorageService().putBooleanToStorage(name, key, value);
    }

    @Override
    public void putFloatToStorage(String name, String key, float value) {
        IMCore.get().getStorageService().putFloatToStorage(name, key, value);
    }

    @Override
    public void putLongToStorage(String name, String key, long value) {
        IMCore.get().getStorageService().putLongToStorage(name, key, value);
    }

    @Override
    public void putByteArrayToStorage(String name, String key, byte[] value) {
        IMCore.get().getStorageService().putByteArrayToStorage(name, key, value);
    }

    @Override
    public void putStringToStorage(String name, String key, String value) {
        IMCore.get().getStorageService().putStringToStorage(name, key, value);
    }

    @Override
    public String getStringFromStorage(String name, String key, String defValue) {
        return IMCore.get().getStorageService().getStringFromStorage(name, key, defValue);
    }

    @Override
    public boolean getBooleanFromStorage(String name, String key, boolean defValue) {
        return IMCore.get().getStorageService().getBooleanFromStorage(name, key, defValue);
    }

    @Override
    public float getFloatFromStorage(String name, String key, float defValue) {
        return IMCore.get().getStorageService().getFloatFromStorage(name, key, defValue);
    }

    @Override
    public long getLongFromStorage(String name, String key, long defValue) {
        return IMCore.get().getStorageService().getLongFromStorage(name, key, defValue);
    }

    @Override
    public byte[] getByteArrayFromStorage(String name, String key) {
        return IMCore.get().getStorageService().getByteArrayFromStorage(name, key);
    }

    @Override
    public void removeFromStorage(String name, String key) {
        IMCore.get().getStorageService().removeFromStorage(name, key);
    }

    @Override
    public void putStringToUserStorage(String key, String value) {
        IMCore.get().getStorageService().putStringToUserStorage(key, value);
    }

    @Override
    public void putBooleanToUserStorage(String key, boolean value) {
        IMCore.get().getStorageService().putBooleanToUserStorage(key, value);
    }

    @Override
    public void putFloatToUserStorage(String key, float value) {
        IMCore.get().getStorageService().putFloatToUserStorage(key, value);
    }

    @Override
    public void putLongToUserStorage(String key, long value) {
        IMCore.get().getStorageService().putLongToUserStorage(key, value);
    }

    @Override
    public void putByteArrayToUserStorage(String key, byte[] value) {
        IMCore.get().getStorageService().putByteArrayToUserStorage(key, value);
    }

    @Override
    public String getStringFromUserStorage(String key, String defValue) {
        return IMCore.get().getStorageService().getStringFromUserStorage(key, defValue);
    }

    @Override
    public boolean getBooleanFromUserStorage(String key, boolean defValue) {
        return IMCore.get().getStorageService().getBooleanFromUserStorage(key, defValue);
    }

    @Override
    public float getFloatFromUserStorage(String key, float defValue) {
        return IMCore.get().getStorageService().getFloatFromUserStorage(key, defValue);
    }

    @Override
    public long getLongFromUserStorage(String key, long defValue) {
        return IMCore.get().getStorageService().getLongFromUserStorage(key, defValue);
    }

    @Override
    public byte[] getByteArrayFromUserStorage(String key) {
        return IMCore.get().getStorageService().getByteArrayFromUserStorage(key);
    }

    @Override
    public void removeFromUserStorage(String key) {
        IMCore.get().getStorageService().removeFromUserStorage(key);
    }
}

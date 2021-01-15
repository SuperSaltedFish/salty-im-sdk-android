package me.zhixingye.im.sdk.proxy;

import android.annotation.SuppressLint;

import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.IStorageServiceHandle;
import me.zhixingye.im.tool.Logger;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */

@SuppressLint("ApplySharedPref")
public class StorageServiceProxy implements StorageService, RemoteProxy {

    private static final String TAG = "ContactServiceProxy";

    private IStorageServiceHandle mStorageHandle;

    @Override
    public void onBindHandle(IRemoteService service) {
        try {
            mStorageHandle = service.getStorageServiceHandle();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            mStorageHandle = null;
        }
    }


    @Override
    public void putBooleanToStorage(String name, String key, boolean value) {
        try {
            mStorageHandle.putBooleanToStorage(name, key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public void putFloatToStorage(String name, String key, float value) {
        try {
            mStorageHandle.putFloatToStorage(name, key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public void putLongToStorage(String name, String key, long value) {
        try {
            mStorageHandle.putLongToStorage(name, key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public void putByteArrayToStorage(String name, String key, byte[] value) {
        try {
            mStorageHandle.putByteArrayToStorage(name, key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public void putStringToStorage(String name, String key, String value) {
        try {
            mStorageHandle.putStringToStorage(name, key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public String getStringFromStorage(String name, String key, String defValue) {
        try {
            return mStorageHandle.getStringFromStorage(name, key, defValue);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return defValue;
        }
    }

    @Override
    public boolean getBooleanFromStorage(String name, String key, boolean defValue) {
        try {
            return mStorageHandle.getBooleanFromStorage(name, key, defValue);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return defValue;
        }
    }

    @Override
    public float getFloatFromStorage(String name, String key, float defValue) {
        try {
            return mStorageHandle.getFloatFromStorage(name, key, defValue);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return defValue;
        }
    }

    @Override
    public long getLongFromStorage(String name, String key, long defValue) {
        try {
            return mStorageHandle.getLongFromStorage(name, key, defValue);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return defValue;
        }
    }

    @Override
    public byte[] getByteArrayFromStorage(String name, String key) {
        try {
            return mStorageHandle.getByteArrayFromStorage(name, key);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return null;
        }
    }

    @Override
    public void removeFromStorage(String name, String key) {
        try {
            mStorageHandle.removeFromStorage(name, key);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public void putStringToUserStorage(String key, String value) {
        try {
            mStorageHandle.putStringToUserStorage(key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public void putBooleanToUserStorage(String key, boolean value) {
        try {
            mStorageHandle.putBooleanToUserStorage(key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public void putFloatToUserStorage(String key, float value) {
        try {
            mStorageHandle.putFloatToUserStorage(key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public void putLongToUserStorage(String key, long value) {
        try {
            mStorageHandle.putLongToUserStorage(key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public void putByteArrayToUserStorage(String key, byte[] value) {
        try {
            mStorageHandle.putByteArrayToUserStorage(key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public String getStringFromUserStorage(String key, String defValue) {
        try {
            return mStorageHandle.getStringFromUserStorage(key, defValue);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return defValue;
        }
    }

    @Override
    public boolean getBooleanFromUserStorage(String key, boolean defValue) {
        try {
            return mStorageHandle.getBooleanFromUserStorage(key, defValue);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return defValue;
        }
    }

    @Override
    public float getFloatFromUserStorage(String key, float defValue) {
        try {
            return mStorageHandle.getFloatFromUserStorage(key, defValue);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return defValue;
        }
    }

    @Override
    public long getLongFromUserStorage(String key, long defValue) {
        try {
            return mStorageHandle.getLongFromUserStorage(key, defValue);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return defValue;
        }
    }

    @Override
    public byte[] getByteArrayFromUserStorage(String key) {
        try {
            return mStorageHandle.getByteArrayFromUserStorage(key);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return null;
        }
    }

    @Override
    public void removeFromUserStorage(String key) {

    }
}

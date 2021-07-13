package me.zhixingye.im.service.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.UUID;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.service.DeviceService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class DeviceServiceImpl extends BasicServiceImpl implements DeviceService {

    private static final String TAG = "DeviceServiceImpl";

    private static final String STORAGE_NAME = "DeviceServiceImpl";
    private static final String STORAGE_KEY_DEVICE_ID = "DeviceID";

    private volatile String mDeviceId;

    @Override
    public String getAppVersion() {
        Context context = IMCore.getAppContext();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    @Override
    public String getDeviceId() {
        if (TextUtils.isEmpty(mDeviceId)) {
            synchronized (this) {
                if (TextUtils.isEmpty(mDeviceId)) {
                    mDeviceId = IMCore.get().getStorageService().getStringFromStorage(STORAGE_NAME, STORAGE_KEY_DEVICE_ID, "");
                }
                if (TextUtils.isEmpty(mDeviceId)) {
                    mDeviceId = UUID.randomUUID().toString();
                    IMCore.get().getStorageService().putStringToStorage(STORAGE_NAME, STORAGE_KEY_DEVICE_ID, mDeviceId);
                }
            }
        }
        return mDeviceId;
    }
}

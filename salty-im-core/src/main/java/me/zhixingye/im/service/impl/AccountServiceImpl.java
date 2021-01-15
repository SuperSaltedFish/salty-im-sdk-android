package me.zhixingye.im.service.impl;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.UserProfile;

import java.util.concurrent.Semaphore;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.api.UserApi;
import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.database.SQLiteServiceManager;
import me.zhixingye.im.database.UserDao;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.AccountService;
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.tool.CallbackHelper;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.im.util.MD5Util;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class AccountServiceImpl extends BasicServiceImpl implements AccountService {

    private static final String TAG = "AccountServiceImpl";

    private static final int DATABASE_VERSION = 1;

    private static final String STORAGE_KEY_LOGIN_INFO = TAG + ".LoginInfo";

    private final Semaphore mLoginLock;

    private volatile boolean isLogged;

    public AccountServiceImpl() {
        mLoginLock = new Semaphore(1);
    }

    @Override
    public void registerByTelephone(String telephone, String password, RequestCallback<RegisterResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .registerByTelephone(telephone, password, callback);
    }

    @Override
    public void registerByEmail(String email, String password, RequestCallback<RegisterResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .registerByEmail(email, password, callback);
    }

    @Override
    public void resetLoginPasswordByTelephone(String telephone, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .resetLoginPasswordByTelephone(telephone, newPassword, callback);
    }

    @Override
    public void resetLoginPasswordByEmail(String telephone, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .resetLoginPasswordByTelephone(telephone, newPassword, callback);
    }

    @Override
    public void loginByTelephone(String telephone, String password, RequestCallback<UserProfile> callback) {
        login(telephone, null, password, callback);
    }

    @Override
    public void loginByEmail(String email, String password, RequestCallback<UserProfile> callback) {
        login(null, email, password, callback);
    }

    @Override
    public void loginByLastLoginInfo(final RequestCallback<UserProfile> callback) {
        try {
            mLoginLock.acquire();
        } catch (InterruptedException e) {
            return;
        }
        if (isLogged) {
            mLoginLock.release();
            throw new RuntimeException("The user has already logged in, please do not log in again！");
        }

        runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginResp loginResp = getLoginRespFromLocal();
                    if (loginResp == null) {
                        CallbackHelper.callFailure(ResponseCode.INTERNAL_USER_NOT_LOGGED_IN, callback);
                        return;
                    }
                    if (tryInitLoginInfo(loginResp)) {
                        CallbackHelper.callCompleted(loginResp.getProfile(), callback);
                    } else {
                        CallbackHelper.callFailure(ResponseCode.INTERNAL_UNKNOWN, callback);
                    }
                } finally {
                    mLoginLock.release();
                }

            }
        });
    }

    private void login(String telephone, String email, String password, final RequestCallback<UserProfile> callback) {
        try {
            mLoginLock.acquire();
        } catch (InterruptedException e) {
            return;
        }
        if (isLogged) {
            mLoginLock.release();
            throw new RuntimeException("The user has already logged in, please do not log in again！");
        }
        RequestCallback<LoginResp> callbackWrapper = new RequestCallback<LoginResp>() {
            @Override
            public void onCompleted(LoginResp loginResp) {
                try {
                    if (tryInitLoginInfo(loginResp)) {
                        CallbackHelper.callCompleted(loginResp.getProfile(), callback);
                    } else {
                        CallbackHelper.callFailure(ResponseCode.INTERNAL_UNKNOWN, callback);
                    }
                } finally {
                    mLoginLock.release();
                }
            }

            @Override
            public void onFailure(int code, String error) {
                mLoginLock.release();
                CallbackHelper.callFailure(code, error, callback);
            }

        };

        if (!TextUtils.isEmpty(telephone)) {
            ServiceAccessor.get(ApiService.class)
                    .createApi(UserApi.class)
                    .loginByTelephone(telephone, password, callbackWrapper);
        } else {
            ServiceAccessor.get(ApiService.class)
                    .createApi(UserApi.class)
                    .loginByEmail(email, password, callbackWrapper);
        }
    }

    @Override
    public void logout() {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .logout(null);

        saveLoginRespToLocal(null);

        SQLiteServiceManager.get().close();

        isLogged = false;

    }

    @Override
    public boolean isLogged() {
        return isLogged;
    }

    private boolean tryInitLoginInfo(LoginResp loginResp) {
        if (loginResp == null) {
            return false;
        }
        UserProfile profile = loginResp.getProfile();
        String userId = profile.getUserId();
        String token = loginResp.getToken();
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(token)) {
            Logger.e(TAG, "trySaveLoginInfo: TextUtils.isEmpty(userId) || TextUtils.isEmpty(token)");
            return false;
        }

        SQLiteServiceManager.get().open(
                IMCore.getAppContext(),
                MD5Util.encrypt16(userId),
                DATABASE_VERSION
        );


        if (!UserDao.saveUserProfile(profile)) {
            Logger.e(TAG, "trySaveLoginInfo:replace fail");
            SQLiteServiceManager.get().close();
            return false;
        }

        saveLoginRespToLocal(loginResp);

        isLogged = true;


        return true;
    }

    private void saveLoginRespToLocal(@Nullable LoginResp loginResp) {
        if (loginResp == null) {
            ServiceAccessor.get(StorageService.class).removeFromStorage(TAG, STORAGE_KEY_LOGIN_INFO);
        } else {
            ServiceAccessor.get(StorageService.class).putByteArrayToStorage(TAG, STORAGE_KEY_LOGIN_INFO, loginResp.toByteArray());
        }
        ServiceAccessor.get(StorageService.class).getByteArrayFromStorage(TAG, STORAGE_KEY_LOGIN_INFO);
    }

    @Nullable
    private LoginResp getLoginRespFromLocal() {
        byte[] data = ServiceAccessor.get(StorageService.class).getByteArrayFromStorage(TAG, STORAGE_KEY_LOGIN_INFO);
        if (data == null || data.length == 0) {
            return null;
        }
        try {
            return LoginResp.parseFrom(data);
        } catch (Exception e) {
            Logger.i(TAG, "tryInitLoginInfoByLocalData", e);
            return null;
        }
    }

}

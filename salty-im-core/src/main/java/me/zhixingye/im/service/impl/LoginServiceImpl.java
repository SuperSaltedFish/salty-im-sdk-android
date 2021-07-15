package me.zhixingye.im.service.impl;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.salty.protos.LoginResp;
import com.salty.protos.StatusCode;
import com.salty.protos.UserProfile;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Semaphore;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.api.UserApi;
import me.zhixingye.im.constant.ClientErrorCode;
import me.zhixingye.im.database.SQLiteServiceManager;
import me.zhixingye.im.database.UserDao;
import me.zhixingye.im.event.EventManager;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.LoginService;
import me.zhixingye.im.event.OnLoggedInEvent;
import me.zhixingye.im.event.OnLoggedOutEvent;
import me.zhixingye.im.tool.CallbackHelper;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.im.util.MD5Util;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月21日.
 */
public class LoginServiceImpl extends BasicServiceImpl implements LoginService {

    private static final String TAG = "LoginServiceImpl";

    private static final int DATABASE_VERSION = 1;

    private static final String STORAGE_KEY_LOGIN_INFO = TAG + ".LoginInfo";

    private final UserApi mUserApi = new UserApi();

    private final Semaphore mLoginLock = new Semaphore(1);

    private final Set<OnLoginListener> mOnLoginListeners = new CopyOnWriteArraySet<>();

    private volatile boolean isLogged = false;

    public LoginServiceImpl() {
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
                        CallbackHelper.callFailure(StatusCode.STATUS_TOKEN_EXPIRED, "", callback);
                        return;
                    }
                    if (tryInitLoginInfo(loginResp)) {
                        CallbackHelper.callCompleted(loginResp.getProfile(), callback);
                    } else {
                        CallbackHelper.callFailure(ClientErrorCode.INTERNAL_UNKNOWN, callback);
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
        RequestCallback<LoginResp> callbackWrapper = new RequestCallbackWrapper<LoginResp>(null) {
            @Override
            public void onCompleted(LoginResp loginResp) {
                try {
                    if (tryInitLoginInfo(loginResp)) {
                        CallbackHelper.callCompleted(loginResp.getProfile(), callback);
                    } else {
                        CallbackHelper.callFailure(ClientErrorCode.INTERNAL_UNKNOWN, callback);
                    }
                } finally {
                    mLoginLock.release();
                }
                super.onCompleted(loginResp);
            }

            @Override
            public void onFailure(int code, String error) {
                mLoginLock.release();
                CallbackHelper.callFailure(code, error, callback);
                super.onFailure(code, error);
            }

        };

        if (!TextUtils.isEmpty(telephone)) {
            mUserApi.loginByTelephone(telephone, password, callbackWrapper);
        } else {
            mUserApi.loginByEmail(email, password, callbackWrapper);
        }
    }

    @Override
    public void logout() {
        mUserApi.logout(null);

        saveLoginRespToLocal(null);

        SQLiteServiceManager.get().close();

        isLogged = false;

        EventManager.sendEvent(new OnLoggedOutEvent());

        callOnLoggedOutListener();
    }

    @Override
    public boolean isLogged() {
        return isLogged;
    }

    @Override
    public void addOnLoginListener(OnLoginListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mOnLoginListeners) {
            mOnLoginListeners.add(listener);
        }

    }

    @Override
    public void removeOnLoginListener(OnLoginListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mOnLoginListeners) {
            mOnLoginListeners.remove(listener);
        }
    }

    private void callOnLoggedInListener() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                synchronized (mOnLoginListeners) {
                    for (OnLoginListener listener : mOnLoginListeners) {
                        listener.onLoggedIn();
                    }
                }
            }
        });
    }

    private void callOnLoginExpiredListener() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                synchronized (mOnLoginListeners) {
                    for (OnLoginListener listener : mOnLoginListeners) {
                        listener.onLoginExpired();
                    }
                }
            }
        });
    }

    private void callOnLoggedOutListener() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                synchronized (mOnLoginListeners) {
                    for (OnLoginListener listener : mOnLoginListeners) {
                        listener.onLoggedOut();
                    }
                }
            }
        });
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

        EventManager.sendEvent(new OnLoggedInEvent(loginResp));

        callOnLoggedInListener();

        return true;
    }

    private void saveLoginRespToLocal(@Nullable LoginResp loginResp) {
        if (loginResp == null) {
            IMCore.get().getStorageService().removeFromStorage(TAG, STORAGE_KEY_LOGIN_INFO);
        } else {
            IMCore.get().getStorageService().putByteArrayToStorage(TAG, STORAGE_KEY_LOGIN_INFO, loginResp.toByteArray());
        }
    }

    @Nullable
    private LoginResp getLoginRespFromLocal() {
        byte[] data = IMCore.get().getStorageService().getByteArrayFromStorage(TAG, STORAGE_KEY_LOGIN_INFO);
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

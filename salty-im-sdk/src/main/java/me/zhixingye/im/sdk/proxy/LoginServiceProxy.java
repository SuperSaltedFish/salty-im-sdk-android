package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.salty.protos.UserProfile;


import java.util.HashSet;
import java.util.Set;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.ILoginRemoteService;
import me.zhixingye.im.sdk.IOnLoginListener;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.service.LoginService;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月21日.
 */
public class LoginServiceProxy implements LoginService, RemoteProxy {

    private static final String TAG = "AccountServiceProxy";

    private ILoginRemoteService mRemoteService;
    private final Set<OnLoginListener> mOnLoginListeners = new HashSet<>();

    @WorkerThread
    @Override
    public void onBind(IRemoteService service) throws RemoteException {
        mRemoteService = service.getLoginRemoteService();
        setupRemoteListener();
    }

    @Override
    public void onUnbind() {

    }

    @Override
    public void loginByTelephone(String telephone, String password, @Nullable RequestCallback<UserProfile> callback) {
        try {
            mRemoteService.loginByTelephone(telephone, password, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Override
    public void loginByEmail(String email, String password, @Nullable RequestCallback<UserProfile> callback) {
        try {
            mRemoteService.loginByEmail(email, password, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Override
    public void loginByLastLoginInfo(RequestCallback<UserProfile> callback) {
        try {
            mRemoteService.loginByLastLoginInfo(new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Override
    public void logout() {
        try {
            mRemoteService.logout();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public boolean isLogged() {
        try {
            return mRemoteService.isLogged();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
        return false;
    }


    @Override
    public synchronized void addOnLoginListener(final OnLoginListener listener) {
        if (listener == null) {
            return;
        }
        mOnLoginListeners.add(listener);
    }

    @Override
    public synchronized void removeOnLoginListener(OnLoginListener listener) {
        if (listener == null) {
            return;
        }
        mOnLoginListeners.remove(listener);
    }

    public void setupRemoteListener() throws RemoteException {
        mRemoteService.setOnLoginListener(new IOnLoginListener.Stub() {
            @Override
            public void onLoggedOut() {
                for (OnLoginListener listener : mOnLoginListeners) {
                    listener.onLoggedOut();
                }
            }

            @Override
            public void onLoggedIn() {
                for (OnLoginListener listener : mOnLoginListeners) {
                    listener.onLoggedIn();
                }
            }

            @Override
            public void onLoginExpired() {
                for (OnLoginListener listener : mOnLoginListeners) {
                    listener.onLoginExpired();
                }
            }
        });
    }


}

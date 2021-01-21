package me.zhixingye.im.sdk.proxy;

import androidx.annotation.Nullable;

import com.salty.protos.UserProfile;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.ILoginRemoteService;
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

    @Override
    public void onBindHandle(IRemoteService service) {
        try {
            mRemoteService = service.getLoginRemoteService();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            mRemoteService = null;
        }
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

}

// ILoginRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteRequestCallback;

interface ILoginRemoteService {
    void loginByTelephone(String telephone, String password, IRemoteRequestCallback callback);

    void loginByEmail(String email, String password, IRemoteRequestCallback callback);

    void loginByLastLoginInfo(IRemoteRequestCallback callback);

    void logout();

    boolean isLogged();

    String getCurrentUserId();

    String getCurrentUserToken();

    byte[] getCurrentUserProfile();
}
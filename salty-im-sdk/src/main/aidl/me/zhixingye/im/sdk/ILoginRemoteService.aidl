// ILoginRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteCallback;

interface ILoginRemoteService {
    void loginByTelephone(String telephone, String password, IRemoteCallback callback);

    void loginByEmail(String email, String password, IRemoteCallback callback);

    void loginByLastLoginInfo(IRemoteCallback callback);

    void logout();

    boolean isLogged();

    String getCurrentUserId();

    String getCurrentUserToken();

    byte[] getCurrentUserProfile();
}
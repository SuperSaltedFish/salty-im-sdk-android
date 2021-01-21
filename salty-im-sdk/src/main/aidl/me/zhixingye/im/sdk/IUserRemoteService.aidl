// IUserRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteRequestCallback;

interface IUserRemoteService {
    String getCurrentUserId();

    String getCurrentUserToken();

    byte[] getCurrentUserProfile();

    byte[] getUserProfileFromLocal(String userId);

    void updateUserInfo(String nickname, String description, int sex, long birthday, String location, IRemoteRequestCallback callback);

    void getUserInfoByUserId(String userId, IRemoteRequestCallback callback);

    void queryUserInfoByTelephone(String telephone, IRemoteRequestCallback callback);

    void queryUserInfoByEmail(String email, IRemoteRequestCallback callback);
}

// IUserRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteCallback;

interface IUserRemoteService {
    String getCurrentUserId();

    String getCurrentUserToken();

    byte[] getCurrentUserProfile();

    byte[] getUserProfileFromLocal(String userId);

    void updateUserInfo(String nickname, String description, int sex, long birthday, String location, IRemoteCallback callback);

    void getUserInfoByUserId(String userId, IRemoteCallback callback);

    void queryUserInfoByTelephone(String telephone, IRemoteCallback callback);

    void queryUserInfoByEmail(String email, IRemoteCallback callback);
}

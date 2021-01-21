// IPasswordRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteCallback;

interface IPasswordRemoteService {
     void resetLoginPasswordByEmail(String email, String newPassword, IRemoteCallback callback);

     void resetLoginPasswordByTelephone(String telephone, String newPassword, IRemoteCallback callback);
}
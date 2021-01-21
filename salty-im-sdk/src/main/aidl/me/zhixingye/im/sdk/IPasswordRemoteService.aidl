// IPasswordRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteRequestCallback;

interface IPasswordRemoteService {
     void resetLoginPasswordByEmail(String email, String newPassword, IRemoteRequestCallback callback);

     void resetLoginPasswordByTelephone(String telephone, String newPassword, IRemoteRequestCallback callback);
}
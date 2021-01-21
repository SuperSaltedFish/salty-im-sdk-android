// IRegisterRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteCallback;

interface IRegisterRemoteService {
     void registerByTelephone(String telephone, String password, IRemoteCallback callback);

     void registerByEmail(String email, String password, IRemoteCallback callback);
}
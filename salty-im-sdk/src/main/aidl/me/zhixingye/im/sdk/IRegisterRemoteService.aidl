// IRegisterRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteRequestCallback;

interface IRegisterRemoteService {
     void registerByTelephone(String telephone, String password, IRemoteRequestCallback callback);

     void registerByEmail(String email, String password, IRemoteRequestCallback callback);
}
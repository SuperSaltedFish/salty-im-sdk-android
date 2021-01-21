// ISMSRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteRequestCallback;

interface ISMSRemoteService {
    void obtainVerificationCodeForTelephoneType(String telephone, int codeType, IRemoteRequestCallback callback);

    void verifyTelephoneSMSCode(String telephone, String smsCode, int codeType, IRemoteRequestCallback callback);
}
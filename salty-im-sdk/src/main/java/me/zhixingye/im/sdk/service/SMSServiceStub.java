package me.zhixingye.im.sdk.service;

import com.salty.protos.SMSOperationType;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IRemoteRequestCallback;
import me.zhixingye.im.sdk.ISMSRemoteService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SMSServiceStub extends ISMSRemoteService.Stub {
    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, int codeType, IRemoteRequestCallback callback) {
        IMCore.get().getSMSService()
                .obtainVerificationCodeForTelephoneType(
                        telephone,
                        SMSOperationType.forNumber(codeType),
                        new ByteRemoteCallback<>(callback));
    }

    @Override
    public void verifyTelephoneSMSCode(String telephone, String smsCode, int codeType, IRemoteRequestCallback callback) {
        IMCore.get().getSMSService()
                .verifyTelephoneSMSCode(
                        telephone,
                        smsCode,
                        SMSOperationType.forNumber(codeType),
                        new ByteRemoteCallback<>(callback));
    }
}

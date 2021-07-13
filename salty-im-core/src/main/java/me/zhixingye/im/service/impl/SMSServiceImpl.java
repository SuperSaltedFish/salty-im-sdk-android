package me.zhixingye.im.service.impl;

import com.salty.protos.ObtainTelephoneSMSCodeResp;
import com.salty.protos.SMSOperationType;
import com.salty.protos.VerifyTelephoneSMSCodeResp;

import me.zhixingye.im.api.BasicApi;
import me.zhixingye.im.api.SMSApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.SMSService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SMSServiceImpl extends BasicServiceImpl implements SMSService {

    private final SMSApi mSMSApi;

    public SMSServiceImpl() {
        mSMSApi = BasicApi.getApi(SMSApi.class);
    }

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, SMSOperationType type, RequestCallback<ObtainTelephoneSMSCodeResp> callback) {
        mSMSApi.obtainTelephoneSMSCode(telephone, type, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void verifyTelephoneSMSCode(String telephone, String smsCode, SMSOperationType type, RequestCallback<VerifyTelephoneSMSCodeResp> callback) {
        mSMSApi.verifyTelephoneSMSCode(telephone, smsCode, type, new RequestCallbackWrapper<>(callback));
    }
}

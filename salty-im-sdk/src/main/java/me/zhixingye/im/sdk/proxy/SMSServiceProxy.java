package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import androidx.annotation.WorkerThread;

import com.salty.protos.ObtainTelephoneSMSCodeResp;
import com.salty.protos.SMSOperationType;
import com.salty.protos.VerifyTelephoneSMSCodeResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.ISMSRemoteService;
import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SMSServiceProxy extends RemoteProxy implements SMSService {

    private static final String TAG = "ContactServiceProxy";

    private ISMSRemoteService mRemoteService;

    @WorkerThread
    @Override
    public void onBind(IRemoteService service) throws RemoteException {
        mRemoteService = service.getSMSRemoteService();
    }

    @Override
    public void onUnbind() {

    }

    @Override
    public void verifyTelephoneSMSCode(String telephone, String smsCode, SMSOperationType type, RequestCallback<VerifyTelephoneSMSCodeResp> callback) {
        try {
            mRemoteService.verifyTelephoneSMSCode(telephone, smsCode, type.getNumber(), new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
        }
    }

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, SMSOperationType type, RequestCallback<ObtainTelephoneSMSCodeResp> callback) {
        try {
            mRemoteService.obtainVerificationCodeForTelephoneType(telephone, type.getNumber(), new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
        }
    }
}

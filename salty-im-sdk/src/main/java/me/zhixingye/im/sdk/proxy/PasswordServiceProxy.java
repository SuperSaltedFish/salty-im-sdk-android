package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import androidx.annotation.WorkerThread;

import com.salty.protos.ResetPasswordResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IPasswordRemoteService;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.service.PasswordService;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月21日.
 */
public class PasswordServiceProxy extends RemoteProxy implements PasswordService {

    private static final String TAG = "PasswordServiceProxy";

    private IPasswordRemoteService mRemoteService;

    @WorkerThread
    @Override
    public void onBind(IRemoteService service) throws RemoteException {
        mRemoteService = service.getPasswordRemoteService();
    }

    @Override
    public void onUnbind() {

    }

    @Override
    public void resetLoginPasswordByTelephone(String telephone, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        try {
            mRemoteService.resetLoginPasswordByTelephone(telephone, newPassword, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
        }
    }

    @Override
    public void resetLoginPasswordByEmail(String email, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        try {
            mRemoteService.resetLoginPasswordByEmail(email, newPassword, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
        }
    }
}

package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import androidx.annotation.WorkerThread;

import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRegisterRemoteService;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.service.RegisterService;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月21日.
 */
public class RegisterServiceProxy implements RegisterService, RemoteProxy {

    private static final String TAG = "RegisterServiceProxy";

    private IRegisterRemoteService mRemoteService;

    @WorkerThread
    @Override
    public void onBind(IRemoteService service) throws RemoteException {
        mRemoteService = service.getRegisterRemoteService();
    }

    @Override
    public void onUnbind() {
        mRemoteService=null;
    }

    @Override
    public void registerByTelephone(String telephone, String password, RequestCallback<RegisterResp> callback) {
        try {
            mRemoteService.registerByTelephone(telephone, password, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Override
    public void registerByEmail(String email, String password, RequestCallback<RegisterResp> callback) {
        try {
            mRemoteService.registerByEmail(email, password, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }
}

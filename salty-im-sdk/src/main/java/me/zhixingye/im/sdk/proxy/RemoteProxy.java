package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import androidx.annotation.WorkerThread;

import me.zhixingye.im.constant.ClientErrorCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月13日.
 */
public abstract class RemoteProxy {
    @WorkerThread
    public abstract void onBind(IRemoteService service) throws RemoteException;

    @WorkerThread
    public abstract void onUnbind();

    public void callRemoteFail(RequestCallback<?> callback) {
        if (callback != null) {
            callback.onFailure(
                    ClientErrorCode.INTERNAL_IPC_EXCEPTION.getCode(),
                    ClientErrorCode.INTERNAL_IPC_EXCEPTION.getMsg());
        }
    }

}

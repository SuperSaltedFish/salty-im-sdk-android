package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import androidx.annotation.WorkerThread;

import me.zhixingye.im.sdk.IMessageRemoteService;
import me.zhixingye.im.service.MessageService;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class MessageServiceProxy implements MessageService, RemoteProxy {

    private static final String TAG = "ContactServiceProxy";

    private IMessageRemoteService mRemoteService;

    @WorkerThread
    @Override
    public void onBind(IRemoteService service) throws RemoteException {
        mRemoteService = service.getMessageRemoteService();
    }

    @Override
    public void onUnbind() {

    }

}

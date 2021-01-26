package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import androidx.annotation.WorkerThread;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.ContactOperationMessage;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.GetContactOperationMessageListResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import javax.annotation.Nullable;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IContactRemoteService;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.service.ContactService;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ContactServiceProxy implements ContactService, RemoteProxy {

    private static final String TAG = "ContactServiceProxy";

    private IContactRemoteService mRemoteService;

    @WorkerThread
    @Override
    public void onBind(IRemoteService service) throws RemoteException {
        mRemoteService = service.getContactRemoteService();
    }

    @Override
    public void onUnbind() {

    }


    @Override
    public void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback) {
        try {
            mRemoteService.requestContact(userId, reason, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Override
    public void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback) {
        try {
            mRemoteService.refusedContact(userId, reason, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Override
    public void acceptContact(String userId, RequestCallback<AcceptContactResp> callback) {
        try {
            mRemoteService.acceptContact(userId, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Override
    public void deleteContact(String userId, RequestCallback<DeleteContactResp> callback) {
        try {
            mRemoteService.deleteContact(userId, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Override
    public void getContactOperationMessageList(long maxMessageTime, RequestCallback<GetContactOperationMessageListResp> callback) {
        try {
            mRemoteService.getContactOperationMessageList(maxMessageTime, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Nullable
    @Override
    public ContactOperationMessage getContactOperationFromLocal(String targetUserId) {
        try {
            byte[] data = mRemoteService.getContactOperationFromLocal(targetUserId);
            if (data == null) {
                return null;
            }
            return ContactOperationMessage.parseFrom(data);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return null;
        }
    }
}

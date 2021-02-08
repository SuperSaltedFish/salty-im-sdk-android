package me.zhixingye.im.sdk.service;

import com.salty.protos.ContactOperationMessage;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IContactRemoteService;
import me.zhixingye.im.sdk.IRemoteRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ContactRemoteService extends IContactRemoteService.Stub {
    @Override
    public void requestContact(String userId, String reason, IRemoteRequestCallback callback) {
        IMCore.get().getContactService().requestContact(
                userId,
                reason,
                new ByteRemoteCallback<>(callback));
    }

    @Override
    public void refusedContact(String userId, String reason, IRemoteRequestCallback callback) {
        IMCore.get().getContactService().refusedContact(
                userId,
                reason,
                new ByteRemoteCallback<>(callback));
    }

    @Override
    public void acceptContact(String userId, IRemoteRequestCallback callback) {
        IMCore.get().getContactService().acceptContact(
                userId,
                new ByteRemoteCallback<>(callback));
    }

    @Override
    public void deleteContact(String userId, IRemoteRequestCallback callback) {
        IMCore.get().getContactService().deleteContact(
                userId,
                new ByteRemoteCallback<>(callback));
    }

    @Override
    public void getContactOperationMessageList(long maxMessageTime, IRemoteRequestCallback callback) {
        IMCore.get().getContactService().getContactOperationMessageList(
                maxMessageTime,
                new ByteRemoteCallback<>(callback));
    }

    @Override
    public void getContacts(IRemoteRequestCallback callback) {
        IMCore.get().getContactService().getContacts(
                new ByteRemoteCallback<>(callback));
    }

    @Override
    public byte[] getContactOperationFromLocal(String targetUserId) {
        ContactOperationMessage operation = IMCore.get().getContactService().getContactOperationFromLocal(targetUserId);
        if (operation == null) {
            return null;
        }
        return operation.toByteArray();
    }
}

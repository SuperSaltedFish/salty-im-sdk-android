package me.zhixingye.im.sdk.service;

import android.os.RemoteException;

import com.salty.protos.ContactOperationMessage;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IContactRemoteService;
import me.zhixingye.im.sdk.IOnContactOperationChangeListener;
import me.zhixingye.im.sdk.IRemoteRequestCallback;
import me.zhixingye.im.service.ContactService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ContactServiceStub extends IContactRemoteService.Stub {

    private IOnContactOperationChangeListener mRemoteOnContactOperationChangeListener;
    private ContactService.OnContactOperationChangeListener mLocalOnContactOperationChangeListener;

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
    public void getContactOperationList(long startDateTime, long endDateTime, IRemoteRequestCallback callback) {
        IMCore.get().getContactService().getContactOperationList(
                startDateTime,
                endDateTime,
                new ByteRemoteCallback<>(callback));
    }

    @Override
    public void getContactList(IRemoteRequestCallback callback) {
        IMCore.get().getContactService().getContactList(
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

    @Override
    public synchronized void setOnContactOperationChangeListener(IOnContactOperationChangeListener listener) {
        mRemoteOnContactOperationChangeListener = listener;
        if (mLocalOnContactOperationChangeListener == null) {
            mLocalOnContactOperationChangeListener = new ContactService.OnContactOperationChangeListener() {
                @Override
                public void onContactOperationChange(ContactOperationMessage message, @ChangeType int type) {
                    if (mRemoteOnContactOperationChangeListener != null) {
                        try {
                            mRemoteOnContactOperationChangeListener.onContactOperationChange(message.toByteArray(), type);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            IMCore.get().getContactService().addOnContactOperationChangeListener(mLocalOnContactOperationChangeListener);
        }
    }
}
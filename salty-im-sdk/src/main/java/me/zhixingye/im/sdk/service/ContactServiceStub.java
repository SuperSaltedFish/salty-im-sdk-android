package me.zhixingye.im.sdk.service;

import android.os.RemoteException;

import com.google.protobuf.InvalidProtocolBufferException;
import com.salty.protos.ContactOperationMessage;
import com.salty.protos.ContactProfile;
import com.salty.protos.ContactRemark;
import com.salty.protos.UpdateRemarkInfoResp;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.constant.ClientErrorCode;
import me.zhixingye.im.sdk.IContactRemoteService;
import me.zhixingye.im.sdk.IOnContactOperationChangeListener;
import me.zhixingye.im.sdk.IOnContactChangeListener;
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

    private IOnContactChangeListener mRemoteOnContactChangeListener;
    private ContactService.OnContactChangeListener mLocalOnContactChangeListener;

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
    public void updateContactRemarkInfo(String userId, byte[] contactRemark, IRemoteRequestCallback callback) {
        ByteRemoteCallback<UpdateRemarkInfoResp> remoteCallback = new ByteRemoteCallback<>(callback);
        ContactRemark remark;
        try {
            remark = ContactRemark.parseFrom(contactRemark);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            remoteCallback.onFailure(ClientErrorCode.INTERNAL_UNKNOWN.getCode(), e.toString());
            return;
        }
        IMCore.get().getContactService().updateContactRemarkInfo(
                userId,
                remark,
                new ByteRemoteCallback<>(callback));
    }

    @Override
    public byte[] getContactProfileFromLocal(String contactId) {
        ContactProfile profile = IMCore.get()
                .getContactService()
                .getContactProfileFromLocal(contactId);
        if (profile == null) {
            return null;
        }
        return profile.toByteArray();
    }

    @Override
    public byte[] getContactOperationFromLocal(String targetUserId) {
        ContactOperationMessage operation = IMCore.get()
                .getContactService()
                .getContactOperationFromLocal(targetUserId);
        if (operation == null) {
            return null;
        }
        return operation.toByteArray();
    }

    @Override
    public void setOnContactChangeListener(IOnContactChangeListener listener) {
        mRemoteOnContactChangeListener = listener;
        if (mLocalOnContactChangeListener == null) {
            mLocalOnContactChangeListener = new ContactService.OnContactChangeListener() {
                @Override
                public void onContactProfileChange(ContactProfile profile, @ChangeType int type) {
                    if (mRemoteOnContactChangeListener != null) {
                        try {
                            mRemoteOnContactChangeListener.onContactChange(profile.toByteArray(), type);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            IMCore.get().getContactService().addOnContactOperationChangeListener(mLocalOnContactOperationChangeListener);
        }
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

// IContactRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteRequestCallback;
import me.zhixingye.im.sdk.IOnContactChangeListener;
import me.zhixingye.im.sdk.IOnContactOperationChangeListener;

interface IContactRemoteService {
    void requestContact(String userId, String reason, IRemoteRequestCallback callback);

    void refusedContact(String userId, String reason, IRemoteRequestCallback callback);

    void acceptContact(String userId, IRemoteRequestCallback callback);

    void deleteContact(String userId, IRemoteRequestCallback callback);

    void getContactOperationList(long startDateTime, long endDateTime, IRemoteRequestCallback callback);

    void getContactList(IRemoteRequestCallback callback);

    void updateContactRemarkInfo(String userId, in byte[] contactRemark, IRemoteRequestCallback callback);

    byte[] getContactProfileFromLocal(String contactId);

    byte[] getContactOperationFromLocal(String targetUserId);

    void setOnContactChangeListener(IOnContactChangeListener listener);

    void setOnContactOperationChangeListener(IOnContactOperationChangeListener listener);
}

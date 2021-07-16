package me.zhixingye.im.service.impl;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.ContactOperationMessage;
import com.salty.protos.ContactProfile;
import com.salty.protos.ContactRemark;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.GetContactOperationListResp;
import com.salty.protos.GetContactListResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;
import com.salty.protos.UpdateRemarkInfoResp;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Nullable;

import me.zhixingye.im.api.BasicApi;
import me.zhixingye.im.api.ContactApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ContactService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ContactServiceImpl extends BasicServiceImpl implements ContactService {

    private final ContactApi mContactApi = new ContactApi();

    private final Map<String, ContactProfile> mLocalContactMap = new ConcurrentHashMap<>();
    private final Map<String, ContactOperationMessage> mLocalContactOperationMap = new ConcurrentHashMap<>();

    private final Set<OnContactOperationChangeListener> mOnContactOperationChangeListeners = new CopyOnWriteArraySet<>();
    private final Set<OnContactChangeListener> mOnContactChangeListeners = new CopyOnWriteArraySet<>();

    public ContactServiceImpl() {
    }

    @Override
    public void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback) {
        mContactApi.requestContact(userId, reason, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback) {
        mContactApi.refusedContact(userId, reason, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void acceptContact(String userId, RequestCallback<AcceptContactResp> callback) {
        mContactApi.acceptContact(userId, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void deleteContact(String userId, RequestCallback<DeleteContactResp> callback) {
        mContactApi.deleteContact(userId, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void getContactList(RequestCallback<GetContactListResp> callback) {
        mContactApi.getAllContact(new RequestCallbackWrapper<GetContactListResp>(callback) {
            @Override
            public void onCompleted(GetContactListResp response) {
                synchronized (mLocalContactMap) {
                    List<ContactProfile> contactList = response.getContactListList();
                    mLocalContactMap.clear();
                    for (ContactProfile contact : contactList) {
                        mLocalContactMap.put(contact.getUserProfile().getUserId(), contact);
                    }
                }
                super.onCompleted(response);
            }
        });
    }

    @Override
    public void getContactOperationList(long startDateTime, long endDateTime, RequestCallback<GetContactOperationListResp> callback) {
        mContactApi.getContactOperationList(startDateTime, endDateTime, new RequestCallbackWrapper<GetContactOperationListResp>(callback) {
            @Override
            public void onCompleted(GetContactOperationListResp response) {
                synchronized (mLocalContactOperationMap) {
                    List<ContactOperationMessage> contactOperationList = response.getMessageListList();
                    mLocalContactOperationMap.clear();
                    for (ContactOperationMessage operation : contactOperationList) {
                        mLocalContactOperationMap.put(operation.getTriggerProfile().getUserId(), operation);
                    }
                }
                super.onCompleted(response);
            }
        });
    }

    @Override
    public void updateContactRemarkInfo(String userId, ContactRemark remark, RequestCallback<UpdateRemarkInfoResp> callback) {
        mContactApi.updateContactRemarkInfo(userId, remark, new RequestCallbackWrapper<>(callback));
    }

    @Nullable
    @Override
    public ContactProfile getContactProfileFromLocal(String contactId) {
        return mLocalContactMap.get(contactId);
    }

    @Nullable
    @Override
    public ContactOperationMessage getContactOperationFromLocal(String targetUserId) {
        return mLocalContactOperationMap.get(targetUserId);
    }

    @Override
    public synchronized void addOnContactChangeListener(OnContactChangeListener listener) {
        if (listener != null) {
            mOnContactChangeListeners.add(listener);
        }
    }

    @Override
    public synchronized void removeOnContactChangeListener(OnContactChangeListener listener) {
        mOnContactChangeListeners.remove(listener);
    }

    @Override
    public synchronized void addOnContactOperationChangeListener(OnContactOperationChangeListener listener) {
        if (listener != null) {
            mOnContactOperationChangeListeners.add(listener);
        }
    }

    @Override
    public synchronized void removeOnContactOperationChangeListener(OnContactOperationChangeListener listener) {
        mOnContactOperationChangeListeners.remove(listener);
    }


}

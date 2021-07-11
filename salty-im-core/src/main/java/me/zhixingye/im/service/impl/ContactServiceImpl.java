package me.zhixingye.im.service.impl;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.ContactOperationMessage;
import com.salty.protos.ContactProfile;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.GetContactOperationListResp;
import com.salty.protos.GetContactListResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Nullable;

import me.zhixingye.im.api.ContactApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.service.ContactService;
import me.zhixingye.im.service.LoginService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ContactServiceImpl extends BasicServiceImpl implements ContactService {

    private final Set<OnContactOperationChangeListener> mOnContactOperationChangeListeners = new CopyOnWriteArraySet<>();
    private final Set<OnContactProfileChangeListener> mOnContactProfileChangeListeners = new CopyOnWriteArraySet<>();

    public ContactServiceImpl() {
        super();
    }

    @Override
    public void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ContactApi.class)
                .requestContact(userId, reason, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ContactApi.class)
                .refusedContact(userId, reason, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void acceptContact(String userId, RequestCallback<AcceptContactResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ContactApi.class)
                .acceptContact(userId, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void deleteContact(String userId, RequestCallback<DeleteContactResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ContactApi.class)
                .deleteContact(userId, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void getContactList(RequestCallback<GetContactListResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ContactApi.class)
                .getAllContact(new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void getContactOperationList(long startDateTime, long endDateTime, RequestCallback<GetContactOperationListResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ContactApi.class)
                .getContactOperationList(startDateTime, endDateTime, new RequestCallbackWrapper<>(callback));
    }

    @Nullable
    @Override
    public ContactProfile getContactProfileFromLocal(String contactId) {
        return null;
    }

    @Nullable
    @Override
    public ContactOperationMessage getContactOperationFromLocal(String targetUserId) {
        return null;
    }

    @Override
    public synchronized void addOnContactProfileChangeListener(OnContactProfileChangeListener listener) {
        if (listener != null) {
            mOnContactProfileChangeListeners.add(listener);
        }
    }

    @Override
    public synchronized void removeOnContactProfileChangeListener(OnContactProfileChangeListener listener) {
        mOnContactProfileChangeListeners.remove(listener);
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

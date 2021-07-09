package me.zhixingye.im.service;

import androidx.annotation.IntDef;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.ContactOperationMessage;
import com.salty.protos.ContactProfile;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.GetContactOperationMessageListResp;
import com.salty.protos.GetContactsResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import javax.annotation.Nullable;

import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public interface ContactService extends BasicService {

    void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback);

    void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback);

    void acceptContact(String userId, RequestCallback<AcceptContactResp> callback);

    void deleteContact(String userId, RequestCallback<DeleteContactResp> callback);

    void getContacts(RequestCallback<GetContactsResp> callback);

    void getContactOperationList(long maxMessageTime, RequestCallback<GetContactOperationMessageListResp> callback);

    @Nullable
    ContactOperationMessage getContactOperationFromLocal(String targetUserId);

    void addOnContactOperationChangeListener(OnContactOperationChangeListener listener);

    void removeOnContactOperationChangeListener(OnContactOperationChangeListener listener);

    interface OnContactOperationChangeListener {
        int CHANGE_TYPE_RECEIVE = 1;
        int CHANGE_TYPE_UPDATE = 2;
        int CHANGE_TYPE_DELETE = 3;

        @IntDef({
                CHANGE_TYPE_RECEIVE,
                CHANGE_TYPE_UPDATE,
                CHANGE_TYPE_DELETE})
        @Retention(RetentionPolicy.SOURCE)
        @interface ChangeType {
        }

        void onContactOperationChange(ContactOperationMessage message, @ChangeType int type);
    }
}

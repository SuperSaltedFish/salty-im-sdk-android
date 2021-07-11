package me.zhixingye.im.service;

import androidx.annotation.IntDef;

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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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

    void getContactList(RequestCallback<GetContactListResp> callback);

    void getContactOperationList(long startDateTime, long endDateTime, RequestCallback<GetContactOperationListResp> callback);

    void updateContactRemarkInfo(String userId, ContactRemark remark, RequestCallback<UpdateRemarkInfoResp> callback);

    @Nullable
    ContactProfile getContactProfileFromLocal(String contactId);

    @Nullable
    ContactOperationMessage getContactOperationFromLocal(String targetUserId);

    void addOnContactChangeListener(OnContactChangeListener listener);

    void removeOnContactChangeListener(OnContactChangeListener listener);

    void addOnContactOperationChangeListener(OnContactOperationChangeListener listener);

    void removeOnContactOperationChangeListener(OnContactOperationChangeListener listener);

    interface OnContactChangeListener {
        int CHANGE_TYPE_ADDED = 1;
        int CHANGE_TYPE_UPDATED = 2;
        int CHANGE_TYPE_DELETED = 3;

        @IntDef({
                CHANGE_TYPE_ADDED,
                CHANGE_TYPE_UPDATED,
                CHANGE_TYPE_DELETED})
        @Retention(RetentionPolicy.SOURCE)
        @interface ChangeType {
        }

        void onContactProfileChange(ContactProfile profile, @ChangeType int type);
    }

    interface OnContactOperationChangeListener {
        int CHANGE_TYPE_RECEIVED = 1;
        int CHANGE_TYPE_UPDATED = 2;
        int CHANGE_TYPE_DELETED = 3;

        @IntDef({
                CHANGE_TYPE_RECEIVED,
                CHANGE_TYPE_UPDATED,
                CHANGE_TYPE_DELETED})
        @Retention(RetentionPolicy.SOURCE)
        @interface ChangeType {
        }

        void onContactOperationChange(ContactOperationMessage message, @ChangeType int type);
    }
}

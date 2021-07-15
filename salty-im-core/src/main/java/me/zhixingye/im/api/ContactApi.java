package me.zhixingye.im.api;

import com.salty.protos.AcceptContactReq;
import com.salty.protos.AcceptContactResp;
import com.salty.protos.ContactRemark;
import com.salty.protos.ContactServiceGrpc;
import com.salty.protos.DeleteContactReq;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.GetContactListReq;
import com.salty.protos.GetContactListResp;
import com.salty.protos.GetContactOperationListReq;
import com.salty.protos.GetContactOperationListResp;
import com.salty.protos.RefusedContactReq;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactReq;
import com.salty.protos.RequestContactResp;
import com.salty.protos.UpdateRemarkInfoReq;
import com.salty.protos.UpdateRemarkInfoResp;

import java.util.concurrent.TimeUnit;

import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ContactApi extends BasicApi {

    public ContactServiceGrpc.ContactServiceStub newServiceStub() {
        return ContactServiceGrpc.newStub(getManagedChannel())
                .withDeadlineAfter(30, TimeUnit.SECONDS);
    }

    public void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback) {
        RequestContactReq req = RequestContactReq.newBuilder()
                .setUserId(userId)
                .setReason(reason)
                .build();

        newServiceStub().requestContact(req, new UnaryStreamObserver<>(callback));
    }

    public void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback) {
        RefusedContactReq req = RefusedContactReq.newBuilder()
                .setUserId(userId)
                .setReason(reason)
                .build();

        newServiceStub().refusedContact(req, new UnaryStreamObserver<>(callback));
    }

    public void acceptContact(String userId, RequestCallback<AcceptContactResp> callback) {
        AcceptContactReq req = AcceptContactReq.newBuilder()
                .setUserId(userId)
                .build();

        newServiceStub().acceptContact(req, new UnaryStreamObserver<>(callback));
    }

    public void deleteContact(String userId, RequestCallback<DeleteContactResp> callback) {
        DeleteContactReq req = DeleteContactReq.newBuilder()
                .setUserId(userId)
                .build();

        newServiceStub().deleteContact(req, new UnaryStreamObserver<>(callback));
    }

    public void getAllContact(RequestCallback<GetContactListResp> callback) {
        GetContactListReq req = GetContactListReq.newBuilder()
                .build();

        newServiceStub().getContactList(req, new UnaryStreamObserver<>(callback));
    }

    public void getContactOperationList(long startDateTime, long endDateTime, RequestCallback<GetContactOperationListResp> callback) {
        GetContactOperationListReq req = GetContactOperationListReq.newBuilder()
                .setStartDateTime(startDateTime)
                .setEndDateTime(endDateTime)
                .build();

        newServiceStub().getContactOperationList(req, new UnaryStreamObserver<>(callback));
    }

    public void updateContactRemarkInfo(String userId, ContactRemark remark, RequestCallback<UpdateRemarkInfoResp> callback) {
        UpdateRemarkInfoReq req = UpdateRemarkInfoReq.newBuilder()
                .setRemarkInfo(remark)
                .setUserId(userId)
                .build();

        newServiceStub().updateRemarkInfo(req, new UnaryStreamObserver<>(callback));
    }
}

package me.zhixingye.im.api;

import com.salty.protos.ClearConversationMessageReq;
import com.salty.protos.ClearConversationMessageResp;
import com.salty.protos.Conversation;
import com.salty.protos.ConversationServiceGrpc;
import com.salty.protos.GetAllConversationReq;
import com.salty.protos.GetAllConversationResp;
import com.salty.protos.GetConversationDetailReq;
import com.salty.protos.GetConversationDetailResp;
import com.salty.protos.RemoveConversationReq;
import com.salty.protos.RemoveConversationResp;
import com.salty.protos.UpdateConversationTitleReq;
import com.salty.protos.UpdateConversationTitleResp;
import com.salty.protos.UpdateConversationTopReq;
import com.salty.protos.UpdateConversationTopResp;
import com.salty.protos.UpdateNotificationStatusReq;
import com.salty.protos.UpdateNotificationStatusResp;

import java.util.concurrent.TimeUnit;

import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ConversationApi extends BasicApi {

    public ConversationServiceGrpc.ConversationServiceStub newServiceStub() {
        return ConversationServiceGrpc.newStub(getManagedChannel())
                .withDeadlineAfter(30, TimeUnit.SECONDS);
    }

    public void getAllConversations(RequestCallback<GetAllConversationResp> callback) {
        GetAllConversationReq req = GetAllConversationReq.newBuilder()
                .build();
        newServiceStub().getAllConversation(
                createReq(req),
                new InnerStreamObserver<>(GetAllConversationResp.getDefaultInstance(), callback));
    }

    public void getConversationDetail(String conversationId, Conversation.ConversationType type, RequestCallback<GetConversationDetailResp> callback) {
        GetConversationDetailReq req = GetConversationDetailReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .build();
        newServiceStub().getConversationDetail(
                createReq(req),
                new InnerStreamObserver<>(GetConversationDetailResp.getDefaultInstance(), callback));
    }

    public void removeConversation(String conversationId, Conversation.ConversationType type, RequestCallback<RemoveConversationResp> callback) {
        RemoveConversationReq req = RemoveConversationReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .build();
        newServiceStub().removeConversation(
                createReq(req),
                new InnerStreamObserver<>(RemoveConversationResp.getDefaultInstance(), callback));
    }

    public void clearConversationMessage(String conversationId, Conversation.ConversationType type, RequestCallback<ClearConversationMessageResp> callback) {
        ClearConversationMessageReq req = ClearConversationMessageReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .build();
        newServiceStub().clearConversationMessage(
                createReq(req),
                new InnerStreamObserver<>(ClearConversationMessageResp.getDefaultInstance(), callback));
    }


    public void updateConversationTitle(String conversationId, Conversation.ConversationType type, String title, RequestCallback<UpdateConversationTitleResp> callback) {
        UpdateConversationTitleReq req = UpdateConversationTitleReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .setTitle(title)
                .build();
        newServiceStub().getConversationDetail(
                createReq(req),
                new InnerStreamObserver<>(UpdateConversationTitleResp.getDefaultInstance(), callback));
    }

    public void updateConversationTop(String conversationId, Conversation.ConversationType type, boolean isTop, RequestCallback<UpdateConversationTopResp> callback) {
        UpdateConversationTopReq req = UpdateConversationTopReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .setIsTop(isTop)
                .build();
        newServiceStub().getConversationDetail(
                createReq(req),
                new InnerStreamObserver<>(UpdateConversationTopResp.getDefaultInstance(), callback));
    }

    public void updateNotificationStatus(String conversationId, Conversation.ConversationType type, Conversation.NotificationStatus status, RequestCallback<UpdateNotificationStatusResp> callback) {
        UpdateNotificationStatusReq req = UpdateNotificationStatusReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .setNotificationStatus(status)
                .build();
        newServiceStub().getConversationDetail(
                createReq(req),
                new InnerStreamObserver<>(UpdateNotificationStatusResp.getDefaultInstance(), callback));
    }
}

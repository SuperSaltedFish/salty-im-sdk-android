package me.zhixingye.im.sdk.service;

import com.salty.protos.Conversation;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IConversationRemoteService;
import me.zhixingye.im.sdk.IRemoteRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ConversationRemoteService extends IConversationRemoteService.Stub {
    @Override
    public void getAllConversations(IRemoteRequestCallback callback) {
        IMCore.get().getConversationService()
                .getAllConversations(
                        new ByteRemoteCallback<>(callback));
    }

    @Override
    public void getConversationDetail(String conversationId, int type, IRemoteRequestCallback callback) {
        IMCore.get().getConversationService()
                .getConversationDetail(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        new ByteRemoteCallback<>(callback));
    }

    @Override
    public void removeConversation(String conversationId, int type, IRemoteRequestCallback callback) {
        IMCore.get().getConversationService()
                .removeConversation(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        new ByteRemoteCallback<>(callback));
    }

    @Override
    public void clearConversationMessage(String conversationId, int type, IRemoteRequestCallback callback) {
        IMCore.get().getConversationService()
                .clearConversationMessage(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        new ByteRemoteCallback<>(callback));
    }

    @Override
    public void updateConversationTitle(String conversationId, int type, String title, IRemoteRequestCallback callback) {
        IMCore.get().getConversationService()
                .updateConversationTitle(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        title,
                        new ByteRemoteCallback<>(callback));
    }

    @Override
    public void updateConversationTop(String conversationId, int type, boolean isTop, IRemoteRequestCallback callback) {
        IMCore.get().getConversationService()
                .updateConversationTop(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        isTop,
                        new ByteRemoteCallback<>(callback));
    }

    @Override
    public void updateNotificationStatus(String conversationId, int type, int status, IRemoteRequestCallback callback) {
        IMCore.get().getConversationService()
                .updateNotificationStatus(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        Conversation.NotificationStatus.forNumber(status),
                        new ByteRemoteCallback<>(callback));
    }
}

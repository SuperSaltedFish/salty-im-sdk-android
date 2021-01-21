// IConversationRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteRequestCallback;

interface IConversationRemoteService {
    void getAllConversations(IRemoteRequestCallback callback);

    void getConversationDetail(String conversationId, int type, IRemoteRequestCallback callback);

    void removeConversation(String conversationId, int type, IRemoteRequestCallback callback);

    void clearConversationMessage(String conversationId, int type, IRemoteRequestCallback callback);

    void updateConversationTitle(String conversationId, int type, String title, IRemoteRequestCallback callback);

    void updateConversationTop(String conversationId, int type, boolean isTop, IRemoteRequestCallback callback);

    void updateNotificationStatus(String conversationId, int type, int status, IRemoteRequestCallback callback);
}

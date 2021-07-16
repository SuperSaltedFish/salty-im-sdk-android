// IGroupRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteRequestCallback;

interface IGroupRemoteService {
    void createGroup(String groupName, in List<String> memberUserIdArr, IRemoteRequestCallback callback);

    void joinGroup(String groupId, String reason, IRemoteRequestCallback callback);

    void quitGroup(String groupId, IRemoteRequestCallback callback);

    void addGroupMember(String groupId, in List<String> memberUserIdArr, IRemoteRequestCallback callback);

    void kickGroupMember(String groupId, String memberUserId, IRemoteRequestCallback callback);

    void updateGroupName(String groupId, String groupName, IRemoteRequestCallback callback);

    void updateGroupNotice(String groupId, String notice, IRemoteRequestCallback callback);

    void updateMemberNickname(String groupId, String memberNickname, IRemoteRequestCallback callback);
}

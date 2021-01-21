package me.zhixingye.im.sdk.service;

import com.salty.protos.AddGroupMemberResp;
import com.salty.protos.CreateGroupResp;
import com.salty.protos.JoinGroupResp;
import com.salty.protos.KickGroupMemberResp;
import com.salty.protos.QuitGroupResp;
import com.salty.protos.UpdateGroupNameResp;

import java.util.List;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IGroupRemoteService;
import me.zhixingye.im.sdk.IRemoteRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class GroupRemoteService extends IGroupRemoteService.Stub {
    @Override
    public void createGroup(String groupName, List<String> memberUserIdArr, IRemoteRequestCallback callback) {
        IMCore.get().getGroupService()
                .createGroup(
                        groupName,
                        memberUserIdArr,
                        new ByteRemoteCallback<CreateGroupResp>(callback));
    }

    @Override
    public void joinGroup(String groupId, String reason, IRemoteRequestCallback callback) {
        IMCore.get().getGroupService()
                .joinGroup(
                        groupId,
                        reason, new ByteRemoteCallback<JoinGroupResp>(callback));
    }

    @Override
    public void quitGroup(String groupId, IRemoteRequestCallback callback) {
        IMCore.get().getGroupService()
                .quitGroup(
                        groupId,
                        new ByteRemoteCallback<QuitGroupResp>(callback));
    }

    @Override
    public void addGroupMember(String groupId, List<String> memberUserIdArr, IRemoteRequestCallback callback) {
        IMCore.get().getGroupService()
                .addGroupMember(
                        groupId,
                        memberUserIdArr,
                        new ByteRemoteCallback<AddGroupMemberResp>(callback));
    }

    @Override
    public void kickGroupMember(String groupId, String memberUserId, IRemoteRequestCallback callback) {
        IMCore.get().getGroupService()
                .kickGroupMember(
                        groupId,
                        memberUserId,
                        new ByteRemoteCallback<KickGroupMemberResp>(callback));
    }

    @Override
    public void updateGroupName(String groupId, String groupName, IRemoteRequestCallback callback) {
        IMCore.get().getGroupService()
                .updateGroupName(
                        groupId,
                        groupName,
                        new ByteRemoteCallback<UpdateGroupNameResp>(callback));
    }

    @Override
    public void updateGroupNotice(String groupId, String notice, IRemoteRequestCallback callback) {
        IMCore.get().getGroupService()
                .updateGroupName(
                        groupId,
                        notice,
                        new ByteRemoteCallback<UpdateGroupNameResp>(callback));
    }

    @Override
    public void updateMemberNickname(String groupId, String memberNickname, IRemoteRequestCallback callback) {
        IMCore.get().getGroupService()
                .updateGroupName(
                        groupId,
                        memberNickname,
                        new ByteRemoteCallback<UpdateGroupNameResp>(callback));
    }
}

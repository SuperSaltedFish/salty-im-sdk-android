package me.zhixingye.im.service.impl;

import com.salty.protos.AddGroupMemberResp;
import com.salty.protos.CreateGroupResp;
import com.salty.protos.JoinGroupResp;
import com.salty.protos.KickGroupMemberResp;
import com.salty.protos.QuitGroupResp;
import com.salty.protos.UpdateGroupNameResp;
import com.salty.protos.UpdateGroupNoticeResp;
import com.salty.protos.UpdateMemberNicknameResp;

import java.util.List;

import me.zhixingye.im.api.BasicApi;
import me.zhixingye.im.api.GroupApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.GroupService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class GroupServiceImpl extends BasicServiceImpl implements GroupService {

    private final GroupApi mGroupApi = new GroupApi();

    public GroupServiceImpl() {
    }

    @Override
    public void createGroup(String groupName, List<String> memberUserIdArr, RequestCallback<CreateGroupResp> callback) {
        mGroupApi.createGroup(groupName, memberUserIdArr, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void joinGroup(String groupId, String reason, RequestCallback<JoinGroupResp> callback) {
        mGroupApi.joinGroup(groupId, reason, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void quitGroup(String groupId, RequestCallback<QuitGroupResp> callback) {
        mGroupApi.quitGroup(groupId, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void addGroupMember(String groupId, List<String> memberUserIdArr, RequestCallback<AddGroupMemberResp> callback) {
        mGroupApi.addGroupMember(groupId, memberUserIdArr, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void kickGroupMember(String groupId, String memberUserId, RequestCallback<KickGroupMemberResp> callback) {
        mGroupApi.kickGroupMember(groupId, memberUserId, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void updateGroupName(String groupId, String groupName, RequestCallback<UpdateGroupNameResp> callback) {
        mGroupApi.updateGroupName(groupId, groupName, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void updateGroupNotice(String groupId, String notice, RequestCallback<UpdateGroupNoticeResp> callback) {
        mGroupApi.updateGroupNotice(groupId, notice, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void updateMemberNickname(String groupId, String memberNickname, RequestCallback<UpdateMemberNicknameResp> callback) {
        mGroupApi.updateMemberNickname(groupId, memberNickname, new RequestCallbackWrapper<>(callback));
    }
}

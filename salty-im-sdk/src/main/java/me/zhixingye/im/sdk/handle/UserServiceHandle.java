package me.zhixingye.im.sdk.handle;


import com.salty.protos.GetUserInfoResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IRemoteCallback;
import me.zhixingye.im.sdk.IUserServiceHandle;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class UserServiceHandle extends IUserServiceHandle.Stub {

    @Override
    public byte[] getCurrentUserProfile() {
        UserProfile profile = IMCore.get().getUserService().getCurrentUserProfile();
        if (profile == null) {
            return null;
        }
        return profile.toByteArray();
    }

    @Override
    public byte[] getUserProfileFromLocal(String userId) {
        UserProfile profile = IMCore.get().getUserService().getUserProfileFromLocal(userId);
        if (profile == null) {
            return null;
        }
        return profile.toByteArray();
    }

    @Override
    public void updateUserInfo(String nickname, String description, int sex, long birthday, String location, IRemoteCallback callback) {
        IMCore.get().getUserService().updateUserInfo(
                nickname,
                description,
                UserProfile.Sex.forNumber(sex),
                birthday,
                location,
                new ByteRemoteCallback<UpdateUserInfoResp>(callback));
    }

    @Override
    public void getUserInfoByUserId(String userId, IRemoteCallback callback) {
        IMCore.get().getUserService().getUserInfoByUserId(
                userId,
                new ByteRemoteCallback<GetUserInfoResp>(callback));
    }

    @Override
    public void queryUserInfoByTelephone(String telephone, IRemoteCallback callback) {
        IMCore.get().getUserService().queryUserInfoByTelephone(
                telephone,
                new ByteRemoteCallback<QueryUserInfoResp>(callback));
    }

    @Override
    public void queryUserInfoByEmail(String email, IRemoteCallback callback) {
        IMCore.get().getUserService().queryUserInfoByEmail(
                email,
                new ByteRemoteCallback<QueryUserInfoResp>(callback));
    }
}

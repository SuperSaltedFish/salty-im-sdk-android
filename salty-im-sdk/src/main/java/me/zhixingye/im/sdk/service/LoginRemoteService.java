package me.zhixingye.im.sdk.service;

import com.salty.protos.UserProfile;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.ILoginRemoteService;
import me.zhixingye.im.sdk.IRemoteCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月21日.
 */
public class LoginRemoteService extends ILoginRemoteService.Stub {
    @Override
    public void loginByTelephone(String telephone, String password, IRemoteCallback callback) {
        IMCore.get().getLoginService().loginByTelephone(
                telephone,
                password,
                new ByteRemoteCallback<UserProfile>(callback));
    }

    @Override
    public void loginByEmail(String email, String password, IRemoteCallback callback) {
        IMCore.get().getLoginService().loginByEmail(
                email,
                password,
                new ByteRemoteCallback<UserProfile>(callback));
    }

    @Override
    public void loginByLastLoginInfo(IRemoteCallback callback) {
        IMCore.get().getLoginService().loginByLastLoginInfo(
                new ByteRemoteCallback<UserProfile>(callback));
    }

    @Override
    public void logout() {
        IMCore.get().getLoginService().logout();
    }

    @Override
    public boolean isLogged() {
        return IMCore.get().getLoginService().isLogged();
    }

    @Override
    public String getCurrentUserId() {
        return IMCore.get().getUserService().getCurrentUserId();
    }

    @Override
    public String getCurrentUserToken() {
        return IMCore.get().getUserService().getCurrentUserToken();
    }

    @Override
    public byte[] getCurrentUserProfile() {
        UserProfile profile = IMCore.get().getUserService().getCurrentUserProfile();
        if (profile == null) {
            return null;
        } else {
            return profile.toByteArray();
        }
    }
}

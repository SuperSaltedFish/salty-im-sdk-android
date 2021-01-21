package me.zhixingye.im.sdk.proxy;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import javax.annotation.Nullable;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IUserRemoteService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class UserServiceProxy implements UserService, RemoteProxy {

    private static final String TAG = "ContactServiceProxy";

    private IUserRemoteService mRemoteService;

    @Override
    public void onBindHandle(IRemoteService service) {
        try {
            mRemoteService = service.getUserRemoteService();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            mRemoteService = null;
        }
    }

    @Nullable
    @Override
    public String getCurrentUserId() {
        try {
            return mRemoteService.getCurrentUserId();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return null;
        }
    }

    @Nullable
    @Override
    public String getCurrentUserToken() {
        try {
            return mRemoteService.getCurrentUserToken();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return null;
        }
    }

    @Override
    public UserProfile getCurrentUserProfile() {
        try {
            byte[] data = mRemoteService.getCurrentUserProfile();
            if (data == null) {
                return null;
            }
            return UserProfile.parseFrom(data);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return null;
        }
    }

    @Nullable
    @Override
    public UserProfile getUserProfileFromLocal(String userId) {
        try {
            byte[] data = mRemoteService.getUserProfileFromLocal(userId);
            if (data == null) {
                return null;
            }
            return UserProfile.parseFrom(data);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return null;
        }
    }

    @Override
    public void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback) {
        try {
            mRemoteService.updateUserInfo(nickname, description, sex.getNumber(), birthday, location, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Override
    public void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback) {
        try {
            mRemoteService.getUserInfoByUserId(userId, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Override
    public void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback) {
        try {
            mRemoteService.queryUserInfoByTelephone(telephone, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }

    @Override
    public void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback) {
        try {
            mRemoteService.queryUserInfoByEmail(email, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }
}
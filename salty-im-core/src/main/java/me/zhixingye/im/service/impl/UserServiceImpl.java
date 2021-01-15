package me.zhixingye.im.service.impl;


import android.text.TextUtils;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import me.zhixingye.im.api.UserApi;
import me.zhixingye.im.database.UserDao;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.tool.CallbackHelper;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class UserServiceImpl implements UserService {

    private static final String TAG = "UserServiceImpl";


    private UserProfile mUserProfile;
    private String mToken;

    private final Map<String, UserProfile> mUserProfileCache;

    public UserServiceImpl() {
        mUserProfileCache = new ConcurrentHashMap<>();
    }

    @Nullable
    @Override
    public String getCurrentUserId() {
        return mUserProfile == null ? null : mUserProfile.getUserId();
    }

    @Nullable
    @Override
    public String getCurrentUserToken() {
        return mToken;
    }

    @Override
    public UserProfile getCurrentUserProfile() {
        return mUserProfile;
    }

    @Nullable
    @Override
    public UserProfile getUserProfileFromLocal(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        return mUserProfileCache.get(userId);
    }

    private void saveUserProfileToCache(UserProfile profile) {
        if (profile == null || TextUtils.isEmpty(profile.getUserId())) {
            return;
        }
        mUserProfileCache.put(profile.getUserId(), profile);
    }

    private void saveUserProfileToLocal(UserProfile profile) {
        if (profile == null || TextUtils.isEmpty(profile.getUserId())) {
            return;
        }
        saveUserProfileToCache(profile);
        if (!UserDao.saveUserProfile(profile)) {
            Logger.i(TAG, "saveUserProfileToLocalCache error");
        }
    }

    @Override
    public void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .updateUserInfo(nickname, description, sex, birthday, location, callback);
    }

    @Override
    public void getUserInfoByUserId(String userId, final RequestCallback<GetUserInfoResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .getUserInfoByUserId(userId, new RequestCallback<GetUserInfoResp>() {
                    @Override
                    public void onCompleted(GetUserInfoResp response) {
                        saveUserProfileToCache(response.getProfile());
                        CallbackHelper.callCompleted(response, callback);

                    }

                    @Override
                    public void onFailure(int code, String error) {
                        CallbackHelper.callFailure(code, error, callback);
                    }
                });
    }

    @Override
    public void queryUserInfoByTelephone(String telephone, final RequestCallback<QueryUserInfoResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .queryUserInfoByTelephone(telephone, new RequestCallback<QueryUserInfoResp>() {
                    @Override
                    public void onCompleted(QueryUserInfoResp response) {
                        saveUserProfileToCache(response.getProfile());
                        CallbackHelper.callCompleted(response, callback);
                    }

                    @Override
                    public void onFailure(int code, String error) {
                        CallbackHelper.callFailure(code, error, callback);
                    }
                });
    }

    @Override
    public void queryUserInfoByEmail(String email, final RequestCallback<QueryUserInfoResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .queryUserInfoByEmail(email, new RequestCallback<QueryUserInfoResp>() {
                    @Override
                    public void onCompleted(QueryUserInfoResp response) {
                        saveUserProfileToCache(response.getProfile());
                        CallbackHelper.callCompleted(response, callback);
                    }

                    @Override
                    public void onFailure(int code, String error) {
                        CallbackHelper.callFailure(code, error, callback);
                    }
                });
    }


}
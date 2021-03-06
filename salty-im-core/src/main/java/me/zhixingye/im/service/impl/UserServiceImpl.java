package me.zhixingye.im.service.impl;


import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.LoginResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import me.zhixingye.im.api.BasicApi;
import me.zhixingye.im.api.UserApi;
import me.zhixingye.im.database.UserDao;
import me.zhixingye.im.event.EventManager;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.event.OnEventListener;
import me.zhixingye.im.event.OnLoggedInEvent;
import me.zhixingye.im.event.OnLoggedOutEvent;
import me.zhixingye.im.event.RequestSaveUserProfileEvent;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class UserServiceImpl extends BasicServiceImpl implements UserService {

    private static final String TAG = "UserServiceImpl";


    private UserProfile mUserProfile;
    private String mToken;

    private final UserApi mUserApi = new UserApi();

    private final Map<String, UserProfile> mUserProfileCache;

    public UserServiceImpl() {
        mUserProfileCache = new ConcurrentHashMap<>();

        listenerOnLoggedInEvent();
        listenerOnLoggedOutEvent();
        listenerRequestSaveUserProfileEvent();
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

    @Override
    public void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback) {
        mUserApi.updateUserInfo(nickname, description, sex, birthday, location, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void getUserInfoByUserId(String userId, final RequestCallback<GetUserInfoResp> callback) {
        mUserApi.getUserInfoByUserId(userId, new RequestCallbackWrapper<GetUserInfoResp>(callback) {
            @Override
            public void onCompleted(GetUserInfoResp response) {
                saveUserProfileToCache(response.getProfile());
                super.onCompleted(response);

            }
        });
    }

    @Override
    public void queryUserInfoByTelephone(String telephone, final RequestCallback<QueryUserInfoResp> callback) {
        mUserApi.queryUserInfoByTelephone(telephone, new RequestCallbackWrapper<QueryUserInfoResp>(callback) {
            @Override
            public void onCompleted(QueryUserInfoResp response) {
                saveUserProfileToCache(response.getProfile());
                super.onCompleted(response);
            }
        });
    }

    @Override
    public void queryUserInfoByEmail(String email, final RequestCallback<QueryUserInfoResp> callback) {
        mUserApi.queryUserInfoByEmail(email, new RequestCallbackWrapper<QueryUserInfoResp>(callback) {
            @Override
            public void onCompleted(QueryUserInfoResp response) {
                saveUserProfileToCache(response.getProfile());
                super.onCompleted(response);
            }
        });
    }

    private void saveUserProfileToCache(UserProfile profile) {
        saveUserProfileToCache(profile, false);
    }

    private void saveUserProfileToCache(UserProfile profile, boolean isNeedSaveToLocal) {
        if (profile == null || TextUtils.isEmpty(profile.getUserId())) {
            return;
        }
        mUserProfileCache.put(profile.getUserId(), profile);
        if (isNeedSaveToLocal && !UserDao.saveUserProfile(profile)) {
            Logger.i(TAG, "saveUserProfileToCache saveLocal error");
        }
    }

    private void listenerOnLoggedInEvent() {
        EventManager.addOnEventListener(OnLoggedInEvent.class, new OnEventListener<OnLoggedInEvent>() {
            @Override
            public void onEvent(@NonNull OnLoggedInEvent eventData) {
                Logger.i(TAG, "listenerOnLoggedInEvent onEvent");
                LoginResp resp = eventData.requireEventData();
                mToken = resp.getToken();
                mUserProfile = resp.getProfile();
            }
        });
    }

    private void listenerOnLoggedOutEvent() {
        EventManager.addOnEventListener(OnLoggedOutEvent.class, new OnEventListener<OnLoggedOutEvent>() {
            @Override
            public void onEvent(@NonNull OnLoggedOutEvent eventData) {
                Logger.i(TAG, "listenerOnLoggedOutEvent onEvent");
                mToken = null;
                mUserProfile = null;
            }
        });
    }

    private void listenerRequestSaveUserProfileEvent() {
        EventManager.addOnEventListener(RequestSaveUserProfileEvent.class, new OnEventListener<RequestSaveUserProfileEvent>() {
            @Override
            public void onEvent(@NonNull RequestSaveUserProfileEvent eventData) {
                Logger.i(TAG, "listenerRequestSaveUserProfileEvent onEvent");
                saveUserProfileToCache(eventData.requireEventData(), eventData.isNeedSaveToLocal());
            }
        });
    }
}
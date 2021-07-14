package me.zhixingye.im.api;

import com.salty.protos.GetUserInfoReq;
import com.salty.protos.GetUserInfoResp;
import com.salty.protos.LoginReq;
import com.salty.protos.LoginResp;
import com.salty.protos.LogoutReq;
import com.salty.protos.LogoutResp;
import com.salty.protos.QueryUserInfoReq;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.RegisterReq;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordReq;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.UpdateUserInfoReq;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;
import com.salty.protos.UserServiceGrpc;

import java.util.concurrent.TimeUnit;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.util.Sha256Util;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class UserApi extends BasicApi {

    private static final String PASSWORD_SALTY = "PasswordSalty";

    public UserServiceGrpc.UserServiceStub newServiceStub() {
        return UserServiceGrpc.newStub(getManagedChannel())
                .withDeadlineAfter(30, TimeUnit.SECONDS);
    }

    public void registerByTelephone(String telephone, String password, RequestCallback<RegisterResp> callback) {
        UserProfile profile = UserProfile.newBuilder()
                .setTelephone(telephone)
                .build();

        RegisterReq req = RegisterReq.newBuilder()
                .setProfile(profile)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .build();

        newServiceStub().register(
                createReq(req),
                new InnerStreamObserver<>(RegisterResp.getDefaultInstance(), callback));
    }

    public void registerByEmail(String email, String password, RequestCallback<RegisterResp> callback) {
        UserProfile profile = UserProfile.newBuilder()
                .setEmail(email)
                .build();

        RegisterReq req = RegisterReq.newBuilder()
                .setProfile(profile)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .build();

        newServiceStub().register(
                createReq(req),
                new InnerStreamObserver<>(RegisterResp.getDefaultInstance(), callback));
    }

    public void loginByTelephone(String telephone, String password, RequestCallback<LoginResp> callback) {
        LoginReq req = LoginReq.newBuilder()
                .setTelephone(telephone)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .build();
        newServiceStub().login(
                createReq(req),
                new InnerStreamObserver<>(LoginResp.getDefaultInstance(), callback));
    }

    public void loginByEmail(String email, String password, RequestCallback<LoginResp> callback) {
        LoginReq req = LoginReq.newBuilder()
                .setEmail(email)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .build();

        newServiceStub().login(
                createReq(req),
                new InnerStreamObserver<>(LoginResp.getDefaultInstance(), callback));
    }

    public void logout(RequestCallback<LogoutResp> callback) {
        LogoutReq req = LogoutReq.newBuilder()
                .build();
        newServiceStub().login(
                createReq(req),
                new InnerStreamObserver<>(LogoutResp.getDefaultInstance(), callback));
    }

    public void resetLoginPasswordByTelephone(String telephone, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ResetPasswordReq req = ResetPasswordReq.newBuilder()
                .setTelephone(telephone)
                .setNewPassword(Sha256Util.sha256WithSalt(newPassword, PASSWORD_SALTY))
                .build();

        newServiceStub().resetPassword(
                createReq(req),
                new InnerStreamObserver<>(ResetPasswordResp.getDefaultInstance(), callback));
    }

    public void resetLoginPasswordByEmail(String email, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ResetPasswordReq req = ResetPasswordReq.newBuilder()
                .setEmail(email)
                .setNewPassword(Sha256Util.sha256WithSalt(newPassword, PASSWORD_SALTY))
                .build();

        newServiceStub().resetPassword(
                createReq(req),
                new InnerStreamObserver<>(ResetPasswordResp.getDefaultInstance(), callback));
    }

    public void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback) {
        UserProfile profile = UserProfile.newBuilder()
                .setNickname(nickname)
                .setDescription(description)
                .setSex(sex)
                .setBirthday(birthday)
                .setLocation(location)
                .build();

        UpdateUserInfoReq req = UpdateUserInfoReq.newBuilder()
                .setProfile(profile)
                .build();

        newServiceStub().updateUserInfo(
                createReq(req),
                new InnerStreamObserver<>(UpdateUserInfoResp.getDefaultInstance(), callback));
    }

    public void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback) {
        GetUserInfoReq req = GetUserInfoReq.newBuilder()
                .setUserId(userId)
                .build();

        newServiceStub().getUserInfo(
                createReq(req),
                new InnerStreamObserver<>(GetUserInfoResp.getDefaultInstance(), callback));
    }

    public void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback) {
        QueryUserInfoReq req = QueryUserInfoReq.newBuilder()
                .setTelephone(telephone)
                .build();

        newServiceStub().queryUserInfo(
                createReq(req),
                new InnerStreamObserver<>(QueryUserInfoResp.getDefaultInstance(), callback));
    }

    public void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback) {
        QueryUserInfoReq req = QueryUserInfoReq.newBuilder()
                .setEmail(email)
                .build();

        newServiceStub().queryUserInfo(
                createReq(req),
                new InnerStreamObserver<>(QueryUserInfoResp.getDefaultInstance(), callback));
    }

}
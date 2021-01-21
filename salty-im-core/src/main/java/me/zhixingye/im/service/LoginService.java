package me.zhixingye.im.service;

import com.salty.protos.UserProfile;

import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月21日.
 */
public interface LoginService extends BasicService {
    void loginByTelephone(String telephone, String password, RequestCallback<UserProfile> callback);

    void loginByEmail(String email, String password, RequestCallback<UserProfile> callback);

    void loginByLastLoginInfo(RequestCallback<UserProfile> callback);

    void logout();

    boolean isLogged();
}

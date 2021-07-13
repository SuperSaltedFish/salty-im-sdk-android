package me.zhixingye.im.service.impl;

import com.salty.protos.ResetPasswordResp;

import me.zhixingye.im.api.BasicApi;
import me.zhixingye.im.api.UserApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.PasswordService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月21日.
 */
public class PasswordServiceImpl extends BasicServiceImpl implements PasswordService {

    private static final String TAG = "PasswordServiceImpl";

    private final UserApi mUserApi;

    public PasswordServiceImpl() {
        mUserApi = BasicApi.getApi(UserApi.class);
    }

    @Override
    public void resetLoginPasswordByTelephone(String telephone, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mUserApi.resetLoginPasswordByTelephone(telephone, newPassword, new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void resetLoginPasswordByEmail(String telephone, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mUserApi.resetLoginPasswordByTelephone(telephone, newPassword, new RequestCallbackWrapper<>(callback));
    }
}

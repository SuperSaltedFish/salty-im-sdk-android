package me.zhixingye.im.service.impl;

import com.salty.protos.RegisterResp;

import me.zhixingye.im.api.BasicApi;
import me.zhixingye.im.api.UserApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.RegisterService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月21日.
 */
public class RegisterServiceImpl extends BasicServiceImpl implements RegisterService {

    private static final String TAG = "RegisterServiceImpl";

    private final UserApi mUserApi;

    public RegisterServiceImpl() {
        mUserApi = BasicApi.getApi(UserApi.class);
    }
    @Override
    public void registerByTelephone(String telephone, String password, RequestCallback<RegisterResp> callback) {
        mUserApi.registerByTelephone(telephone, password,  new RequestCallbackWrapper<>(callback));
    }

    @Override
    public void registerByEmail(String email, String password, RequestCallback<RegisterResp> callback) {
        mUserApi.registerByEmail(email, password,  new RequestCallbackWrapper<>(callback));
    }
}

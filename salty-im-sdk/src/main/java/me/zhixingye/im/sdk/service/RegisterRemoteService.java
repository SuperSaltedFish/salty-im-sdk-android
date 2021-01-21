package me.zhixingye.im.sdk.service;

import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IRegisterRemoteService;
import me.zhixingye.im.sdk.IRemoteCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月21日.
 */
public class RegisterRemoteService extends IRegisterRemoteService.Stub {

    @Override
    public void registerByTelephone(String telephone, String password, IRemoteCallback callback) {
        IMCore.get().getRegisterService()
                .registerByTelephone(
                        telephone,
                        password,
                        new ByteRemoteCallback<RegisterResp>(callback));
    }

    @Override
    public void registerByEmail(String email, String password, IRemoteCallback callback) {
        IMCore.get().getRegisterService()
                .registerByEmail(
                        email,
                        password,
                        new ByteRemoteCallback<RegisterResp>(callback));
    }
}

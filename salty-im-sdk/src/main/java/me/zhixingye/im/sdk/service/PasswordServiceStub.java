package me.zhixingye.im.sdk.service;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IPasswordRemoteService;
import me.zhixingye.im.sdk.IRemoteRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月21日.
 */
public class PasswordServiceStub extends IPasswordRemoteService.Stub {
    @Override
    public void resetLoginPasswordByEmail(String email, String newPassword, IRemoteRequestCallback callback) {
        IMCore.get().getPasswordService()
                .resetLoginPasswordByEmail(
                        email,
                        newPassword,
                        new ByteRemoteCallback<>(callback));
    }

    @Override
    public void resetLoginPasswordByTelephone(String telephone, String newPassword, IRemoteRequestCallback callback) {
        IMCore.get().getPasswordService()
                .resetLoginPasswordByTelephone(
                        telephone,
                        newPassword,
                        new ByteRemoteCallback<>(callback));
    }
}

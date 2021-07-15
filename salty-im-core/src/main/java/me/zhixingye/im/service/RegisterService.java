package me.zhixingye.im.service;

import com.salty.protos.RegisterResp;

import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月21日.
 */
public interface RegisterService extends BasicService {
    void registerByTelephone(String telephone, String password, RequestCallback<RegisterResp> callback);

    void registerByEmail(String email, String password, RequestCallback<RegisterResp> callback);
}

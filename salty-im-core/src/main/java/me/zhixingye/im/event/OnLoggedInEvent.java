package me.zhixingye.im.event;

import com.salty.protos.LoginResp;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月15日.
 */
public class OnLoggedInEvent extends BasicEvent<LoginResp> {
    public OnLoggedInEvent(LoginResp eventData) {
        super(eventData);
    }

}

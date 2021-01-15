package me.zhixingye.im.service.event;

import com.salty.protos.UserProfile;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月15日.
 */
public class RequestSaveUserProfileEvent extends BasicEvent<UserProfile> {

    private final boolean isNeedSaveToLocal;

    public RequestSaveUserProfileEvent(UserProfile profile) {
        this(profile, false);
    }

    public RequestSaveUserProfileEvent(UserProfile profile, boolean isNeedSaveToLocal) {
        super(profile);
        this.isNeedSaveToLocal = isNeedSaveToLocal;
    }

    public boolean isNeedSaveToLocal() {
        return isNeedSaveToLocal;
    }
}

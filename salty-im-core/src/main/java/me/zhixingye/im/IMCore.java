package me.zhixingye.im;

import android.content.Context;

import me.zhixingye.im.service.ContactService;
import me.zhixingye.im.service.ConversationService;
import me.zhixingye.im.service.DeviceService;
import me.zhixingye.im.service.GroupService;
import me.zhixingye.im.service.LoginService;
import me.zhixingye.im.service.MessageService;
import me.zhixingye.im.service.PasswordService;
import me.zhixingye.im.service.RegisterService;
import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.service.impl.ContactServiceImpl;
import me.zhixingye.im.service.impl.ConversationServiceImpl;
import me.zhixingye.im.service.impl.DeviceServiceImpl;
import me.zhixingye.im.service.impl.GroupServiceImpl;
import me.zhixingye.im.service.impl.LoginServiceImpl;
import me.zhixingye.im.service.impl.MessageServiceImpl;
import me.zhixingye.im.service.impl.PasswordServiceImpl;
import me.zhixingye.im.service.impl.RegisterServiceImpl;
import me.zhixingye.im.service.impl.SMSServiceImpl;
import me.zhixingye.im.service.impl.StorageServiceImpl;
import me.zhixingye.im.service.impl.UserServiceImpl;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class IMCore {

    private static final String TAG = "IMCore";

    private volatile static IMCore sClient;
    private static Context mAppContext;

    public synchronized static void tryInit(Context context) {
        if (sClient != null) {
            throw new RuntimeException("IMCore 已经初始化");
        }
        if (context == null) {
            throw new RuntimeException("context == null");
        }
        mAppContext = context;
        sClient = new IMCore();
    }

    public static IMCore get() {
        if (sClient == null) {
            throw new RuntimeException("IMCore 未初始化");
        }
        return sClient;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    private final LoginService mLoginService = new LoginServiceImpl();
    private final RegisterService mRegisterService = new RegisterServiceImpl();
    private final ContactService mContactService = new ContactServiceImpl();
    private final ConversationService mConversationService = new ConversationServiceImpl();
    private final GroupService mGroupService = new GroupServiceImpl();
    private final MessageService mMessageService = new MessageServiceImpl();
    private final SMSService mSMSService = new SMSServiceImpl();
    private final StorageService mStorageService = new StorageServiceImpl();
    private final UserService mUserService = new UserServiceImpl();
    private final PasswordService mPasswordService = new PasswordServiceImpl();
    private final DeviceService mDeviceService = new DeviceServiceImpl();

    private IMCore() {

    }

    public LoginService getLoginService() {
        return mLoginService;
    }

    public RegisterService getRegisterService() {
        return mRegisterService;
    }

    public ContactService getContactService() {
        return mContactService;
    }

    public ConversationService getConversationService() {
        return mConversationService;
    }

    public GroupService getGroupService() {
        return mGroupService;
    }

    public MessageService getMessageService() {
        return mMessageService;
    }

    public SMSService getSMSService() {
        return mSMSService;
    }

    public StorageService getStorageService() {
        return mStorageService;
    }

    public UserService getUserService() {
        return mUserService;
    }

    public PasswordService getPasswordService() {
        return mPasswordService;
    }

    public DeviceService getDeviceService() {
        return mDeviceService;
    }
}

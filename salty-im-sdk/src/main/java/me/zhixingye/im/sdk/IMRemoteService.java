package me.zhixingye.im.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.service.ContactServiceStub;
import me.zhixingye.im.sdk.service.ConversationServiceStub;
import me.zhixingye.im.sdk.service.GroupServiceStub;
import me.zhixingye.im.sdk.service.LoginServiceStub;
import me.zhixingye.im.sdk.service.MessageServiceStub;
import me.zhixingye.im.sdk.service.PasswordServiceStub;
import me.zhixingye.im.sdk.service.RegisterServiceStub;
import me.zhixingye.im.sdk.service.SMSServiceStub;
import me.zhixingye.im.sdk.service.StorageServiceStub;
import me.zhixingye.im.sdk.service.UserServiceStub;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class IMRemoteService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        IMCore.tryInit(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIMServiceBinder;
    }

    private final IBinder mIMServiceBinder = new IRemoteService.Stub() {

        @Override
        public ILoginRemoteService getLoginRemoteService() {
            return new LoginServiceStub();
        }

        @Override
        public IRegisterRemoteService getRegisterRemoteService() {
            return new RegisterServiceStub();
        }

        @Override
        public IContactRemoteService getContactRemoteService() {
            return new ContactServiceStub();
        }

        @Override
        public IConversationRemoteService getConversationRemoteService() {
            return new ConversationServiceStub();
        }

        @Override
        public IGroupRemoteService getGroupRemoteService() {
            return new GroupServiceStub();
        }

        @Override
        public IMessageRemoteService getMessageRemoteService() {
            return new MessageServiceStub();
        }

        @Override
        public ISMSRemoteService getSMSRemoteService() {
            return new SMSServiceStub();
        }

        @Override
        public IStorageRemoteService getStorageRemoteService() {
            return new StorageServiceStub();
        }

        @Override
        public IUserRemoteService getUserRemoteService() {
            return new UserServiceStub();
        }

        @Override
        public IPasswordRemoteService getPasswordRemoteService() {
            return new PasswordServiceStub();
        }
    };
}

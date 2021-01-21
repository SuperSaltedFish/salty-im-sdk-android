package me.zhixingye.im.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.service.ContactRemoteService;
import me.zhixingye.im.sdk.service.ConversationRemoteService;
import me.zhixingye.im.sdk.service.GroupRemoteService;
import me.zhixingye.im.sdk.service.LoginRemoteService;
import me.zhixingye.im.sdk.service.MessageRemoteService;
import me.zhixingye.im.sdk.service.PasswordRemoteService;
import me.zhixingye.im.sdk.service.RegisterRemoteService;
import me.zhixingye.im.sdk.service.SMSRemoteService;
import me.zhixingye.im.sdk.service.StorageRemoteService;
import me.zhixingye.im.sdk.service.UserRemoteService;

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
            return new LoginRemoteService();
        }

        @Override
        public IRegisterRemoteService getRegisterRemoteService() {
            return new RegisterRemoteService();
        }

        @Override
        public IContactRemoteService getContactRemoteService() {
            return new ContactRemoteService();
        }

        @Override
        public IConversationRemoteService getConversationRemoteService() {
            return new ConversationRemoteService();
        }

        @Override
        public IGroupRemoteService getGroupRemoteService() {
            return new GroupRemoteService();
        }

        @Override
        public IMessageRemoteService getMessageRemoteService() {
            return new MessageRemoteService();
        }

        @Override
        public ISMSRemoteService getSMSRemoteService() {
            return new SMSRemoteService();
        }

        @Override
        public IStorageRemoteService getStorageRemoteService() {
            return new StorageRemoteService();
        }

        @Override
        public IUserRemoteService getUserRemoteService() {
            return new UserRemoteService();
        }

        @Override
        public IPasswordRemoteService getPasswordRemoteService() {
            return new PasswordRemoteService();
        }
    };
}

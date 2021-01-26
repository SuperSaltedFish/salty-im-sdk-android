package me.zhixingye.im.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import java.util.concurrent.Executor;

import me.zhixingye.im.sdk.proxy.LoginServiceProxy;
import me.zhixingye.im.sdk.proxy.PasswordServiceProxy;
import me.zhixingye.im.sdk.proxy.ProxyHelper;
import me.zhixingye.im.sdk.proxy.RegisterServiceProxy;
import me.zhixingye.im.sdk.proxy.SMSServiceProxy;
import me.zhixingye.im.service.ContactService;
import me.zhixingye.im.service.ConversationService;
import me.zhixingye.im.service.GroupService;
import me.zhixingye.im.service.LoginService;
import me.zhixingye.im.service.MessageService;
import me.zhixingye.im.service.PasswordService;
import me.zhixingye.im.service.RegisterService;
import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.sdk.proxy.RemoteProxy;
import me.zhixingye.im.sdk.proxy.ContactServiceProxy;
import me.zhixingye.im.sdk.proxy.ConversationServiceProxy;
import me.zhixingye.im.sdk.proxy.GroupServiceProxy;
import me.zhixingye.im.sdk.proxy.MessageServiceProxy;
import me.zhixingye.im.sdk.proxy.StorageServiceProxy;
import me.zhixingye.im.sdk.proxy.UserServiceProxy;
import me.zhixingye.im.sdk.util.SystemUtils;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class IMClient {

    private static final String TAG = "IMClient";

    private static IMClient sIMClient;

    synchronized static void init(Context context) {
        String currentProcess = SystemUtils.getCurrentProcessName(context);
        String mainProcess = context.getPackageName();
        if (TextUtils.equals(currentProcess, mainProcess)) {
            if (sIMClient != null) {
                throw new RuntimeException("IMClient 已经初始化");
            }
            context = context.getApplicationContext();
            sIMClient = new IMClient(context);
        }
    }

    public static IMClient get() {
        if (sIMClient == null) {
            throw new RuntimeException("IMClient 未初始化");
        }
        return sIMClient;
    }

    private LoginServiceProxy mLoginServiceProxy;
    private RegisterServiceProxy mRegisterServiceProxy;
    private ContactServiceProxy mContactServiceProxy;
    private ConversationServiceProxy mConversationServiceProxy;
    private GroupServiceProxy mGroupServiceProxy;
    private MessageServiceProxy mMessageServiceProxy;
    private SMSServiceProxy mSMSServiceProxy;
    private StorageServiceProxy mStorageServiceProxy;
    private UserServiceProxy mUserServiceProxy;
    private PasswordServiceProxy mPasswordServiceProxy;

    private IRemoteService mIRemoteService;

    private Handler mInitWorkerHandler;

    private IMClient(Context context) {
        initProxyService();
        initWorkerHandler();
        autoBindRemoteService(context.getApplicationContext());
    }

    private void initProxyService() {
        mLoginServiceProxy = ProxyHelper.createProxy(new LoginServiceProxy());
        mRegisterServiceProxy = ProxyHelper.createProxy(new RegisterServiceProxy());
        mContactServiceProxy = ProxyHelper.createProxy(new ContactServiceProxy());
        mConversationServiceProxy = ProxyHelper.createProxy(new ConversationServiceProxy());
        mGroupServiceProxy = ProxyHelper.createProxy(new GroupServiceProxy());
        mMessageServiceProxy = ProxyHelper.createProxy(new MessageServiceProxy());
        mSMSServiceProxy = ProxyHelper.createProxy(new SMSServiceProxy());
        mStorageServiceProxy = ProxyHelper.createProxy(new StorageServiceProxy());
        mUserServiceProxy = ProxyHelper.createProxy(new UserServiceProxy());
        mPasswordServiceProxy = ProxyHelper.createProxy(new PasswordServiceProxy());
    }

    private void initWorkerHandler() {
        HandlerThread thread = new HandlerThread(TAG);
        thread.start();
        mInitWorkerHandler = new Handler(thread.getLooper());
    }

    private void autoBindRemoteService(Context context) {
        context.bindService(
                new Intent(context, IMRemoteService.class),
                Context.BIND_AUTO_CREATE,
                new Executor() {
                    @Override
                    public void execute(Runnable command) {
                        mInitWorkerHandler.post(command);
                    }
                },
                new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        Logger.i(TAG, "onServiceConnected");
                        bindRemote(service);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        Logger.w(TAG, "onServiceDisconnected");
                        unbindRemote();
                    }

                    @Override
                    public void onBindingDied(ComponentName name) {
                        Logger.w(TAG, "onBindingDied");
                        onServiceDisconnected(name);
                    }

                });
    }


    private void bindRemote(IBinder service) {
        mIRemoteService = IRemoteService.Stub.asInterface(service);
        try {
            mLoginServiceProxy.onBind(mIRemoteService);
            mRegisterServiceProxy.onBind(mIRemoteService);
            mContactServiceProxy.onBind(mIRemoteService);
            mConversationServiceProxy.onBind(mIRemoteService);
            mGroupServiceProxy.onBind(mIRemoteService);
            mMessageServiceProxy.onBind(mIRemoteService);
            mSMSServiceProxy.onBind(mIRemoteService);
            mStorageServiceProxy.onBind(mIRemoteService);
            mUserServiceProxy.onBind(mIRemoteService);
            mPasswordServiceProxy.onBind(mIRemoteService);
        } catch (RemoteException e) {
            e.printStackTrace();
            unbindRemote();
        }
    }

    private void unbindRemote() {
        mLoginServiceProxy.onUnbind();
        mRegisterServiceProxy.onUnbind();
        mContactServiceProxy.onUnbind();
        mConversationServiceProxy.onUnbind();
        mGroupServiceProxy.onUnbind();
        mMessageServiceProxy.onUnbind();
        mSMSServiceProxy.onUnbind();
        mStorageServiceProxy.onUnbind();
        mUserServiceProxy.onUnbind();
        mPasswordServiceProxy.onUnbind();
        mIRemoteService = null;
    }


    public LoginService getLoginService() {
        return mLoginServiceProxy;
    }

    public RegisterService getRegisterService() {
        return mRegisterServiceProxy;
    }

    public ContactService getContactService() {
        return mContactServiceProxy;
    }

    public ConversationService getConversationService() {
        return mConversationServiceProxy;
    }

    public GroupService getGroupService() {
        return mGroupServiceProxy;
    }

    public MessageService getMessageService() {
        return mMessageServiceProxy;
    }

    public SMSService getSMSService() {
        return mSMSServiceProxy;
    }

    public StorageService getStorageService() {
        return mStorageServiceProxy;
    }

    public UserService getUserService() {
        return mUserServiceProxy;
    }

    public PasswordService getPasswordService() {
        return mPasswordServiceProxy;
    }
}

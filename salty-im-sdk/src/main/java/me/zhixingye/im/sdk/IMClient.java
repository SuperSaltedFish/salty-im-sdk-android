package me.zhixingye.im.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

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
import me.zhixingye.im.service.impl.ServiceAccessor;
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

    private LoginService mLoginServiceProxy;
    private RegisterService mRegisterServiceProxy;
    private ContactService mContactServiceProxy;
    private ConversationService mConversationServiceProxy;
    private GroupService mGroupServiceProxy;
    private MessageService mMessageServiceProxy;
    private SMSService mSMSServiceProxy;
    private StorageService mStorageServiceProxy;
    private UserService mUserServiceProxy;
    private PasswordService mPasswordServiceProxy;

    private IRemoteService mIRemoteService;

    private IMClient(Context context) {
        initProxyService();
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

    private void autoBindRemoteService(Context context) {
        context.bindService(
                new Intent(context, IMRemoteService.class),
                new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        Logger.i(TAG, "onServiceConnected");
                        mIRemoteService = IRemoteService.Stub.asInterface(service);
                        ((RemoteProxy) mLoginServiceProxy).onBindHandle(mIRemoteService);
                        ((RemoteProxy) mRegisterServiceProxy).onBindHandle(mIRemoteService);
                        ((RemoteProxy) mContactServiceProxy).onBindHandle(mIRemoteService);
                        ((RemoteProxy) mConversationServiceProxy).onBindHandle(mIRemoteService);
                        ((RemoteProxy) mGroupServiceProxy).onBindHandle(mIRemoteService);
                        ((RemoteProxy) mMessageServiceProxy).onBindHandle(mIRemoteService);
                        ((RemoteProxy) mSMSServiceProxy).onBindHandle(mIRemoteService);
                        ((RemoteProxy) mStorageServiceProxy).onBindHandle(mIRemoteService);
                        ((RemoteProxy) mUserServiceProxy).onBindHandle(mIRemoteService);
                        ((RemoteProxy) mPasswordServiceProxy).onBindHandle(mIRemoteService);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        Logger.w(TAG, "onServiceDisconnected");
                        mIRemoteService = null;
                    }

                    @Override
                    public void onBindingDied(ComponentName name) {
                        Logger.w(TAG, "onBindingDied");
                        onServiceDisconnected(name);
                    }

                }, Context.BIND_AUTO_CREATE);
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

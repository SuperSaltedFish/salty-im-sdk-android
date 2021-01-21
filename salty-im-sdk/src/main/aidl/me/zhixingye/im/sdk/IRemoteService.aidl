// IRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.ILoginRemoteService;
import me.zhixingye.im.sdk.IRegisterRemoteService;
import me.zhixingye.im.sdk.IContactRemoteService;
import me.zhixingye.im.sdk.IConversationRemoteService;
import me.zhixingye.im.sdk.IGroupRemoteService;
import me.zhixingye.im.sdk.IMessageRemoteService;
import me.zhixingye.im.sdk.ISMSRemoteService;
import me.zhixingye.im.sdk.IStorageRemoteService;
import me.zhixingye.im.sdk.IUserRemoteService;
import me.zhixingye.im.sdk.IPasswordRemoteService;
import me.zhixingye.im.sdk.IRemoteCallback;

interface IRemoteService {
    ILoginRemoteService getLoginRemoteService();

    IRegisterRemoteService getRegisterRemoteService();

    IContactRemoteService getContactRemoteService();

    IConversationRemoteService getConversationRemoteService();

    IGroupRemoteService getGroupRemoteService();

    IMessageRemoteService getMessageRemoteService();

    ISMSRemoteService getSMSRemoteService();

    IStorageRemoteService getStorageRemoteService();

    IUserRemoteService getUserRemoteService();

    IPasswordRemoteService getPasswordRemoteService();
}

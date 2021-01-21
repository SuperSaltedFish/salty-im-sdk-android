// IRemoteEventCallback.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements

interface IRemoteEventCallback {
    void onCompleted(int eventCode, in byte[] eventData);
}
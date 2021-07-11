// IOnLoginListener.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements

interface IOnContactChangeListener {
     void onContactChange(in byte[] protoData, int changeType);
}
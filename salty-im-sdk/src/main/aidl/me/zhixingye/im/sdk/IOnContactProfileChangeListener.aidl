// IOnLoginListener.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements

interface IOnContactProfileChangeListener {
     void onContactProfileChange(in byte[] protoData);
}
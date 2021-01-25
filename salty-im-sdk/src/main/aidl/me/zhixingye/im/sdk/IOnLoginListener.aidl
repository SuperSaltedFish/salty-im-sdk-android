// IOnLoginListener.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements

interface IOnLoginListener {
     void onLoggedOut();

     void onLoggedIn();

     void onLoginExpired();
}
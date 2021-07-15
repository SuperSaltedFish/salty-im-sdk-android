package me.zhixingye.im.sdk.proxy;

import java.lang.reflect.Method;

import me.zhixingye.im.constant.ClientErrorCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteRequestCallback;
import me.zhixingye.im.tool.CallbackHelper;
import me.zhixingye.im.util.ReflectUtil;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class RemoteCallbackWrapper<T> extends IRemoteRequestCallback.Stub {

    private RequestCallback<T> mCallback;

    public RemoteCallbackWrapper(RequestCallback<T> callback) {
        mCallback = callback;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCompleted(byte[] protoData) {
        if (mCallback == null) {
            return;
        }

        Class<T> genericCls = (Class<T>) ReflectUtil.findGenericClass(mCallback.getClass(), RequestCallback.class, 0);
        if (genericCls == null) {
            onFailure(ClientErrorCode.INTERNAL_UNKNOWN.getCode(), "genericCls == null");
            return;
        }

        if (genericCls == Void.class) {
            callCompleted(null);
            return;
        }

        try {
            Method method = genericCls.getMethod("parseFrom", byte[].class);
            T data = (T) method.invoke(null, (Object) protoData);
            if (data == null) {
                onFailure(ClientErrorCode.INTERNAL_UNKNOWN.getCode(), "data == null");
            } else {
                callCompleted(data);
            }
        } catch (Exception e) {
            onFailure(ClientErrorCode.INTERNAL_UNKNOWN.getCode(), e.toString());
        }
    }


    @Override
    public void onFailure(int code, String error) {
        callFailure(code, error);
    }

    private void callCompleted(T data) {
        CallbackHelper.callCompleted(data, mCallback);
        mCallback = null;
    }

    private void callFailure(int code, String error) {
        CallbackHelper.callFailure(code, error, mCallback);
        mCallback = null;
    }


}

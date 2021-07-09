package me.zhixingye.im.sdk.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteRequestCallback;
import me.zhixingye.im.sdk.tool.HandlerFactory;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class RemoteCallbackWrapper<T> extends IRemoteRequestCallback.Stub {

    private static final String TAG = "ResultCallbackWrapper";

    private final RequestCallback<T> mCallback;

    public RemoteCallbackWrapper(RequestCallback<T> callback) {
        mCallback = callback;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCompleted(byte[] protoData) {
        if (mCallback == null) {
            Logger.i(TAG, "mCallback==null");
            return;
        }

        ParameterizedType pType = (ParameterizedType) mCallback.getClass().getGenericSuperclass();
        if (pType == null) {
            Logger.e(TAG, "pType == null");
            callUnknownError();
            return;
        }

        try {
            Class<?> type = (Class<?>) pType.getActualTypeArguments()[0];
            if (type == Void.class) {
                mCallback.onCompleted(null);
                return;
            }
            Method method = type.getMethod("parseFrom", byte[].class);
            final T resultMessage = (T) method.invoke(null, (Object) protoData);
            if (resultMessage == null) {
                Logger.e(TAG, "resultMessage == null");
                callUnknownError();
            } else {
                HandlerFactory.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onCompleted(resultMessage);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            callUnknownError();
        }
    }

    private void callUnknownError() {
        onFailure(ResponseCode.INTERNAL_UNKNOWN.getCode(), ResponseCode.INTERNAL_UNKNOWN.getMsg());
    }

    @Override
    public void onFailure(final int code, final String error) {
        if (mCallback == null) {
            return;
        }
        HandlerFactory.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mCallback.onFailure(code, error);
            }
        });
    }


}

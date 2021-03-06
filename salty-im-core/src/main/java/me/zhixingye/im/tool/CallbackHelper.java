package me.zhixingye.im.tool;

import com.salty.protos.StatusCode;

import me.zhixingye.im.constant.ClientErrorCode;
import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class CallbackHelper {

    public static <T> void callCompleted(final T response, final RequestCallback<T> callback) {
        if (callback != null) {
            HandlerHelper.getUIHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onCompleted(response);
                }
            });
        }
    }

    public static void callFailure(final int code, final String error, final RequestCallback<?> callback) {
        if (callback != null) {
            HandlerHelper.getUIHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailure(code, error);
                }
            });
        }
    }

    public static void callFailure(final StatusCode code, String error, final RequestCallback<?> callback) {
        callFailure(code.getNumber(), error, callback);
    }


    public static void callFailure(final ClientErrorCode code, final RequestCallback<?> callback) {
        callFailure(code.getCode(), code.getMsg(), callback);
    }
}

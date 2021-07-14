package me.zhixingye.im.service.impl;


import androidx.annotation.CallSuper;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.tool.CallbackHelper;
import me.zhixingye.im.tool.ExecutorHelper;
import me.zhixingye.im.tool.HandlerHelper;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月15日.
 */
public class BasicServiceImpl {

    public BasicServiceImpl() {
    }


    protected void runOnUIThread(Runnable runnable) {
        HandlerHelper.runOnUIThread(runnable);
    }

    protected void runOnWorkThread(Runnable runnable) {
        ExecutorHelper.getsWorkExecutor().execute(runnable);
    }


    protected static class RequestCallbackWrapper<Resp> implements RequestCallback<Resp> {

        private final RequestCallback<Resp> mCallback;

        public RequestCallbackWrapper(RequestCallback<Resp> mCallback) {
            this.mCallback = mCallback;
        }

        @CallSuper
        @Override
        public void onCompleted(Resp response) {
            CallbackHelper.callCompleted(response, mCallback);
        }

        @CallSuper
        @Override
        public void onFailure(int code, String error) {
            CallbackHelper.callFailure(code, error, mCallback);
        }
    }
}

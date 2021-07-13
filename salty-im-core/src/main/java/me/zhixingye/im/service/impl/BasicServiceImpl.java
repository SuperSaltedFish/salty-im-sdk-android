package me.zhixingye.im.service.impl;

import android.util.ArraySet;

import androidx.annotation.CallSuper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.event.BasicEvent;
import me.zhixingye.im.service.event.OnEventListener;
import me.zhixingye.im.tool.CallbackHelper;
import me.zhixingye.im.tool.ExecutorHelper;
import me.zhixingye.im.tool.HandlerHelper;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月15日.
 */
public class BasicServiceImpl {

    private static final Map<Class<?>, Set<OnEventListener<BasicEvent<?>>>> EVENT_LISTENER_MAP = new HashMap<>();

    protected  <T extends BasicEvent<?>> void sendEvent(final T event) {
        if (event == null) {
            return;
        }
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                synchronized (EVENT_LISTENER_MAP) {
                    Set<OnEventListener<BasicEvent<?>>> eventListeners = EVENT_LISTENER_MAP.get(event.getClass());
                    if (eventListeners == null) {
                        return;
                    }
                    for (OnEventListener<BasicEvent<?>> listener : eventListeners) {
                        listener.onEvent(event);
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    protected <T extends BasicEvent<?>> void addOnEventListener(Class<T> eventCls, OnEventListener<T> listener) {
        if (eventCls == null || listener == null) {
            return;
        }
        synchronized (EVENT_LISTENER_MAP) {
            Set<OnEventListener<BasicEvent<?>>> eventListenerSet = EVENT_LISTENER_MAP.get(eventCls);
            if (eventListenerSet == null) {
                eventListenerSet = new ArraySet<>();
                EVENT_LISTENER_MAP.put(eventCls, eventListenerSet);
            }
            eventListenerSet.add((OnEventListener<BasicEvent<?>>) listener);
        }
    }

    protected <T extends BasicEvent<?>> void removeOnEventListener(Class<T> eventCls, OnEventListener<T> listener) {
        if (eventCls == null || listener == null) {
            return;
        }
        synchronized (EVENT_LISTENER_MAP) {
            Set<OnEventListener<BasicEvent<?>>> eventListenerSet = EVENT_LISTENER_MAP.get(eventCls);
            if (eventListenerSet != null) {
                eventListenerSet.remove(listener);
            }
        }
    }

    protected void runOnUIThread(Runnable runnable) {
        HandlerHelper.getUIHandler().post(runnable);
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
            handlerErrorCodeIfNeed(code);
        }

        private void handlerErrorCodeIfNeed(int code) {

        }
    }
}

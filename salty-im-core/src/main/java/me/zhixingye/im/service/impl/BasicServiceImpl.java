package me.zhixingye.im.service.impl;

import android.os.Handler;
import android.os.Looper;
import android.util.ArraySet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import me.zhixingye.im.service.event.BasicEvent;
import me.zhixingye.im.service.event.OnEventListener;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月15日.
 */
public class BasicServiceImpl {

    private final Handler mUIHandler = new Handler(Looper.getMainLooper());

    private final Map<Class<?>, Set<OnEventListener<BasicEvent<?>>>> mListenerMap = new HashMap<>();

    private final Executor mWorkExecutor = new ThreadPoolExecutor(
            2,
            Runtime.getRuntime().availableProcessors() + 1,
            30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    public <T extends BasicEvent<?>> void sendEvent(final T event, boolean isRunOnUIThread) {
        if (event == null) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (mListenerMap) {
                    Set<OnEventListener<BasicEvent<?>>> eventListeners = mListenerMap.get(event.getClass());
                    if (eventListeners == null) {
                        return;
                    }
                    for (OnEventListener<BasicEvent<?>> listener : eventListeners) {
                        listener.onEvent(event);
                    }
                }
            }
        };
        if (isRunOnUIThread) {
            runOnUIThread(runnable);
        } else {
            runnable.run();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends BasicEvent<?>> void addOnEventListener(Class<T> eventCls, OnEventListener<T> listener) {
        if (eventCls == null || listener == null) {
            return;
        }
        synchronized (mListenerMap) {
            Set<OnEventListener<BasicEvent<?>>> eventListenerSet = mListenerMap.get(eventCls);
            if (eventListenerSet == null) {
                eventListenerSet = new ArraySet<>();
                mListenerMap.put(eventCls, eventListenerSet);
            }
            eventListenerSet.add((OnEventListener<BasicEvent<?>>) listener);
        }
    }

    public <T extends BasicEvent<?>> void removeOnEventListener(Class<T> eventCls, OnEventListener<T> listener) {
        if (eventCls == null || listener == null) {
            return;
        }
        synchronized (mListenerMap) {
            Set<OnEventListener<BasicEvent<?>>> eventListenerSet = mListenerMap.get(eventCls);
            if (eventListenerSet != null) {
                eventListenerSet.remove(listener);
            }
        }
    }

    public void runOnUIThread(Runnable runnable) {
        mUIHandler.post(runnable);
    }

    public void runOnWorkThread(Runnable runnable) {
        mWorkExecutor.execute(runnable);
    }


}

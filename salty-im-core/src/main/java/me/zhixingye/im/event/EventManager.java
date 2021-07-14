package me.zhixingye.im.event;

import android.util.ArraySet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.zhixingye.im.tool.HandlerHelper;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月15日.
 */
public class EventManager {
    private static final Map<Class<?>, Set<OnEventListener<BasicEvent<?>>>> EVENT_LISTENER_MAP = new HashMap<>();

    public static <T extends BasicEvent<?>> void sendEvent(final T event) {
        if (event == null) {
            return;
        }
        HandlerHelper.runOnUIThread(new Runnable() {
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
    public static <T extends BasicEvent<?>> void addOnEventListener(Class<T> eventCls, OnEventListener<T> listener) {
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

    public static <T extends BasicEvent<?>> void removeOnEventListener(Class<T> eventCls, OnEventListener<T> listener) {
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
}

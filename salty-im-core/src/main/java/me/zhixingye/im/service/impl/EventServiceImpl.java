package me.zhixingye.im.service.impl;

import android.util.ArraySet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.zhixingye.im.service.EventService;
import me.zhixingye.im.service.event.BasicEvent;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月14日.
 */

public class EventServiceImpl implements EventService {

    private final Map<Class<?>, Set<OnEventListener<BasicEvent<?>>>> mListenerMap = new HashMap<>();

    @Override
    public <T extends BasicEvent<?>> void sendEvent(T event) {
        if (event == null) {
            return;
        }
        Set<OnEventListener<BasicEvent<?>>> eventListeners = mListenerMap.get(event.getClass());
        if (eventListeners == null) {
            return;
        }
        for (OnEventListener<BasicEvent<?>> listener : eventListeners) {
            listener.onEvent(event);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BasicEvent<?>> void addOnEventListener(Class<T> eventCls, OnEventListener<T> listener) {
        if (eventCls == null || listener == null) {
            return;
        }
        Set<OnEventListener<BasicEvent<?>>> eventListenerSet = mListenerMap.get(eventCls);
        if (eventListenerSet == null) {
            eventListenerSet = new ArraySet<>();
            mListenerMap.put(eventCls, eventListenerSet);
        }
        eventListenerSet.add((OnEventListener<BasicEvent<?>>) listener);
    }

    @Override
    public <T extends BasicEvent<?>> void removeOnEventListener(Class<T> eventCls, OnEventListener<T> listener) {
        if (eventCls == null || listener == null) {
            return;
        }
        Set<OnEventListener<BasicEvent<?>>> eventListenerSet = mListenerMap.get(eventCls);
        if (eventListenerSet != null) {
            eventListenerSet.remove(listener);
        }
    }
}

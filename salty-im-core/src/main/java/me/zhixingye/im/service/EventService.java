package me.zhixingye.im.service;

import androidx.annotation.NonNull;

import me.zhixingye.im.service.event.BasicEvent;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月14日.
 */
public interface EventService extends BasicService {

    <T extends BasicEvent<?>> void sendEvent(T event);

    <T extends BasicEvent<?>> void addOnEventListener(Class<T> eventCls, OnEventListener<T> listener);

    <T extends BasicEvent<?>> void removeOnEventListener(Class<T> eventCls, OnEventListener<T> listener);

    interface OnEventListener<T extends BasicEvent<?>> {
        void onEvent(@NonNull T eventData);
    }
}

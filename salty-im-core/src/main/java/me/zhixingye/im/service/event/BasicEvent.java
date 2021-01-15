package me.zhixingye.im.service.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月15日.
 */
public class BasicEvent<T> {
    private int code;
    private T eventData;

    public BasicEvent() {
    }

    public BasicEvent(T eventData) {
        this.eventData = eventData;
    }

    public BasicEvent(int code, T eventData) {
        this.code = code;
        this.eventData = eventData;
        Fragment S;
    }

    @NonNull
    public T requireEventData() {
        if (eventData == null) {
            throw new NullPointerException("eventData == null");
        }
        return eventData;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    public T getEventData() {
        return eventData;
    }
}

package me.zhixingye.im.sdk;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年10月23日.
 */
public class IMClientInitializer implements Initializer<IMClient> {

    @NonNull
    @Override
    public IMClient create(@NonNull Context context) {
        IMClient.init(context.getApplicationContext());
        return IMClient.get();
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}

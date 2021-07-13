package me.zhixingye.im.sdk.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import me.zhixingye.im.service.BasicService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月13日.
 */
public class ProxyUtil {

    @SuppressWarnings("unchecked")
    public static <T extends BasicService> T createNotNullStringProxy(final T instance) {
        return (T) Proxy.newProxyInstance(
                instance.getClass().getClassLoader(),
                instance.getClass().getInterfaces(),
                new NotNullStringHandler(instance));
    }

    private static class NotNullStringHandler implements InvocationHandler {

        private final Object mOriginal;

        NotNullStringHandler(Object original) {
            mOriginal = original;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (args != null && args.length != 0) {
                Class<?>[] paramsClass = method.getParameterTypes();
                for (int i = 0, length = paramsClass.length; i < length; i++) {
                    if (paramsClass[i] == String.class && args[i] == null) {
                        args[i] = "";
                    }
                }
            }
            return method.invoke(mOriginal, args);
        }
    }
}

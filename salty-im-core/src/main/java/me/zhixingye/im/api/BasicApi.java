package me.zhixingye.im.api;

import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.android.AndroidChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

import me.zhixingye.im.BuildConfig;
import me.zhixingye.im.IMCore;
import me.zhixingye.im.exception.ClientInternalException;
import me.zhixingye.im.exception.ResponseException;
import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public abstract class BasicApi {

    private static final String TAG = "BasicApi";

    private static ManagedChannel sManagedChannel;

    protected static ManagedChannel getManagedChannel() {
        if (sManagedChannel == null) {
            synchronized (BasicApi.class) {
                if (sManagedChannel == null) {
                    OkHttpChannelBuilder builder = OkHttpChannelBuilder
                            .forTarget(BuildConfig.SERVER_ADDRESS)
                            .disableRetry()
                            .usePlaintext();

                    List<ClientInterceptor> clientInterceptorList = new ArrayList<>();
                    clientInterceptorList.add(new LogClientInterceptor());
                    clientInterceptorList.add(new GrpcClientInterceptor());

                    sManagedChannel = AndroidChannelBuilder.usingBuilder(builder)
                            .context(IMCore.getAppContext().getApplicationContext())
                            .intercept(clientInterceptorList)
                            .build();
                }
            }
        }

        return sManagedChannel;
    }

    static class UnaryStreamObserver<T> implements StreamObserver<T> {

        private final RequestCallback<T> mCallback;
        private T mResponseData;

        UnaryStreamObserver(RequestCallback<T> callback) {
            mCallback = callback;
        }

        @Override
        public void onNext(T data) {
            mResponseData = data;
        }

        @Override
        public void onError(Throwable t) {
            Status status = Status.fromThrowable(t);
            if (mCallback != null) {
                mCallback.onFailure(getErrorCodeFrom(status), getErrorMessageFrom(status));
            }
        }

        @Override
        public void onCompleted() {
            if (mCallback != null) {
                mCallback.onCompleted(mResponseData);
            }

        }

    }

    public static int getErrorCodeFrom(Status status) {
        Throwable cause = status.getCause();
        if (cause instanceof ResponseException) {
            ResponseException respException = (ResponseException) cause;
            return respException.getStatusCodeValue();

        }
        if (cause instanceof ClientInternalException) {
            ClientInternalException clientInternalException = (ClientInternalException) cause;
            return clientInternalException.getErrorCode();
        }
        return -status.getCode().value();
    }


    public static String getErrorMessageFrom(Status status) {
        Throwable cause = status.getCause();
        if (cause instanceof ResponseException) {
            ResponseException respException = (ResponseException) cause;
            return respException.getLocalizedMessage();

        }
        if (cause instanceof ClientInternalException) {
            ClientInternalException clientInternalException = (ClientInternalException) cause;
            return clientInternalException.getLocalizedMessage();
        }
        switch (status.getCode()) {
            case DEADLINE_EXCEEDED:
                return "连接超时，请检查当前网络状态是否正常！";
            default:
                return status.getDescription();
        }
    }
}


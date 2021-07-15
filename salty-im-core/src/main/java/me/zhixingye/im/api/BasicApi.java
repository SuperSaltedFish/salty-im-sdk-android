package me.zhixingye.im.api;


import com.google.protobuf.Any;
import com.google.protobuf.MessageLite;
import com.salty.protos.GrpcReq;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import io.grpc.android.AndroidChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.AbstractList;

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

                    sManagedChannel = AndroidChannelBuilder.usingBuilder(builder)
                            .context(IMCore.getAppContext().getApplicationContext())
                            .intercept(new AbstractList<ClientInterceptor>() {
                                @Override
                                public ClientInterceptor get(int index) {
                                    return new ClientInterceptor() {
                                        @Override
                                        public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
                                            return next.newCall(GrpcMethodDescriptor.createGrpcMethodDescriptor(method), callOptions);
                                        }
                                    };
                                }

                                @Override
                                public int size() {
                                    return 1;
                                }
                            })
                            .build();
                }
            }
        }

        return sManagedChannel;
    }

    static class InnerStreamObserver<T> implements StreamObserver<T> {

        private final RequestCallback<T> mCallback;
        private T mResponseData;

        InnerStreamObserver(RequestCallback<T> callback) {
            mCallback = callback;
        }

        @Override
        public void onNext(T data) {
            mResponseData = data;
        }

        @Override
        public void onError(Throwable t) {
            Status status = Status.fromThrowable(t);
            Throwable cause = status.getCause();

            if (cause instanceof ResponseException) {
                ResponseException respException = (ResponseException) cause;
                callError(respException.getStatusCodeValue(), respException.getLocalizedMessage());
                return;
            }

            if (cause instanceof ClientInternalException) {
                ClientInternalException clientInternalException = (ClientInternalException) cause;
                callError(clientInternalException.getErrorCode(), clientInternalException.getLocalizedMessage());
                return;
            }

            switch (status.getCode()) {
                case DEADLINE_EXCEEDED:
                    callError(-status.getCode().value(), "连接超时，请检查当前网络状态是否正常！");
                    break;
                default:
                    callError(-status.getCode().value(), status.getDescription());
                    break;
            }
        }

        private void callError(int code, String error) {
            if (mCallback != null) {
                mCallback.onFailure(code, error);
            }
        }

        @Override
        public void onCompleted() {
            if (mCallback != null) {
                mCallback.onCompleted(mResponseData);
            }

        }
    }

}


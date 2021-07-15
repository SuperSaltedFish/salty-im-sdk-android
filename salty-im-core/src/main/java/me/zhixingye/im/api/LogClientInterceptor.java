package me.zhixingye.im.api;

import com.google.protobuf.MessageLite;

import java.util.Locale;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月14日.
 */
public class LogClientInterceptor implements ClientInterceptor {

    private static final String TAG = "LogClientInterceptor";

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        ClientCall<ReqT, RespT> clientCall = next.newCall(method, callOptions);
        final String methodName = method.getFullMethodName();
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(clientCall) {
            @Override
            public void sendMessage(ReqT message) {
                printRequest(methodName, (MessageLite) message);
                super.sendMessage(message);
            }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                Listener<RespT> forwardListener = new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onMessage(RespT message) {
                        printResponse(methodName, (MessageLite) message);
                        super.onMessage(message);
                    }

                    @Override
                    public void onClose(Status status, Metadata trailers) {
                        if (!status.isOk()) {
                            printError(methodName, status);
                        }
                        super.onClose(status, trailers);
                    }
                };


                super.start(forwardListener, headers);
            }
        };
    }

    public static void printRequest(String requestName, MessageLite data) {
        String title = "发起请求：" + requestName;
        printProtoMessage(title, data == null ? "" : data.toString());
    }

    public static void printResponse(String requestName, MessageLite data) {
        String title = "请求成功：" + requestName;
        printProtoMessage(title, data == null ? "" : data.toString());
    }

    public static void printError(String requestName, Status status) {
        if (status.isOk()) {
            return;
        }

        Throwable cause = status.getCause();
        Logger.e(TAG, String.format(Locale.getDefault(),
                "\n请求失败：%s\n{\ncode = %d，\nerror = %s，\nexception = %s\n}",
                requestName,
                BasicApi.getErrorCodeFrom(status),
                BasicApi.getErrorMessageFrom(status),
                cause == null ? "null" : cause.toString()));
    }

    public static void printProtoMessage(String title, String dataContent) {
        Logger.e(TAG, String.format(Locale.getDefault(),
                "\n%s\n{\n%s\n}",
                title,
                dataContent));
    }


}

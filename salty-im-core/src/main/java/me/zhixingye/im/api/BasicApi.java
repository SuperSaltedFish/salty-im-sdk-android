package me.zhixingye.im.api;

import android.text.TextUtils;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLite;
import com.salty.protos.GrpcReq;
import com.salty.protos.GrpcResp;

import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.exception.ResponseException;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.DeviceService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.service.impl.ServiceAccessor;
import me.zhixingye.im.tool.CallbackHelper;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.im.util.StringUtil;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public abstract class BasicApi {

    private static final String TAG = "BasicApi";

    public abstract void onBindManagedChannel(ManagedChannel channel);

    GrpcReq createReq(MessageLite message) {
        Any data = Any.newBuilder()
                .setTypeUrl("type.googleapis.com/" + message.getClass().getCanonicalName())
                .setValue(message.toByteString())
                .build();

        DeviceService deviceService = ServiceAccessor.get(DeviceService.class);
        UserService userService = ServiceAccessor.get(UserService.class);

        GrpcReq req = GrpcReq.newBuilder()
                .setDeviceId(StringUtil.getNotNull(deviceService.getDeviceId()))
                .setOs(GrpcReq.OS.ANDROID)
                .setLanguage(GrpcReq.Language.CHINESE)
                .setVersion(StringUtil.getNotNull(deviceService.getAppVersion()))
                .setToken(StringUtil.getNotNull(userService.getCurrentUserToken()))
                .setData(data)
                .build();

        printRequest(req, message);
        return req;
    }

    private static void printRequest(GrpcReq req, MessageLite data) {
        String title = "发起请求：" + getRequestName(data);
        printGrpcData(
                title,
                req.toString(),
                data.toString());
    }

    private static void printResponse(GrpcResp resp, MessageLite data, MessageLite defaultDataInstance) {
        String title = "收到响应：" + getRequestName(defaultDataInstance);
        printGrpcData(
                title,
                resp.toString(),
                data == null ? "" : data.toString());
    }

    private static void printError(int code, String error, @Nullable Throwable t, MessageLite defaultDataInstance) {
        Logger.e(TAG, String.format(Locale.getDefault(),
                "\n请求失败：%s\n{\n  code = %d，\n  error = %s，\n  exception = %s\n}",
                getRequestName(defaultDataInstance),
                code,
                error,
                t == null ? "null" : t.toString()));
    }


    private static void printGrpcData(String title, String grpcStr, String dataContent) {
        Pattern firstLinePattern = Pattern.compile("#.*?\\n");
        String respStr = firstLinePattern.matcher(grpcStr).replaceAll("");
        String responseData;

        if (!TextUtils.isEmpty(dataContent) && dataContent.startsWith("#") && dataContent.indexOf("\n") > 0) {
            dataContent = firstLinePattern.matcher(dataContent).replaceAll("");
            dataContent = "  " + dataContent;
            dataContent = dataContent.replace("\n", "\n  ");

            Pattern dataRegionPattern = Pattern.compile("\\{[\\s\\S]*?\\}");
            Matcher dataRegionMatcher = dataRegionPattern.matcher(respStr);

            responseData = dataRegionMatcher.replaceAll("{\n" + dataContent + "\n}");
        } else {
            responseData = respStr;
        }

        responseData = "  " + responseData;
        responseData = responseData.replace("\n", "\n  ");

        Logger.e(TAG, String.format(Locale.getDefault(),
                "\n%s\n{\n%s\n}",
                title,
                responseData));
    }

    private static String getRequestName(MessageLite message) {
        if (message == null) {
            return "Unknown";
        }
        return message.getClass().getSimpleName();
    }


    @SuppressWarnings("unchecked")
    static class InnerStreamObserver<T extends GeneratedMessageLite<?, ?>> implements StreamObserver<GrpcResp> {

        private final RequestCallback<T> mCallback;
        private final T mRespDataDefaultInstance;
        private GrpcResp mResponse;

        InnerStreamObserver(T respDataDefaultInstance, RequestCallback<T> callback) {
            mCallback = callback;
            mRespDataDefaultInstance = respDataDefaultInstance;
        }

        @Override
        public void onNext(GrpcResp value) {
            mResponse = value;
        }

        @Override
        public void onError(Throwable t) {
            Status status = Status.fromThrowable(t);

            String errorMsg;
            switch (status.getCode()) {
                case DEADLINE_EXCEEDED:
                    errorMsg = "连接超时，请检查当前网络状态是否正常！";
                    break;
                default:
                    errorMsg = status.getDescription();
                    break;
            }

            if (TextUtils.isEmpty(errorMsg) && status.getCause() != null) {
                errorMsg = status.getCause().getMessage();
            }

            onError(-status.getCode().value(), errorMsg, t);
        }

        private void onError(int code, String error, Throwable t) {
            printError(code, error, t, mRespDataDefaultInstance);
            CallbackHelper.callFailure(code, error, mCallback);
        }

        @Override
        public void onCompleted() {
            if (mResponse == null) {
                onError(new ResponseException(ResponseCode.INTERNAL_USER_NOT_LOGGED_IN, "GrpcResp == null"));
                return;
            }

            if (ResponseCode.isErrorCode(mResponse.getCode())) {
                onError(mResponse.getCode().getNumber(), mResponse.getMessage(), null);
                return;
            }

            Any anyData = mResponse.getData();
            if (anyData == null) {
                onError(new ResponseException(ResponseCode.INTERNAL_UNKNOWN_RESP_DATA, "anyData == null"));
                return;
            }

            ByteString protoData = anyData.getValue();
            if (protoData == null) {
                onError(new ResponseException(ResponseCode.INTERNAL_UNKNOWN_RESP_DATA, "protoData == null"));
                return;
            }

            if (mRespDataDefaultInstance == null) {
                onError(new ResponseException(ResponseCode.INTERNAL_UNKNOWN, "respDataDefaultInstance == null"));
                return;
            }

            try {
                T data = (T) mRespDataDefaultInstance.getParserForType().parseFrom(protoData);
                if (data == null) {
                    onError(new ResponseException(ResponseCode.INTERNAL_UNKNOWN, "data == null"));
                } else {
                    printResponse(mResponse, data, mRespDataDefaultInstance);
                    CallbackHelper.callCompleted(data, mCallback);
                }
            } catch (Exception e) {
                onError(new ResponseException(ResponseCode.INTERNAL_UNKNOWN, "create data fail ", e));
            }

        }
    }

}


package me.zhixingye.im.api;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.salty.protos.GrpcReq;
import com.salty.protos.GrpcResp;
import com.salty.protos.StatusCode;

import java.io.InputStream;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.MethodDescriptor;
import io.grpc.protobuf.lite.ProtoLiteUtils;
import me.zhixingye.im.IMCore;
import me.zhixingye.im.constant.ClientErrorCode;
import me.zhixingye.im.exception.ClientInternalException;
import me.zhixingye.im.exception.ResponseException;
import me.zhixingye.im.service.DeviceService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.tool.PrintHelper;
import me.zhixingye.im.util.StringUtil;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月14日.
 */
public class GrpcClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return next.newCall(createGrpcMethodDescriptor(method), callOptions);
    }

    @SuppressWarnings("unchecked")
    private static <ReqT, RespT> MethodDescriptor<ReqT, RespT> createGrpcMethodDescriptor(MethodDescriptor<ReqT, RespT> original) {
        MethodDescriptor.Marshaller<ReqT> reqMarshaller = original.getRequestMarshaller();
        MethodDescriptor.Marshaller<RespT> respMarshaller = original.getResponseMarshaller();
        if (!(reqMarshaller instanceof MethodDescriptor.PrototypeMarshaller)) {
            return original;
        }
        if (!(respMarshaller instanceof MethodDescriptor.PrototypeMarshaller)) {
            return original;
        }

        MethodDescriptor.PrototypeMarshaller<? extends MessageLite> respProtoMarshaller = (MethodDescriptor.PrototypeMarshaller<? extends MessageLite>) original.getResponseMarshaller();
        MessageLite defaultRespDataInstance = respProtoMarshaller.getMessagePrototype();

        if (defaultRespDataInstance == null) {
            return original;
        }

        return original.toBuilder()
                .setRequestMarshaller((MethodDescriptor.Marshaller<ReqT>) new GrpcRequestMarshaller())
                .setResponseMarshaller((MethodDescriptor.Marshaller<RespT>) new GrpcResponseMarshaller(defaultRespDataInstance))
                .build();
    }

    private static class GrpcRequestMarshaller implements MethodDescriptor.Marshaller<MessageLite> {

        private static final String TAG = "GrpcRequestMarshaller";

        private final MethodDescriptor.Marshaller<GrpcReq> mGrpcMarshaller;

        public GrpcRequestMarshaller() {
            mGrpcMarshaller = ProtoLiteUtils.marshaller(GrpcReq.getDefaultInstance());
        }


        @Override
        public InputStream stream(MessageLite value) {
            return mGrpcMarshaller.stream(pack(value));
        }

        @Override
        public MessageLite parse(InputStream stream) {
            throw new UnsupportedOperationException("Unsupported unpack SafeRequest");
        }

        protected GrpcReq pack(MessageLite message) {
            Any data = Any.newBuilder()
                    .setTypeUrl("type.googleapis.com/" + message.getClass().getCanonicalName())
                    .setValue(message.toByteString())
                    .build();

            DeviceService deviceService = IMCore.get().getDeviceService();
            UserService userService = IMCore.get().getUserService();

            GrpcReq req = GrpcReq.newBuilder()
                    .setDeviceId(StringUtil.getNotNull(deviceService.getDeviceId()))
                    .setOs(GrpcReq.OS.ANDROID)
                    .setLanguage(GrpcReq.Language.CHINESE)
                    .setVersion(StringUtil.getNotNull(deviceService.getAppVersion()))
                    .setToken(StringUtil.getNotNull(userService.getCurrentUserToken()))
                    .setData(data)
                    .build();

            PrintHelper.printRequest(TAG, req, message);
            return req;
        }
    }

    private static class GrpcResponseMarshaller implements MethodDescriptor.Marshaller<MessageLite> {

        private static final String TAG = "GrpcResponseMarshaller";

        private final MethodDescriptor.Marshaller<GrpcResp> mGrpcMarshaller;
        private final MessageLite mDefaultRespDataInstance;

        public GrpcResponseMarshaller(MessageLite defaultRespDataInstance) {
            mDefaultRespDataInstance = defaultRespDataInstance;
            mGrpcMarshaller = ProtoLiteUtils.marshaller(GrpcResp.getDefaultInstance());
        }


        @Override
        public InputStream stream(MessageLite value) {
            throw new UnsupportedOperationException("Unsupported create GrpcResp");
        }

        @Override
        public MessageLite parse(InputStream stream) {
            return Unpack(mGrpcMarshaller.parse(stream));
        }

        protected MessageLite Unpack(GrpcResp resp) {
            if (resp == null) {
                throw new ClientInternalException(ClientErrorCode.INTERNAL_UNKNOWN, "req == null");
            }

            if (resp.getCode() != StatusCode.STATUS_OK) {
                throw new ResponseException(resp.getCode(), resp.getMessage());
            }

            Any anyData = resp.getData();
            if (anyData == null) {
                throw new ClientInternalException(ClientErrorCode.INTERNAL_UNKNOWN_RESP_DATA, "anyData == null");
            }

            ByteString protoData = anyData.getValue();
            if (protoData == null) {
                throw new ClientInternalException(ClientErrorCode.INTERNAL_UNKNOWN_RESP_DATA, "protoData == null");
            }

            try {
                MessageLite data = mDefaultRespDataInstance.getParserForType().parseFrom(protoData);
                if (data == null) {
                    throw new ClientInternalException(ClientErrorCode.INTERNAL_UNKNOWN_RESP_DATA, "data == null");
                }
                PrintHelper.printResponse(TAG, resp, data, mDefaultRespDataInstance);
                return data;
            } catch (Exception e) {
                throw new ClientInternalException(ClientErrorCode.INTERNAL_UNKNOWN_RESP_DATA, "parseFrom data fail", e);
            }
        }
    }
}

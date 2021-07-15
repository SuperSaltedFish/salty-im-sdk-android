package me.zhixingye.im.tool;

import android.text.TextUtils;

import com.google.protobuf.MessageLite;
import com.salty.protos.GrpcReq;
import com.salty.protos.GrpcResp;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月14日.
 */
public class PrintHelper {

    public static void printRequest(String tag, GrpcReq req, MessageLite data) {
        String title = "发起请求：" + getRequestName(data);
        printGrpcData(
                tag,
                title,
                req.toString(),
                data.toString());
    }

    public static void printResponse(String tag, GrpcResp resp, MessageLite data, MessageLite defaultDataInstance) {
        String title = "收到响应：" + getRequestName(defaultDataInstance);
        printGrpcData(
                tag,
                title,
                resp.toString(),
                data == null ? "" : data.toString());
    }

    public static void printError(String tag, int code, String error, @Nullable Throwable t, MessageLite defaultDataInstance) {
        Logger.e(tag, String.format(Locale.getDefault(),
                "\n请求失败：%s\n{\n  code = %d，\n  error = %s，\n  exception = %s\n}",
                getRequestName(defaultDataInstance),
                code,
                error,
                t == null ? "null" : t.toString()));
    }


    public static void printGrpcData(String tag, String title, String grpcStr, String dataContent) {
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

        Logger.e(tag, String.format(Locale.getDefault(),
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
}

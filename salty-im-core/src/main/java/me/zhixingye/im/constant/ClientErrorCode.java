package me.zhixingye.im.constant;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public enum ClientErrorCode {

    INTERNAL_UNKNOWN(-101, "未知的错误"),
    INTERNAL_UNKNOWN_RESP_DATA(-102, "无法解析响应数据"),
    INTERNAL_IPC_EXCEPTION(-104, "客户端繁忙，请稍后再试");

    private final int code;
    private final String msg;

    ClientErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}

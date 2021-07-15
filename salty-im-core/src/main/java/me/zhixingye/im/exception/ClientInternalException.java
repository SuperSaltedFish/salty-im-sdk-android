package me.zhixingye.im.exception;

import me.zhixingye.im.constant.ClientErrorCode;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月13日.
 */
public class ClientInternalException extends RuntimeException {
    private final ClientErrorCode mClientErrorCode;

    public ClientInternalException(ClientErrorCode code) {
        this(code, code.getMsg());
    }

    public ClientInternalException(ClientErrorCode code, String error) {
        super(code.getMsg() + ": " + error);
        mClientErrorCode = code;
    }

    public ClientInternalException(ClientErrorCode code, String error, Throwable throwable) {
        super(error, throwable);
        mClientErrorCode = code;
    }

    public int getErrorCode() {
        return mClientErrorCode.getCode();
    }
}

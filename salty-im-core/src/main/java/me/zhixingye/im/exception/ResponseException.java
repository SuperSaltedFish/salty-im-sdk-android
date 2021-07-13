package me.zhixingye.im.exception;

import me.zhixingye.im.constant.ResponseCode;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月13日.
 */
public class ResponseException extends Exception {

    private final ResponseCode mResponseCode;

    public ResponseException(ResponseCode code) {
        this(code, code.getMsg());
    }

    public ResponseException(ResponseCode code, String error) {
        super(error);
        mResponseCode = code;
    }

    public ResponseException(ResponseCode code, String error, Throwable throwable) {
        super(error, throwable);
        mResponseCode = code;
    }

    public ResponseCode getResponseCode() {
        return mResponseCode;
    }
}

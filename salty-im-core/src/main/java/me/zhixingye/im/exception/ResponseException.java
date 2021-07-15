package me.zhixingye.im.exception;

import com.salty.protos.StatusCode;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月13日.
 */
public class ResponseException extends RuntimeException {

    private final StatusCode mStatusCode;

    public ResponseException(StatusCode statusCode, String responseMsg) {
        super(responseMsg);
        this.mStatusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return mStatusCode;
    }

    public int getStatusCodeValue() {
        return mStatusCode.getNumber();
    }
}

package me.zhixingye.im.listener;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月12日.
 */
public class RequestCallbackWrapper<Resp> implements RequestCallback<Resp> {

    private final RequestCallback<Resp> mCallback;

    public RequestCallbackWrapper(RequestCallback<Resp> callback) {
        mCallback = callback;
    }

    @Override
    public void onCompleted(Resp response) {
        if (mCallback != null) {
            mCallback.onCompleted(response);
        }
    }

    @Override
    public void onFailure(int code, String error) {
        if (mCallback != null) {
            mCallback.onFailure(code, error);
        }
    }
}

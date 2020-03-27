package x.com.hl_base.interfaces;

public interface HCallBack<T> {
    void onSuccessed(T result);

    void onFail(String error);
}

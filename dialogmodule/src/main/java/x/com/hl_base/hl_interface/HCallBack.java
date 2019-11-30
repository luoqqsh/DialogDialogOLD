package x.com.hl_base.hl_interface;

public interface HCallBack<T> {
    void onSuccessed(T result);

    void onFail(String error);
}

package x.com.hl_rx.jiu;

public interface OnHttpCallback<T> {

    void onSucceed(T result);

    void onFail(String error);

}

package x.com.hl_rx.jiu;

public interface OnHttpCallback<T> {

    void onSuccess(T result);

    void onFail(String error);

}

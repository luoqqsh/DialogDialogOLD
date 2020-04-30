package x.com.hl_rx.kotlin;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import x.com.hl_dialog.R;
import x.com.hl_log.ViseLog;

/**
 * Author: hebihe
 * E-mail: hbh@erongdu.com
 * Date: 2016/4/22 19:31
 * <p/>
 * Description: 网络请求回调封装类
 */
public abstract class RequestCallBack<T> implements Callback<T> {
    public abstract void onSuccess(Call<T> call, Response<T> response);

    private Object mToastObject;
    protected boolean dismissLoading = true; //关闭加载等待弹窗

    public RequestCallBack() {
    }

    public RequestCallBack(Object toastObject) {
        mToastObject = toastObject;
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
//        ViseLog.d(response.body());
    }

    public void onFailed(Call<T> call, Response<T> response) {
//        ViseLog.d(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
//        ViseLog.d(t);
    }
}

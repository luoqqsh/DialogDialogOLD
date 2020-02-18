package x.com.hl_rx.test;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import x.com.hl_log.ViseLog;

public class retrofit {
    private void testRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lenovodns.com/") //设置网络请求的Url地址,必须以/结尾
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//要返回rxjava的Observable数据，需要加上这个，否则返回Call数据
//                .addCallAdapterFactory(CoroutineCallAdapterFactory.create())//返回协程处理
//                .addConverterFactory(ScalarsConverterFactory.create())//有这个才能解析成String等基本数据类型
//                .addConverterFactory(GsonConverterFactory.create())//有这个才能进行解析Model数据类型
                .build();
        ApiFace request = retrofit.create(ApiFace.class);
        retrofit2.Call<ResponseBody> call = request.getTvList();
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    ViseLog.d(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                ViseLog.d(t);
            }
        });
    }
}

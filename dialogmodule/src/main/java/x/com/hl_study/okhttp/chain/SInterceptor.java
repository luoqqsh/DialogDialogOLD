package x.com.hl_study.okhttp.chain;

import java.io.IOException;

import x.com.hl_study.okhttp.SResponse;

public interface SInterceptor {
    SResponse intercept(InterceptorChain chain) throws IOException;
}

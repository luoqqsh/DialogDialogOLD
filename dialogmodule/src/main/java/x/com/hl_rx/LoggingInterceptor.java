package x.com.hl_rx;


import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import x.com.hl_log.ViseLog;

public class LoggingInterceptor implements Interceptor {
    private volatile Level level = Level.HEADERS;
    private final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        NONE, HEADERS, BODY
    }

    /**
     * Change the level at which this interceptor logs.
     */
    public LoggingInterceptor setLevel(Level level) {
        if (level == null) this.level = Level.HEADERS;
        this.level = level;
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Level level = this.level;
        Request request = chain.request();
        if (level == LoggingInterceptor.Level.NONE) {
            return chain.proceed(request);
        }
        StringBuilder isHttps = new StringBuilder(request.isHttps() ? "https: " : "http: ");
        List<String> url = request.url().pathSegments();
        for (String param : url) {
            isHttps.append(param).append("/");
        }
        StringBuilder requestlog = new StringBuilder("<=======================发起" + isHttps + " 请求=======================>\n");
        StringBuilder responseLog = new StringBuilder("<=======================收到" + isHttps + " 请求结果=======================>\n");
        RequestBody requestBody = request.body();
        Connection connection = chain.connection();
        requestlog.append(request.method()).append(' ').append(request.url()).append("\n")
                .append(connection != null ? " " + connection.protocol() + "\n" : "");
        getUrlParams(request.url().toString(), requestlog);
        //
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestlog.append("------Body参数------\n");
            requestBody.writeTo(buffer);
            pringJson(buffer.readString(UTF8), requestlog);
        }
        //--------------------开始请求--------------------//
        long startNs = System.currentTimeMillis();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            responseLog.append("请求失败:").append(e.getMessage());
            ViseLog.e(requestlog + responseLog.toString());
            throw e;
        }
        long tookMs = System.currentTimeMillis() - startNs;
        //--------------------request中的header--------------------//
        if (level == Level.HEADERS) {
            Headers requestHeaders = response.request().headers();
            requestlog.append("------Headers参数------\n");
            for (int i = 0, count = requestHeaders.size(); i < count; i++) {
                requestlog.append(requestHeaders.name(i)).append(": ").append(requestHeaders.value(i)).append("\n");
            }
        }
        ViseLog.e(requestlog);
        //--------------------response日志处理--------------------//
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        Buffer responseBuffer = source.buffer();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        long contentLength = responseBuffer.size();
        responseLog.append(response.code()).append(response.message().isEmpty() ? "" : ' ' + response.message()).append(' ')
                .append(response.request().url()).append("\n")
                .append("请求耗时 : ").append(tookMs).append(" ms\n")
                .append("返回数据大小 : ").append(contentLength).append(" byte\n");
        if (level == Level.HEADERS) {
            Headers headers = response.headers();
            responseLog.append("------Headers------\n");
            for (int i = 0, count = headers.size(); i < count; i++) {
                responseLog.append(headers.name(i)).append(" : ").append(headers.value(i)).append("\n");
                if (headers.name(i).toLowerCase().equals("date")) {
                    responseLog.append("服务器时间").append(" : ").append(toGMT8(headers.value(i))).append("\n");
                }
            }
        }
        if (!isPlaintext(responseBuffer)) {
            return response;
        }
        responseLog.append("------Body------\n");
        if (contentLength != 0 && contentLength < 20000) {
            pringJson(responseBuffer.clone().readString(UTF8), responseLog);
        } else {
            responseLog.append(responseBuffer.clone().readString(UTF8));
        }
        ViseLog.e(responseLog.toString());
        return response;
    }


    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private void pringJson(String json, StringBuilder log) {
        if (TextUtils.isEmpty(json)) {
            log.append("没有请求参数\n");
        }
        if (isJson(json)) {
            try {
                if (json.startsWith("{")) {
                    JSONObject jsonObject = new JSONObject(json);
                    String msg = jsonObject.toString(2);
                    log.append(msg);
                } else if (json.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(json);
                    String msg = jsonArray.toString(2);
                    log.append(msg);
                }
                log.append("\n");
            } catch (JSONException e) {
                log.append("数据格式化错误，").append(e.getMessage()).append("以下是原始数据：\n");
                log.append(json);
            }
        } else {
            log.append(json);
        }
    }

    private void getUrlParams(String url, StringBuilder log) {
        if (url.contains("?")) {
            log.append("------Url参数------\n");
            String[] argAry = url.substring(url.indexOf("?") + 1).split("&");
            for (String arg : argAry) {
                String[] argArtT = arg.split("=");
                if (!argArtT[0].isEmpty()) {
                    if (argArtT.length == 1) {
                        String[] argArts = Arrays.copyOf(argArtT, 2);
                        argArts[1] = "";
                        log.append(argArts[0]).append(" = ").append(argArts[1]).append("\n");
                    } else {
                        log.append(argArtT[0]).append(" = ").append(argArtT[1]).append("\n");
                    }
                }
            }
        }
    }

    private String toGMT8(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE,dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断是否是json结构
     */
    private boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }


}


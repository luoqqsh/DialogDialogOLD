package x.com.hl_tools;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class hl_net {
    //获取外网ip地址，需要在子线程调用
    public static String GetNetIp() {
        String IP;
        try {
            URL url = new URL("http://ip.taobao.com/service/getIpInfo2.php?ip=myip");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36"); //设置浏览器ua 保证不出现503
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                // 将流转化为字符串
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String tmpString;
                StringBuilder retJSON = new StringBuilder();
                while ((tmpString = reader.readLine()) != null) {
                    retJSON.append(tmpString + "\n");
                }
                JSONObject jsonObject = new JSONObject(retJSON.toString());
                String code = jsonObject.getString("code");

                if (code.equals("0")) {
                    JSONObject data = jsonObject.getJSONObject("data");
//                    IP = data.getString("ip") + "(" + data.getString("country")
//                            + data.getString("area") + "区"
//                            + data.getString("region") + data.getString("city")
//                            + data.getString("isp") + ")";
                    IP = data.getString("ip");
                } else IP = "";

            } else IP = "";

        } catch (Exception e) {
            IP = "";
        }
        return IP;
    }
}

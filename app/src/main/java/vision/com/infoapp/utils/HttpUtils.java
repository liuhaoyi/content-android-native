package vision.com.infoapp.utils;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;



/**
 * Created by liuhy on 2018/12/11.
 */

public class HttpUtils {
    private static final String TAG="HttpUtils";


    public static String requestGet(String baseUrl ,HashMap<String, String> paramsMap) {
        String result = null;
        try {
            baseUrl = baseUrl + "?";
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }
            String requestUrl = baseUrl + tempParams.toString();
            System.out.println(requestUrl);
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                result = streamToString(urlConn.getInputStream());
                LogUtils.e(TAG, "Get方式请求成功，result--->" + result);
            } else {
                LogUtils.e(TAG, "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
        }
        return result;
    }

    public static String requestPost(String baseUrl,HashMap<String, String> paramsMap) {
        String result = null;
        try {
            baseUrl = baseUrl + "?";
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key,  URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }
            String params =tempParams.toString();
            // 请求的参数转换为byte数组
            byte[] postData = params.getBytes();
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(postData);
            dos.flush();
            dos.close();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                result = streamToString(urlConn.getInputStream());
                LogUtils.e(TAG, "Post方式请求成功，result--->" + result);
            } else {
                LogUtils.e(TAG, "Post方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
        }
        return result ;
    }
    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
            return null;
        }
    }

    public static Bitmap downloadBitmap(String imageUrL) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(imageUrL);
            // 使用HttpURLConnection打开连接
            HttpURLConnection urlConn = (HttpURLConnection) url .openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(false);
            urlConn.setRequestMethod("GET");
            urlConn.setConnectTimeout(3000);
            urlConn.setUseCaches(true);
            urlConn.connect();
            int code = urlConn.getResponseCode();
            Log.e("tag", "run: "+code );
            // 将得到的数据转化成InputStream
            InputStream in = urlConn.getInputStream();
            return BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
//    public static void main(String[] args) throws Exception{
//        String baseUrl = "http://39.96.17.251:8080/api/queryBeforeNewsList";
//        HashMap<String,String>  _mapParam  = new HashMap();
//        _mapParam.put("smallCatalog","shouyedongtu");
//        _mapParam.put("time","");
//        String str = HttpUtils.requestGet(baseUrl,_mapParam);
//        if(null!=str){
//            JSONObject json = new JSONObject(str);
//            String state = json.getString("state");
//            JSONArray array = json.getJSONArray("data");
//
//        }
//        System.out.println(str);
//    }
}

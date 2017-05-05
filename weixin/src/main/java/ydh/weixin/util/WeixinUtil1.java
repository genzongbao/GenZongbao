package ydh.weixin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import ydh.weixin.entity.WebChatUser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


public class WeixinUtil1 {
	
	private static String accessToken;
	
	public static DateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 获取access_token
	 * @return
	 */
	public static String getAccessToken() {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+WeiXinConfig.appId()+"&secret="+WeiXinConfig.appSecret();
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); 
			http.setRequestProperty("ContentType", "application/xwwwformurlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
			System.setProperty("sun.net.client.defaultReadTimeout", "30000");
			http.connect();
			
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF8");
			if(message.indexOf("access_token")>0){
				accessToken=message.split("\"")[3];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessToken;
	}
	
	/**
	 * 获取openId
	 * @param code
	 * @return
	 */
	public static String getOpenId(String code) throws Exception{
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+WeiXinConfig.appId()+"&secret="+WeiXinConfig.appSecret()+"&code="+code+"&grant_type=authorization_code";
		String openId = null;
		URL urlGet = new URL(url);
		HttpURLConnection connect = (HttpURLConnection) urlGet.openConnection();
		connect.setRequestMethod("GET"); 
		connect.setRequestProperty("ContentType", "application/xwwwformurlencoded");
		connect.setDoOutput(true);
		connect.setDoInput(true);
		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
		System.setProperty("sun.net.client.defaultReadTimeout", "30000");
		connect.connect();
		InputStream is = connect.getInputStream();
		int size = is.available();
		byte[] jsonBytes = new byte[size];
		is.read(jsonBytes);
		String message = new String(jsonBytes, "UTF8");
		if(message.indexOf("openid")>0){
			openId = message.split("\"")[13];
		}
		return openId;
	}
	
	/**
	 * 获取openId
	 * @param code
	 * @return nextOpenId
	 */
	public static String getOpenIdList(String nextOpenId) throws Exception{
		String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+getAccessToken();//+WeiXinConfig.appId()+"&secret="+WeiXinConfig.appSecret()+"&grant_type=authorization_code";
		if(StringUtils.isNotBlank(nextOpenId)){
			url+="&next_openid="+nextOpenId;
		}
		String openId = null;
		URL urlGet = new URL(url);
		HttpURLConnection connect = (HttpURLConnection) urlGet.openConnection();
		connect.setRequestMethod("GET"); 
		connect.setRequestProperty("ContentType", "application/xwwwformurlencoded");
		connect.setDoOutput(true);
		connect.setDoInput(true);
		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
		System.setProperty("sun.net.client.defaultReadTimeout", "30000");
		connect.connect();
		InputStream is = connect.getInputStream();
		int size = is.available();
		byte[] jsonBytes = new byte[size];
		is.read(jsonBytes);
		String message = new String(jsonBytes, "UTF8");
		if(message.indexOf("openid")>0){
			openId = message.split("\"")[13];
		}
		return openId;
	}
	
	/**
	 * 获取微信用户信息
	 * @param accessToken
	 * @param opId
	 * @return
	 */
	public static WebChatUser getWebChatUserByOpId(String accessToken, String opId){
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+opId;
		WebChatUser webChatUser = null;
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); 
			http.setRequestProperty("ContentType", "application/xwwwformurlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
			System.setProperty("sun.net.client.defaultReadTimeout", "30000");
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF8");
			ObjectMapper objectMapper=new ObjectMapper();
			webChatUser=objectMapper.readValue(message, WebChatUser.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webChatUser;
	}
	
	/**
	 * 公众号推送信息
	 * @param url
	 * @param ob
	 */
	public static void sendMsg(String content) {
		try {
			String result = null;
			URL urlObj = new URL("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+getAccessToken());
			// 连接
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
			con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
			con.setDoInput(true);  
			con.setDoOutput(true);  
			con.setUseCaches(false); 
			con.setRequestProperty("Connection", "Keep-Alive");  
			con.setRequestProperty("Charset", "UTF-8");  
			OutputStream out = new DataOutputStream(con.getOutputStream());
			out.write(content.getBytes("utf-8"));
			out.flush();
			out.close();
			BufferedReader reader = null;
			StringBuffer buffer = new StringBuffer();
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
				System.out.println(result);
			}
			if (reader != null) {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/***
	 * 获取时间戳
	 * @return
	 */
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	
	/**
     * 向指定URL发送GET方法的请求
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
	
}

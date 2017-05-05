package ydh.weixin.service;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import ydh.weixin.util.WeixinUtil1;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeiXinSendMsgService{
	
	public void sendMsgPost(JsonObject jsonObject){
		try {
			String url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ WeixinUtil1.getAccessToken();
			String result = null;
			URL urlObj = new URL(url);
			// 连接
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
			con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
			con.setDoInput(true);  
			con.setDoOutput(true);  
			con.setUseCaches(false); 
			con.setRequestProperty("Connection", "Keep-Alive");  
			con.setRequestProperty("Charset", "UTF-8");  
			OutputStream out = new DataOutputStream(con.getOutputStream());
			
			out.write(jsonObject.toString().getBytes("utf-8"));
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
			}
			if (reader != null) {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

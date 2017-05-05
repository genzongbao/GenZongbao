package ydh.weixin.service;

import org.springframework.stereotype.Service;
import ydh.utils.GsonUtil;
import ydh.weixin.entity.WeiXinConstant;
import ydh.weixin.util.HttpClientHttpsUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Leo on 2017/2/11.
 */
@Service("linkService")
public class LinkServiceImpl implements ILinkService {
    @Override
    public String accessToken() {
        long time = new Date().getTime();
        if (time > WeiXinConstant.ACCESS_TOKEN_STOP) {
            Map<String, Object> param = new HashMap<>();
            param.put("grant_type", "client_credential");
            param.put("appid", WeiXinConstant.APPID);
            param.put("secret", WeiXinConstant.AppSecret);
            String result = null;
            result = HttpClientHttpsUtil.doGet(WeiXinConstant.ACCESSTOKEN_URL, param);
            Map<String, Object> res = GsonUtil.toObject(result, Map.class);
            WeiXinConstant.ACCESS_TOKEN = res.get("access_token").toString();
            WeiXinConstant.ACCESS_TOKEN_STOP = new Date().getTime() + 7200 * 1000;
        }
        return WeiXinConstant.ACCESS_TOKEN;
    }

    @Override
    public void SendTemplateMessage(String temId, String openId, String url, Object templateData) {
        Map<String, Object> wxTemplate = new HashMap<>();
        wxTemplate.put("url", url);
        wxTemplate.put("template_id", temId);
        wxTemplate.put("touser", openId);
        wxTemplate.put("data", templateData);
        String link = WeiXinConstant.TEMPLATE_URL + accessToken();
        String data = GsonUtil.toJSONString(wxTemplate);
        String res = HttpClientHttpsUtil.doPost(link, data);
        System.out.println(res);

    }

    @SuppressWarnings("unchecked")
	@Override
    public String getWebAccessToken(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", WeiXinConstant.APPID);
        map.put("secret", WeiXinConstant.AppSecret);
        map.put("code", code);
        map.put("grant_type", "authorization_code");
        String result = null;
        result = HttpClientHttpsUtil.doGet(WeiXinConstant.WEB_ACCESSTOKEN_URL, map);
        @SuppressWarnings("Unchecked")
        Map<String, Object> access = GsonUtil.toObject(result, Map.class);
        WeiXinConstant.WEB_ACCESS_TOKEN = access.get("access_token") + "";
        WeiXinConstant.WEB_ACCESS_TOKEN_STOP = new Date().getTime() + 7200 * 1000;
        String openId = access.get("openid") + "";
        return openId;
    }

    @Override
    public String refreshWebToken(String refreshToken) {
        return null;
    }

    @Override
    public void getJSAccessToken() {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", WeiXinConstant.APPID);
        params.put("secret", WeiXinConstant.AppSecret);
        String result = null;
        result = HttpClientHttpsUtil.doGet(WeiXinConstant.JSSDK_acceToken_url, params);

        Map<String, Object> res = GsonUtil.toObject(result, Map.class);
        WeiXinConstant.JSSDK_ACCESS_TOKEN = res.get("access_token").toString();
        WeiXinConstant.JSSDK_ACCESS_TOKEN_STOP = new Date().getTime() + 7200 * 1000;
    }

	@Override
    public String getJSTicket() {

        Map<String, Object> params = new HashMap<>();
        if (new Date().getTime() > WeiXinConstant.JSSDK_ACCESS_TOKEN_STOP) this.getJSAccessToken();
        params.put("type", "jsapi");
        params.put("access_token", WeiXinConstant.JSSDK_ACCESS_TOKEN);
        String result = null;
        result = HttpClientHttpsUtil.doGet(WeiXinConstant.JSSDK_ticket_url, params);
        Map<String, String> res = GsonUtil.toObject(result, Map.class);
        return res.get("ticket");
    }
}

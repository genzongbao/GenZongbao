package ydh.weixin.service;

/**
 * Created by Leo on 2017/2/11.
 */
public interface ILinkService {

    String accessToken();

    void SendTemplateMessage(String temId, String openId, String url, Object templateData);

    String getWebAccessToken(String code);

    String refreshWebToken(String refreshToken);

    void getJSAccessToken();

    String getJSTicket();

}

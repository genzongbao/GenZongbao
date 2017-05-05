package ydh.push.aliyun.phone;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;

import ydh.push.aliyun.AliyunConstant;
import ydh.utils.GsonUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * Created by Leo on 2017/2/20.
 */
@Service(value="phoneMsgService1")
public class IPhoneMsgServiceImpl implements IPhoneMsgService {

    @Override
    public void eventInvite(String name, String role, String eventName, String phone) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", name);
        map.put("role", role);
        map.put("eventName", eventName);
        SingleSendSmsRequest request = getRequest(PhoneConstant.Event_INVITE_Template, map, phone);
        send(request);
    }

    @Override
    public void eventChange(String userName, String eventName, String phone) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", userName);
        map.put("eventName", eventName);
        SingleSendSmsRequest request = getRequest(PhoneConstant.Event_CHANG_NOTICE, map, phone);
        send(request);
    }

    @Override
    public boolean sendCode(String code, String phone) {
        try {

            Map<String, String> map = new LinkedHashMap<>();
            map.put("code", code);
            map.put("product", PhoneConstant.MESSAGE_GRAPH);
            SingleSendSmsRequest request = getRequest(PhoneConstant.Id_CHECKE_Template, map, phone);
            send(request);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean downInvite(String phone, String name) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("name", name);
            SingleSendSmsRequest request = getRequest(PhoneConstant.Event_INVITE_Template, map, phone);
            send(request);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void send(SingleSendSmsRequest request) {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", AliyunConstant.AccessKeyID, AliyunConstant.AccessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms", "sms.aliyuncs.com");
            IAcsClient client = new DefaultAcsClient(profile);
            SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
            System.out.print(httpResponse.getModel());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private static SingleSendSmsRequest getRequest(String templateCode, Map<String, String> map, String phone) {
        SingleSendSmsRequest request = new SingleSendSmsRequest();
        request.setSignName(PhoneConstant.MESSAGE_GRAPH);
        request.setTemplateCode(templateCode);
        request.setParamString(GsonUtil.toJSONString(map));
        request.setRecNum(phone);
        return request;
    }
}

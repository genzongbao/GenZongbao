package ydh.push.aliyun.phone;

/**
 * Created by Leo on 2017/2/20.
 */
public interface IPhoneMsgService {
    /**
     * 事件邀请通知
     *
     * @param name      邀请来了姓名
     * @param eventName 事件名称
     * @param role 在时间中的角色
     * @param phone     电话
     */
    void eventInvite(String name, String role, String eventName, String phone);

    void eventChange(String userName, String evnetTile, String phone);

    /**
     * 发送验证码
     *
     * @param code
     * @param phone
     * @return
     */
    boolean sendCode(String code, String phone);

    boolean downInvite(String phone, String name);
}

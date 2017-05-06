package ydh.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * @auther 冯冰
 * @email fengbing8206320@sohu.com
 * @weixin f1908951579
 * @date 2017/5/6
 * @doc 类说明：ehcache工具类
 */
@Component
public class Memory {
    @Resource
    private Cache ehcache;

    /**
     * 设置值
     * @param key 键
     * @param value 值
     */
    public void setValue(String key, Object value) {
        ehcache.put(new Element(key, value));
    }

    /**
     * 通过key获取value
     * @param key
     * @return
     */
    public Object getValue(String key) {
        Element element = ehcache.get(key);
        return element != null ? element.getValue() : null;
    }

    /**
     * 通过key删除value
     * @param key
     */
    public void removeValue(String key) {

        if(ehcache.isElementInMemory(key)) {
            ehcache.remove(key);
        }
    }

    /**
     * 查看value是否存在
     * @param value
     * @return
     */
    public boolean isValueInMemory(Object value){
        return ehcache.isValueInCache(value);
    }

    /**
     * 查看key是否存在
     * @param key
     * @return
     */
    public boolean isKey(Object key) {
        return ehcache.isKeyInCache(key);
    }

    /**
     * 关闭缓存管理器
     */
    @PreDestroy
    protected void shutdown() {
        if (ehcache != null) {
            ehcache.getCacheManager().shutdown();
        }
    }

    /**
     * 保存当前登录用户
     *
     * @param loginUser
     */
//    public String saveLoginUser(Member loginUser, String pwd) {
//        // 生成seed和token值
//        String seed = MD5Utils.md5(loginUser.getLoginName().getBytes(Charset.forName("UTF-8")), MD5Utils.MD5Type.MD5_32);
//        String token = getToken(loginUser.getLoginName(), pwd);
//        // 清空之前的登录信息
//        clearLoginInfoBySeed(seed);
//        // 保存新的token和登录信息
//        ehcache.put(new Element(seed, token, null, null, null));
//        ehcache.put(new Element(token, loginUser, null, null, null));
//        return token;
//    }

    /**
     * 获取当前线程中的用户信息
     *
     * @return
     */
//    public Member currentLoginUser() {
//        Element element = ehcache.get(ThreadTokenHolder.getToken());
//        return element == null ? null : (Member) element.getValue();
//    }

//    public boolean checkLoginInfo(String token) {
//        Element element = ehcache.get(token);
//        return element != null && (Member) element.getValue() != null;
//    }

//    public void clearLoginInfo(Member member) {
//        if (member != null) {
//            // 根据登录的用户名生成seed，然后清除登录信息
//            String seed = MD5Utils.md5(member.getLoginName().getBytes(Charset.forName("UTF-8")), MD5Utils.MD5Type.MD5_32);
//            clearLoginInfoBySeed(seed);
//        }
//    }

    /**
     * 根据seed清空登录信息
     *
     * @param seed
     */
    public void clearLoginInfoBySeed(String seed) {
        // 根据seed找到对应的token
        Element element = ehcache.get(seed);
        if (element != null) {
            // 根据token清空之前的登录信息
            ehcache.remove(seed);
            ehcache.remove(element.getValue());
        }
    }

    private String getToken(String loginName, String pwd) {
        String str = loginName + pwd + String.valueOf(System.currentTimeMillis());
        str = MD5Utils.md5(str.getBytes(Charset.forName("UTF-8")), MD5Utils.MD5Type.MD5_32);
        return str;
    }
}

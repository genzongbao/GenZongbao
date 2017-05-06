package ydh.util;

import java.security.MessageDigest;

/**
 * @auther 冯冰
 * @email fengbing8206320@sohu.com
 * @weixin f1908951579
 * @date 2017/5/6
 * @doc 类说明：md5加密工具类
 */
public class MD5Utils {
    public enum MD5Type {
        MD5_16, MD5_32
    }

    /**
     * 加密
     * @param datas 需要加密的byte数组
     * @param type 加密类型
     * @return
     */
    public static String md5(byte[] datas, MD5Type type) {
        String result = null;
        try {
            MessageDigest mMessageDigest = MessageDigest.getInstance("MD5");
            mMessageDigest.update(datas);
            byte b[] = mMessageDigest.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            switch (type) {
                case MD5_16:
                    result = buf.toString().substring(8, 24);
                    break;
                case MD5_32:
                    result = buf.toString();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

package ydh.mvc;

import ydh.cicada.dict.TitleDict;

/**
 * @auther 华清松
 * @time 2017/3/24 0024 14:27
 * @doc 描述：错误类型
 */
public enum ErrorCode  implements TitleDict{
    ERROR_NET_FAIL("链接失败"),
    SERVER_EXCEPTION("服务器异常"),
	NO_LOGIN("没有登录");
    private String title;

    private ErrorCode(String title) {
        this.title = title;
    }

	@Override
	public String title() {
		// TODO Auto-generated method stub
		return null;
	}
    
}

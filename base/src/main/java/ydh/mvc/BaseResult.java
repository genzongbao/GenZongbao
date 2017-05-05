package ydh.mvc;
/**
 * @auther 华清松
 * @time 2017/3/14 0014 14:05
 * @doc 描述：子项结果实体
 */
public class BaseResult<T> {
    private String msg;
    private boolean error=false;
    private ErrorCode errorCode;
    private int cusId;
    private T data;

    
    
    public int getCusId() {
		return cusId;
	}

	public void setCusId(int cusId) {
		this.cusId = cusId;
	}

	public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

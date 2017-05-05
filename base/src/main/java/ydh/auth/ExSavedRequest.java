package ydh.auth;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.SavedRequest;

public class ExSavedRequest extends SavedRequest {
	private static final long serialVersionUID = 6237386745504821186L;

	private String method;
    private String queryString;
    private String requestURI;
    
	public ExSavedRequest(HttpServletRequest request, String path) {
		super(request);
		this.method = super.getMethod();
		int p = path.indexOf('?');
		if (p >= 0) {
			this.requestURI = path.substring(0, p);
			this.queryString = path.substring(p+1);
		} else {
			this.requestURI = path;
		}
	}

	public String getMethod() {
        return method;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getRequestURI() {
        return requestURI;
    }
}

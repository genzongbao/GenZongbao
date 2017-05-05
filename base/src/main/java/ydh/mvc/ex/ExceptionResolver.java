package ydh.mvc.ex;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

public class ExceptionResolver implements HandlerExceptionResolver {
	
    @Override  
    public ModelAndView resolveException(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex) {
    	if (ex instanceof WorkException) {
    		String referer = request.getHeader("Referer");
    		if (referer != null) {
    			FlashMap flash = RequestContextUtils.getOutputFlashMap(request);
    			flash.put("alertType", "danger");
    			flash.put("alertMsg", ex.getMessage());
    			return new ModelAndView(new RedirectView(referer));
    		}
    	}
    	if (ex instanceof IllegalArgumentException) {
    		
    	}
    	if (ex instanceof UnauthorizedException) {
    		ModelAndView mav = new ModelAndView("/admin/layout");
    		StringWriter writer = new StringWriter();
    		ex.printStackTrace(new PrintWriter(writer));
    		mav.addObject("stackTrace", writer.toString());
    		mav.addObject("exception", ex);
    		mav.addObject("contentView", "forbidden.vm");
    		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    		return mav;
    	}
		ModelAndView mav = new ModelAndView("layout");
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		mav.addObject("stackTrace", writer.toString());
		mav.addObject("exception", ex);
		mav.addObject("contentView", "error.vm");
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return mav;
    }

}  
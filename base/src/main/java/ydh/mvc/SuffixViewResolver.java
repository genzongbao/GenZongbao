/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ydh.mvc;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class SuffixViewResolver implements ViewResolver, Ordered {

	static final Log logger = LogFactory.getLog(SuffixViewResolver.class);

	private ViewResolver viewResolver;
	private View view;
	private String suffix;
	private int order = Ordered.HIGHEST_PRECEDENCE;
	
	private View doResolveViewName(String viewName, Locale locale) throws Exception {
		if (this.viewResolver != null) {
			return this.viewResolver.resolveViewName(viewName, locale);
		}
		return this.view;
	}
	
	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
		HttpServletRequest request = ((ServletRequestAttributes) attrs).getRequest();		
		String suffix = splitSuffix(request.getRequestURI());
		if ( this.suffix.equals(suffix)) {
			return doResolveViewName(viewName, locale);
		}
		return null;
	}
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public void setViewResolver(ViewResolver viewResolver) {
		this.viewResolver = viewResolver;
	}

	public ViewResolver getViewResolver() {
		return this.viewResolver;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	private static String splitSuffix(String servletPath) {
		int pointIndex = servletPath.lastIndexOf('.');
		if (pointIndex < 0) return "";
		int slantIndex = servletPath.lastIndexOf('/');
		if(pointIndex < slantIndex) return "";
		int sessionidIndex = servletPath.lastIndexOf("jsessionid");
		if(sessionidIndex > 0){
			return servletPath.substring(pointIndex + 1,sessionidIndex-1);
		} else {
			return servletPath.substring(pointIndex + 1);
		}
	}

}
